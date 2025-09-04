package com.kozubek.useradapters.rest.in;

import com.kozubek.commonapplication.dtos.AuthenticationJWTToken;
import com.kozubek.commonapplication.dtos.AuthenticationUser;
import com.kozubek.commonapplication.dtos.RegisterUser;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.userapplication.services.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<ResponseEntity<UserId>> register(@RequestBody RegisterUser userCommand) {
        return userService.registerUser(userCommand)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/auth/login")
    public Mono<ResponseEntity<AuthenticationJWTToken>> login(@RequestBody AuthenticationUser userCommand) {
        return userService.loginUser(userCommand)
                .map(ResponseEntity::ok);
    }
}
