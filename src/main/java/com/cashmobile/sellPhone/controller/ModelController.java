package com.cashmobile.sellPhone.controller;

import com.cashmobile.sellPhone.entity.Brand;
import com.cashmobile.sellPhone.entity.Model;
import com.cashmobile.sellPhone.repository.BrandRepository;
import com.cashmobile.sellPhone.repository.ModelRepository;
import com.cashmobile.sellPhone.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
public class ModelController {

    private final ModelService modelService;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ModelRepository modelRepository;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping("/brand/models")
    public List<Model> getAllModels() {
        return modelService.getAllModels();
    }

    @GetMapping("/brand/id/{brandId}")
    public List<Model> getModelsByBrandId(@PathVariable Long brandId) {
        return modelService.getModelsByBrandId(brandId);
    }

    @GetMapping("/brand/name/{brandName}")
    public List<Model> getModelsByBrandName(@PathVariable String brandName) {
        return modelService.getModelsByBrandName(brandName.trim());
    }

    @PostMapping("/createModel_v1")
    public Model createModelv1(@RequestBody Model model) {
        return modelService.saveModel(model);
    }

    @PostMapping
    public ResponseEntity<Model> createModel(@RequestBody Model model) {
        if (model.getBrand() == null ) {
            return ResponseEntity.badRequest().body(null);
        }

        // Fetch full brand from DB using brand ID
        Brand brand = brandRepository.findById(model.getBrand().getId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        // Set the brand properly
        model.setBrand(brand);

        // Set model reference in a
        //
        //
        // ll variants (important for JPA)
        if (model.getVariants() != null) {
            model.getVariants().forEach(v -> v.setModel(model));
        }

        Model savedModel = modelService.saveModel(model);
        return ResponseEntity.ok(savedModel);
    }

    @PreAuthorize("@PreAuthorize(\"hasAuthority('ROLE_ADMIN')\")")
    @PostMapping("/model/delete/{name}")
    public ResponseEntity<String> deleteModelByName(@PathVariable String name) {
        if (!modelRepository.existsByName(name)) {
            return ResponseEntity.notFound().build();
        }
        modelRepository.deleteByName(name);
        return ResponseEntity.ok("Model deleted successfully.");
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/model/update/{name}")
    public ResponseEntity<Model> updateModelByName(@PathVariable String name, @RequestBody Model updatedModel) {
        return modelRepository.findByName(name)
                .map(existingModel -> {
                    existingModel.setName(updatedModel.getName());
                    existingModel.setBrand(updatedModel.getBrand());
                    existingModel.setVariants(updatedModel.getVariants());
                    existingModel.setImageUrl(updatedModel.getImageUrl());
                    return ResponseEntity.ok(modelRepository.save(existingModel));
                }).orElse(ResponseEntity.notFound().build());
    }

}
