package com.thoughtworks.rslist.model.po;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "user")
public class UserPo {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 40)
    private String name;

    @Column(length = 15)
    private String gender;

    @Column
    private int age;

    @Column(length = 30)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column
    private Integer voteNum;

    @OneToMany(mappedBy = "userPo", cascade = CascadeType.REMOVE)
    private List<RsEventPo> rsEventPoList;

    @OneToMany(mappedBy = "userPo", cascade = CascadeType.REMOVE)
    private List<VotePo> votePoList;
}
