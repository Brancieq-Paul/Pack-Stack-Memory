package fr.paulbrancieq.packlistfeatures.mixin;

import fr.paulbrancieq.packlistfeatures.PackListFeaturesMod;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(targets = "net/minecraft/client/resource/server/ServerResourcePackManager$1", remap = false)
public class ServerResourcePackManageOnSuccessMixin {
    @Inject(method = "onSuccess", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/server/PackStateChangeCallback;onFinish(Ljava/util/UUID;Lnet/minecraft/client/resource/server/PackStateChangeCallback$FinishState;)V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    private void afterOnSuccess(CallbackInfo ci) {
        PackListFeaturesMod.onServerResourcePackLoadSuccess(ci);
    }
}
