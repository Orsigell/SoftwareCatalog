package ru.pin120.via.SoftwareCatalog.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.pin120.via.SoftwareCatalog.Models.Synonym;

import java.util.List;

public interface SynonymRepository extends JpaRepository<Synonym, Long> {
    List<Synonym> findByNameIgnoreCase(String name);
}