package ru.skillbox.country.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.skillbox.country.dto.JwtRq;
import ru.skillbox.country.feign.CountryFeignClient;

import jakarta.servlet.FilterChain;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenFilterTest {

    private TokenFilter tokenFilter;
    private CountryFeignClient feignClient;

    @BeforeEach
    public void setup() {
        feignClient = mock(CountryFeignClient.class);
        tokenFilter = new TokenFilter(feignClient);
    }

    @Test
    public void testDoFilter() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testToken");
        FilterChain filterChain = mock(FilterChain.class);

        when(feignClient.validateToken(any(JwtRq.class)))
                .thenReturn(Map.of("authorities", "ROLE_USER", "principal", "testUser"));

        tokenFilter.doFilter(request, null, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("testUser", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    public void testGetToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testToken");

        String token = tokenFilter.getToken(request);

        assertEquals("testToken", token);
    }
}