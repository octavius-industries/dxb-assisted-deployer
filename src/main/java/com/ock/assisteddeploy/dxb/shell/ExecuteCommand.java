package com.ock.assisteddeploy.dxb.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.SortedMap;

@Component(Commands.VERB_RUN)
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

        subCommands.forEach((cmd, arg) -> {
            cmd.instruct(arg);
        });
    }

    @Override
    public void instruct(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit((Command) this);
    }
}
