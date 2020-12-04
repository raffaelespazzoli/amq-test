package com.tigers.amq.rest;

import com.tigers.amq.stream.EventChannels;

public enum StreamDestination {

    request(EventChannels.STREAM_REQUEST),
    response(EventChannels.STREAM_RESPONSE);

    public String streamName;

    StreamDestination(String streamName) {
        this.streamName = streamName;
    }
}
