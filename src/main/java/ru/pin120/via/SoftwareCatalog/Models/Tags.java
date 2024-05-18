package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

/**
 * Модель тегов.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tags {
    /**
     * Уникальный идентификатор тега.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private long id;

    /**
     * Название тега.
     */
    @Column(name = "tag_name")
    @NotEmpty
    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "tags")
    private List<Software> softwares;
    public Tags(String text) {
        this.name = text;
    }
}
