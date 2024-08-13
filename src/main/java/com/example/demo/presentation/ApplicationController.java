package com.example.demo.presentation;

import com.example.demo.application.ApplicationService;
import com.example.demo.dto.ApplicationReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<String> applyToJob(@RequestBody ApplicationReq applicationReq) {
        applicationService.applyToJob(applicationReq);
        return ResponseEntity.ok("success");
    }
}
