package com.fiserve.zelle.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNT")
public class EmployeeDetail {

    @Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID")
    @NotNull
    private Long employeeId;

    @Column(name = "FIRST_NAME")
    @NotNull
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotNull
    private String lastName;

    @Column(name = "EMAIL_ID")
    @NotNull
    private String email;

    @Column(name = "PHONE_NUMBER")
    @NotNull
    private String phoneNumber;

    @Column(name = "DOJ")
    @NotNull
    private Date doj;

    @Column(name = "SALARY")
    @NotNull
    private double salary;


}