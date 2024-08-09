package com.ock.assisteddeploy.dxb.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class DuoShellOptionConsumer extends BasicShellOptionConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DuoShellOptionConsumer.class);

    @Override
    public Map.Entry<Command, Object> consume(CountDownLatch latch, String... opts) {
        // first get the command
        String opt = opts[getIndex(latch, opts)];
        Command cmd = Commands.from(opt);
        next(latch);

        // second get the argument
        String arg = opts[getIndex(latch, opts)];
        next(latch);
        logger.debug("resolved {} as {}", opt, cmd.getClass().getName());

        return new AbstractMap.SimpleImmutableEntry<>(cmd, arg);
    }
}
