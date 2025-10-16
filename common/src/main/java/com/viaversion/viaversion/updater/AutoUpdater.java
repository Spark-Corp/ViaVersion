package com.viaversion.viaversion.updater;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AutoUpdater {

    private static final File updaterDirectory = new File(System.getProperty("user.home"), "updater");

    private final File serverPluginFile, updaterPluginFile;

    private boolean updated;

    public AutoUpdater(File serverPluginFile) {
        this.serverPluginFile = serverPluginFile;
        this.updaterPluginFile = new File(updaterDirectory, serverPluginFile.getName());
    }

    private boolean hasUpdate() {
        return updaterPluginFile.isFile() && updaterPluginFile.lastModified() > serverPluginFile.lastModified();
    }

    private void update() {
        try {
            Files.copy(updaterPluginFile.toPath(), serverPluginFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            updated = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verify() {

        System.out.println("Searching for Via updates...");

        if (hasUpdate()) {
            System.out.println("Via update found!");
            update();
            return true;
        } else {
            System.out.println("No Paper X updates found!");
        }

        return false;
    }

    public boolean isUpdated() {
        return updated;
    }
}
