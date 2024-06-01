package org.kelompokwira.wirakopi.wirakopi.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // @Lazy
    // @Qualifier("userDetailsServiceImpl")
    // @Autowired
    // private UserDetailsService userDetailsService;

    // .jdbcAuthentication().dataSource(ds).passwordEncoder(encoder())
    //     .usersByUsernameQuery("SELECT username, password, enabled FROM wiradb.users WHERE username = ?")
    //     .authoritiesByUsernameQuery("SELECT u.username as username, r.authority as role from wiradb.users u INNER JOIN authorities r ON r.username = u.username where u.username = ?");
    // @Bean
    // public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

    //     AuthenticationManagerBuilder authManagerBuilder =  http.getSharedObject(AuthenticationManagerBuilder.class);
    //     authManagerBuilder.jdbcAuthentication().passwordEncoder(encoder())
    //     .usersByUsernameQuery("SELECT username, password, enabled FROM wiradb.users WHERE username = ?")
    //     .authoritiesByUsernameQuery("SELECT username, role FROM wiradb.users WHERE username = ?");
    //     return authManagerBuilder.build();        
    // }

    // @Bean
    // public UserDetailsService userDetailsService(WiraRepo accountRepository) {
        
    // }

    // @Bean
    // public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
    //     DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    //     authenticationProvider.setUserDetailsService(userDetailsService);
    //     authenticationProvider.setPasswordEncoder(encoder());
    //     return authenticationProvider;
    // }

    // @Bean
    // public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService){
    //     DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
    //     provider.setPasswordEncoder(encoder());
    //     provider.setUserDetailsService(userDetailsService);

    //     return provider;
    // }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((req) -> req.requestMatchers("/auth/**").permitAll()
        .requestMatchers("/static/**").permitAll().requestMatchers("/").permitAll()
        .requestMatchers("/css/**").permitAll().requestMatchers("/img/**").permitAll()
        .requestMatchers("/user/**").hasRole("USER")
        .anyRequest().authenticated())
        .exceptionHandling((error) -> error.accessDeniedPage("/403"))
        .formLogin(
            (login) -> 
            login.loginPage("/auth/login").permitAll()
            .successForwardUrl("/user/userProfile")
        )
        .sessionManagement((session) -> session.maximumSessions(1).expiredUrl("/auth/login?expired=true"))
        .build();
    }

    // @Bean
    // SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     return http.authorizeHttpRequests((req) -> req.requestMatchers("/**").permitAll())
    //     .formLogin((login) -> login.loginPage("/login").permitAll()).build();
    // }

    // @Bean
    // public DaoAuthenticationProvider authProvider() {
    //     DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //     authProvider.setUserDetailsService(userDetailsService);
    //     authProvider.setPasswordEncoder(encoder());
    //     return authProvider;
    // }

    // @Bean
    // SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     return http.csrf((laknat) -> laknat.disable()).authorizeHttpRequests((auth) -> auth.anyRequest().permitAll()).build();
    // }

    // var usersQuery = "select username, password, enabled from users where username = ?";
    // var rolesQuery = "select username, level from users where username = ?";
    // auth.jdbcAuthentication().dataSource(ds)
    // .usersByUsernameQuery(usersQuery).authoritiesByUsernameQuery(rolesQuery);
    // @Autowired
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    //     http.authorizeHttpRequests((auth) -> auth.)
    // }

    // @Bean
    // @Autowired
    // public JdbcUserDetailsManager users(DataSource ds){
    //     JdbcUserDetailsManager jManager = new JdbcUserDetailsManager(ds);
    //     //jManager.createUser();
    //     return jManager;
    // }

    @Bean
    public WebMvcConfigurer cors(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedHeaders(HttpHeaders.CONTENT_TYPE, "X-CSRF-TOKEN")
                        .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name())
                        .allowCredentials(true);
            }

            @Override
            public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry){
                registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
                registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
                registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
                registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
            } 
        };
    }

    @Bean
    PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public AuthenticationManager customAuthenticationManager() throws Exception {
    //     return 
    // }
}