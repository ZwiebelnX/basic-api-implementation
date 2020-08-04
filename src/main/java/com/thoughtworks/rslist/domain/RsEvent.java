package com.thoughtworks.rslist.domain;

import lombok.Data;

@Data
public class RsEvent {
    private String eventName;

    private String key;

    public RsEvent(){}

    public RsEvent(String eventName, String key) {
        this.eventName = eventName;
        this.key = key;
    }

}
