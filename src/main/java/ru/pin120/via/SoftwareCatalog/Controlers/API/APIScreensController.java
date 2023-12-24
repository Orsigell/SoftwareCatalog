package ru.pin120.via.SoftwareCatalog.Controlers.API;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Models.Screens;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Services.ScreensService;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class APIScreensController {

    @Autowired
    private ScreensService screensService;

    @GetMapping("/screens")
    public Iterable<Screens> main(Model model){
        Iterable<Screens> screensList = screensService.getAllScreens();
        for (Screens screens : screensList) {
            screens.setScreen("/api/screens/image/" + screens.getId()); // путь к изображению
        }
        return screensList;
    }
    @Autowired
    public void setScreensService(ScreensService screensService){
        this.screensService = screensService;
    }
    /**
     * Добавление
     * */
    @GetMapping("/screens/image/{id}")
    public ResponseEntity<Resource> getSoftwareImage(@PathVariable long id) {
        Optional<Screens> software = Optional.ofNullable(screensService.getScreensById(id));
        Resource imageResource = null; // предполагается, что image - это путь к файлу на сервере
        try {
            imageResource = new UrlResource("file:F:\\Downloads\\SoftwareCatalog\\target\\classes\\static\\uploads\\" + software.get().getScreen());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // или другой тип изображения
                .body(imageResource);
    }
    @GetMapping("/screens/adding")
    public String add(Model model){
        model.addAttribute("screens", new Screens());
        return "screens/adding";
    }
    @PostMapping("/screens/adding")
    public Screens add(@RequestBody @Valid Screens screens, BindingResult result, Model model){
        if(result.hasErrors()){
            throw new ValidationException("Ошибка при попытке добавить скриншот");
        }
        return screensService.createScreens(screens);
    }
    /**
     * Изменение аудитории
     * */
    @GetMapping("/screens/update/{id}")
    public String update(@PathVariable Long id, Model model){
        Screens screens = screensService.getScreensById(id);
        model.addAttribute("screens", screens);
        return "screens/editing";
    }
    @PostMapping("/screens/update")
    public String update(@Valid Screens screens, BindingResult result, Model model){
        List<Screens> screensList = screensService.findByNameIgnoreCase(screens.getScreen());
        if(!screensList.isEmpty()){
            if(!(screensList.get(0).getId() == screens.getId())){
                result.rejectValue("name", "error.screens", "Это имя уже используется");
            }
        }
        if(result.hasErrors()){
            return "screens/editing";
        }
        screensService.updateScreens(screens);
        return "redirect:/screens";
    }
    /**
     * Удаление
     * */
    @GetMapping("/screens/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        screensService.deleteScreens(id);
        return "redirect:/screens";
    }
}

