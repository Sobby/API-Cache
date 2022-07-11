package com.redhat.custom.sap.data.spi.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.jboss.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

import com.redhat.custom.sap.data.SapDataRepresentation;
import com.redhat.custom.sap.data.spi.CustomSapDataStorageService;

import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.broker.provider.util.SimpleHttp.Response;
import org.keycloak.connections.httpclient.HttpClientProvider;

public class CustomSapDataStorageServiceImpl implements CustomSapDataStorageService {

    private final CloseableHttpClient httpClient;
    private final KeycloakSession session;
    private static final Logger logger = Logger.getLogger(CustomSapDataStorageServiceImpl.class);
    private String apiEndpoint;
    private String apiKey;
    private final SapDataRepresentation sapDataRepresentation = new SapDataRepresentation();

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public CustomSapDataStorageServiceImpl(KeycloakSession session) {
        this.session = session;
        this.httpClient = session.getProvider(HttpClientProvider.class).getHttpClient();
        if (getRealm() == null) {
            logger.error("Realm is null");
            throw new IllegalStateException("The service cannot accept a session without a realm in its context.");
        }
    }

    protected RealmModel getRealm() {
        return session.getContext().getRealm();
    }

    @Override
    // return type was changed SapDataRepresentation to from Map<SapDataRepresentation, Map<String, Map<String, List<String>>>>
    public SapDataRepresentation parseData(Response response, String userId) {
        logger.info("<<<<<< Attempting to parse data >>>>>>");

        try {
            // Map<SapDataRepresentation, Map<String, Map<String, List<String>>>> ret = new HashMap<SapDataRepresentation, Map<String, Map<String, List<String>>>>();
            String inline = response.asJson().toPrettyString();

            JSONParser parse = new JSONParser();
            JSONObject dataObject = (JSONObject) parse.parse(inline);

            JSONArray json = (JSONArray) dataObject.get("wans");
            Map<String, Map<String, List<String>>> wanMap = null;
            Map<String, List<String>> homesiteMap = null;

            for (Object o : json) {
                JSONObject jsonWan = (JSONObject) o;
                String wanName = (String) jsonWan.get("wan_name");
                sapDataRepresentation.addWan(wanName);
                wanMap = new HashMap<>();
                JSONArray jsonHomesites = (JSONArray) jsonWan.get("homesites");
                homesiteMap = new HashMap<>();

                for (Object homesite : jsonHomesites) {
                    JSONObject jsonHomesite = (JSONObject) homesite;
                    String homesiteName = (String) jsonHomesite.get("homesite_name");
                    sapDataRepresentation.getWan(wanName).addHomesite(homesiteName);
                    JSONObject jsonServices = (JSONObject) jsonHomesite.get("services");
                    List<String> serviceUrls = new ArrayList<>();

                    for (int s = 0; s < jsonServices.size(); s++) {
                        JSONObject jsonService = (JSONObject) jsonServices.get(s);
                        sapDataRepresentation.getWan(wanName).getHomesite(homesiteName).addService((String) jsonService.get("service_id"), (String) jsonService.get("service_url"));
                        serviceUrls.add(jsonService.get("service_url").toString());
                    }
                    homesiteMap.put(homesiteName, serviceUrls);
                }
                wanMap.put(wanName, homesiteMap);
                // ret.put(sapDataRepresentation, wanMap);
                logger.info("<<<<<< Parsed data >>>>>>");
                logger.info(homesiteMap.toString());
            }
            // return ret;
            return sapDataRepresentation;
        }
        catch (IOException | ParseException i) {
            logger.error(i);
            i.printStackTrace();
        }
        return null;
    }

    @Override
    public Response getRequest(){
        logger.info("<<<<<< Sending request to " + apiEndpoint + " >>>>>>");
        try {
            Response response = SimpleHttp.doGet(apiEndpoint, httpClient).header("x-api-key", apiKey).asResponse();
            if (response.getStatus() != 200) {
                RuntimeException cause = new RuntimeException("HTTP GET request was not successful:" + apiEndpoint);
                logger.error("<<<<<< Request to " + apiEndpoint + " failed >>>>>>");
                throw new WebApplicationException(cause, response.getStatus());
            }
            return response;
        } catch (IOException u) {
            logger.error(u);
            u.printStackTrace();
        }
        return null;
    }

    public void close() {

    }
}
