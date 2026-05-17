package com.myproject.retail.billingsoftware.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

	private String email;
	private String password;
	
}
