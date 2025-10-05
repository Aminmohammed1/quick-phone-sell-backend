package com.cashmobile.sellPhone.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ram;
    private String rom;

    private String price;


    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Model model;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "Variant{" +
                "id=" + id +
                ", ram='" + ram + '\'' +
                ", rom='" + rom + '\'' +
                ", price='" + price + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", model=" + model +
                '}';
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
