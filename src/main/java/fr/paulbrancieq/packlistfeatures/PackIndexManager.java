package fr.paulbrancieq.packlistfeatures;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackProfile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackIndexManager {
    private static final File configFolder = FabricLoader.getInstance().getConfigDir().toFile();
    private static final File modConfigFolder = new File(configFolder, PackListFeaturesMod.MOD_ID);
    private static File packIndexFile;
    private final List<String> packIndex = new ArrayList<>();
    private static final String[] oldDirectoryNames = {"packstackmemory", "packposmemory"};

    public PackIndexManager() {
        generateFoldersAndFiles();
        readJSON();
        writeJSON();
    }

    public List<ResourcePackProfile> organizePacks(Map<String, ResourcePackProfile> enabledPacks, List<String> enabledNames) {
        for (String pack : enabledNames) {
            if (!packIndex.contains(pack)) {
                updateIndex(new ArrayList<>(enabledNames));
                break;
            }
        }
        List<ResourcePackProfile> organizedPacks = new ArrayList<>();
        for (String pack : packIndex) {
            if (enabledPacks.containsKey(pack)) {
                organizedPacks.add(enabledPacks.get(pack));
            }
        }
        return organizedPacks;
    }

    public void updateIndex(List<String> enabledNames) {
        String back_pack = null;
        for (String pack : enabledNames) {
            // Case 1: pack is not in the index and back_pack is null, so we add it in first position
            // Case 2: pack is not in the index and back_pack is not null, so we add it after back_pack
            // Case 3: pack is in the index and back_pack is null, so we do nothing
            // Case 4: pack is in the index and back_pack is not null, so we verify if pack is after back_pack, if not we move it after back_pack
            if (!packIndex.contains(pack)) {
                if (back_pack == null) {
                    packIndex.add(packIndex.size(), pack);
                } else {
                    int index = packIndex.indexOf(back_pack);
                    packIndex.add(index + 1, pack);
                }
            } else if (back_pack != null) {
                int index = packIndex.indexOf(back_pack);
                if (packIndex.indexOf(pack) < index) {
                    packIndex.remove(pack);
                    packIndex.add(index, pack);
                }
            }
            back_pack = pack;
        }
        writeJSON();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void generateFoldersAndFiles() {
        for (String oldDirectoryName : oldDirectoryNames) {
            File oldDirectory = new File(configFolder, oldDirectoryName);
            if (oldDirectory.exists()) {
                PackListFeaturesMod.LOGGER.info("Renaming old config folder");
                oldDirectory.renameTo(modConfigFolder);
            }
        }
        if (!configFolder.exists()) {
            PackListFeaturesMod.LOGGER.info("Creating new config folder");
            configFolder.mkdir();
        }
        if (!modConfigFolder.exists()) {
            PackListFeaturesMod.LOGGER.info("Creating new mod config folder");
            modConfigFolder.mkdir();
        }
        if (modConfigFolder.isDirectory()) {
            packIndexFile = new File(modConfigFolder, "pack-index.json");
            if (!packIndexFile.exists()) {
                PackListFeaturesMod.LOGGER.info("Creating new pack index file");
                try {
                    packIndexFile.createNewFile();
                } catch (Exception e) {
                    PackListFeaturesMod.LOGGER.error("Unexpected error: can't create config file", e);
                }
            } else if (packIndexFile.isDirectory()) {
                throw new IllegalStateException("'rpp-pack-index.json' must be a file!");
            }
        } else {
            throw new IllegalStateException("'config' must be a folder!");
        }
    }

    private void readJSON() {
        try {
            JsonArray json = new Gson().fromJson(new FileReader(packIndexFile), JsonArray.class);
            if (json == null) {
                PackListFeaturesMod.LOGGER.error("Invalid configuration!");
                return;
            }
            json.asList().forEach(value -> packIndex.add(value.getAsString()));
        } catch (JsonSyntaxException e) {
            PackListFeaturesMod.LOGGER.error("Invalid configuration!", e);
        } catch (JsonIOException e) {
            PackListFeaturesMod.LOGGER.error("Unexpected error with Json reading", e);
        } catch (FileNotFoundException e) {
            PackListFeaturesMod.LOGGER.error("Unexpected error: config file not found", e);
        }
    }

    private void writeJSON() {
        try {
            String json = new Gson().toJson(packIndex);
            FileWriter writer = new FileWriter(packIndexFile);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException("Can't update config file", e);
        }
    }
}
