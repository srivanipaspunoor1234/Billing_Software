package com.myproject.retail.billingsoftware.io;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

	private String name;
	private Double price;
	private String description;
	private String categoryId;
}
