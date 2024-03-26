package io.mountblue.BlogApplication.service;

import ch.qos.logback.classic.encoder.JsonEncoder;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Role;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.PostRepository;
import io.mountblue.BlogApplication.repository.RoleRepository;
import io.mountblue.BlogApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation  implements UserService{
    public UserServiceImplementation(){
    }
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository,RoleRepository roleRepository){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
    }

    @Override
    public User findByName(String author) {
        return userRepository.findByName(author);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByName(String user) {
        return userRepository.findUserByName(user);
    }

    @Override
    public boolean register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return false;
        }
        User newUser =new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword("{bcrypt}"+passwordEncoder().encode(user.getPassword()));
        Role role =new Role();
        userRepository.save(newUser);
        User registeredUser = userRepository.findByName(newUser.getName());
        role.setRole("ROLE_AUTHOR");
        role.setName(user.getName());
        role.setId(registeredUser.getId());
        roleRepository.save(role);
        return true;
    }

}
