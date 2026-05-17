package com.myproject.retail.billingsoftware.io;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {

	private String itemId;
	private String name;
	private Double price;
	private String description;
	private String categoryId;
	private String imgUrl;
	private String categoryName;
	private Timestamp createdAt;
	private Timestamp updatedAt;
}
