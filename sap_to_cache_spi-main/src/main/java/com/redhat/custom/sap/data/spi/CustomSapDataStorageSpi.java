package com.redhat.custom.sap.data.spi;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

public class CustomSapDataStorageSpi implements Spi {
    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "custom-sap-data-storage";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return CustomSapDataStorageService.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return CustomSapDataStorageProviderFactory.class;
    }

}
