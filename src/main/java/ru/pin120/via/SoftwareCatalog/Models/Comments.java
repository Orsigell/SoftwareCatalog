package ru.pin120.via.SoftwareCatalog.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "comment", columnDefinition = "LONGTEXT")
    @NotEmpty
    private String comment;

    /**
     * Оценка комментария.
     */
    @Column(name = "rating")
    @NotNull
    private Long rating;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "software_id")
    private Software software;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private User user;

    public Comments(String commentText, Long rating) {
        this.comment = commentText;
        this.rating = rating;
    }
}