package com.sta.xuptsta.controller;

import com.sta.xuptsta.pojo.dto.UserDTO;
import com.sta.xuptsta.result.Result;
import com.sta.xuptsta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO)
    {
        userService.register(userDTO);
        return Result.ok();
    }

    @PostMapping("/passwordLogin")
    public Result passwordLogin(@RequestBody UserDTO userDTO)
    {
        return Result.ok(userService.passwordLogin(userDTO));
    }

    @PostMapping("/codeLogin")
    public Result codeLogin(@RequestBody UserDTO userDTO)
    {
        return Result.ok(userService.codeLogin(userDTO));
    }

    @PutMapping("/refreshToken")
    public Result refreshToken(@RequestHeader("refreshToken") String refreshToken)
    {
        return Result.ok(userService.refreshToken(refreshToken));
    }

    @PutMapping("/changePassword")
    public Result changePassword(@RequestBody UserDTO userDTO)
    {
        userService.changePassword(userDTO);
        return Result.ok();
    }

    @DeleteMapping("/logout")
    public Result logout()
    {
        userService.logout();
        return Result.ok();
    }
}
