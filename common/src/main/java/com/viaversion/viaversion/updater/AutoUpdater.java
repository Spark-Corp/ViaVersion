package com.viaversion.viaversion.updater;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class AutoUpdater {

    protected final File updateDirectory = new File(System.getProperty("user.home"), "updater");

    private final File serverPluginFile, updaterPluginFile;

    private boolean updated;

    public AutoUpdater(File serverPluginFile) {
        this.serverPluginFile = serverPluginFile;
        this.updaterPluginFile = new File(updateDirectory, serverPluginFile.getName());
    }

    private boolean hasUpdate() {
        return updaterPluginFile.isFile() && updaterPluginFile.lastModified() > serverPluginFile.lastModified() && validate(updaterPluginFile);
    }

    private void update(Runnable afterUpdate) {

        try {

            Files.copy(updaterPluginFile.toPath(), serverPluginFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            updated = true;

            afterUpdate.run();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verify(Runnable runnable) {
        Logger.getGlobal().info("Searching for updates...");

        if (hasUpdate()) {

            Logger.getGlobal().info("Update found!");

            update(runnable);

            return true;

        } else {
            Logger.getGlobal().info("No updates found, skipping!");
        }

        return false;
    }

    protected boolean validate(File file) {
        try (JarFile jar = new JarFile(file)) {
            return jar.getJarEntry("plugin.yml") != null || jar.getJarEntry("bungee.yml") != null;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isUpdated() {
        return updated;
    }
}
