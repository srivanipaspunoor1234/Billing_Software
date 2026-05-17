package com.myproject.retail.billingsoftware.service;

import java.util.List;
import com.myproject.retail.billingsoftware.io.UserRequest;
import com.myproject.retail.billingsoftware.io.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);
    String getUserRole(String email);
    List<UserResponse> readUsers();       
    List<UserResponse> readAdmins();      
    void deleteUser(String id);
}