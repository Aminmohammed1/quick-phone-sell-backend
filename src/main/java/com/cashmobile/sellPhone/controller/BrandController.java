package com.cashmobile.sellPhone.controller;

import com.cashmobile.sellPhone.entity.Brand;
import com.cashmobile.sellPhone.repository.BrandRepository;
import com.cashmobile.sellPhone.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandRepository brandRepository;


    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Brand> addBrand(@RequestBody Brand brand) {
        return ResponseEntity.ok(brandRepository.save(brand));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/brand/update/{name}")
    public ResponseEntity<Brand> updateBrand(@PathVariable String name, @RequestBody Brand updatedBrand) {
        return brandRepository.findByName(name)
                .map(brand -> {
                    brand.setName(updatedBrand.getName());
                    return ResponseEntity.ok(brandRepository.save(brand));
                }).orElse((ResponseEntity.notFound().build()));
    }


    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/brand/delete/{name}")
    public ResponseEntity<String> deleteBrandByName(@PathVariable String name) {
        if(!brandRepository.existsByName(name)){
            return ResponseEntity.notFound().build();
        }
        brandRepository.deleteByName(name);
        return ResponseEntity.ok("Brand deleted sucessfully.");
    }

}
