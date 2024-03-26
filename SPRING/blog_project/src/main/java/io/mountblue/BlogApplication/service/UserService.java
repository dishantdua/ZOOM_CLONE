package io.mountblue.BlogApplication.service;

import io.mountblue.BlogApplication.entity.User;

import java.util.List;

public interface UserService {

    User findByName(String author);

    List<User> findAll();

    boolean register(User user);

    User findByEmail(String email);

    User findUserByName(String user);


}
