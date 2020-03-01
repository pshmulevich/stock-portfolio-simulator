package com.portfolio.management.app.config.jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthToken {

    private String token;
    private String userName;

    public AuthToken(){
    }

    public AuthToken(String token, String userName){
        this.token = token;
        this.userName = userName;
    }

}
