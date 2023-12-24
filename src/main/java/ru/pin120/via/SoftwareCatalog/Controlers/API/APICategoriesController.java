package ru.pin120.via.SoftwareCatalog.Controlers.API;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.pin120.via.SoftwareCatalog.Models.Categories;
import ru.pin120.via.SoftwareCatalog.Services.CategoriesService;

import java.util.List;

@RequestMapping("/api")
@RestController
public class APICategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("/categories")
    public Iterable<Categories> main(Model model){
        return categoriesService.getAllCategories();
    }
    @Autowired
    public void setCategoriesService(CategoriesService categoriesService){
        this.categoriesService = categoriesService;
    }
    /**
     * Добавление
     * */
    @GetMapping("/categories/adding")
    public String add(Model model){
        model.addAttribute("categories", new Categories());
        return "categories/adding";
    }
    @PostMapping("/categories/adding")
    public Categories add(@RequestBody @Valid Categories categories, BindingResult result, Model model){
        if(!categoriesService.findByNameIgnoreCase(categories.getName()).isEmpty()){
            throw new EntityExistsException("Категория с таким названием уже существует");
        }
        if(result.hasErrors()){
            throw new ValidationException("Проверьте правильность введённых данных");
        }
        return categoriesService.createCategories(categories);
    }
    /**
     * Изменение аудитории
     * */
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

