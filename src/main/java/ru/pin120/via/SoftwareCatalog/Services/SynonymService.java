package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Synonym;
import ru.pin120.via.SoftwareCatalog.Repositories.SynonymRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SynonymService {
    private SynonymRepository synonymRepository;

    @Autowired
    public SynonymService(SynonymRepository synonymRepository) {
        this.synonymRepository = synonymRepository;
    }

    public List<Synonym> getAllSynonyms() {
        List<Synonym> synonyms = (List<Synonym>) synonymRepository.findAll();
        return synonyms.stream()
                .sorted((s1, s2) -> Long.compare(s2.getId(), s1.getId()))
                .collect(Collectors.toList());
    }

    public Synonym createSynonym(Synonym synonym) {
        return synonymRepository.save(synonym);
    }

    public List<Synonym> findByNameIgnoreCase(String name) {
        return synonymRepository.findByNameIgnoreCase(name);
    }

    public Synonym getSynonymById(Long id) {
        return synonymRepository.findById(id).orElse(null);
    }

    public void updateSynonym(Synonym synonym) {
        synonymRepository.save(synonym);
    }

    public void deleteSynonym(Long id) {
        synonymRepository.deleteById(id);
    }

    public void deleteSynonyms(List<Synonym> synonyms) {
        for (Synonym synonym : synonyms) {
            synonymRepository.delete(synonym);
        }
    }
}