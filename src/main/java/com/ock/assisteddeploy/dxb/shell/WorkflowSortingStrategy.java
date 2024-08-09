package com.ock.assisteddeploy.dxb.shell;

import java.util.Comparator;

public class WorkflowSortingStrategy implements Comparator<Command> {
    @Override
    public int compare(Command cmd1, Command cmd2) {
        WorkflowOrderVisitor orderVisitor1 = new WorkflowOrderVisitor();
        WorkflowOrderVisitor orderVisitor2 = new WorkflowOrderVisitor();

        cmd1.accept(orderVisitor1);
        cmd2.accept(orderVisitor2);

        return orderVisitor1.getOrder() - orderVisitor2.getOrder();
    }
}
