package com.learnwebclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Integer age;
    private String firstName;
    private String gender;
    private Integer id;
    private String lastName;
    private String role;
}
