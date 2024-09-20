package com.example.demo.security;

import com.example.demo.service.email.CustomAuthenticationSuccessHandler;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.function.Function;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class MySecurityConfig {

    public static final Logger logger = LoggerFactory.getLogger(MySecurityConfig.class);

    private final Environment env;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;


    @Autowired
    public MySecurityConfig(
            Environment env, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.env = env;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    // Câu truy vấn SQL để lấy thông tin người dùng
    private static final String USERS_BY_USERNAME_QUERY =
            "SELECT username, password, status_id FROM user WHERE username=?";
    private static final String AUTHORITIES_BY_USERNAME_QUERY =
            "SELECT user.username, role.role_name FROM user INNER JOIN role ON user.role_id = role.id WHERE user.username=?";

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        logger.info(">>> spring.datasource.url={}", env.getProperty("spring.datasource.url"));
        logger.info(">>> spring.datasource.username={}", env.getProperty("spring.datasource.username"));

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(USERS_BY_USERNAME_QUERY);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY);
        return jdbcUserDetailsManager;
    }

    @Bean
    public Function<String, Boolean> emailValidator() {
        return email -> {
            try {
                InternetAddress emailAddr = new InternetAddress(email);
                emailAddr.validate();
                return true;
            } catch (AddressException ex) {
                return false;
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers(
                                        // nơi đặt các request ko cần xác thực
                                        "/layout/**", "/uploads/**", "/scss/**", "/css/**",
                                        "/fonts/**", "/js/**", "/icons/**", "/images/**", "/logo/**",
                                        "/auth/register/**", "/auth/logout", "/test", "/","/bestPost/**",
                                        "/auth/verifycation",  "/auth/verify/**", "/auth/verifyAgain/**",
                                        "/searchRecruitmentTitle", "/searchCompanyName", "/searchCompanyLocation",
                                        "/recruitment/detail/**"

                                ).permitAll()
                                // nơi đặt các request cần xác thực bằng role
                                .requestMatchers("/leaders/**").hasRole("USER")
                                .requestMatchers(
                                        "/recruitment/update/**",
                                        "/systems/**",
                                        "/follow/**",
                                        "/user/**",
                                        "/apply/**",
                                        "/files/**"
                                ).hasAnyRole("USER", "MANAGER")
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // nơi đặt các cấu hình liên quan đến form login
                        .loginPage("/auth/showMyLoginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }
}

