package ru.pin120.via.SoftwareCatalog.Controlers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pin120.via.SoftwareCatalog.Models.Categories;

@Controller
public class MainController {
    @GetMapping("/")
    public String main(Model model){return "redirect:/software";}
    @GetMapping("/about")
    public String about(@RequestParam(name="name", required = false, defaultValue = "По умолчанию") String name, Model model){
        model.addAttribute("author", name);
        return "main/about";
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
