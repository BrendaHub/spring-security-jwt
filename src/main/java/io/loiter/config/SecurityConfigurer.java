package io.loiter.config;

import io.loiter.service.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.compiler.lir.StandardOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@EnableWebSecurity
@Slf4j
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    // 自定义的UserDetailsService 的service类
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private  Filter jwtRequestFilter;

    // 配置了自定义的用户处理service
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("用户名为： {}",myUserDetailsService.loadUserByUsername("foo").getUsername());
        log.info("用户密码为： {}",myUserDetailsService.loadUserByUsername("foo").getPassword());
        myUserDetailsService.loadUserByUsername("foo").getAuthorities().stream().forEach(item -> {
            System.out.println(item);
        });

        auth.userDetailsService(myUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/authenticate").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 配置用户密码编码
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 这是demo级的密码处理
//        return NoOpPasswordEncoder.getInstance();
        // spring security 默认的密码处理机制
//        return new BCryptPasswordEncoder();
        // 实现项目中密码处理机制， 默认密码处理机制就是上面那种BCryptPasswordEncoder
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // MD4
//        return new org.springframework.security.crypto.password.Md4PasswordEncoder();
//        return new DelegatingPasswordEncoder("MD5", encoders);
    }

}
