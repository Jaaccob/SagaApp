package com.kozubek.useradapters.rest.in;

import com.kozubek.commonapplication.dtos.AuthenticationJWTToken;
import com.kozubek.commonapplication.dtos.AuthenticationUser;
import com.kozubek.commonapplication.dtos.RegisterUser;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.userapplication.services.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserApplicationService userService;

    @GetMapping("/auth/hello")
    public String hello() {
        return "Hello World from UserController";
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserId> register(@RequestBody RegisterUser userCommand) {
        UserId registeredUser = userService.registerUser(userCommand);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationJWTToken> login(@RequestBody AuthenticationUser userCommand) {
        AuthenticationJWTToken token = userService.loginUser(userCommand);
        return ResponseEntity.ok(token);
    }
}
