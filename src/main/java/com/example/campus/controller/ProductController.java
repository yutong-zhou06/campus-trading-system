package com.example.campus.controller;

import com.example.campus.dto.ProductForm;
import com.example.campus.entity.Product;
import com.example.campus.service.CategoryService;
import com.example.campus.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    /** 商品列表 + 搜索 */
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Page<Product> products = productService.search(keyword, categoryId, page, 12);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        return "products";
    }

    /** 商品详情 */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.viewDetail(id));
        return "product-detail";
    }

    /** 发布商品表单 */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("editMode", false);
        return "product-form";
    }

    /** 提交发布 */
    @PostMapping
    public String create(@ModelAttribute ProductForm form,
                         @AuthenticationPrincipal UserDetails user,
                         RedirectAttributes ra) {
        try {
            Product product = productService.create(form, user.getUsername());
            ra.addFlashAttribute("success", "商品发布成功");
            return "redirect:/products/" + product.getId();
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/products/new";
        }
    }

    /** 编辑商品表单 */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails user,
                           Model model,
                           RedirectAttributes ra) {
        Product product = productService.findById(id);
        if (!product.getSeller().getUsername().equals(user.getUsername())) {
            ra.addFlashAttribute("error", "无权编辑该商品");
            return "redirect:/products/" + id;
        }
        ProductForm form = new ProductForm();
        form.setTitle(product.getTitle());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setOriginalPrice(product.getOriginalPrice());
        form.setCategoryId(product.getCategory() == null ? null : product.getCategory().getId());
        form.setConditionLevel(product.getConditionLevel());
        form.setLocation(product.getLocation());

        model.addAttribute("productForm", form);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("product", product);
        model.addAttribute("editMode", true);
        return "product-form";
    }

    /** 提交编辑 */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute ProductForm form,
                         @AuthenticationPrincipal UserDetails user,
                         RedirectAttributes ra) {
        try {
            productService.update(id, form, user.getUsername());
            ra.addFlashAttribute("success", "商品更新成功");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products/" + id;
    }

    /** 下架商品 */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails user,
                         RedirectAttributes ra) {
        try {
            productService.remove(id, user.getUsername());
            ra.addFlashAttribute("success", "商品已下架");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products/my";
    }

    /** 我发布的商品 */
    @GetMapping("/my")
    public String myProducts(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("products", productService.findBySeller(user.getUsername()));
        return "my-products";
    }
}