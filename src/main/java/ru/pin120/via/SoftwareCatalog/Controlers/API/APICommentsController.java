package ru.pin120.via.SoftwareCatalog.Controlers.API;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Services.CommentsService;
import ru.pin120.via.SoftwareCatalog.Services.SoftwareService;

import java.util.List;

@RequestMapping("/api")
@RestController
public class APICommentsController {

    @Autowired
    private CommentsService commentsService;
    @Autowired
    private SoftwareService softwareService;

    @PostMapping("/comments/adding/{softwareId}")
    public Comments createComment(@RequestBody @Valid Comments comments, @PathVariable Long softwareId, BindingResult result, Model model){
        if(result.hasErrors()){
            throw new ValidationException("Ошибка при попытке добавить комментарий");
        }
        Comments comment = commentsService.createComment(comments);
        softwareService.getSoftwareById(softwareId).get().getComments().add(comment);
        return commentsService.createComment(comments);
    }

    @GetMapping("/comments/for-software/{softwareId}")
    public Iterable<Comments> forSoftware(Model model, @PathVariable Long softwareId){
        return  softwareService.getSoftwareById(softwareId).get().getComments();
    }

    @GetMapping("/comments")
    public Iterable<Comments> main(Model model){
        return  commentsService.getAllComments();
    }
    @Autowired
    public void setCommentsService(CommentsService commentsService){
        this.commentsService = commentsService;
    }
    @Autowired
    public void setSoftwareService(SoftwareService softwareService){
        this.softwareService = softwareService;
    }
    /**
     * Добавление
     * */
    @GetMapping("/comments/adding")
    public String add(Model model){
        model.addAttribute("comments", new Comments());
        return "comments/adding";
    }
    @PostMapping("/comments/adding")
    public Comments add(@RequestBody @Valid Comments comments, BindingResult result, Model model){
        if(result.hasErrors()){
            throw new ValidationException("Ошибка при попытке добавить комментарий");
        }
        return commentsService.createComment(comments);
    }
    /**
     * Изменение аудитории
     * */
    @GetMapping("/comments/update/{id}")
    public String update(@PathVariable Long id, Model model){
        Comments comments = commentsService.getCommentById(id);
        model.addAttribute("comments", comments);
        return "comments/editing";
    }
    @PostMapping("/comments/update")
    public String update(@Valid Comments comments, BindingResult result, Model model){
        List<Comments> commentsList = commentsService.findByTextIgnoreCase(comments.getComment());
        if(!commentsList.isEmpty()){
            if(!(commentsList.get(0).getId() == comments.getId())){
                result.rejectValue("name", "error.comments", "Это имя уже используется");
            }
        }
        if(result.hasErrors()){
            return "comments/editing";
        }
        commentsService.updateComment(comments);
        return "redirect:/comments";
    }
    /**
     * Удаление
     * */
    @GetMapping("/comments/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        commentsService.deleteComment(id);
        return "redirect:/comments";
    }
}

