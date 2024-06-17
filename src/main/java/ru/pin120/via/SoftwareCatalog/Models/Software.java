package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
     * Идентификатор программного обеспечения.
     */
    @Column(name = "reestr_id")
    private long reestr_id;

    /**
     * Название программного обеспечения.
     */
    @Column(name = "name")
    @NotEmpty
    private String name;

    /**
     * Описание программного обеспечения.
     */
    @Column(name = "description", length = 65535)
    private String description;
    /**
     * Цена программного обеспечения в виде текста.
     */
    @Column(name = "price_text", length = 65535)
    private String priceText;

    @Column(name = "is_image_updatable", columnDefinition = "boolean default true")
    private boolean isImageUpdatable = true;

    @Column(name = "is_description_updatable", columnDefinition = "boolean default true")
    private boolean isDescriptionUpdatable = true;

    @Column(name = "is_price_updatable", columnDefinition = "boolean default true")
    private boolean isPriceUpdatable = true;

    @ManyToMany(mappedBy = "favorites")
    private Set<User> subscribedUsers = new HashSet<>();

    /**
     * Путь к изображению программного обеспечения.
     */
    @Column(name = "image")
    private String image;

    /**
     * Ссылка на программное обеспечение.
     */
    @Column(name = "link", length = 65535)
    @NotEmpty
    private String link;

    @Column(name = "view_count")
    private int viewCount;

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

    @ManyToMany
    @JoinTable(
            name = "software_cods",
            joinColumns = @JoinColumn(name = "software_id"),
            inverseJoinColumns = @JoinColumn(name = "code_id")
    )
    private List<Code> cods;

    @ManyToMany
    @JoinTable(
            name = "software_os",
            joinColumns = @JoinColumn(name = "software_id"),
            inverseJoinColumns = @JoinColumn(name = "os_id")
    )
    private List<OS> os;

    @OneToMany(mappedBy = "software")
    @JsonBackReference
    private List<Comments> comments;

    @OneToMany(mappedBy = "software")
    @JsonBackReference
    private List<Screens> screens;

    @OneToMany(mappedBy = "software")
    @JsonBackReference
    private List<Synonym> synonyms;

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, link);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Software software = (Software) o;
        return reestr_id == software.reestr_id &&
                name.equals(software.name) &&
                ((!isDescriptionUpdatable) || (description == null ? "" : description).equals((software.description == null ? "" : software.description))) &&
                ((!isPriceUpdatable) || (priceText == null ? "" : priceText).equals((software.priceText == null ? "" : software.priceText))) &&
                link.equals(software.link);
    }
}
