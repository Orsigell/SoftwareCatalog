package ru.pin120.via.SoftwareCatalog.Controlers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.pin120.via.SoftwareCatalog.FileUtil;
import ru.pin120.via.SoftwareCatalog.Models.Screens;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Services.ScreensService;
import ru.pin120.via.SoftwareCatalog.Services.SoftwareService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class ScreensController {

    @Autowired
    private ScreensService screensService;

    @GetMapping("/screens")
    public String main(Model model) {
        List<Screens> allScreens = screensService.getAllScreens();
        model.addAttribute("screens", allScreens);
        return "screens/index";
    }

    @Autowired
    private SoftwareService softwareService;

    @PostMapping("/screens/adding")
    public String add(
            @Valid Screens screens,
            BindingResult result,
            @RequestParam("file") MultipartFile file,
            @RequestParam("softwareId") Long softwareId,
            Model model) {
        // ...

        try {
            // Сохранение файла на сервере
            String uploadDir = FileUtil.getUploadsFolderPath();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(uploadDir, fileName));
            screens.setScreen(fileName);

            // Получите программу по ее ID и добавьте скриншот к ней
            Software software = softwareService.getSoftwareById(softwareId).orElseThrow(() -> new RuntimeException("Программа не найдена"));
            screens.getSoftwares().add(software);

            // Сохранение информации о скриншоте в базе данных
            screensService.createScreens(screens);
        } catch (IOException e) {
            e.printStackTrace();  // Обработка ошибки сохранения файла
        }

        return "redirect:/screens";
    }
    @Autowired
    public void setSoftwareService(SoftwareService softwareService) {
        this.softwareService = softwareService;
    }

    @Autowired
    public void setScreensService(ScreensService screensService) {
        this.screensService = screensService;
    }

    /**
     * Добавление
     */
    @GetMapping("/screens/adding")
    public String add(Model model) {
        model.addAttribute("screens", new Screens());

        // Получите список всех программ и передайте его в модель
        List<Software> allSoftware = softwareService.getAllSoftware();
        model.addAttribute("allSoftware", allSoftware);

        return "screens/adding";
    }




    /**
     * Изменение скриншота
     */
    @GetMapping("/screens/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Screens screens = screensService.getScreensById(id);
        model.addAttribute("screens", screens);
        return "screens/editing";
    }

    @PostMapping("/screens/update")
    public String update(@Valid Screens screens, BindingResult result, Model model) {
        List<Screens> screensList = screensService.findByNameIgnoreCase(screens.getScreen());
        if (!screensList.isEmpty()) {
            if (!(screensList.get(0).getId() == screens.getId())) {
                result.rejectValue("name", "error.screens", "Это имя уже используется");
            }
        }

        if (result.hasErrors()) {
            return "screens/editing";
        }

        screensService.updateScreens(screens);
        return "redirect:/screens";
    }

    /**
     * Удаление скриншота
     */
    @GetMapping("/screens/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        Screens screens = screensService.getScreensById(id);

        // Получите путь к скриншоту
        String filePath = FileUtil.getUploadsFolderPath() + screens.getScreen();

        try {
            // Удалите файл
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();  // Обработка ошибки удаления файла
        }

        // Удаление записи из базы данных
        screensService.deleteScreens(id);

        return "redirect:/screens";
    }
}
