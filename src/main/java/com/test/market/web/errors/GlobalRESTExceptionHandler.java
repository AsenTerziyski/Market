package com.test.market.web.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalRESTExceptionHandler {
    @ExceptionHandler(Exception.class)

    public @ResponseBody
    ErrorInfo handleRestErrors(HttpServletRequest req, Exception e) {
        return new ErrorInfo(req.getRequestURL().toString(), e);
    }
}
