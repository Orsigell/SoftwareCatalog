package ru.pin120.via.SoftwareCatalog.Controlers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import ru.pin120.via.SoftwareCatalog.FileUtil;
import ru.pin120.via.SoftwareCatalog.Models.Categories;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Models.Screens;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Services.CategoriesService;
import ru.pin120.via.SoftwareCatalog.Services.CommentsService;
import ru.pin120.via.SoftwareCatalog.Services.ScreensService;
import ru.pin120.via.SoftwareCatalog.Services.SoftwareService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
public class SoftwareController {
    private SoftwareService softwareService;
    private CategoriesService categoriesService;
    private CommentsService commentsService;
    private ScreensService screensService;
    @Autowired
    public void setSoftwareService(SoftwareService softwareService){
        this.softwareService = softwareService;
    }
    @Autowired
    public void setCategoriesService(CategoriesService categoriesService){ this.categoriesService = categoriesService; }
    @Autowired
    public void setCommentsService(CommentsService commentsService){
        this.commentsService = commentsService;
    }
    @Autowired
    public void setScreensService(ScreensService screensService){
        this.screensService = screensService;
    }
    @GetMapping("/software")
    public String main(@RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Software> softwarePage = softwareService.getAllSoftware(pageable);
        List<Categories> allCategories = categoriesService.getAllCategories(); // Получение списка категорий
        model.addAttribute("softwares", softwarePage.getContent());
        model.addAttribute("categories", allCategories); // Добавление списка категорий в модель
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", softwarePage.getTotalPages());
        return "software/index";
    }

    @PostMapping("/software")
    public String filterSoftware(@RequestParam(value = "categories", required = false) List<Long> categoryIds,
                                 @RequestParam(value = "searchText", required = false) String searchText,
                                 Model model) {
        List<Software> filteredSoftware;

        if (categoryIds != null && !categoryIds.isEmpty()) {
            // Фильтрация по категориям, если они указаны
            Predicate<Software> categoryFilter = software -> software.getCategories()
                    .stream()
                    .map(Categories::getId)
                    .collect(Collectors.toList())
                    .containsAll(categoryIds);

            filteredSoftware = softwareService.getAllSoftware().stream()
                    .filter(categoryFilter)
                    .collect(Collectors.toList());
        } else {
            // Если категории не указаны, получаем все программы
            filteredSoftware = softwareService.getAllSoftware();
        }

        if (searchText != null && !searchText.isEmpty()) {
            // Фильтрация по тексту, если он указан
            filteredSoftware = filteredSoftware.stream()
                    .filter(software ->
                            software.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                                    software.getDescription().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("softwares", filteredSoftware);
        model.addAttribute("categories", categoriesService.getAllCategories());
        return "software/index";
    }

    /**
     * Удаление скриншота
     */
    @GetMapping("/software/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        softwareService.deleteSoftware(id);
        return "redirect:/software";
    }

    @GetMapping("software/adding")
    public String showAddSoftwareForm(Model model) {
        model.addAttribute("software", new Software());
        return "software/addSoftware";
    }

    @PostMapping("software/adding")
    public String addSoftware(
            @Valid Software newSoftware,
            BindingResult result,
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("software", newSoftware);
            return "software/addSoftware";
        }

        // Обработка изображения
        try {

            String uploadDir = FileUtil.getUploadsFolderPath();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());

            // Установка пути к файлу в поле image объекта Software
            newSoftware.setImage(fileName);
        } catch (IOException e) {
            e.printStackTrace(); // Обработайте ошибку сохранения файла
        }

        softwareService.createSoftware(newSoftware);

        return "redirect:/software";
    }
    @GetMapping("/software/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        Optional<Software> softwareOptional = softwareService.getSoftwareById(id);
        if (softwareOptional.isPresent()) {
            Software software = softwareOptional.get();
            model.addAttribute("software", software);
            model.addAttribute("screens", software.getScreens());
            model.addAttribute("comments", software.getComments());
            return "software/details";
        } else {
            return "redirect:/software";
        }
    }


    @GetMapping("/software/editing/{id}")
    public String editSoftware(@PathVariable Long id, Model model) {
        Optional<Software> softwareOptional = softwareService.getSoftwareById(id);

        if (softwareOptional.isPresent()) {
            Software software = softwareOptional.get();
            model.addAttribute("software", software);
            model.addAttribute("screens", software.getScreens());  // Добавьте атрибут для скриншотов
            model.addAttribute("categories", categoriesService.getAllCategories()); // Передача списка всех категорий
            return "software/editSoftware";
        } else {
            // Обработка случая, когда программу не удалось найти
            return "redirect:/software";
        }
    }

