package com.tigers.amq.database;

import com.tigers.amq.database.entity.Account;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, String> {

    @Cacheable("ACCOUNT_STATE")
    Account findByUserName(final String userName);

    @Cacheable("DESCENDING_QUERY")
    @Query("FROM accounts a WHERE a.firstName = ?1 AND a.lastName = ?2 ORDER BY a.userName DESC")
    List<Account> findByFirstNameAndLastNameOrderByUserName(final String firstName, final String lastName);

}
