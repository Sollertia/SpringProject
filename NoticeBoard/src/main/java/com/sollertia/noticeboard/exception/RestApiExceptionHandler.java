package com.sollertia.noticeboard.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestControllerAdvice
public class RestApiExceptionHandler {
    ModelAndView model = new ModelAndView();
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ModelAndView handleApiRequestException(IllegalArgumentException ex) {
        // 비밀번호 닉네임 포함,확인 검사 오류처리 - 에러코드에 정의해놓은 값들로 throw처리 한 곳의 메세지와 비교해서
        // 나머지 값들 처리하기
        if (ErrorCode.CONTAIN_PASSWORD.getErrorMessage().equals(ex.getMessage())) {
            log.info(ex.getMessage());
            model.addObject("errorMessage", ex.getMessage());
            model.addObject("errorCode", ErrorCode.CONTAIN_PASSWORD.getErrorCode());
            model.setViewName("signup");
            return model;
        }else if(ErrorCode.CHEK_PASSWORD.getErrorMessage().equals(ex.getMessage())){
            log.info(ex.getMessage());
            model.addObject("errorMessage", ex.getMessage());
            model.addObject("errorCode", ErrorCode.CHEK_PASSWORD.getErrorCode());
            model.setViewName("signup");
            return model;
        }

        model.setViewName("boardList");
        return model;
    }

}

