package com.sta.xuptsta.service;

public interface EmailService {
    void sendRegisterCode(String email);
    void sendLoginCode(String email);
    void sendPasswordCode(String email);
}
