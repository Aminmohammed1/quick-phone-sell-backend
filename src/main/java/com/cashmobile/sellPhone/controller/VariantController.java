package com.cashmobile.sellPhone.controller;

import com.cashmobile.sellPhone.entity.Variant;
import com.cashmobile.sellPhone.service.VariantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variants")
public class VariantController {

    private final VariantService variantService;

    public VariantController(VariantService variantService) {
        this.variantService = variantService;
    }

    @GetMapping("/model/{modelId}")
    public List<Variant> getVariantsByModel(@PathVariable Long modelId) {
        return variantService.getVariantsByModelId(modelId);
    }

    @PostMapping
    public Variant createVariant(@RequestBody Variant variant) {
        return variantService.saveVariant(variant);
    }
}
