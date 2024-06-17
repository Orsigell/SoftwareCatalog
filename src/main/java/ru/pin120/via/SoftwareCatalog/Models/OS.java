package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

/**
 * Модель операционной системы.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OS {
    /**
     * Уникальный идентификатор ОС.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "os_id")
    private long id;

    /**
     * Название тега.
     */
    @Column(name = "os_name")
    @NotEmpty
    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "os")
    private List<Software> softwares;
    public OS(String name) {
        this.name = name;
    }
}
