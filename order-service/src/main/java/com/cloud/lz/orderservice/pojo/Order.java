package com.cloud.lz.orderservice.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "`order`")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "money")
    private String money;

}
