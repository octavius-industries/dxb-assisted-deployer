package com.ock.assisteddeploy.dxb.shellcommand;

import com.ock.assisteddeploy.dxb.Configuration;
import com.ock.assisteddeploy.dxb.VaultKeeper;
import com.ock.assisteddeploy.dxb.acquirer.ArtifactAcquirer;
import com.ock.assisteddeploy.dxb.acquirer.LocalBuildAcquirer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;

@ShellComponent
public class BuildCommand {
    private static final Logger logger = LoggerFactory.getLogger(BuildCommand.class);

    @Autowired
    private Configuration config;

    @Autowired
    @Qualifier("localBuildAcquirer")
    private ArtifactAcquirer acquirer;

    @Autowired
    private VaultKeeper vaultKeeper;

    @ShellMethod(value = "Build the source code locally. This compiles the source into artifacts for deployment.")
    public void build(
            @ShellOption(
                    value = "repo",
                    defaultValue = ".",
                    help = "Specify the path to the source code repository"
            ) File repoDirectory
    ) {
        logger.info("Build start. {}", repoDirectory.getAbsolutePath());
        int exitCode = acquirer.acquire(repoDirectory.toURI());

        if (exitCode == 0) {
            logger.info("Build complete. {}", repoDirectory.getAbsolutePath());
            vaultKeeper.clear();
            vaultKeeper.store();
        } else {
            logger.info("Build fail. {}", repoDirectory.getAbsolutePath());
        }


    }
}
