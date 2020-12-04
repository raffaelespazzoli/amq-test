package com.tigers.amq.stream;

import com.tigers.amq.Application;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StreamConsumerTest {

    @Autowired
    private EventChannels eventChannels;

    @Autowired
    private MessageCollector messageCollector;

    Stream<Arguments> consumerRequests() {
        return Stream.of(
                Arguments.of("prefix.key1.key2.route", "{\"data\":\"test\"}"),
                Arguments.of("prefix.ignore.key2.route", null)
        );
    }

    @ParameterizedTest
    @MethodSource("consumerRequests")
    void shouldHandleRequestMessageEvent(String routeKey, String expectedResponse) throws Exception {
        // given
        Message<String> streamMessage = MessageBuilder.withPayload("{\"name\":\"test\"}")
                .setHeader("correlationID", "correlationID")
                .setHeader("event-type", routeKey)
                .build();

        // when
        eventChannels.streamRequest().send(streamMessage);

        // then
        Message<?> consumerMessage = messageCollector.forChannel(eventChannels.streamResponse()).poll();
        Object actualResponse = consumerMessage == null ? null : consumerMessage.getPayload();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @SpringBootApplication(exclude={TestSupportBinderAutoConfiguration.class})
    public static class UnitApplication extends Application {}

}
