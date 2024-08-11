package com.ock.assisteddeploy.dxb.distribute;

import com.ock.assisteddeploy.dxb.Configuration;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FtpDistributor implements ArtifactDistributor {

    private static final Logger logger = LoggerFactory.getLogger(FtpDistributor.class);

    @Autowired
    private Configuration config;

    @Override
    public void distribute() {
        Configuration.Distribute distributeConfig = config.getDistribute();
        FTPClient ftp = new FTPClient();
        try {
            // connect ftp server
            ftp.connect(distributeConfig.getHostname(), distributeConfig.getPort());
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            // login
            boolean validLogin = ftp.login(distributeConfig.getUser(), distributeConfig.getPassword());
            if (validLogin) {
                // make deployment folder
                ftp.changeWorkingDirectory(distributeConfig.getDeploymentBase());

                String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
                String folderLabel = String.format(config.getLabelTemplate(), timestamp);

                if (ftp.makeDirectory(folderLabel)) {
                    logger.info("Deployment folder create: {}/{}.", ftp.printWorkingDirectory(), folderLabel);

                    // upload deployment skeleton
                    uploadDeploymentSkeleton(ftp, folderLabel);
                    // upload artifact vault
                    uploadArtifact(ftp, folderLabel);
                } else {
                    logger.error("Cannot create deployment folder:{}/{}.", ftp.printWorkingDirectory(), folderLabel);
                }
                ftp.logout();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ftp.isConnected()) {
                    ftp.disconnect();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    protected void uploadArtifact(FTPClient ftp, String folderLabel) throws IOException {
        Configuration.Distribute distributeConfig = config.getDistribute();
        ftp.changeWorkingDirectory(distributeConfig.getDeploymentBase());
        ftp.changeWorkingDirectory(folderLabel);
        ftp.changeWorkingDirectory(distributeConfig.getArtifactBase());

        File[] artifacts = config.getArtifactVault().listFiles();
        for (File artifact : artifacts) {
            putFile(ftp, artifact);
            logger.info("Uploaded {} to {}.", artifact.getName(), ftp.printWorkingDirectory());
        }

    }

    protected void uploadDeploymentSkeleton(FTPClient ftp, String folderLabel) {
        Configuration.Distribute distributeConfig = config.getDistribute();
        try {
            ftp.changeWorkingDirectory(distributeConfig.getDeploymentBase());
            ftp.changeWorkingDirectory(folderLabel);
            for (File file : distributeConfig.getDeploymentSkeleton().listFiles()) {
                putDirectory(ftp, file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void putDirectory(FTPClient ftp, File skeleton) throws IOException {
        // create directory
        if (!ftp.changeWorkingDirectory(skeleton.getName())) {
            ftp.makeDirectory(skeleton.getName());
            ftp.changeWorkingDirectory(skeleton.getName());
        }

        // for each directory content, either create subdirectory or file
        File[] files = skeleton.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    putDirectory(ftp, file);
                } else {
                    putFile(ftp, file);
                }
            }
            ftp.changeToParentDirectory();
        }
    }

    protected void putFile(FTPClient ftp, File file) throws IOException {
        try (InputStream fileStream = new FileInputStream(file)) {
            if (!ftp.storeFile(file.getName(), fileStream)) {
                logger.error("Cannot create file {}/{}, reason: {}({})",
                        ftp.printWorkingDirectory(), file.getName(), ftp.getReplyString(), ftp.getReplyCode());
            }
        }
    }


}
