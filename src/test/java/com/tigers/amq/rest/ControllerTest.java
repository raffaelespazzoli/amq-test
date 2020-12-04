package com.tigers.amq.rest;

import com.google.gson.Gson;
import com.tigers.amq.database.DatabaseConsumer;
import com.tigers.amq.queue.QueueProducer;
import com.tigers.amq.stream.StreamProducer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(Controller.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueueProducer queueProducer;

    @MockBean
    private StreamProducer streamProducer;

    @MockBean
    private DatabaseConsumer databaseConsumer;

    @Test
    public void shouldProcessQueueCall() throws Exception {
        // given

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/queue/{dest}/{name}", QueueDestination.queue.name(), "test")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertThat(HttpStatus.valueOf(result.getResponse().getStatus()), equalTo(HttpStatus.OK));

        Map actual = new Gson().fromJson(result.getResponse().getContentAsString(), Map.class);
        Map expected = new HashMap<String,String>();
        expected.put("sendMessage", "{\"name\":\"test\"}");
        expected.put("status", "success");
        assertThat(actual, equalTo(expected));

        Mockito.verify(queueProducer, Mockito.times(1)).sendMessage(eq(QueueDestination.queue), eq("{\"name\":\"test\"}"));
        Mockito.verify(queueProducer, Mockito.times(1)).sendMessage(any(), anyString());

    }

    @Test
    public void shouldProcessStreamCall() throws Exception {
        // given

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/stream/{dest}/{name}", StreamDestination.request.name(), "test")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertThat(HttpStatus.valueOf(result.getResponse().getStatus()), equalTo(HttpStatus.OK));

        Map actual = new Gson().fromJson(result.getResponse().getContentAsString(), Map.class);
        Map expected = new HashMap<String,String>();
        expected.put("sendMessage", "{\"name\":\"test\"}");
        expected.put("status", "success");
        assertThat(actual, equalTo(expected));

        Mockito.verify(streamProducer, Mockito.times(1)).sendMessage(eq(StreamDestination.request), eq("{\"name\":\"test\"}"));
        Mockito.verify(streamProducer, Mockito.times(1)).sendMessage(any(), anyString());
    }

}
