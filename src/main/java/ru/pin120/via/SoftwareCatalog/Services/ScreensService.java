package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.*;
import ru.pin120.via.SoftwareCatalog.Repositories.ScreensRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreensService {
    private ScreensRepository screensRepository;

    @Autowired
    public ScreensService(ScreensRepository screensRepository) {
        this.screensRepository = screensRepository;
    }
    public List<Screens> getAllScreens() {
        List<Screens> screens = (List<Screens>) screensRepository.findAll();
        return screens.stream()
                .sorted((s1, s2) -> Long.compare(s2.getId(), s1.getId())) // Сортировка в обратном порядке
                .collect(Collectors.toList());
    }
    public Screens createScreens(Screens screens) {
       return screensRepository.save(screens);
    }

    public List<Screens> findByNameIgnoreCase(String screen) {
       return screensRepository.findByScreenIgnoreCase(screen);
    }

    public Screens getScreensById(Long id) {
        return screensRepository.getById(id);
    }

    public void updateScreens(Screens screens) {
        screensRepository.save(screens);
    }

    public void deleteScreens(Long id) {
        Screens screens = screensRepository.findById(id).orElse(null);
        screens.getSoftware().getScreens().remove(screens);
        screensRepository.deleteById(id);
    }
    public void deleteScreens(List <Screens> screens){
        for(Screens screen : screens){
            screensRepository.delete(screen);
        }
    }
}
