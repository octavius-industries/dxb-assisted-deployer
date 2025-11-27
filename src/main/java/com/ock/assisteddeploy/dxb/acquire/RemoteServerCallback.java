package com.ock.assisteddeploy.dxb.acquire;

import com.ock.assisteddeploy.dxb.Configuration;
import com.ock.assisteddeploy.dxb.VaultKeeper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class RemoteServerCallback implements Callback {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServerCallback.class);

    @Autowired
    private VaultKeeper vaultKeeper;

    private Configuration config;

    private CountDownLatch latch;

    private AtomicInteger successedCount;

    public RemoteServerCallback(Configuration config, CountDownLatch latch, AtomicInteger count) {
        this.config = config;
        this.latch = latch;
        this.successedCount = count;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        latch.countDown();
        String artifactUrl = call.request().url().toString();
        logger.info("Downloading {}", artifactUrl);
        logger.error("Connection failed", new RuntimeException(e));
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        try {
            String artifactUrl = call.request().url().toString();
            logger.info("Downloading {}", artifactUrl);
            if (!response.isSuccessful()) {
                logger.error("UnSuccessful callback", new IOException("Unexpected response: " + response));
                return;
            }

            String artifactName = FilenameUtils.getName(artifactUrl);
            vaultKeeper.write(response.body(), artifactName);
            successedCount.incrementAndGet();
        } finally {
            latch.countDown();
        }

    }
}
