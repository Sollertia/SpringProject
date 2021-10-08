package com.sollertia.noticeboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sollertia.noticeboard.dto.SignupRequestDto;
import com.sollertia.noticeboard.exception.ErrorCode;
import com.sollertia.noticeboard.model.User;
import com.sollertia.noticeboard.repository.UserRepository;
import com.sollertia.noticeboard.service.KakaoUserService;
import com.sollertia.noticeboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;
    private final UserRepository userRepository;


    @Autowired
    public UserController(UserService userService, KakaoUserService kakaoUserService,UserRepository userRepository) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
        this.userRepository = userRepository;
    }

    // 회원 로그인 페이지
    @GetMapping("/user/login") // anonymous 걸어도 불가능
    public String login(Model model) {

        return "login";
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup() {
        return "signup";
    }

    // 아이디 중복확인 테스트용..... 구지 이렇게 까지 해야하나 싶음.
    @PostMapping("/user/redunancy")
    @ResponseBody
    public String redunancy(@RequestBody SignupRequestDto requestDto){
        Optional<User> found = userRepository.findByUsername(requestDto.getUsername());
        if (found.isPresent()) {
            return "fail";
        }
        return "success";
    }



//    // 아이디 중복확인
//    @PostMapping("/user/redunancy")
//    @ResponseBody
//    public String redunancy(@RequestBody SignupRequestDto requestDto){
//        String check = userService.redunancy(requestDto.getUsername());
//        // 테스트에서 생긴 널 오류 처리
//        System.out.println(userService.redunancy(requestDto.getUsername()));
//            if (check.equals("fail")) {
//                return "fail";
//            }
//            return "success";
//    }

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(Model model,
                               @Valid SignupRequestDto requestDto,
                               BindingResult bindingResult) {

        // 비밀번호에 닉네임 포함여부 및 일치확인
        if(requestDto.getPassword().contains(requestDto.getUsername())){
            // 예외처리 사용 해보기
            throw new IllegalArgumentException(ErrorCode.CONTAIN_PASSWORD.getErrorMessage());
//            model.addAttribute("pwdcheckfail","비밀번호에 닉네임이 포함되어서는 안됩니다.");
//            return"signup";
        }else if(!requestDto.getPassword().equals(requestDto.getPasswordcheck())){
            throw new IllegalArgumentException(ErrorCode.CHEK_PASSWORD.getErrorMessage());
//            model.addAttribute("pwdcheckfail","비밀번호확인이 일치하지 않습니다.");
//            return"signup";
        }

        // @Valid 유효성 검사로 생긴 에러 메세지 담기
        List<String> errorMessage = new ArrayList<>();
        if(bindingResult.hasErrors()){
            List<ObjectError> list =  bindingResult.getAllErrors();
            for(ObjectError e : list) {
                errorMessage.add(e.getDefaultMessage());
                // System.out.println(e.getDefaultMessage()); // 테스트코드 때 오류 확인
            }
            model.addAttribute("validMessage",errorMessage);
            return "signup";
        }
            userService.registerUser(requestDto);
        return "redirect:/user/login";
    }

    @GetMapping("/user/kakao/callback") // 카카오 로그인 처리
    public String kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        kakaoUserService.kakaoLogin(code); // 카카오에서 보내주는 코드로 로그인계정 정보 가져오기
        return "redirect:/";
    }











}
