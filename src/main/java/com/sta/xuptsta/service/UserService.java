package com.sta.xuptsta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sta.xuptsta.pojo.dto.UserDTO;
import com.sta.xuptsta.pojo.entity.User;
import com.sta.xuptsta.pojo.vo.LoginVO;

public interface UserService extends IService<User> {
    void register(UserDTO userDTO);

    LoginVO passwordLogin(UserDTO userDTO);

    LoginVO codeLogin(UserDTO userDTO);

    LoginVO refreshToken(String refreshToken);

    void changePassword(UserDTO userDTO);

    void logout();
}
