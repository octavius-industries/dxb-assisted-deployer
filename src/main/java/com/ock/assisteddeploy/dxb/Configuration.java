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

    private String labelTemplate;

    private Build build;

    private Retrieve retrieve;

    private Distribute distribute;

    private Deploy deploy;

    public File getArtifactVault() {
        return artifactVault;
    }

    public void setArtifactVault(File artifactVault) {
        this.artifactVault = artifactVault;
    }

    public String getLabelTemplate() {
        return labelTemplate;
    }

    public void setLabelTemplate(String labelTemplate) {
        this.labelTemplate = labelTemplate;
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

    public Distribute getDistribute() {

        return distribute;
    }

    public void setDistribute(Distribute distribute) {
        this.distribute = distribute;
    }

    public Deploy getDeploy() {
        return deploy;
    }

    public void setDeploy(Deploy deploy) {
        this.deploy = deploy;
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

    public static class Distribute {

        private String hostname;

        private int port;

        private String user;

        private String password;

        private String deploymentBase;

        private File deploymentSkeleton;

        private String artifactBase;

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDeploymentBase() {
            return deploymentBase;
        }

        public void setDeploymentBase(String deploymentBase) {
            this.deploymentBase = deploymentBase;
        }

        public File getDeploymentSkeleton() {
            return deploymentSkeleton;
        }

        public void setDeploymentSkeleton(File deploymentSkeleton) {
            this.deploymentSkeleton = deploymentSkeleton;
        }

        public String getArtifactBase() {
            return artifactBase;
        }

        public void setArtifactBase(String artifactBase) {
            this.artifactBase = artifactBase;
        }
    }

    public static class Deploy {

        private String hostname;

        private int port;

        private String user;

        private String password;

        private List<String> scripts;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<String> getScripts() {
            return scripts;
        }

        public void setScripts(List<String> scripts) {
            this.scripts = scripts;
        }
    }
}
