package com.tistory.jaimemin.springbatch;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class Customer {

    private Long id;

    private String firstName;

    private String lastName;

    private Date birthDate;

}
