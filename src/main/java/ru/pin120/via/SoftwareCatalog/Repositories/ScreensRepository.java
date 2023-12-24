package ru.pin120.via.SoftwareCatalog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pin120.via.SoftwareCatalog.Models.Screens;

import java.util.List;

public interface ScreensRepository extends JpaRepository<Screens,Long> {
    List<Screens> findByScreenIgnoreCase(String screen);
    //List<Screens> findByNumberIgnoreCase(String number);
}
