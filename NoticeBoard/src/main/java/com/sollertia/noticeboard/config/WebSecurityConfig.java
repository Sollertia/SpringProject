package com.sollertia.noticeboard.config;

import com.sollertia.noticeboard.model.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // UserService의 HttpHeaders 빈 등록
    @Bean
    public HttpHeaders headers(){
        return new HttpHeaders();
    }

    @Bean // 비밀번호 암호화해서 등록할 때 사용
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
// h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    //스프링 부트 의 Spring IoC 컨테이너 = ApplicationContext 에서 MessageSource를 이미 포함하고 있어서 빈등록 하지 않아도 사용가능
//    @Bean
//    public MessageSource messageSource() {  // 메세지태그 사용하기위한 빈등록
//        ResourceBundleMessageSource MS = new
//                ResourceBundleMessageSource();
//        // MesageSource의 인코딩 방식 설정
//        MS.setDefaultEncoding("utf-8");
//        // 메시지를 관리할 파일 이름 지정
//        // messages라고 지정하면 src/main/resources/messages.properties 로 설정
//        MS.setBasenames("messages");
//        return MS;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/").permitAll()// 로그인 없이 처음 접근 시 게시판 리스트로 접근
// css 폴더를 login 없이 허용
                .antMatchers("/css/**").permitAll()
// js 폴더를 login 없이 허용
                .antMatchers("/js/**").permitAll()
// 회원 관리 API 는 비인증 유저만 접근 가능
                .antMatchers("/user/**").anonymous()
// 게시판 상세 페이지 login 없이 허용
                .antMatchers("/BoardDetail").permitAll()
//               .antMatchers("/user/**").anonymous()  // 비인증 사용자 접근 가능하게 하는 기능
// 그 외 어떤 요청이든 '인증' 필요
                .anyRequest().authenticated()
                .and()
// [로그인 기능]
                .formLogin()
// 로그인 View 제공 (GET /user/login)
                .loginPage("/user/login")
// 로그인 처리 (POST /user/login)
                .loginProcessingUrl("/user/login")
// 로그인 처리 후 성공 시 URL
                .defaultSuccessUrl("/")
// 로그인 처리 후 실패 시 URL
                .failureUrl("/user/login?error")
                .and()
// [로그아웃 기능]
                .logout()
// 로그아웃 요청 처리 URL
                .logoutUrl("/user/logout")
                .permitAll() // 로그아웃은 누구나 접근가능
                .and()
                .exceptionHandling()
// "접근 불가" 페이지 URL 설정 로그인한 유저가 로그인, 회원가입 페이지 접근시 처리
                .accessDeniedPage("/?chk=ylogin");
    }

}
