package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    private String comment;

    @JsonBackReference
    @ManyToMany(mappedBy = "comments")
    private List<Software> softwares;
    public Comments(String commentText) {
        this.comment = commentText;
    }
}
