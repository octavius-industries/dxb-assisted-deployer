package com.ock.assisteddeploy.dxb.shell;

import com.ock.assisteddeploy.dxb.distribute.ArtifactDistributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class DistributeCommand implements Instructable {

    private static final Logger logger = LoggerFactory.getLogger(DistributeCommand.class);

    @Autowired
    private ArtifactDistributor distributor;

    @ShellMethod(
            value = "Distribute the artifact vault to the specified target locations or servers.",
            key = "put"
    )
    public void distribute() {
        logger.info("Distribute start.");
        distributor.distribute();
        logger.info("Distribute complete.");
    }

    @Override
    public void instruct(Object obj) {
        distribute();
    }
}
