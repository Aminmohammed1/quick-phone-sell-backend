package com.cashmobile.sellPhone.controller;

import com.cashmobile.sellPhone.entity.Model;
import com.cashmobile.sellPhone.service.ModelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping("/brand/{brandId}")
    public List<Model> getModelsByBrand(@PathVariable Long brandId) {
        return modelService.getModelsByBrandId(brandId);
    }

    @PostMapping
    public Model createModel(@RequestBody Model model) {
        return modelService.saveModel(model);
    }
}
