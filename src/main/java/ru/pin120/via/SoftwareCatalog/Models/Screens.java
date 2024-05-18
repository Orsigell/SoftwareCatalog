package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Модель скриншота.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Screens {
    /**
     * Уникальный идентификатор скриншота.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private long id;

    /**
     * Путь к файлу скриншота.
     */
    @Column(name = "screen")
    private String screen;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "software_id")
    private Software software;
    public Screens(String path) {
        this.screen = path;
    }
}
