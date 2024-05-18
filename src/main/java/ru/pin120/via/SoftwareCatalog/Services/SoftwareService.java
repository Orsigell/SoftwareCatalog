package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Models.Screens;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Repositories.CommentsRepository;
import ru.pin120.via.SoftwareCatalog.Repositories.ScreensRepository;
import ru.pin120.via.SoftwareCatalog.Repositories.SoftwareRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class SoftwareService {

    private SoftwareRepository softwareRepository;

    private CommentsService commentsService;
    private ScreensService screensService;
    @Autowired
    public void setCommentsService(CommentsService commentsService){
        this.commentsService = commentsService;
    }
    @Autowired
    public void setScreensService(ScreensService screensService){
        this.screensService = screensService;
    }

    @Autowired
    public SoftwareService(SoftwareRepository softwareRepository) {
        this.softwareRepository = softwareRepository;
    }

    // Метод для создания
    public Software createSoftware(Software software) {
        return softwareRepository.save(software);
    }


    // Метод для обновления
    public Software updateSoftware(Software Software) {
        return softwareRepository.save(Software);
    }

    // Метод для получения всех
    public List<Software> getAllSoftware() {
        List<Software> softwareList = (List<Software>) softwareRepository.findAll();
        return softwareList.stream()
                .sorted((s1, s2) -> Long.compare(s2.getId(), s1.getId())) // Сортировка в обратном порядке
                .collect(Collectors.toList());
    }
    // Метод для удаления по ID
    public void deleteSoftware(Long id) {
        Optional<Software> software = softwareRepository.findById(id);
        screensService.deleteScreens(software.get().getScreens());
        commentsService.deleteComments(software.get().getComments());
        softwareRepository.deleteById(id);
    }

    public Page<Software> getAllSoftware(Pageable pageable) {
        return softwareRepository.findAll(pageable);
    }

    public Optional<Software> getSoftwareById(Long softwareId) {
        return softwareRepository.findById(softwareId);
    }
}
