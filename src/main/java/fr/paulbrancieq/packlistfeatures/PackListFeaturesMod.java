package fr.paulbrancieq.packlistfeatures;

import net.fabricmc.api.ModInitializer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.resource.ResourcePackProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

public class PackListFeaturesMod implements ModInitializer {
	private static PackListFeaturesMod instance;

    public static final Logger LOGGER = LoggerFactory.getLogger("rpp");

	private PackIndexManager packIndexManager;

	public static final String MOD_ID = "packlistfeatures";

	public static final ThreadLocal<Boolean> needReloadSkip = ThreadLocal.withInitial(() -> false);

	@Override
	public void onInitialize() {
		instance = this;
		packIndexManager = new PackIndexManager();
	}

	public static PackListFeaturesMod getInstance() {
		return instance;
	}

	public PackIndexManager getPackIndexManager() {
		return packIndexManager;
	}
	public static void toggle(ResourcePackOrganizer.Pack pack, List<ResourcePackProfile> oppositeList) {
		if (!pack.isEnabled()) {
			List<String> enabledNames = new ArrayList<>();
			Map<String, ResourcePackProfile> map = new HashMap<>();
			for (ResourcePackProfile profile : oppositeList) {
				enabledNames.add(profile.getDescription().getString());
				map.put(profile.getDescription().getString(), profile);
			}
			Collections.reverse(enabledNames);
			List<ResourcePackProfile> temp = PackListFeaturesMod.getInstance().getPackIndexManager().organizePacks(map, enabledNames);
			Collections.reverse(temp);
			oppositeList.clear();
			oppositeList.addAll(temp);
		}
	}

	public static void onBuildEnabledProfiles(Collection<String> enabledNames, CallbackInfoReturnable<List<ResourcePackProfile>> cir) {
		ArrayList<ResourcePackProfile> enabledPacks = new ArrayList<>(cir.getReturnValue());
		ArrayList<String> enabledNamesList = new ArrayList<>();
		Map<String, ResourcePackProfile> map = new HashMap<>();
		for (ResourcePackProfile pack : enabledPacks) {
			map.put(pack.getDescription().getString(), pack);
			enabledNamesList.add(pack.getDescription().getString());
		}
		enabledPacks = new ArrayList<>(PackListFeaturesMod.getInstance().getPackIndexManager().organizePacks(map, enabledNamesList));
		cir.setReturnValue(enabledPacks);
	}

	public static void onServerResourcePackLoadSuccess(CallbackInfo ci) {
		needReloadSkip.set(true);
		MinecraftClient.getInstance().options.refreshResourcePacks(MinecraftClient.getInstance().getResourcePackManager());
		needReloadSkip.set(false);
	}
}