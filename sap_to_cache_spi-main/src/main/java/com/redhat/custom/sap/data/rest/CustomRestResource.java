package com.redhat.custom.sap.data.rest;

import javax.ws.rs.Path;

import org.keycloak.models.KeycloakSession;
// import org.keycloak.services.managers.AppAuthManager;
// import org.keycloak.services.managers.AuthenticationManager;

public class CustomRestResource {

    private final KeycloakSession session;
    // issues with the below classes. The bundled JAR claims to not contain the classes. Auth will not work for this endpoint.

    // private final AppAuthManager.BearerTokenAuthenticator bearerTokenAuthenticator;
    // private final AuthenticationManager.AuthResult auth;

    public CustomRestResource(KeycloakSession session) {
        this.session = session;
        // this.bearerTokenAuthenticator = new AppAuthManager.BearerTokenAuthenticator(session);
        // this.auth = bearerTokenAuthenticator.authenticate();

    }

    // private void checkRealmAdmin() {
    //     if (auth == null) {
    //         throw new NotAuthorizedException("Bearer token is required");
    //     }
    //     else if (auth.getToken().getRealmAccess() == null || !auth.getToken().getRealmAccess().isUserInRole("admin")) {
    //         throw new ForbiddenException("Does not have admin rights");
    //     }
    // }

    @Path("get-sap-data")
    public SapDataResource getSapDataResource() {
        // checkRealmAdmin();
        // return new SapDataResource(session, auth.getUser().getId());
        return new SapDataResource(session, "userIdHere");
    }
}
