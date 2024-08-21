package com.ock.assisteddeploy.dxb.invoke;

import com.jcraft.jsch.*;
import com.ock.assisteddeploy.dxb.Configuration;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.*;

@Component
public class RemoteShellExecutor {

    private static final Logger logger = LoggerFactory.getLogger(RemoteShellExecutor.class);

    private static final String CHANNEL_TYPE_SHELL = "shell";

    private static final String SSH_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";


    @Autowired
    private Configuration config;

    public void execute() {

        Configuration.Deploy deployConfig = config.getDeploy();

        try {
            Session session = createJschSession();
            session.connect();

            // open shell
            Channel shell = session.openChannel(CHANNEL_TYPE_SHELL);
            shell.connect();

            // start logging ssh output
            RemoteShellLoggingThread loggingThread = new RemoteShellLoggingThread(shell);
            loggingThread.start();

            // execute scripts
            PrintStream printStream = new PrintStream(shell.getOutputStream(), true);
            for (String script : deployConfig.getScripts()) {
                printStream.println(script);
                Thread.sleep(100);
            }


            // wait for logging thread complete
            loggingThread.join();

            shell.disconnect();
            session.disconnect();

        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    protected @NotNull Session createJschSession() throws JSchException {
        Configuration.Deploy deployConfig = config.getDeploy();

        JSch jsch = new JSch();
        Session session = jsch.getSession(deployConfig.getUser(), deployConfig.getHostname(), deployConfig.getPort());
        session.setPassword(deployConfig.getPassword());

        // setup session configuration
        Properties config = new Properties();
        // disable strict host key checking for simplicity
        config.put(SSH_STRICT_HOST_KEY_CHECKING, "no");
        session.setConfig(config);

        return session;
    }

}