    @PostMapping("/software/editing/{softwareId}/addCategory")
    public String addCategory(@PathVariable Long softwareId, @RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) String newCategory) {
        Software software = softwareService.getSoftwareById(softwareId).orElse(null);

        if (software != null) {
            if (categoryId != null) {
                // Добавление существующей категории
                Categories category = categoriesService.getCategoriesById(categoryId);
                if (category != null && !software.getCategories().contains(category)) {
                    software.getCategories().add(category);
                    softwareService.updateSoftware(software);
                }
            } else if (newCategory != null && !newCategory.isEmpty()) {
                // Добавление новой категории
                Categories category = new Categories();
                category.setName(newCategory);
                category.setSoftwares(List.of(software));
                software.getCategories().add(categoriesService.createCategories(category));
                softwareService.updateSoftware(software);
            }
        }

        return "redirect:/software/editing/" + softwareId;
    }



    @GetMapping("/software/editing/{softwareId}/deleteCategory/{categoryId}")
    public String deleteCategory(@PathVariable Long softwareId, @PathVariable Long categoryId) {
        categoriesService.deleteCategories(categoryId);
        return "redirect:/software/editing/" + softwareId;
    }

    @PostMapping("/software/editing/{id}")
    public String updateSoftware(@PathVariable Long id, @ModelAttribute @Valid Software updatedSoftware,
                                 @RequestParam("file") MultipartFile file, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("software", updatedSoftware);
            return "software/editSoftware";
        }

        Optional<Software> existingSoftwareOptional = softwareService.getSoftwareById(id);

        if (existingSoftwareOptional.isPresent()) {
            Software existingSoftware = existingSoftwareOptional.get();

            // Обработка изображения
            try {
                if (!file.isEmpty()) {
                    String uploadDir = FileUtil.getUploadsFolderPath();
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName);
                    Files.write(filePath, file.getBytes());

                    // Установка пути к файлу в поле image объекта Software
                    existingSoftware.setImage(fileName);
                }
            } catch (IOException e) {
                e.printStackTrace(); // Обработайте ошибку сохранения файла
            }

            // Update fields based on data from updatedSoftware
            existingSoftware.setName(updatedSoftware.getName());
            existingSoftware.setDescription(updatedSoftware.getDescription());
            existingSoftware.setSystemRequirements(updatedSoftware.getSystemRequirements());
            existingSoftware.setLicenseName(updatedSoftware.getLicenseName());
            existingSoftware.setLicenseType(updatedSoftware.getLicenseType());
            existingSoftware.setLicensePrice(updatedSoftware.getLicensePrice());
            existingSoftware.setLicenseDuration(updatedSoftware.getLicenseDuration());
            // Update other fields as needed
            softwareService.updateSoftware(existingSoftware);
        } else {
            // Handle the case when the software cannot be found
            return "redirect:/software";
        }

        return "redirect:/software/details/{id}";
    }


    @GetMapping("/software/editing/{softwareId}/deletescreen/{screenId}")
    public String deleteScreen(@PathVariable Long softwareId, @PathVariable Long screenId) {

        try {
            String filePath = FileUtil.getUploadsFolderPath() + "/uploads/" + screensService.getScreensById(screenId).getScreen();
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace(); // Обработайте ошибку удаления файла, если необходимо
        }

        screensService.deleteScreens(screenId);
        return "redirect:/software/editing/" + softwareId;
    }


    @PostMapping("/software/editing/{softwareId}/addscreen")
    public String addScreen(@PathVariable Long softwareId, @RequestParam("screen") MultipartFile screen) {
        try {
            String uploadDir = FileUtil.getUploadsFolderPath();
            String fileName = System.currentTimeMillis() + "_" + screen.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, screen.getBytes());

            Screens screens = new Screens();
            screens.setScreen(fileName);

            // Получаем программу из базы данных
            Software software = softwareService.getSoftwareById(softwareId).orElse(null);

            if (software != null) {
                // Связываем скриншот с программой
                screens.setSoftware(software);
                software.getScreens().add(screensService.createScreens(screens));

                // Сохраняем изменения в базе данных
                softwareService.updateSoftware(software);
            } else {
                // Обработка случая, если программа не найдена
                return "redirect:/software";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/software/editing/" + softwareId;
    }



    @PostMapping("/comments/adding/{softwareId}")
    public String createComment(@PathVariable Long softwareId, @RequestParam String comment, Model model) {
        Software software = softwareService.getSoftwareById(softwareId).orElse(null);
        if (software != null) {
            Comments newComment = new Comments();
            newComment.setSoftware(software);
            newComment.setComment(comment);
            Comments createdComment = commentsService.createComment(newComment);
            software.getComments().add(createdComment);
            softwareService.updateSoftware(software);
        }

        return "redirect:/software/details/" + softwareId;
    }

}
