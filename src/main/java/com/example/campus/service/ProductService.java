package com.example.campus.service;

import com.example.campus.dto.ProductForm;
import com.example.campus.entity.*;
import com.example.campus.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    /**
     * 分页搜索在售商品
     */
    public Page<Product> search(String keyword, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), size);
        String kw = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        return productRepository.search(ProductStatus.AVAILABLE, kw, categoryId, pageable);
    }

    /**
     * 首页推荐商品
     */
    public List<Product> latestProducts() {
        return productRepository.findTop8ByStatusOrderByCreatedAtDesc(ProductStatus.AVAILABLE);
    }

    /**
     * 查看商品详情（并累加浏览次数）
     */
    @Transactional
    public Product viewDetail(Long id) {
        Product product = findById(id);
        product.setViewCount(product.getViewCount() == null ? 1 : product.getViewCount() + 1);
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
    }

    /**
     * 发布商品
     */
    @Transactional
    public Product create(ProductForm form, String username) {
        User seller = userService.findByUsername(username);
        Product product = Product.builder()
                .title(form.getTitle())
                .description(form.getDescription())
                .price(form.getPrice())
                .originalPrice(form.getOriginalPrice())
                .category(form.getCategoryId() == null ? null : categoryService.findById(form.getCategoryId()))
                .conditionLevel(form.getConditionLevel())
                .location(form.getLocation())
                .seller(seller)
                .status(ProductStatus.AVAILABLE)
                .build();
        String imageUrl = fileStorageService.store(form.getImage());
        if (imageUrl != null) {
            product.setImageUrl(imageUrl);
        }
        return productRepository.save(product);
    }

    /**
     * 编辑商品（仅卖家本人可操作）
     */
    @Transactional
    public Product update(Long id, ProductForm form, String username) {
        Product product = findById(id);
        checkOwner(product, username);

        product.setTitle(form.getTitle());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setOriginalPrice(form.getOriginalPrice());
        product.setCategory(form.getCategoryId() == null ? null : categoryService.findById(form.getCategoryId()));
        product.setConditionLevel(form.getConditionLevel());
        product.setLocation(form.getLocation());

        String imageUrl = fileStorageService.store(form.getImage());
        if (imageUrl != null) {
            product.setImageUrl(imageUrl);
        }
        return productRepository.save(product);
    }

    /**
     * 下架商品
     */
    @Transactional
    public void remove(Long id, String username) {
        Product product = findById(id);
        checkOwner(product, username);
        product.setStatus(ProductStatus.REMOVED);
        productRepository.save(product);
    }

    /**
     * 我发布的商品
     */
    public List<Product> findBySeller(String username) {
        User seller = userService.findByUsername(username);
        return productRepository.findBySellerOrderByCreatedAtDesc(seller);
    }

    public long countAvailable() {
        return productRepository.countByStatus(ProductStatus.AVAILABLE);
    }

    private void checkOwner(Product product, String username) {
        if (!product.getSeller().getUsername().equals(username)) {
            throw new RuntimeException("无权操作该商品");
        }
    }
}