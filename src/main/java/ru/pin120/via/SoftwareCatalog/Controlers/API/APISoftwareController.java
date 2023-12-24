package ru.pin120.via.SoftwareCatalog.Controlers.API;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Models.Screens;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Services.CategoriesService;
import ru.pin120.via.SoftwareCatalog.Services.CommentsService;
import ru.pin120.via.SoftwareCatalog.Services.ScreensService;
import ru.pin120.via.SoftwareCatalog.Services.SoftwareService;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class APISoftwareController {
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
    public Iterable<Software> getAllSoftware(Model model) {
        Iterable<Software> softwareList = softwareService.getAllSoftware();
        for (Software software : softwareList) {
            software.setImage("/api/software/image/" + software.getId()); // путь к изображению
            Iterable<Screens> screensList = software.getScreens();
            for (Screens screens : screensList) {
                screens.setScreen("/api/screens/image/" + screens.getId()); // путь к изображению
            }
        }
        return softwareList;
    }

    @GetMapping("/software/image/{id}")
    public ResponseEntity<Resource> getSoftwareImage(@PathVariable long id) {
        Optional<Software> software = softwareService.getSoftwareById(id);
        Resource imageResource = null; // предполагается, что image - это путь к файлу на сервере
        try {
            imageResource = new UrlResource("file:F:\\Downloads\\SoftwareCatalog\\target\\classes\\static\\uploads\\" + software.get().getImage());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // или другой тип изображения
                .body(imageResource);
    }

    /**
     * Добавление копии ПО
     * */
    @GetMapping("/software/adding")
    public String selectSoftwareToInsert(Model model){
        model.addAttribute("softwares", softwareService.getAllSoftware());
        model.addAttribute("url", "adding/");
        return "software/selectSoftware";
    }

    @PostMapping("/software/adding")
    public Software createSoftware(@RequestBody @Valid Software software, BindingResult result, Model model){
        if(result.hasErrors()){
            throw new ValidationException("Ошибка при попытке добавить программу");
        }
        return softwareService.createSoftware(software);
    }
    @GetMapping("/software/adding/{softwareId}")
    public String SelectLicenceTypeToInsert(@PathVariable Long softwareId, Model model){
        model.addAttribute("categories", categoriesService.getAllCategories());
        model.addAttribute("url", softwareId + "/");
        model.addAttribute("selectedCategories", new HashSet<>());
        return "software/selectCategories";
    }
    @GetMapping("/software/adding/{softwareId}/{categoriesId}")
    public String selectEmployeeToInsert(@PathVariable Long softwareId, @PathVariable String categoriesId, Model model){
        model.addAttribute("screens", screensService.getAllScreens());
        model.addAttribute("url",   categoriesId + "/");
        return "software/selectScreens";
    }
    //@PostMapping("/software/adding/{softwareId}/{categoriesId}/{screensId}")
    //public String changeSoftware(@PathVariable Long softwareId, @PathVariable String categoriesId, @PathVariable String screensId, BindingResult result, Model model){
    //    Optional<Software> software = softwareService.getSoftwareById(softwareId);
    //    return "redirect:/software";
    //}
}
