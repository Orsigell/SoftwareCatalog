package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Comments;
import ru.pin120.via.SoftwareCatalog.Repositories.CommentsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public Comments createComment(Comments comments) {
        return commentsRepository.save(comments);
    }

    public Comments getCommentById(Long id) {
        Optional<Comments> optionalComment = commentsRepository.findById(id);
        return optionalComment.orElse(null);
    }

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

    // Метод для получения всех с пагинацией
    public Page<Comments> getAllComments(Pageable pageable) {
        return commentsRepository.findAll(pageable);
    }

    // Метод для удаления по ID
    public void deleteComment(Long id) {
        Comments comments = commentsRepository.findById(id).orElse(null);
        if (comments != null) {
            comments.getSoftware().getComments().remove(comments);
            commentsRepository.deleteById(id);
        }
    }

    public List<Comments> findByTextIgnoreCase(String text) {
        return commentsRepository.findByCommentIgnoreCase(text);
    }

    public void deleteComments(List<Comments> comments) {
        for (Comments comment : comments) {
            commentsRepository.delete(comment);
        }
    }

    public List<Comments> getCommentsByUser(Long userId) {
        return commentsRepository.findByUserId(userId);
    }

    public Integer getCommentCount(long id) {
        return commentsRepository.findBySoftware_id(id).size();
    }
}
