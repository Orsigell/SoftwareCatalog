package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private String name;

    /**
     * Описание программного обеспечения.
     */
    @Column(name = "description")
    @NotEmpty
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
    @NotEmpty
    private String link;

    /**
     * Системные требования для программного обеспечения.
     */
    @Column(name = "system_requirements")
    @NotEmpty
    private String systemRequirements;

    /**
     * Название лицензии программного обеспечения.
     */
    @Column(name = "license_name")
    @NotEmpty
    private String licenseName;

    /**
     * Тип лицензии программного обеспечения.
     */
    @Column(name = "license_type")
    @NotEmpty
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
            name = "software_tags",
            joinColumns = @JoinColumn(name = "software_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tags> tags;

    @OneToMany(mappedBy = "software")
    @JsonBackReference
    private List<Comments> comments;

    @OneToMany(mappedBy = "software")
    @JsonBackReference
    private List<Screens> screens;
}
