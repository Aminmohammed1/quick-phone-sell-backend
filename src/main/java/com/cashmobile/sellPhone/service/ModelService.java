package com.cashmobile.sellPhone.service;

import com.cashmobile.sellPhone.entity.Model;
import com.cashmobile.sellPhone.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {

    private final ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<Model> getModelsByBrandId(Long brandId) {
        return modelRepository.findByBrandId(brandId);
    }

    public Model saveModel(Model model) {
        return modelRepository.save(model);
    }
}
