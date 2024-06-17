package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Code;
import ru.pin120.via.SoftwareCatalog.Models.OS;
import ru.pin120.via.SoftwareCatalog.Repositories.OSRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OSService {

    @Autowired
    private OSRepository osRepository;

    public Page<OS> findAll(Pageable pageable) {
        return osRepository.findAll(pageable);
    }

    public Page<OS> searchOS(String name, Pageable pageable) {
        return osRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public void save(OS os) {
        osRepository.save(os);
    }

    public OS findById(long id) {
        Optional<OS> os = osRepository.findById(id);
        return os.orElse(null);
    }

    public void deleteById(long id) {
        osRepository.deleteById(id);
    }
    public List<OS> getAllOs() {
        List<OS> codes = osRepository.findAll();
        return new ArrayList<>(codes);
    }

    public Optional<OS> getOsById(Long osId) {
       return osRepository.findById(osId);
    }
}
