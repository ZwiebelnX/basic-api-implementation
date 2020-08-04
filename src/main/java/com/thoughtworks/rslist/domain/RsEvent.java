package com.thoughtworks.rslist.domain;

import lombok.Data;

@Data
public class RsEvent {
    private String name;

    private String key;

    public RsEvent(){}

    public RsEvent(String name, String key) {
        this.name = name;
        this.key = key;
    }

}
