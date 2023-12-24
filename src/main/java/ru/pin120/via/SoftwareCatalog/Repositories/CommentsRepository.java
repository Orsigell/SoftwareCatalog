package ru.pin120.via.SoftwareCatalog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pin120.via.SoftwareCatalog.Models.Comments;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments,Long> {
    List<Comments> findByCommentIgnoreCase(String text);
    // List<Comments> findByNumberIgnoreCase(String number);
}
