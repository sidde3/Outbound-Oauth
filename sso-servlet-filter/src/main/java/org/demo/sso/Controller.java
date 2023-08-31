package org.demo.sso;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.constants.ServiceUrlConstants;
import org.keycloak.representations.AccessToken;

import java.io.IOException;

public class Controller {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public boolean isLoggedIn(HttpServletRequest req) {
        return getSession(req) != null;
    }

    public void handleLogout(HttpServletRequest req) throws ServletException {
        if (isLogoutAction(req)) {
            req.logout();
        }
    }

    public String getPrincipalName(HttpServletRequest request){
        return getSession(request).getToken().getPreferredUsername();
    }

    public boolean isLogoutAction(HttpServletRequest req) {
        return getAction(req).equals("logout");
    }

    public boolean showToken(HttpServletRequest req) {
        return req.getParameter("showToken") != null;
    }

    public AccessToken getIDToken(HttpServletRequest req) {
        return getSession(req).getToken();
    }

    public AccessToken getToken(HttpServletRequest req) {
        return getSession(req).getToken();
    }

    public String getAccountUri(HttpServletRequest req) {
        KeycloakSecurityContext session = getSession(req);
        String baseUrl = getAuthServerBaseUrl(req);
        String realm = session.getRealm();
        return KeycloakUriBuilder.fromUri(baseUrl).path(ServiceUrlConstants.ACCOUNT_SERVICE_PATH)
                .queryParam("referrer", "app-jsp")
                .queryParam("referrer_uri", getReferrerUri(req)).build(realm).toString();
    }

    private String getReferrerUri(HttpServletRequest req) {
        StringBuffer uri = req.getRequestURL();
        String q = req.getQueryString();
        if (q != null) {
            uri.append("?").append(q);
        }
        return uri.toString();
    }

    private String getAuthServerBaseUrl(HttpServletRequest req) {
        AdapterDeploymentContext deploymentContext = (AdapterDeploymentContext) req.getServletContext().getAttribute(AdapterDeploymentContext.class.getName());
        KeycloakDeployment deployment = deploymentContext.resolveDeployment(null);
        return deployment.getAuthServerBaseUrl();
    }

    public String getMessage(HttpServletRequest req) {
        String action = getAction(req);
        if (action.equals("")) return "";
        if (isLogoutAction(req)) return "";

        try {
            return "Message: " + ServiceClient.callService(req, getSession(req), action);
        } catch (ServiceClient.Failure f) {
            return "<span class='error'>" + f.getStatus() + " " + f.getReason() + "</span>";
        }
    }


    private KeycloakSecurityContext getSession(HttpServletRequest req) {
        return (KeycloakSecurityContext) req.getAttribute(KeycloakSecurityContext.class.getName());
    }

    private String getAction(HttpServletRequest req) {
        if (req.getParameter("action") == null) return "";
        return req.getParameter("action");
    }

    public String getTokenString(HttpServletRequest req) throws IOException {
        return mapper.writeValueAsString(getToken(req));
    }

    public Boolean invokeRest(HttpServletRequest req){
        return req.getParameter("invokeRest") != null;
    }

    public String getInfo(HttpServletRequest req) {
        ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = resteasyClient.target("http://localhost:8081/version");
        String tokenString = getSession(req).getTokenString();
        Response response = target.request().header("Authorization","Bearer "+tokenString).get();
        //System.err.printf("Status: %s \n Headers: %s\n", response.getStatus(), response.getHeaders());
        //System.out.printf("Content: %s", response.readEntity(String.class));

        return response.getStatus() == 200 ? response.readEntity(String.class) : "Failed in Execution";
    }
}