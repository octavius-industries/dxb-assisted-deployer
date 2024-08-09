package com.ock.assisteddeploy.dxb.shell;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

public interface ShellOptionConsumer {

    Map.Entry<Command, Object> consume(CountDownLatch latch, String... opts);

    int getIndex(CountDownLatch latch, String... opts);

    void next(CountDownLatch latch);
}
