# Sample Oauth Integration with Keycloak
#### This project contains two application
- Servlet based Web Application
  - This application uses keycloak servlet-filter for the Oauth integration with Keycloak.
  ```xml
    <filter>
        <filter-name>Keycloak Filter</filter-name>
        <filter-class>org.keycloak.adapters.servlet.KeycloakOIDCFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>Keycloak Filter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
  ```
- Spring Boot Application
  - This is a simple REST API exposed and secured with Keycloak. 

**Note:** Both the application is integrated using a single client in Keycloak. ``sso-servlet-filter.json`` can be imported to make the client configuration. 
