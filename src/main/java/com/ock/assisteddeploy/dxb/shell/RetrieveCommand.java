package com.ock.assisteddeploy.dxb.shell;

import com.ock.assisteddeploy.dxb.acquire.ArtifactAcquirer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class RetrieveCommand implements Command, AcquireSupport {

    private static final Logger logger = LoggerFactory.getLogger(RetrieveCommand.class);

    @Autowired
    @Qualifier("remoteServerAcquirer")
    private ArtifactAcquirer acquirer;

    @ShellMethod(
            value = "Retrieve the artifact from the build server to artifact vault",
            key = Commands.VERB_GET
    )
    public void retrieve() {
        logger.info("Retrieve start.");
        acquirer.acquire(null);
        logger.info("Retrieve complete.");
    }

    @Override
    public void instruct(Object obj) {
        retrieve();
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit((AcquireSupport) this);
    }
}
