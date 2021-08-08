package com.test.coindesk;

import com.test.coindesk.currencymapping.CurrencyNameMappingException;
import com.test.coindesk.service.CoindeskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);
    @ExceptionHandler({CurrencyNameMappingException.class, CoindeskException.class})
    public ResponseEntity<Map> handleException(HttpServletRequest requset, Throwable throwable) {
        logger.error(MessageFormat.format("request: {0} message: {1}", requset.getServletPath(), throwable.getMessage()), throwable);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HashMap<String, Object> body = new LinkedHashMap<>();
        body.put("status","error");
        body.put("message",throwable.getMessage());
        ResponseEntity<Map> entity = ResponseEntity.status(500).headers(httpHeaders).body(body);
        return entity;
    }
}
