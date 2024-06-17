package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Tags;
import ru.pin120.via.SoftwareCatalog.Repositories.TagsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagsService {

    @Autowired
    private TagsRepository tagsRepository;

    public List<Tags> getAllTags() {
        return tagsRepository.findAll();
    }
    public Page<Tags> getAllTagsPag(Pageable pageable) {
        return tagsRepository.findAll(pageable);
    }

    public Optional<Tags> getTagById(long id) {
        return tagsRepository.findById(id);
    }

    public Tags saveTag(Tags tag) {
        return tagsRepository.save(tag);
    }

    public void deleteTag(long id) {
        tagsRepository.deleteById(id);
    }

    public Page<Tags> searchTagsByName(String name, Pageable pageable) {
        return tagsRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
