package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RsEvent {
    private String eventName;

    private String keyWord;

    @Valid
    @NotNull
    @JsonIgnore
    @JsonProperty
    private User user;

    public RsEvent(String eventName, String keyWord,@Valid User user) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }

}
