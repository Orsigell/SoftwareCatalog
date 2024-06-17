package ru.pin120.via.SoftwareCatalog.Controlers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Models.User;
import ru.pin120.via.SoftwareCatalog.Services.CommentsService;
import ru.pin120.via.SoftwareCatalog.Services.SoftwareService;
import ru.pin120.via.SoftwareCatalog.Services.UserService;

import java.util.List;

@Controller
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private SoftwareService softwareService;

    @Autowired
    private UserService userService;

//    @GetMapping("/comments")
//    public String main(Model model) {
//        List<Comments> allComments = commentsService.getAllComments();
//        model.addAttribute("comments", allComments);
//        return "comments/index";
//    }
    @GetMapping("/comments")
    public String main(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<Comments> comments;

        if (user.getRole().contains("ROLE_ADMIN")) {
            comments = commentsService.getAllComments();
        } else {
            comments = commentsService.getCommentsByUser(user.getId());
        }

        model.addAttribute("comments", comments);
        return "comments/index";
    }



//    @GetMapping("/comments")
//    public String main(@RequestParam(value = "page", defaultValue = "0") int page,
//                       @RequestParam(value = "size", defaultValue = "10") int size,
//                       Model model) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Comments> commentsPage = commentsService.getAllComments(pageable);
//        model.addAttribute("comments", commentsPage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", commentsPage.getTotalPages());
//        return "comments/index";
//    }

    @Autowired
    public void setCommentsService(CommentsService commentsService) {
        this.commentsService = commentsService;
    }
//    @GetMapping("/comments/adding/{softwareId}")
//    public String add(@PathVariable("softwareId") Long softwareId, Model model){
//        model.addAttribute("comments", new Comments());
//        model.addAttribute("softwareId", softwareId);
//        return "comments/adding";
//    }
//
//    @PostMapping("/comments/adding/{softwareId}")
//    public String add(@PathVariable("softwareId") Long softwareId, @Valid Comments comments, BindingResult result, Model model){
//        if(result.hasErrors()){
//            return "comments/adding";
//        }
//        Software software = softwareService.getSoftwareById(softwareId).orElse(null);
//        if (software != null) {
//            comments.setSoftware(software);
//            commentsService.createComment(comments);
//        }
//        return "redirect:/software/details/" + softwareId;
//    }
    @PostMapping("/comments/adding/{softwareId}")
    public String createComment(@PathVariable Long softwareId, @RequestParam String comment, Long rating, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Software software = softwareService.getSoftwareById(softwareId).orElse(null);
        if (software != null) {
            Comments newComment = new Comments();
            User user = userService.getUserByEmail(userDetails.getUsername());
            newComment.setSoftware(software);
            newComment.setComment(comment);
            newComment.setRating(rating);
            newComment.setUser(user);
            Comments createdComment = commentsService.createComment(newComment);
            software.getComments().add(createdComment);
            softwareService.updateSoftware(software);
        }

        return "redirect:/software/details/" + softwareId;
    }

    @GetMapping("/comments/update/{id}")
    public String update(@PathVariable Long id, Model model){
        Comments comments = commentsService.getCommentById(id);
        model.addAttribute("comments", comments);
        return "comments/editing";
    }

    @PostMapping("/comments/update")
    public String update(@Valid Comments comments, BindingResult result, Model model){
        if(result.hasErrors()){
            return "comments/editing";
        }
        commentsService.updateComment(comments);
        return "redirect:/comments";
    }

//    @GetMapping("/comments/delete/{id}")
//    public String delete(@PathVariable("id") Long id){
//        commentsService.deleteComment(id);
//        return "redirect:/comments";
//    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/comments/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        Comments comment = commentsService.getCommentById(id);
        if (comment != null && comment.getSoftware() != null) {
            Long softwareId = comment.getSoftware().getId();
            commentsService.deleteComment(id);
            return "redirect:/software/details/" + softwareId;
        }
        return "redirect:/comments";
    }
}