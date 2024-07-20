package com.sta.xuptsta.controller;

import com.sta.xuptsta.pojo.dto.UserDTO;
import com.sta.xuptsta.result.Result;
import com.sta.xuptsta.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Tag(name = "用户")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "注册", description = "注册新用户")
    @PostMapping("/register")
    public Result register(@Valid @RequestBody UserDTO userDTO)
    {
        userService.register(userDTO);
        return Result.ok();
    }

    @Operation(summary = "登录", description = "使用邮箱和密码登录")
    @PostMapping("/passwordLogin")
    public Result passwordLogin(@Valid @RequestBody UserDTO userDTO)
    {
        return Result.ok(userService.passwordLogin(userDTO));
    }

    @Operation(summary = "登录", description = "使用邮箱和验证码登录")
    @PostMapping("/codeLogin")
    public Result codeLogin(@Valid @RequestBody UserDTO userDTO)
    {
        return Result.ok(userService.codeLogin(userDTO));
    }

    @Operation(summary = "刷新token", description = "刷新token")
    @PostMapping("/refreshToken")
    public Result refreshToken(@RequestHeader("refreshToken") String refreshToken)
    {
        return Result.ok(userService.refreshToken(refreshToken));
    }

    @Operation(summary = "修改密码", description = "使用验证码修改密码")
    @PutMapping("/changePassword")
    public Result changePassword(@Valid @RequestBody UserDTO userDTO)
    {
        userService.changePassword(userDTO);
        return Result.ok();
    }

}
