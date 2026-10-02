package com.divine.autofind.config;

import com.divine.autofind.repository.ProviderAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/favicon.ico", "/error", "/services/**",
                                "/requests/**", "/providers/register", "/login").permitAll()
                        .requestMatchers("/provider/**").hasRole("PROVIDER")
                        .anyRequest().denyAll())
                .formLogin(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/provider/dashboard", true).permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/?loggedOut"));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(ProviderAccountRepository accounts) {
        return email -> {
            var account = accounts.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Provider not found"));
            return User.withUsername(account.getEmail())
                    .password(account.getPasswordHash())
                    .roles("PROVIDER")
                    .build();
        };
    }
}
