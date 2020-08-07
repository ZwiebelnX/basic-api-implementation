package com.thoughtworks.rslist.model.po;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "vote")
public class VotePo {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private UserPo userPo;

    @ManyToOne
    private RsEventPo rsEventPo;

    @Column
    private Timestamp voteTime;

    @Column
    private Integer voteNum;
}
