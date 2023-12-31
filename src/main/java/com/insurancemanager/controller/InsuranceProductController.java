package com.insurancemanager.controller;

import com.insurancemanager.model.InsuranceProduct;
import com.insurancemanager.service.InsuranceProductService;
import com.insurancemanager.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class InsuranceProductController {
    @Autowired
    private InsuranceProductService productService;

    @Autowired
    private PolicyService policyService;

    @GetMapping("/add")
    public String showProductForm(Model model) {
        model.addAttribute("product", new InsuranceProduct());
        return "create_product";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") @Valid InsuranceProduct product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create_product";
        }
        productService.saveInsuranceProduct(product);
        return "redirect:/products/findall";
    }

    @GetMapping("/findall")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllInsuranceProducts());
        return "all_products";
    }

    @GetMapping("/{id}")
    public String showProductPage(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getInsuranceProductById(id));
        model.addAttribute("policies", policyService.getAllPoliciesByProductId(id));
        return "product";
    }

    @GetMapping("{id}/update")
    public String showUpdateProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getInsuranceProductById(id));
        return "edit_product";
    }

    @PostMapping("{id}/update")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute("product") @Valid InsuranceProduct updatedProduct,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit_product";
        }
        InsuranceProduct product = productService.getInsuranceProductById(id);

        product.setTitle(updatedProduct.getTitle());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());

        productService.updateInsuranceProduct(product);
        return "redirect:/products/findall";
    }

    @GetMapping("{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteInsuranceProduct(id);
        return "redirect:/products/findall";
    }
}
