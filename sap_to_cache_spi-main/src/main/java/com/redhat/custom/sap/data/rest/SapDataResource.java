package com.redhat.custom.sap.data.rest;

import java.util.List;
// import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.broker.provider.util.SimpleHttp.Response;
import org.keycloak.models.KeycloakSession;

import com.redhat.custom.sap.data.SapDataRepresentation;
import com.redhat.custom.sap.data.client.hotrod.InfinispanUtils;
import com.redhat.custom.sap.data.spi.CustomSapDataStorageService;
import com.redhat.custom.sap.data.spi.impl.CustomSapDataStorageProviderFactoryImpl;
import com.redhat.custom.sap.data.spi.impl.CustomSapDataStorageServiceImpl;

public class SapDataResource {

    public final KeycloakSession session;
    public final String userId;

    public SapDataResource(KeycloakSession session, String userId) {
        this.session = session;
        this.userId = userId;
    }

    @GET
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    // Return type was changed from Map<SapDataRepresentation, Map<String, Map<String, List<String>>>> to Object
    public Object getSapData() {
        if (session == null) {
            throw new IllegalStateException("The service cannot accept a null session.");
        }

        CustomRealmResourceProvider restProvider = session.getProvider(CustomRealmResourceProvider.class);
        if (restProvider == null) {
            throw new IllegalStateException("The service cannot accept a session without a CustomRealmResourceProvider in its context.");
        }

        CustomSapDataStorageService provider = session.getProvider(CustomSapDataStorageServiceImpl.class);
        if (provider == null) {
            provider = new CustomSapDataStorageProviderFactoryImpl().create(session);
            if (session.getProvider(CustomSapDataStorageServiceImpl.class) == null) {
                throw new IllegalStateException("The service cannot accept a session without a CustomSapDataStorageServiceImpl provider in its context.");
            }
        }
        Response response = provider.getRequest();
        // Map<SapDataRepresentation, Map<String, Map<String, List<String>>>> data = session.getProvider(CustomSapDataStorageService.class).parseData(response, userId);
        SapDataRepresentation data = session.getProvider(CustomSapDataStorageServiceImpl.class).parseData(response, userId);
        List<String> sapData = data.getAllServiceUrls();
        InfinispanUtils hotrodClient = new InfinispanUtils();
        // hotrodClient.insertData("sapData", userId, sapData.toString());
        return hotrodClient.insertData("sapData", userId, sapData.toString());
    }

    // @GET
    // @Path("")
    // @NoCache
    // @Produces(MediaType.APPLICATION_JSON)
    // public Map<SapDataRepresentation, Map<String, Map<String, List<String>>>> getSapData() throws ParseException {
    //     Response response = session.getProvider(CustomSapDataStorageService.class).getRequest();
    //     return session.getProvider(CustomSapDataStorageService.class).parseData(response, userId);
    // }
}
