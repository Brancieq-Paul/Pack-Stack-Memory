package fr.paulbrancieq.packlistfeatures.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.option.GameOptions;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePackManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow protected abstract CompletableFuture<Void> reloadResources(boolean force, MinecraftClient.LoadingContext loadingContext);

    @Shadow @Final public GameOptions options;

    @Shadow @Final private ResourcePackManager resourcePackManager;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;reload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Ljava/util/List;)Lnet/minecraft/resource/ResourceReload;"))
    private void refreshPacks(RunArgs args, CallbackInfo ci) {
        options.refreshResourcePacks(resourcePackManager);
    }

    @Redirect(method = "onResourceReloadFailure", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackManager;setEnabledProfiles(Ljava/util/Collection;)V", ordinal = 0))
    private void onResourceReloadFailure(ResourcePackManager resourcePackManager, Collection<String> enabledNames) {
        if (MinecraftClient.getInstance().options.incompatibleResourcePacks.isEmpty()) {
            resourcePackManager.setEnabledProfiles(enabledNames);
        } else {
            for (String incompatibleResourcePack : MinecraftClient.getInstance().options.incompatibleResourcePacks) {
                this.options.resourcePacks.remove(incompatibleResourcePack);
            }
            resourcePackManager.setEnabledProfiles(this.options.resourcePacks);
        }
    }
    @Redirect(method = "onResourceReloadFailure", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 0))
    private void onResourceReloadFailureClearRedirect(List<?> list) {
        if (MinecraftClient.getInstance().options.incompatibleResourcePacks.isEmpty()) {
            list.clear();
            this.options.incompatibleResourcePacks.clear();
        }
    }

    @Redirect(method = "onResourceReloadFailure",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;reloadResources(ZLnet/minecraft/client/MinecraftClient$LoadingContext;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<Void> redirectReloadResources(MinecraftClient instance, boolean value, MinecraftClient.LoadingContext loadingContext) {
        return reloadResources(false, loadingContext);
    }
}
