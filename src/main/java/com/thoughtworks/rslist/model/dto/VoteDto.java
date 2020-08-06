package com.thoughtworks.rslist.model.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteDto {

    private Integer voteNum;

    private Integer userId;

    private Timestamp voteTime;
}
