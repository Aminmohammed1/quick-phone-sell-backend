package com.cashmobile.sellPhone.repository;

import com.cashmobile.sellPhone.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findByBrandId(Long BrandId);

    Optional<Model> findByName(String Name);

    boolean existsByName(String Name);

    void deleteByName(String Name);

}
