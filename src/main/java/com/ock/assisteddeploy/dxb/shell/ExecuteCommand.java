package com.ock.assisteddeploy.dxb.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Map;

@ShellComponent
public class ExecuteCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteCommand.class);

    @ShellMethod(
            value = "Execute a sequence of operations in a single command.",
            key = Commands.VERB_RUN
    )
    public void execute(@ShellOption String... opts) {
        Map<Command, Object> subCommands = new ShellOptionInterpreter().interpret(opts);
        // TODO execute subCommands
    }

    @Override
    public void instruct(Object obj) {
        throw new UnsupportedOperationException();
    }
}
