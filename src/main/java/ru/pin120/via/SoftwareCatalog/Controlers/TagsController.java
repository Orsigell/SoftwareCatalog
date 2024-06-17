package ru.pin120.via.SoftwareCatalog.Controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Models.Tags;
import ru.pin120.via.SoftwareCatalog.Services.TagsService;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagsController {

    @Autowired
    private TagsService tagsService;

    @GetMapping
    public String getAllTags(@RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "search", required = false) String search,
                             Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tags> tagsPage;
        if (search != null && !search.isEmpty()) {
            tagsPage = tagsService.searchTagsByName(search, pageable);
        } else {
            tagsPage = tagsService.getAllTagsPag(pageable);
        }

        model.addAttribute("tags", tagsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tagsPage.getTotalPages());
        model.addAttribute("search", search);

        return "tags/list";
    }

    @GetMapping("/{id}")
    public String getTagById(@PathVariable("id") long id, Model model) {
        Tags tag = tagsService.getTagById(id).orElse(null);
        model.addAttribute("tag", tag);
        return "tags/detail";
    }

    @GetMapping("/add")
    public String showAddTagForm(Model model) {
        model.addAttribute("tag", new Tags());
        return "tags/add";
    }

    @PostMapping
    public String createTag(@ModelAttribute Tags tag) {
        tagsService.saveTag(tag);
        return "redirect:/tags";
    }

    @GetMapping("/edit/{id}")
    public String showEditTagForm(@PathVariable("id") long id, Model model) {
        Tags tag = tagsService.getTagById(id).orElse(null);
        model.addAttribute("tag", tag);
        return "tags/edit";
    }

    @PostMapping("/update")
    public String updateTag(@ModelAttribute Tags tag) {
        tagsService.saveTag(tag);
        return "redirect:/tags";
    }
    @GetMapping("/delete/{id}")
    public String deleteTag(@PathVariable("id") long id) {
        Tags tags = tagsService.getTagById(id).orElse(null);
        tagsService.deleteTag(id);
        return "redirect:/tags";
    }
}
