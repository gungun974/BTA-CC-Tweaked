/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.fabric.IWorldDirNameAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class IDAssigner {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
        .create();
    private static final Type ID_TOKEN = new TypeToken<Map<String, Integer>>() {
    }.getType();
    private static Map<String, Integer> ids;
    private static Path idFile;

    private IDAssigner() {
    }

    public static synchronized int getNextId(String kind) {
        File dir = getWorldDir();
        dir.mkdirs();

        // Load our ID file from disk
        idFile = new File(dir, "ids.json").toPath();
        if (Files.isRegularFile(idFile)) {
            try (Reader reader = Files.newBufferedReader(idFile, StandardCharsets.UTF_8)) {
                ids = GSON.fromJson(reader, ID_TOKEN);
            } catch (Exception e) {
                ComputerCraft.log.error("Cannot load id file '" + idFile + "'", e);
                ids = new HashMap<>();
            }
        } else {
            ids = new HashMap<>();
        }

        Integer existing = ids.get(kind);
        int next = existing == null ? 0 : existing + 1;
        ids.put(kind, next);

        try (Writer writer = Files.newBufferedWriter(idFile, StandardCharsets.UTF_8)) {
            GSON.toJson(ids, writer);
        } catch (Exception e) {
            ComputerCraft.log.error("Cannot update ID file '" + idFile + "'", e);
        }

        return next;
    }

    public static File getWorldDir() {
        if (EnvironmentHelper.isServerEnvironment()) {
            return new File(MinecraftServer.getInstance().getMinecraftDir(), ((IWorldDirNameAccess) MinecraftServer.getInstance()).cc_bta$getWorldDirName() + "/computercraft");
        }
        return new File(Minecraft.getMinecraft().getMinecraftDir(), "saves/" + ((IWorldDirNameAccess) Minecraft.getMinecraft()).cc_bta$getWorldDirName() + "/computercraft");
    }
}
