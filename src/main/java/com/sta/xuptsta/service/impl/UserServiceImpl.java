package com.sta.xuptsta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sta.xuptsta.constant.EmailConstant;
import com.sta.xuptsta.constant.JWTConstant;
import com.sta.xuptsta.exception.GlobalException;
import com.sta.xuptsta.mapper.UserMapper;
import com.sta.xuptsta.pojo.dto.UserDTO;
import com.sta.xuptsta.pojo.entity.User;
import com.sta.xuptsta.pojo.vo.LoginVO;
import com.sta.xuptsta.result.ResultCode;
import com.sta.xuptsta.service.UserService;
import com.sta.xuptsta.util.JWTUtil;
import com.sta.xuptsta.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void register(UserDTO userDTO) {
        String email = userDTO.getEmail();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, userDTO.getEmail());
        if (this.count(queryWrapper) > 0) {
            throw new GlobalException("用户已存在，请登录!");
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 6) {
            throw new GlobalException("密码长度不能小于6位");
        }

        String code = redisTemplate.opsForValue().get(EmailConstant.EMAIL_CODE + email);
        if (code == null || userDTO.getCode() == null || !code.equals(userDTO.getCode())) {
            throw new GlobalException("验证码错误");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(MD5Util.md5(userDTO.getPassword()));
        this.save(user);
        redisTemplate.delete(EmailConstant.EMAIL_CODE + email);
    }

    @Override
    public LoginVO passwordLogin(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, userDTO.getEmail());
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new GlobalException("用户不存在，请注册!");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 6) {
            throw new GlobalException("密码长度不能小于6位");
        }
        if (!user.getPassword().equals(MD5Util.md5(userDTO.getPassword()))) {
            throw new GlobalException("邮箱或密码错误");
        }
        LoginVO loginVO = new LoginVO();
        loginVO.setAuthorization(JWTUtil.createToken(user.getId(), JWTConstant.JWT_SHORT_EXPIRE));
        loginVO.setRefreshToken(JWTUtil.createToken(user.getId(), JWTConstant.JWT_LONG_EXPIRE));
        return loginVO;
    }

    @Override
    public LoginVO codeLogin(UserDTO userDTO) {
        String email = userDTO.getEmail();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, email);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new GlobalException("用户不存在，请注册!");
        }
        String code = redisTemplate.opsForValue().get(EmailConstant.EMAIL_CODE + email);
        if (code == null || userDTO.getCode() == null || !code.equals(userDTO.getCode())) {
            throw new GlobalException("验证码错误");
        }
        LoginVO loginVO = new LoginVO();
        loginVO.setAuthorization(JWTUtil.createToken(user.getId(), JWTConstant.JWT_SHORT_EXPIRE));
        loginVO.setRefreshToken(JWTUtil.createToken(user.getId(), JWTConstant.JWT_LONG_EXPIRE));
        redisTemplate.delete(EmailConstant.EMAIL_CODE + email);
        return loginVO;
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        try {
            if (!JWTUtil.checkToken(refreshToken)) {
                throw new GlobalException(ResultCode.NOT_LOGIN);
            }
            Long userId = JWTUtil.getUserId(refreshToken);
            LoginVO loginVO = new LoginVO();
            loginVO.setAuthorization(JWTUtil.createToken(userId, JWTConstant.JWT_SHORT_EXPIRE));
            loginVO.setRefreshToken(JWTUtil.createToken(userId, JWTConstant.JWT_LONG_EXPIRE));
            return loginVO;
        } catch (Exception e) {
            throw new GlobalException(ResultCode.NOT_LOGIN);
        }
    }

    @Override
    public void changePassword(UserDTO userDTO) {
        String email = userDTO.getEmail();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, email);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new GlobalException("用户不存在，请注册!");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 6) {
            throw new GlobalException("密码长度不能小于6位");
        }
        String code = redisTemplate.opsForValue().get(EmailConstant.EMAIL_CODE + email);
        if (code == null || userDTO.getCode() == null || !code.equals(userDTO.getCode())) {
            throw new GlobalException("验证码错误");
        }
        user.setPassword(MD5Util.md5(userDTO.getPassword()));
        user.setUpdateTime(LocalDateTime.now());
        this.updateById(user);
        redisTemplate.delete(EmailConstant.EMAIL_CODE + email);
    }


}
