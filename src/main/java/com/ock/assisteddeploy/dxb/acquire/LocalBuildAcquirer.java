package com.ock.assisteddeploy.dxb.acquire;

import com.ock.assisteddeploy.dxb.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

@Component("localBuildAcquirer")
public class LocalBuildAcquirer implements ArtifactAcquirer {

    private static final Logger logger = LoggerFactory.getLogger(LocalBuildAcquirer.class);

    @Autowired
    private Configuration config;

    @Override
    public int acquire(URI repoUri) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // set working directory to repository
        processBuilder.directory(new File(repoUri));
        // split the whole property by space
        processBuilder.command(config.getBuild().getCommand().split(" "));

        try {
            // start the build process
            Process process = processBuilder.start();
            // print process output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
            //return process exit code
            return process.waitFor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
