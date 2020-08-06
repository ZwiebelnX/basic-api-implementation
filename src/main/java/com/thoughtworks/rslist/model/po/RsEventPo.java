package com.thoughtworks.rslist.model.po;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    @ManyToOne
    private UserPo userPo;

    @OneToMany(mappedBy = "rsEventPo", cascade = CascadeType.REMOVE)
    private List<VotePo> votePoList;
}
