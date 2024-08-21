package com.ock.assisteddeploy.dxb.invoke;

import com.jcraft.jsch.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class RemoteShellLoggingThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(RemoteShellLoggingThread.class);

    private static final String ANSI_ESC_SEQUENCE = "\\u001B\\[[;?0-9]*[A-Za-z]";

    private static final int TIMEOUT_IN_SECOND = 5;

    private Channel channel;

    public RemoteShellLoggingThread(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()))) {

            while (!executor.isShutdown()) {
                Future<String> futureRead = executor.submit(() -> reader.readLine());

                try {
                    String line = futureRead.get(TIMEOUT_IN_SECOND, TimeUnit.SECONDS);
                    line = line.replaceAll(ANSI_ESC_SEQUENCE, "");
                    if (!line.isEmpty()) {
                        logger.info(line);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    futureRead.cancel(true);
                    executor.shutdown();
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}