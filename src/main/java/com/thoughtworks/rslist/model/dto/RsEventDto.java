package com.thoughtworks.rslist.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RsEventDto {

    @NotEmpty
    private String eventName;

    @NotEmpty
    private String keyWord;

    @Valid
    @NotNull
    private Integer userId;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @JsonIgnore
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
