package com.xxxx.server.config.security;

import com.xxxx.server.config.security.component.*;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: MAQJ
 * @Date: 2021/05/21/17:08
 * @Description:
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Autowired
    private CustomFilter customFilter;
    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    /**
     * 配置springsecurity登录逻辑的时候，走自定义的UserDetailsService
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    /**
     * 此方法中指定的端点将被Spring Security忽略
     * 不会保护他们免受CSRF, XSS, Clickjacking等
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login",
                "/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha",
                "/ws/**");
    }

    /**
     * springsecurity完整配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 使用JWT，不需要csrf
        http.csrf().disable()
                // 基于token，不需要session
                .sessionManagement()
                // 会话创建策略设置为STATELESS
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 允许登录设置
                .authorizeRequests()
//                .antMatchers("/login","/logout").permitAll() 上面configure放行了
                // 除了上面的请求，所有请求都要求权限认证
                .anyRequest().authenticated()
                //动态配置权限，即根据请求的url进行拦截
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                     @Override
                     public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                         object.setAccessDecisionManager(customUrlDecisionManager);
                         object.setSecurityMetadataSource(customFilter);
                         return object;
                     }
                 })
                .and()
                //禁用缓存
                .headers()
                .cacheControl();

        //添加jwt 登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                // 无登录异常
                .accessDeniedHandler(restfulAccessDeniedHandler)
                // 无权限访问异常
                .authenticationEntryPoint(restAuthorizationEntryPoint);
    }

    /**
     * 由于登录逻辑（controller）使用的是UserDetailsService的loadUserByUsername，所以重写loadUserByUsername,使用我们的mapper去查询
     * 同时admin继承了UserDetails，所以可以返回Admin
     * 相当于UserDetailsService已使用自定义的，而不是使用默认的
     *
     * @return the {@link UserDetailsService} to use
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return username -> {
            Admin admin = adminService.getAdminByUsername(username);
            if (null != admin) {
                admin.setRoles(adminService.getRoles(admin.getId()));
                return admin;
            }
            throw new UsernameNotFoundException("用户名或密码不正确");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }
}
