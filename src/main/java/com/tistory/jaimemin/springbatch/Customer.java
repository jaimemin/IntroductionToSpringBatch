package com.tistory.jaimemin.springbatch;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private int age;

    @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY)
    private Address address;
}
