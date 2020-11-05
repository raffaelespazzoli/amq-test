package com.tigers.amq.rest;

import com.google.gson.Gson;
import com.tigers.amq.queue.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class Controller {

    @Autowired
    private Producer producer;

    @RequestMapping(method = {RequestMethod.PUT}, path = "/post/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String sendMessage (@PathVariable String name) {
        String message = String.format("{ \"name\":\"%s\"}", name);
        producer.sendMessage("inbound.queue", message);
        Map<String, String> output = new HashMap();
        output.put("status", "success");
        output.put("sendMessage", message);
        return new Gson().toJson(output);
    }

}
