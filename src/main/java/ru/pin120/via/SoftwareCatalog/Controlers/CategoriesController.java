package ru.pin120.via.SoftwareCatalog.Controlers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pin120.via.SoftwareCatalog.Models.Categories;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Services.CategoriesService;
import ru.pin120.via.SoftwareCatalog.Services.SoftwareService;

import java.util.List;

@Controller
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("/categories")
    public String main(Model model){
        List<Categories> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "categories/index";
    }

    @Autowired
    private SoftwareService softwareService;  // Добавьте инъекцию зависимости для сервиса программ


    @PostMapping("/categories/adding")
    public String add(
            @Valid Categories categories,
            BindingResult result,
            @RequestParam(value = "softwareId", required = false) Long softwareId,  // Добавьте параметр для выбора программы
            Model model) {
        if (softwareId != null) {
            Software software = softwareService.getSoftwareById(softwareId).orElse(null);
            software.getCategories().add(categories);
        }

        if (!categoriesService.findByNameIgnoreCase(categories.getName()).isEmpty()) {
            result.rejectValue("name", "error.categories", "Это имя уже используется");
        }

        if (result.hasErrors()) {
            model.addAttribute("allSoftware", softwareService.getAllSoftware());  // Передайте список программ в модель
            return "categories/adding";
        }

        categoriesService.createCategories(categories);
        return "redirect:/categories";
    }
    @Autowired
    public void setSoftwareService(SoftwareService softwareService){
        this.softwareService = softwareService;
    }
    @Autowired
    public void setCategoriesService(CategoriesService categoriesService){
        this.categoriesService = categoriesService;
    }
    /**
     * Добавление
     * */
    @GetMapping("/categories/adding")
    public String add(Model model) {
        model.addAttribute("categories", new Categories());
        model.addAttribute("allSoftware", softwareService.getAllSoftware());
        return "categories/adding";
    }

    @GetMapping("/categories/update/{id}")
    public String update(@PathVariable Long id, Model model){
        Categories categories = categoriesService.getCategoriesById(id);
        model.addAttribute("categories", categories);
        return "categories/editing";
    }
    @PostMapping("/categories/update")
    public String update(@Valid Categories categories, BindingResult result, Model model){
        List<Categories> categoriesList = categoriesService.findByNameIgnoreCase(categories.getName());
        if(!categoriesList.isEmpty()){
            if(!(categoriesList.get(0).getId() == categories.getId())){
                result.rejectValue("name", "error.categories", "Это имя уже используется");
            }
        }
        if(result.hasErrors()){
            return "categories/editing";
        }
        categoriesService.updateCategories(categories);
        return "redirect:/categories";
    }
    /**
     * Удаление
     * */
    @GetMapping("/categories/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        categoriesService.deleteCategories(id);
        return "redirect:/categories";
    }
}

