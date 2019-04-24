package com.example.demo.config;

import com.example.demo.service.UserService;
import com.example.demo.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ProjectName: demo
 * @Package: com.example.demo.config
 * @ClassName: ${TYPE_NAME}
 * @Description: java类作用描述
 * @Author: yueChunPeng
 * @CreateDate: 2018-12-21 09:19
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2018-12-21 09:19
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        基于内存的权限验证
//        auth.inMemoryAuthentication().withUser("admin")
//                .password("admin")
//                .roles("ADMIN");
//        auth.inMemoryAuthentication().withUser("chpyue")
//                .password("admin")
//                .roles("USER");
//        auth.inMemoryAuthentication().withUser("root")
//                .password("admin")
//                .roles("ADMIN");

        auth.userDetailsService(userService).passwordEncoder(new PasswordEncoder(){

            @Override
            public String encode(CharSequence charSequence) {
                return MD5Util.encode((String)charSequence);
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(MD5Util.encode((String)charSequence));
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                //主路径可随意访问
                .antMatchers("/","/toRegister","/register","/login","/login-error",
                        "/product/allProduct","/product/productInfo","/product/findKindProduct",
                        "/admins/**","/users/**","/update/**").permitAll()
                //其他请求需要经过验证
                .anyRequest().authenticated()
                .and()
                //注销可随意访问
                .logout().permitAll()
                .and()
                //允许表单登录
                .formLogin().loginPage("/login").defaultSuccessUrl("/login-allot").failureUrl("/login-error")
                //处理异常,拒绝访问就重定向到403页面
                .and().exceptionHandling().accessDeniedPage("/403");

        //关闭默认csrf认证
        http.csrf().disable();

    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        //不拦截
////         web.ignoring().antMatchers("/js/**","/css/**","/images/**");
//    }
}
