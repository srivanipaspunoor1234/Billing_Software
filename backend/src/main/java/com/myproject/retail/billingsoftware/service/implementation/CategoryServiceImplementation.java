package com.myproject.retail.billingsoftware.service.implementation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.myproject.retail.billingsoftware.entity.CategoryEntity;
import com.myproject.retail.billingsoftware.io.CategoryRequest;
import com.myproject.retail.billingsoftware.io.CategoryResponse;
import com.myproject.retail.billingsoftware.repository.CategoryRepository;
import com.myproject.retail.billingsoftware.repository.ItemRepository;
import com.myproject.retail.billingsoftware.service.CategoryService;
import com.myproject.retail.billingsoftware.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final FileUploadService fileUploadService;

@Override
public CategoryResponse add(CategoryRequest request, MultipartFile file) {

	String imageUrl = null;
	String publicId = null;

	if (file != null && !file.isEmpty()) {
	    var response = fileUploadService.uploadFile(file);
	    imageUrl = response.getUrl();
	    publicId = response.getPublicId();
	}

    CategoryEntity newCategory = CategoryEntity.builder()
            .categoryId(UUID.randomUUID().toString())
            .name(request.getName())
            .description(request.getDescription())
            .bgColor(request.getBgColor())
            .imgUrl(imageUrl)
            .publicId(publicId)
            .build();

    newCategory = categoryRepository.save(newCategory);

    return convertToResponse(newCategory);
}

    @Override
    public List<CategoryResponse> read() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

   @Override
public void delete(String categoryId) {

    CategoryEntity existingCategory = categoryRepository
            .findByCategoryId(categoryId)
            .orElseThrow(() ->
                    new RuntimeException("Category not found: " + categoryId)
            );

    if (existingCategory.getPublicId() != null) {
        fileUploadService.deleteFile(existingCategory.getPublicId());
    }

    categoryRepository.delete(existingCategory);
}
    private CategoryResponse convertToResponse(CategoryEntity category) {

        // SAFE count (never null)
        Long itemsCount = itemRepository.countByCategory_Id(category.getId());
        if (itemsCount == null) {
            itemsCount = 0L;
        }

        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .description(category.getDescription())
                .bgColor(category.getBgColor())
                .imgUrl(category.getImgUrl())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .items(itemsCount)   
                .build();
    }
}