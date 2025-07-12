package com.cashmobile.sellPhone.controller;

import com.cashmobile.sellPhone.entity.Model;
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
    private ModelRepository modelRepository;

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
