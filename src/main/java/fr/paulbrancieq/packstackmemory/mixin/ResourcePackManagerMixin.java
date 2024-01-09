package fr.paulbrancieq.packstackmemory.mixin;

import fr.paulbrancieq.packstackmemory.PackStackMemoryMod;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(ResourcePackManager.class)
public class ResourcePackManagerMixin {

    @Inject(method = "buildEnabledProfiles", at = @At("RETURN"), cancellable = true)
    private void onBuildEnabledProfiles(Collection<String> enabledNames, CallbackInfoReturnable<List<ResourcePackProfile>> cir) {
        PackStackMemoryMod.onBuildEnabledProfiles(enabledNames, cir);
    }
}
