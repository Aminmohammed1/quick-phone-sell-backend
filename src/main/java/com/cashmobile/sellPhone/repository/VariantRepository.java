package com.cashmobile.sellPhone.repository;

import com.cashmobile.sellPhone.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariantRepository extends JpaRepository<Variant, Long> {
    List<Variant> findByModelId(Long modelId);
}
