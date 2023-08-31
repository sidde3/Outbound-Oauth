package org.demo.sso;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String index() {
        return "Hello World";
    }

    @GetMapping(path= "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getVersion(){
        System.out.println("version: 1.0");
        return "Version: 1.0";
    }
}