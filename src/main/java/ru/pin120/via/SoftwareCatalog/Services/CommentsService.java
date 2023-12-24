package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Categories;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Repositories.CommentsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentsService {
    private CommentsRepository commentsRepository;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    // Метод для создания
    public Comments createComment(Comments comments) {
        return commentsRepository.save(comments);
    }

    // Метод для получения по ID
    public Comments getCommentById(Long id) {
        Optional<Comments> optionalComment = commentsRepository.findById(id);
        return optionalComment.orElse(null);
    }

    // Метод для обновления
    public Comments updateComment(Comments comments) {
        return commentsRepository.save(comments);
    }

    // Метод для получения всех
    public List<Comments> getAllComments() {
        List<Comments> comments = (List<Comments>) commentsRepository.findAll();
        return comments.stream()
                .sorted((c1, c2) -> Long.compare(c2.getId(), c1.getId())) // Сортировка в обратном порядке
                .collect(Collectors.toList());
    }

    // Метод для удаления по ID
    public void deleteComment(Long id) {

        Comments comments = commentsRepository.findById(id).orElse(null);
        if (comments != null) {
            for (Software software : comments.getSoftwares()) {
                software.getComments().remove(comments);
            }
            commentsRepository.deleteById(id);
        }
    }

    public List<Comments> findByTextIgnoreCase(String text) {
        return commentsRepository.findByCommentIgnoreCase(text);
    }
}