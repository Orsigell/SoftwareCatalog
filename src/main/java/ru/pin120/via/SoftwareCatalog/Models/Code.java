package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

/**
 * Модель кода продуктов.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Code {
    /**
     * Уникальный идентификатор кода.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private long id;

    /**
     * Название кода.
     */
    @Column(name = "code_name")
    @NotEmpty
    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "cods")
    private List<Software> softwares;
    public Code(String text) {
        this.name = text;
    }
}
