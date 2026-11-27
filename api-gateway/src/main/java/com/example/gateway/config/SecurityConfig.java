package com.example.gateway.config;

import static java.nio.charset.StandardCharsets.UTF_8;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import reactor.core.publisher.Mono;

/**
 * Seguridad del gateway, controlada por propiedades:
 *
 *  gateway.security.enabled=false  -> todo abierto (para desarrollo y primeras pruebas)
 *  gateway.security.enabled=true   -> exige un JWT (HS256) valido en "Authorization: Bearer ..."
 *                                     salvo en las rutas de gateway.security.public-paths
 *
 * El gateway solo VALIDA tokens. Quien los emite (login) es otro microservicio
 * que debe firmar con el mismo secreto.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
SecurityWebFilterChain securityWebFilterChain(
        ServerHttpSecurity http,
        @Value("${gateway.security.enabled:false}") boolean enabled,
        @Value("${gateway.security.public-paths:/actuator/health}") String[] publicPaths) {

    http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable);

    if (enabled) {
        http.authorizeExchange(ex -> ex
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(publicPaths).permitAll()
                .pathMatchers(HttpMethod.DELETE, "/api/v1/edificio/**").hasRole("Administrador")
                .pathMatchers("/api/v1/edificio/**").hasAnyRole("Administrador", "Guardia")
                .anyExchange().authenticated())
            .oauth2ResourceServer(oauth -> oauth.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(rolConverter())));
    } else {
        http.authorizeExchange(ex -> ex.anyExchange().permitAll());
    }
    return http.build();
}

private Converter<Jwt, Mono<AbstractAuthenticationToken>> rolConverter() {
    JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
    authorities.setAuthoritiesClaimName("rol");
    authorities.setAuthorityPrefix("ROLE_");
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(authorities);
    return new ReactiveJwtAuthenticationConverterAdapter(converter);
}

    @Bean
    @ConditionalOnProperty(name = "gateway.security.enabled", havingValue = "true")
    ReactiveJwtDecoder jwtDecoder(@Value("${gateway.security.jwt-secret:}") String secret) {
        if (secret.getBytes(UTF_8).length < 32) {
            throw new IllegalStateException(
                    "gateway.security.jwt-secret (JWT_SECRET) debe tener al menos 32 caracteres para HS256");
        }
        SecretKey key = new SecretKeySpec(secret.getBytes(UTF_8), "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}
