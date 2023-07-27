package com.tistory.jaimemin.springbatch;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    private String location;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
