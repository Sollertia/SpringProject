package com.sollertia.noticeboard.service;

import com.sollertia.noticeboard.dto.SignupRequestDto;
import com.sollertia.noticeboard.model.RoleEnum;
import com.sollertia.noticeboard.model.User;
import com.sollertia.noticeboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // Username 중복확인
    public String redunancy(String username){
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return "fail";
        }
        return "ok";
    }

    // 회원가입
    public User registerUser(SignupRequestDto requestDto) {
// 패스워드 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());

// 사용자 ROLE 확인
        RoleEnum role = RoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = RoleEnum.ADMIN;
        }

        User user = new User(requestDto.getUsername(), password, role);
        userRepository.save(user);
        return user;
    }




}
