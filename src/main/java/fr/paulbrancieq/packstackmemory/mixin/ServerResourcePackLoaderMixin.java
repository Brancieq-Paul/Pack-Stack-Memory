package fr.paulbrancieq.packstackmemory.mixin;

import net.minecraft.client.resource.server.ServerResourcePackLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerResourcePackLoader.class)
public class ServerResourcePackLoaderMixin {
    @ModifyArg(
            method = "toProfiles(Ljava/util/List;)Ljava/util/List;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackProfile;of(Ljava/lang/String;Lnet/minecraft/text/Text;ZLnet/minecraft/resource/ResourcePackProfile$PackFactory;Lnet/minecraft/resource/ResourcePackProfile$Metadata;Lnet/minecraft/resource/ResourcePackProfile$InsertionPosition;ZLnet/minecraft/resource/ResourcePackSource;)Lnet/minecraft/resource/ResourcePackProfile;"),
            index = 6
    )
    private boolean toProfiles(boolean isPinned) {
        return false;
    }
}
