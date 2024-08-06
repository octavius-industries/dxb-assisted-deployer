package com.ock.assisteddeploy.dxb.acquire;

import java.net.URI;

public interface ArtifactAcquirer {

    int acquire(URI uri);
}
