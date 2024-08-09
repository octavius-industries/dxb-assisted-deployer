package com.ock.assisteddeploy.dxb.shell;

public interface CommandVisitor {

    void visit(Command cmd);

    void visit(AcquireSupport cmd);

    void visit(DistributeSupport cmd);

    void visit(DeploySupport cmd);
}
