package com.example.sfmproject.Repositories;

import com.example.sfmproject.DTO.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository

public interface OTPRepository extends JpaRepository<OTP, Long> {

    OTP findByIdentificationAndExpiredDateAfter(String identification, Date now);

    OTP findByIdentification(String identification);
}