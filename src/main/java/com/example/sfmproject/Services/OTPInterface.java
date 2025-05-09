package com.example.sfmproject.Services;

import com.example.sfmproject.DTO.OTP;

public interface OTPInterface {

    OTP GenerateOTp( );
    Boolean VerifOTP ( String identification )  ;
    void userstatus(String emailuser, Boolean result);
    OTP ResendOTP(String email);
    void  DeleteALLOTP();
}
