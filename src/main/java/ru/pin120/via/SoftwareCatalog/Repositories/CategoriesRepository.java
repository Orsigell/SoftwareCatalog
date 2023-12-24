package ru.pin120.via.SoftwareCatalog.Repositories;

import ru.pin120.via.SoftwareCatalog.Models.Categories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    List<Categories> findByNameIgnoreCase(String category_name);
}
