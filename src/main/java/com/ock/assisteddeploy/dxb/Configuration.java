package com.ock.assisteddeploy.dxb;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dad")
public class Configuration {

    private Build build;


    public Build getBuild() {
        return build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }


    public static class Build {

        private String command;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }


    }
}
