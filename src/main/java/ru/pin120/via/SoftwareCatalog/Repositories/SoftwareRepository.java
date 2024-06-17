package ru.pin120.via.SoftwareCatalog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SoftwareRepository extends JpaRepository<Software, Long> {
    Page<Software> findAll(Pageable pageable);
    @Query("SELECT s FROM Software s WHERE s.reestr_id = :pReestr_id")
    Optional<Software> findAllbyReestrId(@Param("pReestr_id") Long reestr_id);
//    @Query("SELECT s FROM Software s " +
//            "JOIN s.osList os " +
//            "JOIN s.tagsList t " +
//            "JOIN s.categoriesList c " +
//            "JOIN s.codesList cd " +
//            "LEFT JOIN s.synonyms syn " +
//            "LEFT JOIN s.comments com " +
//            "LEFT JOIN s.userFavorites uf " +
//            "WHERE (:osIds IS NULL OR os.id IN :osIds) " +
//            "AND (:tagIds IS NULL OR t.id IN :tagIds) " +
//            "AND (:categoryIds IS NULL OR c.id IN :categoryIds) " +
//            "AND (:codeIds IS NULL OR cd.id IN :codeIds) " +
//            "AND (s.name LIKE %:keyword% OR s.description LIKE %:keyword% OR syn.name LIKE %:keyword%) " +
//            "GROUP BY s.id " +
//            "ORDER BY CASE :sort " +
//            "WHEN 'rating' THEN AVG(com.rating) " +
//            "WHEN 'comments' THEN COUNT(com) " +
//            "WHEN 'views' THEN s.viewCount " +
//            "WHEN 'subscriptions' THEN COUNT(uf) " +
//            "ELSE s.name END DESC")
//    Page<Software> filterSoftware(@Param("osIds") List<Long> osIds,
//                                  @Param("tagIds") List<Long> tagIds,
//                                  @Param("categoryIds") List<Long> categoryIds,
//                                  @Param("codeIds") List<Long> codeIds,
//                                  @Param("keyword") String keyword,
//                                  @Param("sort") String sort,
//                                  Pageable pageable);


    @Query("SELECT s FROM Software s LEFT JOIN FETCH s.subscribedUsers WHERE s.id = :id")
    Optional<Software> findByIdWithSubscribedUsers(Long id);
}
