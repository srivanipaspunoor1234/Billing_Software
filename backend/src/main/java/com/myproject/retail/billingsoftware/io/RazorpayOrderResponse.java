package com.myproject.retail.billingsoftware.io;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RazorpayOrderResponse {

	private String id;
	private String entity;
	private String currency;
	private String status;
	private String receipt;
	private Long amount;
	private String createdAtFormatted;
	
	 @JsonProperty("created_at")
	    private Long createdAt;
	
}
