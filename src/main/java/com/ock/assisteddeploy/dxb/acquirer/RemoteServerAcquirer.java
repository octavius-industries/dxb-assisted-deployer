package com.ock.assisteddeploy.dxb.acquirer;

import com.ock.assisteddeploy.dxb.Configuration;
import com.ock.assisteddeploy.dxb.VaultKeeper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component("remoteServerAcquirer")
public class RemoteServerAcquirer implements ArtifactAcquirer {

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

            client.newCall(request).enqueue(new RemoteServerCallback(config, latch));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }


}