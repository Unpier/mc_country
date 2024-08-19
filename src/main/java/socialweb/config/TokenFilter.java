package socialweb.config;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import socialweb.dto.JwtRq;
import socialweb.feign.CountryFeignClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class TokenFilter extends GenericFilterBean {

    private final CountryFeignClient feignClient;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authenticate((HttpServletRequest) servletRequest));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Authentication authenticate(HttpServletRequest request) {
        String token = getToken(request);
        TokenAuthentication authentication = new TokenAuthentication(null, false, null);
        if (token != null) {
            try {
                Map<String, String> tokenPayload = feignClient.validateToken(new JwtRq(token));
                authentication = new TokenAuthentication(
                        Arrays.stream(tokenPayload.get("authorities").split(","))
                                .map(authority -> (GrantedAuthority) () -> authority)
                                .toList(),
                        true,
                        tokenPayload.get("principal")
                );
            } catch (FeignException e) {
                log.info("ошибка валидации токена");
            }
        }
        return authentication;
    }

    public String getToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
//        Cookie[] cookies = request.getCookies();
//        return cookies != null ? Arrays.stream(cookies)
//                .filter(cookie -> cookie.getName().equals("jwt"))
//                .findFirst()
//                .map(Cookie::getValue)
//                .orElse(null) : null;
    }
}
