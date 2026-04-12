package com.atl.map.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

class SecurityErrorResponseTest {

    @Test
    @DisplayName("인증 실패 entry point는 401과 AR 코드를 반환한다")
    void failedAuthenticationEntryPointReturnsUnauthorizedJson() throws Exception {
        FailedAuthenticationEntryPoint entryPoint = new FailedAuthenticationEntryPoint();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(request, response, new BadCredentialsException("bad credentials"));

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContentAsString()).contains("\"code\":\"AR\"");
        assertThat(response.getContentAsString()).contains("Authentication is required.");
    }

    @Test
    @DisplayName("인가 실패 handler는 403과 NF 코드를 반환한다")
    void failedAccessDeniedHandlerReturnsForbiddenJson() throws Exception {
        FailedAccessDeniedHandler accessDeniedHandler = new FailedAccessDeniedHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        accessDeniedHandler.handle(request, response, new AccessDeniedException("forbidden"));

        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContentAsString()).contains("\"code\":\"NF\"");
        assertThat(response.getContentAsString()).contains("Do not have Permission.");
    }
}
