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

public class RemoteServerCallback implements Callback {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServerCallback.class);

    @Autowired
    private VaultKeeper vaultKeeper;

    private Configuration config;

    private CountDownLatch latch;

    public RemoteServerCallback(Configuration config, CountDownLatch latch) {
        this.config = config;
        this.latch = latch;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        latch.countDown();
        throw new RuntimeException(e);
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String artifactUrl = call.request().url().toString();
        String artifactName = FilenameUtils.getName(artifactUrl);
        logger.info("Downloading {}", artifactUrl);

        vaultKeeper.write(response.body(), artifactName);
        latch.countDown();
    }
}
