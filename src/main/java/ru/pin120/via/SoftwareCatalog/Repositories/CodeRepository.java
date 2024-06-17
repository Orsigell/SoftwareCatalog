package ru.pin120.via.SoftwareCatalog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pin120.via.SoftwareCatalog.Models.Code;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, Long> {
    List<Code> findByNameIgnoreCase(String name);
}
