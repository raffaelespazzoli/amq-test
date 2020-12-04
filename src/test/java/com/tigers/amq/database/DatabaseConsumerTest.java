package com.tigers.amq.database;

import com.tigers.amq.database.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DatabaseConsumerTest {
    @Autowired
    private DatabaseConsumer databaseConsumer;

    Stream<Arguments> accountRequests() {
        return Stream.of(
                Arguments.of("test", "first", "last"),
                Arguments.of("empty", null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("accountRequests")
    public void testFindAccount(String userName, String expectedFirstName, String expectedLastName) throws Exception {
        Account account = databaseConsumer.findAccount(userName);
        assertThat(account).isNotNull();
        assertThat(account.getFirstName()).isEqualTo(expectedFirstName);
        assertThat(account.getLastName()).isEqualTo(expectedLastName);
    }

    @Test
    public void testFindByFirstLast() throws Exception {
        List<Account> accounts = databaseConsumer.findByFirstLast("first", "last");
        assertThat(accounts).isNotNull();
        assertThat(accounts.size()).isEqualTo(3);
    }
}
