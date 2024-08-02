package com.ock.assisteddeploy.dxb.shellcommand;

import com.ock.assisteddeploy.dxb.Configuration;
import com.ock.assisteddeploy.dxb.acquirer.ArtifactAcquirer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;

@ShellComponent
public class BuildCommand {

    @Autowired
    private Configuration config;

    @Autowired
    private ArtifactAcquirer acquirer;

    @ShellMethod(value = "Build the source code locally. This compiles the source into artifacts for deployment.")
    public void build(
            @ShellOption(
                    value = "repo",
                    defaultValue = ".",
                    help = "Specify the path to the source code repository"
            ) File repoDirectory
    ) {
        System.out.println("Build start. " + repoDirectory.getAbsolutePath());
        int exitCode = acquirer.acquire(repoDirectory.toURI());

        if (exitCode == 0) {
            System.out.println("Build complete. " + repoDirectory.getAbsolutePath());
        } else {
            System.out.println("Build fail. " + repoDirectory.getAbsolutePath());
        }


    }
}
