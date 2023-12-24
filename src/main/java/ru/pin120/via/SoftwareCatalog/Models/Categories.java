package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Модель категорий.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Categories {
    /**
     * Уникальный идентификатор категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private long id;

    /**
     * Название категории.
     */
    @Column(name = "category_name")
    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "categories")
    private List<Software> softwares;
    public Categories(String text) {
        this.name = text;
    }
}
