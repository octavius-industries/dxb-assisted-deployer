package com.ock.assisteddeploy.dxb.shell;

public interface Command {

    void instruct(Object obj);

    void accept(CommandVisitor visitor);
}
