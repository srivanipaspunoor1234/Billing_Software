package com.myproject.retail.billingsoftware.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CategoryRequest {

	@NotBlank(message = "Name is required")
    @Size(max = 100)
	private String name;
	 @Size(max = 500)
	private String description;
	private String bgColor;
	
}
