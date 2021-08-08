package com.test.coindesk.controller;

import com.test.coindesk.service.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/coindesk")
public class CoindeskApiController {

    @Autowired private ConvertService convertService;

    @GetMapping("origin")
    public ResponseEntity getOrigin() {
        return convertService.getOrigin();
    }

    @GetMapping("converted")
    public ResponseEntity getConvert() {
        return convertService.getConvert();
    }

}
