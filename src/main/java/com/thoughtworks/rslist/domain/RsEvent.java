package com.thoughtworks.rslist.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RsEvent {
    private String eventName;

    private String key;

    public RsEvent(String eventName, String key) {
        this.eventName = eventName;
        this.key = key;
    }

}
