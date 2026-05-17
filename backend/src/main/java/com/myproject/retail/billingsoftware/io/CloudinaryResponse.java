package com.myproject.retail.billingsoftware.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudinaryResponse {
	private String url;
	private String publicId;
}