package ru.pin120.via.SoftwareCatalog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pin120.via.SoftwareCatalog.Models.Software;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface SoftwareRepository extends JpaRepository<Software, Long> {
}
