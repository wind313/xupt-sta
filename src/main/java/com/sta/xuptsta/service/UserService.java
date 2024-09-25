package com.sta.xuptsta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sta.xuptsta.pojo.dto.UserCodeDTO;
import com.sta.xuptsta.pojo.dto.UserDTO;
import com.sta.xuptsta.pojo.dto.UserPasswordDTO;
import com.sta.xuptsta.pojo.entity.User;
import com.sta.xuptsta.pojo.vo.LoginVO;

public interface UserService extends IService<User> {
    void register(UserDTO userDTO);

    LoginVO passwordLogin(UserPasswordDTO userDTO);

    LoginVO codeLogin(UserCodeDTO userDTO);

    LoginVO refreshToken(String refreshToken);

    void changePassword(UserDTO userDTO);
}
