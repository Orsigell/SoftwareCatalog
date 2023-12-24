package ru.pin120.via.SoftwareCatalog.Services;

import ru.pin120.via.SoftwareCatalog.Models.Categories;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Repositories.CategoriesRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    // Метод для создания
    public Categories createCategories(Categories category) {
        return categoriesRepository.save(category);
    }

    // Метод для получения по ID
    public Categories getCategoriesById(Long id) {
        return categoriesRepository.findById(id).orElse(null);
    }

    // Метод для обновления
    public Categories updateCategories(Categories categories) {
        return categoriesRepository.save(categories);
    }

    public List<Categories> getAllCategories() {
        List<Categories> categories = categoriesRepository.findAll();
        return categories.stream()
                .sorted((c1, c2) -> Long.compare(c2.getId(), c1.getId())) // Сортировка в обратном порядке
                .collect(Collectors.toList());
    }

    // Метод для удаления по ID
    public void deleteCategories(Long id) {
        Categories categories = categoriesRepository.findById(id).orElse(null);
        if (categories != null) {
            for (Software software : categories.getSoftwares()) {
                software.getCategories().remove(categories);
            }
            categoriesRepository.deleteById(id);
        }
    }
    public List<Categories> findByNameIgnoreCase(String name){ return categoriesRepository.findByNameIgnoreCase(name); }
}
