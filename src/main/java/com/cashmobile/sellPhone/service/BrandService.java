package com.cashmobile.sellPhone.service;

import com.cashmobile.sellPhone.entity.Brand;
import com.cashmobile.sellPhone.entity.BrandDTO;
import com.cashmobile.sellPhone.repository.BrandRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandDTO> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brand -> new BrandDTO(brand.getId(), brand.getName(), brand.getLogoUrl())).collect(Collectors.toList());
    }

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }
}
