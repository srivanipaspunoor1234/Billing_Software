package com.myproject.retail.billingsoftware.io;

import java.sql.Timestamp;   // ✅ Correct import

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String userId;
    private String name;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String role;
}