package fr.paulbrancieq.packlistfeatures.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fr.paulbrancieq.packlistfeatures.PackListFeaturesMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackProfile;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.option.GameOptions;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Shadow public List<String> resourcePacks;
    @Shadow public List<String> incompatibleResourcePacks;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;load()V", ordinal = 0, shift = At.Shift.BEFORE))
    private void addVanillaResourcePack(MinecraftClient client, File optionsFile, CallbackInfo ci) {
        resourcePacks.add("vanilla");
    }

    @Inject(method = "refreshResourcePacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;write()V", ordinal = 0, shift = At.Shift.AFTER), cancellable = true)
    private void afterRefreshResourcePacks(CallbackInfo ci) {
        if (PackListFeaturesMod.needReloadSkip.get()) {
            ci.cancel();
        }
    }

    @Redirect(method = "addResourcePackProfilesToManager", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 1, remap = false))
    private void onLoggerWarn(Logger logger, String s, Object o) {
        PackListFeaturesMod.LOGGER.warn("Skipping newly incompatible resource pack from being removed from the list");
    }

    @Redirect(method = "addResourcePackProfilesToManager", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;remove()V", ordinal = 1))
    private void onIteratorRemove(Iterator<ResourcePackProfile> iterator, @Local ResourcePackProfile resourcePackProfile, @Local Set<String> set) {
        PackListFeaturesMod.LOGGER.warn("Adding newly incompatible resource pack to the incompatible list");
        incompatibleResourcePacks.add(resourcePackProfile.getName());
        set.add(resourcePackProfile.getName());
        MinecraftClient.getInstance().options.write();
    }
}
