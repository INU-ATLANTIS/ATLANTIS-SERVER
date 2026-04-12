package com.atl.map.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.atl.map.config.EmailCertificationRateLimiter;
import com.atl.map.dto.response.auth.ReissueTokenResponseDto;
import com.atl.map.dto.response.auth.SignInResponseDto;
import com.atl.map.handler.GlobalExceptionHandler;
import com.atl.map.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private EmailCertificationRateLimiter emailCertificationRateLimiter;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AuthController authController = new AuthController(authService, emailCertificationRateLimiter);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("이메일 인증 요청 제한 초과 시 429와 에러 코드를 반환한다")
    void emailCertificationRateLimitedReturnsTooManyRequests() throws Exception {
        when(emailCertificationRateLimiter.isAllowed(any())).thenReturn(false);

        mockMvc.perform(post("/api/v1/auth/email-certification")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "test@example.com"
                                }
                                """))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.code").value("TMR"))
                .andExpect(jsonPath("$.message").value("Too many requests."));
    }

    @Test
    @DisplayName("로그인 성공 시 access token과 refresh token을 함께 반환한다")
    void signInReturnsAccessAndRefreshTokens() throws Exception {
        ResponseEntity<? super SignInResponseDto> response =
                SignInResponseDto.success("access-token", 3600, "refresh-token", 604800);
        doReturn(response).when(authService).signIn(any());

        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "test@example.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SU"))
                .andExpect(jsonPath("$.token").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.expirationTime").value(3600))
                .andExpect(jsonPath("$.refreshExpirationTime").value(604800));

        verify(authService).signIn(any());
    }

    @Test
    @DisplayName("재발급 성공 시 새 access token과 refresh token을 반환한다")
    void reissueReturnsNewTokens() throws Exception {
        ResponseEntity<? super ReissueTokenResponseDto> response =
                ReissueTokenResponseDto.success("new-access", 3600, "new-refresh", 604800);
        doReturn(response).when(authService).reissue(any());

        mockMvc.perform(post("/api/v1/auth/reissue")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "refresh-token"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SU"))
                .andExpect(jsonPath("$.token").value("new-access"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh"));

        verify(authService).reissue(any());
    }
}
