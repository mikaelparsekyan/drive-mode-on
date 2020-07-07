package com.project.drivemodeon.config.security;

import com.project.drivemodeon.service.impl.user.UserDetail;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetail userDetailsService;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserDetail userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/feed").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/", "/signup", "/signin").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/signin")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/feed")
                .failureUrl("/signin?error")
                .and()
                .logout()
                .logoutSuccessUrl("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/css/**",
                "/fonts/**", "/icons/**", "/js/**", "/photos/**");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
