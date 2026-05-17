package com.myproject.retail.billingsoftware.controller;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.myproject.retail.billingsoftware.io.ItemRequest;
import com.myproject.retail.billingsoftware.io.ItemResponse;
import com.myproject.retail.billingsoftware.response.ApiResponse;
import com.myproject.retail.billingsoftware.service.ItemService;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class ItemController {

    private final ItemService itemService;

   @PostMapping(value = "/admin/items", consumes = "multipart/form-data")
public ApiResponse<ItemResponse> addItem(
        @RequestPart("item") String itemJson,
        @RequestPart("file") MultipartFile file
) throws Exception {

    ObjectMapper mapper = new ObjectMapper();
    ItemRequest request = mapper.readValue(itemJson, ItemRequest.class);

    ItemResponse response = itemService.add(request, file);

    return ApiResponse.success("Item added successfully", response);
}

   @GetMapping("/items")
public ApiResponse<List<ItemResponse>> getItems() {

    return ApiResponse.success(
            "Items fetched successfully",
            itemService.fetchItems()
    );
}

   @DeleteMapping("/admin/items/{itemId}")
   public ApiResponse<String> deleteItem(@PathVariable String itemId) {

       itemService.deleteItem(itemId);

       return ApiResponse.success("Item deleted successfully", itemId);
   }
}