package com.ock.assisteddeploy.dxb;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Component
public class VaultKeeper {
    private static final Logger logger = LoggerFactory.getLogger(VaultKeeper.class);

    private static final int SIZE_8KB = 8192;

    @Autowired
    private Configuration config;

    public void clear() {
        try {
            File vaultDir = config.getDedicatedVault();
            if (vaultDir.exists()) {
                FileUtils.cleanDirectory(vaultDir);
            }
            logger.info("Vault cleared");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void store() {
        try {
            for (File artifact : config.getBuild().getArtifacts()) {
                FileUtils.moveFileToDirectory(artifact, config.getDedicatedVault(), true);
            }
            logger.info("Artifacts are stored into vault: {}", config.getDedicatedVault().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // write http response to file
    public void write(okhttp3.ResponseBody responseBody, String artifactName) throws IOException {
        File vault = config.getDedicatedVault();
        vault.mkdirs();

        File artifact = new File(vault, artifactName);
        try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(artifact.toPath()))) {
            byte[] buffer = new byte[SIZE_8KB];
            int byteRead;

            while ((byteRead = responseBody.byteStream().read(buffer)) != -1) {
                out.write(buffer, 0, byteRead);
            }
            out.flush();
        }

    }
}
