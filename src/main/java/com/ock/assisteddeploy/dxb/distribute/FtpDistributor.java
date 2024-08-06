package com.ock.assisteddeploy.dxb.distribute;

import com.ock.assisteddeploy.dxb.Configuration;
import com.ock.assisteddeploy.dxb.shell.RetrieveCommand;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FtpDistributor implements ArtifactDistributor {

    private static final Logger logger = LoggerFactory.getLogger(FtpDistributor.class);

    @Autowired
    private Configuration config;

    private static final String SKELETON_LABEL_TEMPLATE = "dxb_%s";

    @Override
    public void distribute() {
        Configuration.Distribute distributeConfig = config.getDistribute();
        FTPClient ftp = new FTPClient();
        try {
            // connect ftp server
            ftp.connect(distributeConfig.getHostname(), distributeConfig.getPort());
            // login
            boolean validLogin = ftp.login(distributeConfig.getUser(), distributeConfig.getPassword());
            if (validLogin) {
                // make deployment folder
                ftp.changeWorkingDirectory(distributeConfig.getDeploymentBase());

                String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
                String folderLabel = String.format(SKELETON_LABEL_TEMPLATE, timestamp);
                if (ftp.makeDirectory(folderLabel)) {
                    logger.info("Deployment folder create: {}", folderLabel);
                    // upload deployment skeleton
                    uploadDeploymentSkeleton(ftp, folderLabel);
                    // upload artifact vault
                    uploadArtifact(ftp, folderLabel);
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

    private void uploadArtifact(FTPClient ftp, String folderLabel) {
        // TODO
    }

    private void uploadDeploymentSkeleton(FTPClient ftp, String folderLabel) {
        // TODO
    }
}
