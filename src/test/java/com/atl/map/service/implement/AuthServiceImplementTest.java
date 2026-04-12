package com.atl.map.service.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.atl.map.dto.request.auth.ReissueTokenRequestDto;
import com.atl.map.dto.request.auth.SignInRequestDto;
import com.atl.map.dto.response.auth.ReissueTokenResponseDto;
import com.atl.map.dto.response.auth.SignInResponseDto;
import com.atl.map.entity.UserEntity;
import com.atl.map.exception.BusinessException;
import com.atl.map.exception.ErrorCode;
import com.atl.map.provider.EmailProvider;
import com.atl.map.provider.JwtProvider;
import com.atl.map.repository.FavoriteRepository;
import com.atl.map.repository.NotificationRepository;
import com.atl.map.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplementTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailProvider emailProvider;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private StringRedisTemplate stringRedisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    private AuthServiceImplement authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImplement(
                userRepository,
                emailProvider,
                jwtProvider,
                favoriteRepository,
                notificationRepository,
                stringRedisTemplate
        );
    }

    @Test
    @DisplayName("로그인 성공 시 refresh token을 Redis에 저장하고 토큰 정보를 반환한다")
    void signInStoresRefreshTokenInRedis() {
        SignInRequestDto requestDto = new SignInRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password123");

        UserEntity userEntity = new UserEntity(
                1,
                "test@example.com",
                new BCryptPasswordEncoder().encode("password123"),
                null,
                null,
                "tester",
                "ROLE_USER",
                null,
                0
        );

        when(userRepository.findByEmail("test@example.com")).thenReturn(userEntity);
        when(jwtProvider.createAccessToken("test@example.com")).thenReturn("access-token");
        when(jwtProvider.createRefreshToken("test@example.com")).thenReturn("refresh-token");
        when(jwtProvider.getAccessTokenExpirationSeconds()).thenReturn(3600L);
        when(jwtProvider.getRefreshTokenExpirationSeconds()).thenReturn(604800L);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);

        ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestDto);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        SignInResponseDto responseBody = (SignInResponseDto) response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getToken()).isEqualTo("access-token");
        assertThat(responseBody.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(responseBody.getExpirationTime()).isEqualTo(3600);
        assertThat(responseBody.getRefreshExpirationTime()).isEqualTo(604800);

        verify(valueOperations).set(
                eq("refresh-token:test@example.com"),
                eq("refresh-token"),
                eq(Duration.ofSeconds(604800L))
        );
    }

    @Test
    @DisplayName("refresh token 검증 실패 시 INVALID_REFRESH_TOKEN 예외를 던진다")
    void reissueThrowsWhenRefreshTokenIsInvalid() {
        ReissueTokenRequestDto requestDto = new ReissueTokenRequestDto();
        requestDto.setRefreshToken("invalid-refresh-token");

        when(jwtProvider.validateRefreshToken("invalid-refresh-token")).thenReturn(null);

        assertThatThrownBy(() -> authService.reissue(requestDto))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    @Test
    @DisplayName("재발급 성공 시 저장된 refresh token을 교체하고 새 토큰을 반환한다")
    void reissueReplacesStoredRefreshToken() {
        ReissueTokenRequestDto requestDto = new ReissueTokenRequestDto();
        requestDto.setRefreshToken("stored-refresh-token");

        when(jwtProvider.validateRefreshToken("stored-refresh-token")).thenReturn("test@example.com");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("refresh-token:test@example.com")).thenReturn("stored-refresh-token");
        when(jwtProvider.createAccessToken("test@example.com")).thenReturn("new-access-token");
        when(jwtProvider.createRefreshToken("test@example.com")).thenReturn("new-refresh-token");
        when(jwtProvider.getAccessTokenExpirationSeconds()).thenReturn(3600L);
        when(jwtProvider.getRefreshTokenExpirationSeconds()).thenReturn(604800L);

        ResponseEntity<? super ReissueTokenResponseDto> response = authService.reissue(requestDto);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        ReissueTokenResponseDto responseBody = (ReissueTokenResponseDto) response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getToken()).isEqualTo("new-access-token");
        assertThat(responseBody.getRefreshToken()).isEqualTo("new-refresh-token");

        verify(valueOperations).set(
                eq("refresh-token:test@example.com"),
                eq("new-refresh-token"),
                eq(Duration.ofSeconds(604800L))
        );
    }
}
