package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Модель программного обеспечения.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Software {
    /**
     * Уникальный идентификатор программного обеспечения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "software_id")
    private long id;

    /**
     * Название программного обеспечения.
     */
    @Column(name = "name")
    private String name;

    /**
     * Описание программного обеспечения.
     */
    @Column(name = "description")
    private String description;

    /**
     * Путь к изображению программного обеспечения.
     */
    @Column(name = "image")
    private String image;

    /**
     * Ссылка на программное обеспечение.
     */
    @Column(name = "link")
    private String link;

    /**
     * Системные требования для программного обеспечения.
     */
    @Column(name = "system_requirements")
    private String systemRequirements;

    /**
     * Название лицензии программного обеспечения.
     */
    @Column(name = "license_name")
    private String licenseName;

    /**
     * Тип лицензии программного обеспечения.
     */
    @Column(name = "license_type")
    private String licenseType;

    /**
     * Цена лицензии программного обеспечения.
     */
    @Column(name = "license_price")
    private double licensePrice;

    /**
     * Длительность лицензии программного обеспечения.
     */
    @Column(name = "license_duration")
    private double licenseDuration;

    @ManyToMany
    @JoinTable(
            name = "software_categories",
            joinColumns = @JoinColumn(name = "software_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Categories> categories;

    @ManyToMany
    @JoinTable(
            name = "software_comments",
            joinColumns = @JoinColumn(name = "software_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private List<Comments> comments;

    @ManyToMany
    @JoinTable(
            name = "software_screens",
            joinColumns = @JoinColumn(name = "software_id"),
            inverseJoinColumns = @JoinColumn(name = "screen_id")
    )
    private List<Screens> screens;
}
