package com.ock.assisteddeploy.dxb.invoke;

import com.ock.assisteddeploy.dxb.Configuration;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class RemoteShellExecutor {

    private static final Logger logger = LoggerFactory.getLogger(RemoteShellExecutor.class);

    @Autowired
    private Configuration config;

    public void execute() {
        SSHClient ssh = new SSHClient();
        Configuration.Deploy deployConfig = config.getDeploy();
        try {
            // connect ssh
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(deployConfig.getHostname(), deployConfig.getPort());
            ssh.authPassword(deployConfig.getUser(), deployConfig.getPassword());

            // execute deployment script
            for (String script : deployConfig.getScripts()) {
                try (Session session = ssh.startSession();
                     Session.Command cmd = session.exec(script)) {
                    // Read the output
                    logStream(cmd.getInputStream());
                    logStream(cmd.getErrorStream());

                    // wait for command complete
                    cmd.join();
                }
            }

            ssh.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected void logStream(InputStream stream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                logger.info(line);
            }
        }
    }
}
