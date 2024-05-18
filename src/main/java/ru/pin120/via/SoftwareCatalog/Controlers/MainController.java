package ru.pin120.via.SoftwareCatalog.Controlers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.pin120.via.SoftwareCatalog.Models.Categories;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.SoftwareParser;

import java.util.List;

@Controller
public class MainController {
    @GetMapping("/")
    public String main(Model model){
        return "redirect:/software";
    }
    @GetMapping("/about")
    public String about(){
        return "main/about";
    }
    @PostMapping("/uploadData")
    public String uploadData(@RequestParam("filePath") String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "redirect:/software";
        }
        List<Software> entries = SoftwareParser.readExcelFile(filePath);

        return "redirect:/software";
    }
    //@GetMapping("/form")
    //public String mainForm(Model model){
    //    model.addAttribute("student", new Categories());
    //    return "main-form";
    // }
    //@PostMapping("/form")
    //public String mainForm(@ModelAttribute Categories student, Model model) {
    //    student.setId(0);
    //    student.setName("");
    //    model.addAttribute("student", student);
    //    return "result";
    //}
}
