package ru.pin120.via.SoftwareCatalog.Controlers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Services.CommentsService;

import java.util.List;

@Controller
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("/comments")
    public String main(Model model){
        List<Comments> allComments = commentsService.getAllComments();
        model.addAttribute("comments", allComments);
        return "comments/index";
    }
    @Autowired
    public void setCommentsService(CommentsService commentsService){
        this.commentsService = commentsService;
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
    public String add(@Valid Comments comments, BindingResult result, Model model){
        if(!commentsService.findByTextIgnoreCase(comments.getComment()).isEmpty()){
            result.rejectValue("name", "error.comments", "Это имя уже используется");
        }
        if(result.hasErrors()){
            return "comments/adding";
        }
        commentsService.createComment(comments);
        return "redirect:/comments";
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

