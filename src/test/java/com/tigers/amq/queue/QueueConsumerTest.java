package com.tigers.amq.queue;

import com.google.gson.Gson;
import com.tigers.amq.Application;
import com.tigers.amq.rest.QueueDestination;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QueueConsumerTest {

    @Autowired
    JmsTemplate jmsTemplate;

    Stream<Arguments> consumerRequests() {
        return Stream.of(
                Arguments.of("CorrelationId", "Hello test"),
                Arguments.of("random", null)
        );
    }

    @ParameterizedTest
    @MethodSource("consumerRequests")
    void shouldHandleRequestMessageEvent(String correlationID, String expectedResponse) throws Exception {
        // given
        Map<String, String> fields = new HashMap();
        fields.put("name", "test");
        String message = new Gson().toJson(fields);

        // when
        jmsTemplate.send(QueueDestination.queue.queueName, session -> {
            TextMessage txtMsg = session.createTextMessage(message);
            txtMsg.setJMSCorrelationID(correlationID);
            return txtMsg;
        });

        // then
        TextMessage jmsMessage = (TextMessage)jmsTemplate.receive("outbound.queue");

        String actualResponse = jmsMessage == null ? null : jmsMessage.getText();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @SpringBootApplication(exclude={TestSupportBinderAutoConfiguration.class})
    public static class UnitApplication extends Application {}

}
