package com.sollertia.noticeboard.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.noticeboard.config.WebSecurityConfig;
import com.sollertia.noticeboard.controller.UserController;
import com.sollertia.noticeboard.dto.SignupRequestDto;
import com.sollertia.noticeboard.model.RoleEnum;
import com.sollertia.noticeboard.model.User;
import com.sollertia.noticeboard.repository.UserRepository;
import com.sollertia.noticeboard.security.UserDetailsImpl;
import com.sollertia.noticeboard.service.KakaoUserService;
import com.sollertia.noticeboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import java.security.Principal;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = {UserController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class UserMvcTest {
    private MockMvc mvc;
    private Principal mockPrincipal; // 스프링 시큐리티의 로그인 객체 따라서 임시로 지정 아이디 중복값 체크 하려면 필요

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    KakaoUserService kakaoUserService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String username = "q1234";
        String password = "1234";
        RoleEnum role = RoleEnum.USER;
        User testUser = new User(username, password,role);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }   //mockPrincipal 로그인 완료된 상태 UsernamePasswordAuthenticationToken 이게 생성되면서 토큰 발급 받음.

    MultiValueMap<String, String> signupRequestDto = new LinkedMultiValueMap<>();

    public  MultiValueMap<String, String> redunancysignTestcase1(){
        signupRequestDto.add("username","we22@");
        signupRequestDto.add("password","1234");
        signupRequestDto.add("passwordcheck","1234");
        return signupRequestDto;
    }

    public  MultiValueMap<String, String> redunancysignTestcase2(){
        signupRequestDto.add("username","we123");
        signupRequestDto.add("password","123");
        signupRequestDto.add("passwordcheck","123");
        return signupRequestDto;
    }

    public  MultiValueMap<String, String> redunancysignTestcase3(){
        signupRequestDto.add("username","qw12");
        signupRequestDto.add("password","qw1234");
        signupRequestDto.add("passwordcheck","qw1234");
        return signupRequestDto;
    }

    public  MultiValueMap<String, String> redunancysignTestcase4(){
        signupRequestDto.add("username","wonbin");
        signupRequestDto.add("password","1234");
        signupRequestDto.add("passwordcheck","1234");
        return signupRequestDto;
    }

    @Test
    @DisplayName("유저 이름 중복확인")
    void test1() throws Exception {
        // given
        // 임시 유저 생성
        this.mockUserSetup(); //String username = "q1234";

        SignupRequestDto dto = new SignupRequestDto();
        //dto.setUsername("q1234"); // 중복 일 때
        dto.setUsername("q7890"); // no 중복
        String postInfo = objectMapper.writeValueAsString(dto);  //객체 타입 변환

        // when - then
            mvc.perform(post("/user/redunancy")
                            .content(postInfo)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .principal(mockPrincipal) // 임의 생성한 유저 객체 같이 넘김
                    )
                    .andExpect(content().string("success")) // 중복이면 fail, 중복 아니면 success
                    .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 요청 처리")
    void test2() throws Exception {
            // given
           // signupRequestDto = redunancysignTestcase1(); //알파벳대소문자와 숫자 확인 오류
          // signupRequestDto = redunancysignTestcase2(); // 패스워드 길이 오류
           // signupRequestDto = redunancysignTestcase3();
           signupRequestDto = redunancysignTestcase4();  // 이것만 정상 출력 나머지는 비밀번호 불일치 및 유효성검사 걸림

        // when - then
            mvc.perform(post("/user/signup")
                            .params(signupRequestDto)
                    )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/user/login"))
                    .andDo(print());

    }



}
