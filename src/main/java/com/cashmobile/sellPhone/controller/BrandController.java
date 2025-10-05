package com.cashmobile.sellPhone.controller;

import com.cashmobile.sellPhone.entity.Brand;
import com.cashmobile.sellPhone.entity.BrandDTO;
import com.cashmobile.sellPhone.entity.Model;
import com.cashmobile.sellPhone.entity.Variant;
import com.cashmobile.sellPhone.repository.BrandRepository;
import com.cashmobile.sellPhone.service.BrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final Logger logger = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private BrandRepository brandRepository;


    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public List<BrandDTO> getAllBrands() {
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
    @Modifying
    @DeleteMapping("/brand/delete/{name}")
    public ResponseEntity<String> deleteBrandByName(@PathVariable String name) {
        if(!brandRepository.existsByName(name)){
            return ResponseEntity.notFound().build();
        }
        brandRepository.deleteByName(name);
        return ResponseEntity.ok("Brand deleted sucessfully.");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/bulk")
    public ResponseEntity<?> addAllBrands(@RequestBody List<Brand> brands) {
        for(Brand incomingBrand : brands) {
            Optional<Brand> existingBrandOpt = brandRepository.findByName(incomingBrand.getName());

        if (existingBrandOpt.isPresent()) {
            Brand existingBrand = existingBrandOpt.get();

            for(Model incomingModel : incomingBrand.getModels()) {
                //for models
                Optional<Model> existingModelOpt = existingBrand.getModels().stream()
                        .filter(m -> m.getName().equalsIgnoreCase(incomingModel.getName())).findFirst();
                if(existingModelOpt.isPresent()) {
                    logger.info("brand exists .. updating ");
                    Model existingModel = existingModelOpt.get();

                    //check new colors
                    for (String newColor : incomingModel.getColors()) {
                        if(!existingModel.getColors().contains(newColor)) {
                            existingModel.getColors().add(newColor);
                        }
                    }
                    //check new variants
                    for (Variant newVariant : incomingModel.getVariants()) {
                        Optional<Variant> existingVariantOpt = existingModel.getVariants().stream()
                                .filter(v -> v.getRam().trim().equalsIgnoreCase(newVariant.getRam().trim()) &&
                                        v.getRom().trim().equalsIgnoreCase(newVariant.getRom().trim())).findFirst();

                        if(existingVariantOpt.isPresent()) {
                            logger.info("variant exists .. updating " + existingVariantOpt);
                            Variant existingVariant = existingVariantOpt.get();

                            // Update fields of the existing variant (e.g., price, colors)
                            existingVariant.setPrice(newVariant.getPrice());
                        } else {
                            logger.info("variant not present  .. creating " + existingVariantOpt);
                            // new variant...
                            newVariant.setModel(existingModel); // set FK
                            existingModel.getVariants().add(newVariant);
                        }
                    }
                } else {
                    // If model doesn't exist, add new model
                    logger.info("modle not present  .. creating " );
                    incomingModel.setBrand(existingBrand);
                    for (Variant v: incomingModel.getVariants()) {
                        v.setModel(incomingModel);
                    }
                    existingBrand.getModels().add(incomingModel);
                }
            }
            brandRepository.save(existingBrand);
        } else {
            // 🔹 New brand: set relationships and save
            logger.info("brand not present .. creating ");
            for(Model newModel: incomingBrand.getModels()){
                newModel.setBrand(incomingBrand);
                for(Variant variant : newModel.getVariants()){
                    variant.setModel(newModel);
                }
            }
            brandRepository.save(incomingBrand);
            }
        }
        return ResponseEntity.ok("Brands saved");
    }


}
