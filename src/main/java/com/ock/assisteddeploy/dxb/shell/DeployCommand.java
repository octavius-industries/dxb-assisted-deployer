package com.ock.assisteddeploy.dxb.shell;

import com.ock.assisteddeploy.dxb.invoke.RemoteShellExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

@Component(Commands.VERB_DEPLOY)
@ShellComponent
public class DeployCommand implements Command, DeploySupport {

    private static final Logger logger = LoggerFactory.getLogger(DeployCommand.class);

    @Autowired
    private RemoteShellExecutor shellExecutor;

    @ShellMethod(
            value = "Deploy the distributed artifact to the target environment and restart.",
            key = Commands.VERB_DEPLOY
    )
    public void deploy() {
        logger.info("Deploy start.");
        shellExecutor.execute();
        logger.info("Deploy complete.");
    }

    @Override
    public void instruct(Object obj) {
        deploy();
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit((DeploySupport) this);
    }
}
