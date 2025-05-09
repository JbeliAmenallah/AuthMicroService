package com.example.sfmproject.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType; // Add this import
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String identification;
    private Date expiredDate;
}