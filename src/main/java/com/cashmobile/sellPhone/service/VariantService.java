package com.cashmobile.sellPhone.service;

import com.cashmobile.sellPhone.entity.Variant;
import com.cashmobile.sellPhone.repository.VariantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariantService {

    private final VariantRepository variantRepository;

    public VariantService(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    public List<Variant> getVariantsByModelId(Long modelId) {
        return variantRepository.findByModelId(modelId);
    }

    public Variant saveVariant(Variant variant) {
        return variantRepository.save(variant);
    }
}
