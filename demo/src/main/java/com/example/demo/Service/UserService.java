package com.example.demo.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entitydto.User;
import com.example.demo.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 注册用户
     */
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("邮箱已被使用");
        }
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new IllegalArgumentException("手机号已被使用");
        }
        return userRepository.save(user);
    }

    /**
     * 用户登录
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));
        
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        
        return user;
    }

    /**
     * 根据ID查找用户
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 根据用户名查找用户
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据邮箱查找用户
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * 根据手机号查找用户
     */
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 更新用户信息
     */
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new IllegalArgumentException("邮箱已被使用");
            }
            user.setEmail(userDetails.getEmail());
        }
        
        if (userDetails.getPhone() != null && !userDetails.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(userDetails.getPhone())) {
                throw new IllegalArgumentException("手机号已被使用");
            }
            user.setPhone(userDetails.getPhone());
        }
        
        if (userDetails.getAddress() != null) {
            user.setAddress(userDetails.getAddress());
        }
        
        if (userDetails.getRealName() != null) {
            user.setRealName(userDetails.getRealName());
        }
        
        return userRepository.save(user);
    }

    /**
     * 修改密码
     */
    public User changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (!user.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("原密码错误");
        }
        
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    /**
     * 删除用户
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        userRepository.delete(user);
    }
}
