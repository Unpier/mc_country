package ru.skillbox.country.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.skillbox.country.dto.JwtRq;

import java.util.Map;

@FeignClient(name = "country", url = "${app.feignClient.url}")
public interface CountryFeignClient {

    @PostMapping("/auth/getclaims")
    Map<String, String> validateToken(@RequestBody JwtRq request);
}
