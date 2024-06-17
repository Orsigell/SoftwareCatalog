package ru.pin120.via.SoftwareCatalog.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pin120.via.SoftwareCatalog.Models.OS;

import java.util.List;

public interface OSRepository extends JpaRepository<OS, Long> {
    List<OS> findByNameIgnoreCase(String name);
    Page<OS> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
