package com.tigers.amq.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface EventChannels {

    String STREAM_REQUEST = "stream-request";
    String STREAM_RESPONSE = "stream-response";

    @Input(STREAM_REQUEST)
    MessageChannel streamRequest();

    @Output(STREAM_RESPONSE)
    MessageChannel streamResponse();
}