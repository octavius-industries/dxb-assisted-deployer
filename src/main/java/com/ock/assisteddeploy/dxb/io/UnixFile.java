package com.ock.assisteddeploy.dxb.io;

import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;

public class UnixFile extends File {

    public UnixFile(@NotNull String pathname) {
        super(pathname);
    }

    public UnixFile(String parent, @NotNull String child) {
        super(parent, child);
    }

    public UnixFile(File parent, @NotNull String child) {
        super(parent, child);
    }

    public UnixFile(@NotNull URI uri) {
        super(uri);
    }

    public String getUnixPath() {
        return FilenameUtils.separatorsToUnix(super.getPath());

    }
}
