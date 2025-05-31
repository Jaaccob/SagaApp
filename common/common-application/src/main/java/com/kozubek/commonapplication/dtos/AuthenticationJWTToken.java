package com.kozubek.commonapplication.dtos;

public record AuthenticationJWTToken(String access_token, String refresh_token, String id_token, String token_type, long expires_in, long refresh_token_expires_in) {
}
