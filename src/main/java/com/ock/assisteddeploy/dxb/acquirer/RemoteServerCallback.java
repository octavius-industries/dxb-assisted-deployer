package com.ock.assisteddeploy.dxb.acquirer;

import com.ock.assisteddeploy.dxb.Configuration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;

public class RemoteServerCallback implements Callback {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServerCallback.class);

    private static final int SIZE_8KB = 8192;

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
        latch.countDown();
        write(response.body(), new File(config.getArtifactVault(), artifactName));

    }

    // write http response to file
    protected void write(okhttp3.ResponseBody responseBody, File file) throws IOException {
        file.getParentFile().mkdirs();
        try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(file.toPath()))) {
            byte[] buffer = new byte[SIZE_8KB];
            int byteRead;

            while ((byteRead = responseBody.byteStream().read(buffer)) != -1) {
                out.write(buffer, 0, byteRead);
            }
            out.flush();
        }

    }
}
