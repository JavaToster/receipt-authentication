package com.example.authentication.util.models;

public enum RedisKey {
    RECOVERY_CODE("recovery_code:%d");

    private final String pattern;

    RedisKey(String pattern){
        this.pattern = pattern;
    }


    public String getPattern() {
        return pattern;
    }
}
