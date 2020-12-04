package com.tigers.amq.database;

import com.tigers.amq.database.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseConsumer {
    private static Logger log = LoggerFactory.getLogger(DatabaseConsumer.class);

    @Autowired
    AccountRepository repository;

    public Account findAccount(String userName) {
        log.info("Query database for account: "+userName);
        return repository.findByUserName(userName);
    }

    public List<Account> findByFirstLast(String firstName, String lastName) {
        log.info("Query database by name: "+firstName+":"+lastName);
        return repository.findByFirstNameAndLastNameOrderByUserName(firstName, lastName);
    }
}
