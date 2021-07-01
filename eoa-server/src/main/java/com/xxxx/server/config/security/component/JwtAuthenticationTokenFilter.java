package com.xxxx.server.config.security.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: MAQJ
 * @Date: 2021/05/21/18:05
 * @Description: JWT登录授权过滤器
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 前置拦截
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(tokenHeader);
        // 存在token。即token是服务器正确发放的
        if (null != authHeader && authHeader.startsWith(this.tokenHead)) {
            // token的格式是 Bearer xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            // 如果能拿到用户名，但在全局上下文拿不到用户对象，也是判断为无登录
            // 请求的token中可以解析出用户名但未登录，即token能拿到用户名但springsecurity全局上下文不存在用户对象
            if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
                // 无登录走自定义登录逻辑并获取userDetails，实现登录
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                // 验证token是否有效，重新设置用户对象
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    // 判断token是否有效，有效的放入到全局上下文中，放到全局上下文中，下次登录就不需要走登录，直接去验证token
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        // 放行
        filterChain.doFilter(request, response);
    }
}
