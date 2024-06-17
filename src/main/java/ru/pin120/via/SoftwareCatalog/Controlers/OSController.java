package ru.pin120.via.SoftwareCatalog.Controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.pin120.via.SoftwareCatalog.Models.OS;
import ru.pin120.via.SoftwareCatalog.Services.OSService;

import java.util.List;

@Controller
@RequestMapping("/os")
public class OSController {

    @Autowired
    private OSService osService;

    @GetMapping
    public String listOS(Model model,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size,
                         @RequestParam(value = "search", required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OS> osPage;
        if (search != null && !search.isEmpty()) {
            osPage = osService.searchOS(search, pageable);
        } else {
            osPage = osService.findAll(pageable);
        }
        model.addAttribute("oss", osPage.getContent());
        model.addAttribute("totalPages", osPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("search", search);
        return "os/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("os", new OS());
        return "os/add";
    }

    @PostMapping
    public String addOS(@ModelAttribute OS os) {
        osService.save(os);
        return "redirect:/os";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        OS os = osService.findById(id);
        model.addAttribute("os", os);
        return "os/edit";
    }

    @PostMapping("/update")
    public String updateOS(@ModelAttribute OS os) {
        osService.save(os);
        return "redirect:/os";
    }

    @GetMapping("/delete/{id}")
    public String deleteOS(@PathVariable("id") long id) {
        osService.deleteById(id);
        return "redirect:/os";
    }
}
