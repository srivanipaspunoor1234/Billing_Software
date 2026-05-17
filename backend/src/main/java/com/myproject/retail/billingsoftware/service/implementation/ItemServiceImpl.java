package com.myproject.retail.billingsoftware.service.implementation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.myproject.retail.billingsoftware.entity.CategoryEntity;
import com.myproject.retail.billingsoftware.entity.ItemEntity;
import com.myproject.retail.billingsoftware.io.ItemRequest;
import com.myproject.retail.billingsoftware.io.ItemResponse;
import com.myproject.retail.billingsoftware.repository.CategoryRepository;
import com.myproject.retail.billingsoftware.repository.ItemRepository;
import com.myproject.retail.billingsoftware.service.FileUploadService;
import com.myproject.retail.billingsoftware.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Slf4j 
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;


    @Override
    public ItemResponse add(ItemRequest request, MultipartFile file) {

        try {

        	if (file == null || file.isEmpty()) {
        	    log.warn("File upload attempted with empty file");
        	    throw new ResponseStatusException(
        	            HttpStatus.BAD_REQUEST,
        	            "File is required"
        	    );
        	}

        	var response = fileUploadService.uploadFile(file);

        	String imgUrl = response.getUrl();
        	String publicId = response.getPublicId();
        	
            CategoryEntity existingCategory = categoryRepository
                    .findByCategoryId(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            ItemEntity newItem = ItemEntity.builder()
            		.itemId(UUID.randomUUID().toString())
                    .name(request.getName())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .imgUrl(imgUrl)
                    .publicId(publicId)
                    .category(existingCategory)
                    .build();

            newItem = itemRepository.save(newItem);

            return convertToResponse(newItem);

        }catch (Exception e) {
            log.error("Failed to add item", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        }
    }

    @Override
    public List<ItemResponse> fetchItems() {
        return itemRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteItem(String itemId) {

        ItemEntity existingItem = itemRepository
                .findByItemId(itemId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Item not found: " + itemId
                        )
                );

      
        fileUploadService.deleteFile(existingItem.getPublicId());      
        itemRepository.delete(existingItem);
    }


    private ItemResponse convertToResponse(ItemEntity item) {
        return ItemResponse.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .imgUrl(item.getImgUrl())
                .categoryName(item.getCategory().getName())
                .categoryId(item.getCategory().getCategoryId())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}