package fr.paulbrancieq.packlistfeatures.mixin;

import fr.paulbrancieq.packlistfeatures.PackListFeaturesMod;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ResourcePackOrganizer.class)
public abstract class ResourcePackOrganizerMixin {
    @Shadow @Final
    List<ResourcePackProfile> enabledPacks;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(CallbackInfo ci) {
        Map<String, ResourcePackProfile> map = new HashMap<>();
        List<String> nameList = new ArrayList<>();
        enabledPacks.forEach(pack -> map.put(pack.getId(), pack));
        enabledPacks.forEach(pack -> nameList.add(pack.getId()));
        Collections.reverse(nameList);
        List<ResourcePackProfile> temp = PackListFeaturesMod.getInstance().getPackIndexManager().organizePacks(map, nameList);
        Collections.reverse(temp);
        enabledPacks.clear();
        enabledPacks.addAll(temp);
    }
}
