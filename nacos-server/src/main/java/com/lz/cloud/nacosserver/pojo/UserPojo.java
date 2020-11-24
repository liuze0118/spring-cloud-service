package com.lz.cloud.nacosserver.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class UserPojo implements Serializable {
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(name = "name")
    private String name;

    @Column(name="nick_name")
    private String nickName;

    @Column(name = "password")
    private String password;

    @Column(name="phone_number")
    private String phoneNumber;

}
