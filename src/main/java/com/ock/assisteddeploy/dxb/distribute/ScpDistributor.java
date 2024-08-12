package com.ock.assisteddeploy.dxb.distribute;

import com.ock.assisteddeploy.dxb.Configuration;
import com.ock.assisteddeploy.dxb.io.UnixFile;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.scp.SCPFileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Qualifier("scpDistributor")
public class ScpDistributor implements ArtifactDistributor {

    private static final Logger logger = LoggerFactory.getLogger(ScpDistributor.class);

    @Autowired
    private Configuration config;

    @Override
    public void distribute() {
        SSHClient ssh = new SSHClient();
        Configuration.Distribute distributeConfig = config.getDistribute();

        try {
            // connect ssh
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(distributeConfig.getHostname(), distributeConfig.getPort());
            ssh.authPassword(distributeConfig.getUser(), distributeConfig.getPassword());

            ssh.useCompression();

            // create deployment base
            UnixFile deployBase = createDeploymentBase(ssh);

            // update deployment skeleton
            SCPFileTransfer scp = ssh.newSCPFileTransfer();
            File skeleton = distributeConfig.getDeploymentSkeleton();
            scp.upload(new FileSystemFile(skeleton), deployBase.getUnixPath());

            // rename to timestamp
            UnixFile stampedPath = renameAsNow(ssh, new UnixFile(deployBase, skeleton.getPath()));
            uploadArtifact(scp, stampedPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected void uploadArtifact(SCPFileTransfer scp, UnixFile stampedPath) throws IOException {
        Configuration.Distribute distributeConfig = config.getDistribute();
        UnixFile artifactBase = new UnixFile(stampedPath, distributeConfig.getArtifactBase());
        for (File artifact : config.getArtifactVault().listFiles()) {
            scp.upload(new FileSystemFile(artifact), artifactBase.getUnixPath());
            logger.info("Uploaded {} to {}.", artifact.getName(), artifactBase.getPath());

        }
    }

    protected UnixFile renameAsNow(SSHClient ssh, UnixFile skeletonPath) throws TransportException, ConnectionException {

        Configuration.Distribute distributeConfig = config.getDistribute();

        String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
        String folderLabel = String.format(config.getLabelTemplate(), timestamp);

        UnixFile stampedPath = new UnixFile(distributeConfig.getDeploymentBase(), folderLabel);
        // name skeleton to release folder
        String mv = String.format("mv %s %s", skeletonPath.getUnixPath(), stampedPath.getUnixPath());
        issue(ssh, mv);
        logger.info("Deployment folder create: {}.", stampedPath.getPath());

        return stampedPath;
    }

    protected UnixFile createDeploymentBase(SSHClient ssh) throws TransportException, ConnectionException {

        UnixFile deployBase = new UnixFile(config.getDistribute().getDeploymentBase());
        String mkdir = String.format("mkdir -p %s", deployBase.getUnixPath());
        issue(ssh, mkdir);

        return deployBase;
    }

    protected void issue(SSHClient ssh, String command) throws TransportException, ConnectionException {
        Session session = ssh.startSession();
        session.exec(command).join();
        session.close();
    }
}
