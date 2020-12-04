package com.tigers.amq.rest;

import com.google.gson.Gson;
import com.tigers.amq.database.DatabaseConsumer;
import com.tigers.amq.database.entity.Account;
import com.tigers.amq.queue.QueueProducer;
import com.tigers.amq.stream.StreamProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class Controller {

    @Autowired
    private QueueProducer queueProducer;

    @Autowired
    private StreamProducer streamProducer;

    @Autowired
    private DatabaseConsumer databaseConsumer;

    @RequestMapping(method = {RequestMethod.PUT}, path = "/queue/{dest}/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String sendQueueMessage (@PathVariable QueueDestination dest, @PathVariable String name) {
        Map<String, String> fields = new HashMap();
        fields.put("name", name);
        String message = new Gson().toJson(fields);
        queueProducer.sendMessage(dest, message);
        Map<String, String> output = new HashMap();
        output.put("status", "success");
        output.put("sendMessage", message);
        return new Gson().toJson(output);
    }

    @RequestMapping(method = {RequestMethod.PUT}, path = "/stream/{dest}/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String sendStreamMessage (@PathVariable StreamDestination dest, @PathVariable String name) {
        Map<String, String> fields = new HashMap();
        fields.put("name", name);
        String message = new Gson().toJson(fields);
        streamProducer.sendMessage(dest, message);
        Map<String, String> output = new HashMap();
        output.put("status", "success");
        output.put("sendMessage", message);
        return new Gson().toJson(output);
    }

    @RequestMapping(method = {RequestMethod.GET}, path = "/database/user/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String findAccountByUser (@PathVariable String name) {
        Account account = databaseConsumer.findAccount(name);
        return new Gson().toJson(account);
    }

    @RequestMapping(method = {RequestMethod.GET}, path = "/database/name/{firstName}/{lastName}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String findAccountByName (@PathVariable String firstName, @PathVariable String lastName) {
        List<Account> accounts = databaseConsumer.findByFirstLast(firstName, lastName);
        return new Gson().toJson(accounts);
    }

}
