package uz.pdp.citypaymentservice.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uz.pdp.citypaymentservice.domain.dto.UserDto;
import uz.pdp.citypaymentservice.domain.dto.UserReadDto;
import uz.pdp.citypaymentservice.domain.entity.token.JwtTokenEntity;
import uz.pdp.citypaymentservice.exception.DataNotFoundException;
import uz.pdp.citypaymentservice.repository.token.JwtTokenRepository;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final RestTemplate restTemplate;
    private final JwtTokenRepository jwtTokenRepository;
    @Value("${services.user-service-url}")
    private String url;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity= new HttpEntity<>(username,httpHeaders);
        return restTemplate.exchange(URI.create(url+"/api/v1/get"), HttpMethod.GET,entity, UserDto.class).getBody();
    }


    public UserReadDto loadByName(String username){
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + "/api/v1/get/user")
                .queryParam("username", username);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JwtTokenEntity jwtTokenEntity = jwtTokenRepository.findById(username).orElseThrow(() -> new DataNotFoundException("Jwt token not found or expired!"));
        httpHeaders.add("authorization","Bearer " + jwtTokenEntity.getToken());
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                UserReadDto.class).getBody();
    }


    public UserReadDto loadById(UUID userId, Principal principal){
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + "/api/v1/get/id")
                .queryParam("id", userId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JwtTokenEntity jwtTokenEntity = jwtTokenRepository.findById(principal.getName()).orElseThrow(() -> new DataNotFoundException("Jwt token not found or expired!"));
        httpHeaders.add("authorization","Bearer " + jwtTokenEntity.getToken());
        HttpEntity<UUID> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                UserReadDto.class).getBody();
    }
}
