package ru.pin120.via.SoftwareCatalog.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Models.User;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments,Long> {
    List<Comments> findByCommentIgnoreCase(String text);
    // List<Comments> findByNumberIgnoreCase(String number);
    Page<Comments> findAll(Pageable pageable);

//    @Query("SELECT c FROM comments c WHERE c.user_id = :userId")
//    List<Comments> findCommentByUserId(@Param("userId") Long userId);
    List<Comments> findByUserId(Long userId);
    List<Comments> findBySoftware_id(Long software_id);
}
