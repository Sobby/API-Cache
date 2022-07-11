package com.redhat.custom.sap.data.client.hotrod;

import static org.infinispan.client.hotrod.logging.Log.HOTROD;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.infinispan.commons.util.FileLookupFactory;
import org.infinispan.commons.util.Util;
import org.infinispan.client.hotrod.configuration.Configuration;

public class InfinispanUtils {

    private RemoteCacheManager remoteCacheManager;
    public static final String HOTROD_CLIENT_PROPERTIES = "hotrod-client.properties";

    public InfinispanUtils() {
        initRemoteCacheManager();
    }

    private void initRemoteCacheManager() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        builder.classLoader(cl);
        InputStream stream = FileLookupFactory.newInstance().lookupFile(HOTROD_CLIENT_PROPERTIES, cl);
        if (stream == null) {
            HOTROD.couldNotFindPropertiesFile(HOTROD_CLIENT_PROPERTIES);
        }
        else {
            try {
				builder.withProperties(loadFromStream(stream));
			} finally {
				Util.close(stream);
			}
        }
        // getAllSerializationContextInitializers().stream().forEach(builder::addContextInitializer);
		Configuration config = builder.build();
		remoteCacheManager = new RemoteCacheManager(config);
		remoteCacheManager.getConfiguration().marshallerClass();
    }

    private Properties loadFromStream(InputStream stream) {
		Properties properties = new Properties();
		try {
			properties.load(stream);
		} catch (IOException e) {
			throw new HotRodClientException("Issues configuring from client hotrod-client.properties", e);
		}
		return properties;
	}

    public Object getData(String cacheName, String key) {
		if (remoteCacheManager == null) {
			initRemoteCacheManager();
		}
		RemoteCache<String, Object> cache = remoteCacheManager.getCache(cacheName);
		// return cache.values().iterator().next();
		System.out.println("Getting cached data from '" + cacheName + "' for the key: " + key);
		return cache.get(key);
	}

    public Object insertData(String cacheName, String key, Object value) {
		if (remoteCacheManager == null) {
			initRemoteCacheManager();
		}
		RemoteCache<String, Object> cache = remoteCacheManager.getCache(cacheName);
		return cache.put(key, value);
	}

    public Set<String> getCacheNames() {
		if (remoteCacheManager == null) {
			initRemoteCacheManager();
		}
		return remoteCacheManager.getCacheNames();
	}
}
