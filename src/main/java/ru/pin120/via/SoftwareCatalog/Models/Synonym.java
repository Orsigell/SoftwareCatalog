package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Synonym {
    /**
     * Уникальный идентификатор синонима.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "synonym_id")
    private long id;

    /**
     * Название синонима.
     */
    @Column(name = "synonym_name", length = 65535)
    @NotEmpty
    private String name;

    /**
     * Необходимость обновления синонима.
     */
    @Column(name = "updatable")
    private Long updatable = 1L;

    @ManyToOne
    @JoinColumn(name = "software_id", nullable = false)
    @JsonBackReference
    private Software software;
}