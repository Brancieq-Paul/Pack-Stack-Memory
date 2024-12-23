package fr.paulbrancieq.packlistfeatures.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resource.server.ServerResourcePackLoader;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackPosition;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerResourcePackLoader.class)
public class ServerResourcePackLoaderMixin {

  @Unique final private static ResourcePackPosition POSITION = new ResourcePackPosition(true,
          ResourcePackProfile.InsertionPosition.TOP, false);

  @ModifyArg(
          method = "toProfiles(Ljava/util/List;)Ljava/util/List;",
          at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), index = 0
  )
  private Object toProfiles(Object e,
                            @Local ResourcePackInfo resourcePackInfo,
                            @Local ResourcePackProfile.PackFactory packFactory,
                            @Local ResourcePackProfile.Metadata metadata) {
    return new ResourcePackProfile(resourcePackInfo, packFactory, metadata, POSITION);
  }
}
