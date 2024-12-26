package fr.paulbrancieq.packlistfeatures.mixin;

import fr.paulbrancieq.packlistfeatures.PackListFeaturesMod;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(targets = "net/minecraft/client/gui/screen/pack/ResourcePackOrganizer$AbstractPack")
public abstract class AbstractPackMixin {

    @Shadow protected abstract List<ResourcePackProfile> getOppositeList();
    @Shadow protected abstract List<ResourcePackProfile> getCurrentList();

    @Inject(method = "move(I)V", at = @At("RETURN"), cancellable = true)
    private void move(int offset, CallbackInfo in) {
        ResourcePackOrganizer.Pack pack = (ResourcePackOrganizer.Pack) (Object) this;
        if (pack.isEnabled()) {
            List<String> temp = new ArrayList<>();
            for (ResourcePackProfile profile : getCurrentList()) {
                temp.add(profile.getId());
            }
            Collections.reverse(temp);
            PackListFeaturesMod.getInstance().getPackIndexManager().updateIndex(temp);
        }
    }

    @Inject(
            method = "toggle()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackProfile$InsertionPosition;insert(Ljava/util/List;Ljava/lang/Object;Ljava/util/function/Function;Z)I", shift = At.Shift.AFTER)
    )
    private void toggle(CallbackInfo in) {
        ResourcePackOrganizer.Pack pack = (ResourcePackOrganizer.Pack) (Object) this;
        PackListFeaturesMod.toggle(pack, getOppositeList());
    }
}
