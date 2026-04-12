package com.atl.map.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class ReissueTokenResponseDto extends ResponseDto {

    private final String token;
    private final int expirationTime;
    private final String refreshToken;
    private final int refreshExpirationTime;

    private ReissueTokenResponseDto(
            String token,
            long expirationTime,
            String refreshToken,
            long refreshExpirationTime
    ) {
        super();
        this.token = token;
        this.expirationTime = Math.toIntExact(expirationTime);
        this.refreshToken = refreshToken;
        this.refreshExpirationTime = Math.toIntExact(refreshExpirationTime);
    }

    public static ResponseEntity<ReissueTokenResponseDto> success(
            String token,
            long expirationTime,
            String refreshToken,
            long refreshExpirationTime
    ) {
        ReissueTokenResponseDto responseBody = new ReissueTokenResponseDto(
                token,
                expirationTime,
                refreshToken,
                refreshExpirationTime
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
