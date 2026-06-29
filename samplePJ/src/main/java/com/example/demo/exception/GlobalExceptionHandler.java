package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

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
        // ログには詳細を出す(画面には出さない)
        e.printStackTrace();
        ModelAndView mv = new ModelAndView("error/error");
        mv.addObject("message", "予期しないエラーが発生しました");
        mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mv;
    }
}