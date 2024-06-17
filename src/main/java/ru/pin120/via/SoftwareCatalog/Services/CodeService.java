package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Code;
import ru.pin120.via.SoftwareCatalog.Repositories.CodeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CodeService {
    private CodeRepository codeRepository;

    @Autowired
    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public List<Code> getAllCodes() {
        List<Code> codes = (List<Code>) codeRepository.findAll();
        return new ArrayList<>(codes);
    }

    public Code createCode(Code code) {
        return codeRepository.save(code);
    }

    public List<Code> findByNameIgnoreCase(String name) {
        return codeRepository.findByNameIgnoreCase(name);
    }

    public Code getCodeById(Long id) {
        return codeRepository.findById(id).orElse(null);
    }

    public void updateCode(Code code) {
        codeRepository.save(code);
    }

    public void deleteCode(Long id) {
        Code code = codeRepository.findById(id).orElse(null);
        if (code != null) {
            code.getSoftwares().forEach(software -> software.getCods().remove(code));
            codeRepository.deleteById(id);
        }
    }

    public void deleteCodes(List<Code> codes) {
        for (Code code : codes) {
            deleteCode(code.getId());
        }
    }
}
