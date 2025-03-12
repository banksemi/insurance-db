package com.sideproject.caregiver_management.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server")
public class ServerInfoController {

    @GetMapping("/status")
    public ResponseEntity<ServerStatusResponse> status(){
        return ResponseEntity.status(HttpStatus.OK).body(
                ServerStatusResponse.builder()
                .status("ok")
                .build()
        );
    }
}
