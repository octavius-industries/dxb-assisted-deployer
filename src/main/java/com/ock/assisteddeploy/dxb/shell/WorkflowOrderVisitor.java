package com.ock.assisteddeploy.dxb.shell;

public class WorkflowOrderVisitor implements CommandVisitor {

    private int order = 0;

    @Override
    public void visit(Command cmd) {
    }

    @Override
    public void visit(AcquireSupport cmd) {
        order = 1;
    }

    @Override
    public void visit(DistributeSupport cmd) {
        order = 2;
    }


    @Override
    public void visit(DeploySupport cmd) {
        order = 3;
    }

    public int getOrder() {
        return order;
    }

}
