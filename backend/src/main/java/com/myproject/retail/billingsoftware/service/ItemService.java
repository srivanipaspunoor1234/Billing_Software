package com.myproject.retail.billingsoftware.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.myproject.retail.billingsoftware.io.ItemRequest;
import com.myproject.retail.billingsoftware.io.ItemResponse;

public interface ItemService {

	ItemResponse add(ItemRequest request, MultipartFile file);
	
	List<ItemResponse> fetchItems();
	
	void deleteItem(String itemId);
	
}
