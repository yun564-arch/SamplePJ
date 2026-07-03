package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // タスクが見つからない場合(存在しない or 他人のもの)
    @ExceptionHandler(TaskNotFoundException.class)
    public ModelAndView handleTaskNotFound(TaskNotFoundException e) {
        ModelAndView mv = new ModelAndView("error/not-found");
        mv.addObject("message", e.getMessage());
        mv.setStatus(HttpStatus.NOT_FOUND);
        return mv;
    }

    // 想定外の例外(バグなど)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleOther(Exception e) {
        logger.error("予期しない例外が発生しました", e);
        ModelAndView mv = new ModelAndView("error/error");
        mv.addObject("message", "予期しないエラーが発生しました");
        mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mv;
    }
}