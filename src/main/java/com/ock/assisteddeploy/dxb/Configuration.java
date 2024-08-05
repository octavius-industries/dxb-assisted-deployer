package com.ock.assisteddeploy.dxb;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.net.URL;

@Component
@ConfigurationProperties(prefix = "dad")
public class Configuration {

    private File artifactVault;

    private Build build;

    private Retrieve retrieve;

    public File getArtifactVault() {
        return artifactVault;
    }

    public void setArtifactVault(File artifactVault) {
        this.artifactVault = artifactVault;
    }

    public Build getBuild() {
        return build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public Retrieve getRetrieve() {
        return retrieve;
    }

    public void setRetrieve(Retrieve retrieve) {
        this.retrieve = retrieve;
    }

    public static class Build {

        private String command;

        private List<File> artifacts;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public List<File> getArtifacts() {
            return artifacts;
        }

        public void setArtifacts(List<File> artifacts) {
            this.artifacts = artifacts;
        }
    }

    public static class Retrieve {

        private List<URL> artifacts;

        public List<URL> getArtifacts() {
            return artifacts;
        }

        public void setArtifacts(List<URL> artifacts) {
            this.artifacts = artifacts;
        }
    }
}