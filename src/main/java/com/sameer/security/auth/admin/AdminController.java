package com.sameer.security.auth.admin;

import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
    public ResponseEntity<String> sayHi()
    {
        return ResponseEntity.ok("Hi Admin from behind the auth");
    }

}
