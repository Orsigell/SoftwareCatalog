package ru.pin120.via.SoftwareCatalog.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pin120.via.SoftwareCatalog.Models.Tags;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {
    Tags findByName(String name);
    Page<Tags> findAll(Pageable pageable);

    Page<Tags> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
