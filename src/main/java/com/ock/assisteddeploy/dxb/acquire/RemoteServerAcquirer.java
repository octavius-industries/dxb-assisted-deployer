package com.ock.assisteddeploy.dxb.acquire;

import com.ock.assisteddeploy.dxb.Configuration;
import com.ock.assisteddeploy.dxb.VaultKeeper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component("remoteServerAcquirer")
public class RemoteServerAcquirer implements ArtifactAcquirer {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServerAcquirer.class);

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private Configuration config;

    @Autowired
    private VaultKeeper vaultKeeper;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public int acquire(URI uri) {
        vaultKeeper.clear();
        List<URL> urls = config.getRetrieve().getArtifacts();
        CountDownLatch latch = new CountDownLatch(urls.size());

        for (URL artifactsUrl : urls) {
            Request request = new Request.Builder()
                    .url(artifactsUrl)
                    .build();

            RemoteServerCallback callback = new RemoteServerCallback(config, latch);
            beanFactory.autowireBean(callback);
            client.newCall(request).enqueue(callback);
        }
        try {
            latch.await();
            logger.info("Artifacts are stored into vault: {}", config.getDedicatedVault().getAbsolutePath());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }


}