package com.marketplace.marketplace.secutiry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/auth/login",
                                "/auth/register",
                                "/trabajo/**",
                                "/productos/**",
                                "/diseno-grafico",
                                "/desarrollo-web",
                                "/ilustraciones",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/oauth2/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/formulario").hasRole("ESTUDIANTE")
                        .requestMatchers("/chat/**").authenticated() // ✅ Protege todo el sistema de chat
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customSuccessHandler())
                        .permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauth2UserService()))
                        .successHandler(customSuccessHandler()))
                .logout(logout -> logout
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // ✅ Redirección según rol luego de login
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                    HttpServletResponse response,
                    Authentication authentication) throws IOException {
                System.out.println("✅ Login exitoso: " + authentication.getName());

                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                for (GrantedAuthority authority : authorities) {
                    if (authority.getAuthority().equals("ROLE_ADMIN")) {
                        response.sendRedirect("/admin/trabajos");
                        return;
                    }
                }
                response.sendRedirect("/");
            }
        };
    }

    // ❌ Mostrar en consola errores si falló el login
    @Bean
    public AuthenticationFailureHandler customFailureHandler() {
        return (request, response, exception) -> {
            System.out.println("❌ Fallo en el login: " + exception.getMessage());
            response.sendRedirect("/auth/login?error=true");
        };
    }

    // ✅ Login con Google, asignar rol
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return userRequest -> {
            OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);
            String email = oauth2User.getAttribute("email");

            System.out.println("✅ Login con Google: " + email);

            List<GrantedAuthority> authorities = new ArrayList<>();
            if ("admin@gmail.com".equalsIgnoreCase(email)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            return new DefaultOAuth2User(authorities, oauth2User.getAttributes(), "email");
        };
    }

    // Servicio para cargar usuarios de base de datos
    @Bean
    public UserDetailsService userDetailsService() {
        return usuarioDetailsService;
    }

    // Codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager necesario para login manual
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(usuarioDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}
