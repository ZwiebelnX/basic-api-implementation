package com.thoughtworks.rslist.model.po;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rs_event")
public class RsEventPo {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 200)
    private String eventName;

    @Column(length = 100)
    private String keyWord;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserPo userPo;

}
