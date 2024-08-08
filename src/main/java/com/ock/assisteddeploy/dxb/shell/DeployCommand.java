package com.ock.assisteddeploy.dxb.shell;

import com.ock.assisteddeploy.dxb.invoke.RemoteShellExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class DeployCommand implements Instructable {

    private static final Logger logger = LoggerFactory.getLogger(DeployCommand.class);

    @Autowired
    private RemoteShellExecutor shellExecutor;

    @ShellMethod(value = "Deploy the distributed artifact to the target environment and restart.")
    public void deploy() {
        logger.info("Deploy start.");
        shellExecutor.execute();
        logger.info("Deploy complete.");
    }

    @Override
    public void instruct(Object obj) {
        deploy();
    }
}