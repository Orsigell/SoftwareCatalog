package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

/**
 * Модель комментариев.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comments {
    /**
     * Уникальный идентификатор комментария.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

    /**
     * Текст комментария.
     */
    @Column(name = "comment")
    @NotEmpty
    private String comment;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "software_id")
    private Software software;
    public Comments(String commentText) {
        this.comment = commentText;
    }
}
