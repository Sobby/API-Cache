package com.redhat.custom.sap.data.spi.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import com.redhat.custom.sap.data.spi.CustomSapDataStorageProviderFactory;
import com.redhat.custom.sap.data.spi.CustomSapDataStorageService;

public class CustomSapDataStorageProviderFactoryImpl implements CustomSapDataStorageProviderFactory, ServerInfoAwareProviderFactory {

    private static final Logger logger = Logger.getLogger(CustomSapDataStorageProviderFactoryImpl.class);
    public String apiEndpoint = "";
    public String apiKey = "";
    // Try loading in a properties file from the classpath?
    // public static final String CUSTOM_SAP_DATA_PROPERTIES = "custom-sap-data-storage.properties";
    // public Properties properties;

    // private Properties loadFromStream(InputStream inputStream) {
    //     Properties properties = new Properties();
    //     try {
    //         properties.load(inputStream);
    //     } catch (Exception e) {
    //         logger.error("Error loading properties from stream", e);
    //     }
    //     return properties;
    // }

    @Override
    public void init(Config.Scope config) {
        logger.info("Initializing factory >>>>>>");
        // properties = loadFromStream(getClass().getClassLoader().getResourceAsStream(CUSTOM_SAP_DATA_PROPERTIES));
        String apiEndpoint = config.get("apiEndpoint");
        String apiKey = config.get("apiKey");
        this.apiEndpoint = apiEndpoint;
        this.apiKey = apiKey;
        logger.info("<<<<<< Got API endpoint: " + apiEndpoint + " and API key: " + apiKey + " >>>>>>");
    }

    @Override
    public CustomSapDataStorageService create(KeycloakSession session) {
        try {
            // InitialContext ctx = new InitialContext();
            // CustomSapDataStorageProvider provider = (CustomSapDataStorageProvider) ctx
            //         .lookup("java:global/custom-sap-data-storage/"
            //                 + CustomSapDataStorageProvider.class.getSimpleName());
            // provider.setSession(session);
            // provider.setApiEndpoint(apiEndpoint);
            // provider.setApiKey(apiKey);
            CustomSapDataStorageService provider = new CustomSapDataStorageServiceImpl(session);
            provider.setApiEndpoint(apiEndpoint);
            provider.setApiKey(apiKey);
            logger.info("<<<<<< Creating factory >>>>>>");
            return provider;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {
        logger.info("<<<<<< Closing factory");
    }

    @Override
    public String getId() {
        return "custom-sap-data-storage";
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        Map<String, String> ret = new LinkedHashMap<>();
        ret.put("API Endpoint", apiEndpoint);
        return ret;
    }
}
