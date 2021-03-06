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
    private Principal mockPrincipal; // ????????? ??????????????? ????????? ?????? ????????? ????????? ?????? ????????? ????????? ?????? ????????? ??????

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
        // Mock ????????? ?????? ??????
        String username = "q1234";
        String password = "1234";
        RoleEnum role = RoleEnum.USER;
        User testUser = new User(username, password,role);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }   //mockPrincipal ????????? ????????? ?????? UsernamePasswordAuthenticationToken ?????? ??????????????? ?????? ?????? ??????.

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
    @DisplayName("?????? ?????? ????????????")
    void test1() throws Exception {
        // given
        // ?????? ?????? ??????
        this.mockUserSetup(); //String username = "q1234";

        SignupRequestDto dto = new SignupRequestDto();
        //dto.setUsername("q1234"); // ?????? ??? ???
        dto.setUsername("q7890"); // no ??????
        String postInfo = objectMapper.writeValueAsString(dto);  //?????? ?????? ??????

        // when - then
            mvc.perform(post("/user/redunancy")
                            .content(postInfo)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .principal(mockPrincipal) // ?????? ????????? ?????? ?????? ?????? ??????
                    )
                    .andExpect(content().string("success")) // ???????????? fail, ?????? ????????? success
                    .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void test2() throws Exception {
            // given
           // signupRequestDto = redunancysignTestcase1(); //???????????????????????? ?????? ?????? ??????
          // signupRequestDto = redunancysignTestcase2(); // ???????????? ?????? ??????
           // signupRequestDto = redunancysignTestcase3();
           signupRequestDto = redunancysignTestcase4();  // ????????? ?????? ?????? ???????????? ???????????? ????????? ??? ??????????????? ??????

        // when - then
            mvc.perform(post("/user/signup")
                            .params(signupRequestDto)
                    )
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/user/login"))
                    .andDo(print());

    }



}
