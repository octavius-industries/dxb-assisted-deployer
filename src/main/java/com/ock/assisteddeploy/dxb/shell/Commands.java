package com.ock.assisteddeploy.dxb.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Commands {

    public static final String VERB_GET = "get";
    public static final String VERB_BUILD = "build";
    public static final String VERB_PUT = "put";
    public static final String VERB_DEPLOY = "deploy";
    public static final String VERB_RUN = "run";


    private static Commands instance;
/*
    private final Map<Verb, Command> commandMap = Map.of(
            Verb.BUILD, new BuildCommand(),
            Verb.GET, new RetrieveCommand(),
            Verb.PUT, new DistributeCommand(),
            Verb.DEPLOY, new DeployCommand(),
            Verb.RUN, new ExecuteCommand()
    );

 */

    private Map<String, Command> commandMap;

    @Autowired
    public Commands(Map<String, Command> commands) {
        this.commandMap = commands;
        Commands.instance = this;
    }

    public static Command from(String verb) {
        return instance.commandMap.get(verb);
    }

    public static Command get(Commands.Verb verb) {
        return instance.commandMap.get(verb.getString().toLowerCase());
    }

    public enum Verb {
        BUILD(Commands.VERB_BUILD),
        GET(Commands.VERB_GET),
        PUT(Commands.VERB_PUT),
        DEPLOY(Commands.VERB_DEPLOY),
        RUN(Commands.VERB_RUN);

        private String verb;

        Verb(String v) {
            this.verb = v;
        }

        public String getString() {
            return verb;
        }
    }
}
