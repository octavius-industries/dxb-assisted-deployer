package com.ock.assisteddeploy.dxb.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ShellOptionInterpreter {

    private static final Logger logger = LoggerFactory.getLogger(ShellOptionInterpreter.class);

    private static final ShellOptionConsumer basicShellOptionConsumer = new BasicShellOptionConsumer();

    private static final ShellOptionConsumer duoShellOptionConsumer = new DuoShellOptionConsumer();


    private static final Map<String, ShellOptionConsumer> consumerMap = Map.of(
            Commands.VERB_BUILD, duoShellOptionConsumer,
            Commands.VERB_GET, basicShellOptionConsumer,
            Commands.VERB_PUT, basicShellOptionConsumer,
            Commands.VERB_DEPLOY, basicShellOptionConsumer
    );

    public Map<Command, Object> interpret(String... options) {

        Map<Command, Object> cmdArgMap = new HashMap<>();
        CountDownLatch latch = new CountDownLatch(options.length);

        while (latch.getCount() > 0) {
            String opt = options[options.length - (int) latch.getCount()];
            ShellOptionConsumer consumer = consumerMap.get(opt);

            if (consumer != null) {
                Map.Entry<Command, Object> cmdArg = consumer.consume(latch, options);
                cmdArgMap.put(cmdArg.getKey(), cmdArg.getValue());
            } else {
                logger.warn("Dropping unknown command \"{}\"", opt);
                latch.countDown();
            }

        }

        return cmdArgMap;
    }
}
