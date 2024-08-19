package ru.skillbox.country.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class TokenAuthenticationTest {

    @Test
    public void testTokenAuthentication() {
        TokenAuthentication authentication = new TokenAuthentication(
                Collections.singletonList((GrantedAuthority) () -> "ROLE_USER"),
                true,
                "testAccount"
        );

        assertEquals("testAccount", authentication.getName());
        assertTrue(authentication.isAuthenticated());
        assertEquals(1, authentication.getAuthorities().size());
    }
}
