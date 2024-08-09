package com.ock.assisteddeploy.dxb.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class BasicShellOptionConsumer implements ShellOptionConsumer {

    private static final Logger logger = LoggerFactory.getLogger(BasicShellOptionConsumer.class);

    @Override
    public Map.Entry<Command, Object> consume(CountDownLatch latch, String... opts) {

        String opt = opts[getIndex(latch, opts)];
        Command cmd = Commands.from(opt);

        logger.debug("resolved {} as {}", opt, cmd.getClass().getName());
        next(latch);
        return new AbstractMap.SimpleImmutableEntry<>(cmd, null);
    }

    @Override
    public int getIndex(CountDownLatch latch, String... opts) {
        int index = opts.length - (int) latch.getCount();
        checkIndex(index, opts);
        return index;
    }

    @Override
    public void next(CountDownLatch latch) {
        latch.countDown();
    }

    protected void checkIndex(int index, String... opts) {
        if (index >= opts.length || index < 0) {
            throw new RuntimeException("A option argument is missing.");
        }
    }
}
