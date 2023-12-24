package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Repositories.SoftwareRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class SoftwareService {

    private SoftwareRepository softwareRepository;

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
        softwareRepository.deleteById(id);
    }

    public Optional<Software> getSoftwareById(Long softwareId) {
        return softwareRepository.findById(softwareId);
    }
}
