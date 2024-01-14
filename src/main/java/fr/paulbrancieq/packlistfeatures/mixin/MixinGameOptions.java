package fr.paulbrancieq.packlistfeatures.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.option.GameOptions;

import java.io.File;
import java.util.List;

@Mixin(GameOptions.class)
public abstract class MixinGameOptions {

    @Shadow public List<String> resourcePacks;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;load()V", ordinal = 0, shift = At.Shift.BEFORE))
    private void addVanillaResourcePack(MinecraftClient client, File optionsFile, CallbackInfo ci) {
        resourcePacks.add("vanilla");
    }
}
