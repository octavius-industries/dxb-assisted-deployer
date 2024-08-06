package com.ock.assisteddeploy.dxb;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


@Component
public class VaultKeeper {
    private static final Logger logger = LoggerFactory.getLogger(VaultKeeper.class);

    @Autowired
    private Configuration config;

    public void clear() {
        try {
            File vaultDir = config.getArtifactVault();
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
                FileUtils.moveFileToDirectory(artifact, config.getArtifactVault(), true);
            }
            logger.info("Artifacts are stored into vault");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
