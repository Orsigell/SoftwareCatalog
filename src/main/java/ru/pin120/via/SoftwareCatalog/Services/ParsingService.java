package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.FileUtil;
import ru.pin120.via.SoftwareCatalog.Models.Categories;
import ru.pin120.via.SoftwareCatalog.Models.Code;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Models.Synonym;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParsingService {
    @Autowired
    private SoftwareService softwareService;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private CodeService codsService;
    @Autowired
    private SynonymService synonymService;

    public void parseAndUpdate(List<Software> newSoftwares) {
        for (Software software: newSoftwares) {
            Software oldSoftware = softwareService.getSoftwareByReestrId(software.getReestr_id()).orElse(null);
            Software newSoftware = null;
            boolean isNewSoftware = false;
            if (oldSoftware == null){
                Software emptySoftware = new Software();
                emptySoftware.setReestr_id(software.getReestr_id());
                emptySoftware.setName(software.getName());
                emptySoftware.setImage(software.getImage());
                emptySoftware.setLink(software.getLink());
                emptySoftware.setDescription(software.getDescription());
                emptySoftware.setSynonyms(new ArrayList<>());
                emptySoftware.setCods(new ArrayList<>());
                emptySoftware.setCategories(new ArrayList<>());
                try {
                    newSoftware = softwareService.createSoftware(emptySoftware);
                }catch (Exception ex){
                    ex.printStackTrace();
                    continue;
                }
                isNewSoftware = true;
            }
            else{
                if(!"NotFound.png".equals(oldSoftware.getImage()) && !oldSoftware.isImageUpdatable()){
                    String oldImagePath = FileUtil.getUploadsFolderPath() + "/" + oldSoftware.getImage();
                    try {
                        Path oldImageFile = Paths.get(oldImagePath);
                        if (Files.exists(oldImageFile)) {
                            Files.delete(oldImageFile);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (!oldSoftware.equals(software)) {
                    oldSoftware.setName(software.getName());
                    oldSoftware.setLink(software.getLink());
                    if(oldSoftware.isImageUpdatable()){
                        oldSoftware.setImage(software.getImage());
                    }
                    if(oldSoftware.isDescriptionUpdatable()){
                        oldSoftware.setDescription(software.getDescription());
                    }
                    if(oldSoftware.isPriceUpdatable()){
                        oldSoftware.setPriceText(software.getPriceText());
                    }
                    notificationService.addSoftwareToUpdateList(oldSoftware);
                }
                newSoftware = oldSoftware;
            }
            if (software.getCategories() != null){
                List<Categories> categories = software.getCategories();
                for (Categories category: categories) {
                    Categories oldCategory = categoriesService.findByNameIgnoreCase(category.getName()).stream().findFirst().orElse(null);
                    if (oldCategory != null){
                        if(newSoftware.getCategories().stream().noneMatch(newSoftCat->newSoftCat.getName().equals(category.getName()))){
                            newSoftware.getCategories().add(oldCategory);
                        }
                    }
                    else {
                        try {
                            Categories newCategory = categoriesService.createCategories(category);
                            newSoftware.getCategories().add(newCategory);
                        }catch (Exception ex){
                            ex.printStackTrace();
                            continue;
                        }
                    }
                }
                List<Categories> newCategories = software.getCategories();
                newSoftware.getCategories().removeIf(currentCategory ->
                        newCategories.stream().noneMatch(newCategory -> newCategory.getName().equalsIgnoreCase(currentCategory.getName())));


            }
            if (software.getCods() != null){
                List<Code> codes = software.getCods();
                for (Code code: codes) {
                    Code oldCode = codsService.findByNameIgnoreCase(code.getName()).stream().findFirst().orElse(null);
                    if (oldCode != null){
                        if(newSoftware.getCods().stream().noneMatch(ns-> ns.getName().equals(code.getName()))){
                            newSoftware.getCods().add(oldCode);
                        }
                    }
                    else {
                        try {
                            Code newCode = codsService.createCode(code);
                            newSoftware.getCods().add(newCode);
                        }catch (Exception ex){
                            ex.printStackTrace();
                            continue;
                        }
                    }
                }
                List<Code> newCodeList = software.getCods();
                newSoftware.getCods().removeIf(currentCodes ->
                        newCodeList.stream().noneMatch(newCodes -> newCodes.getName().equalsIgnoreCase(currentCodes.getName())));
            }
            if (software.getSynonyms() != null){
                for (Synonym newSynonym : software.getSynonyms())
                    if(newSoftware.getSynonyms().stream().noneMatch(synonym -> synonym.getName().equals(newSynonym.getName()))){
                        Synonym synonym = new Synonym();
                        synonym.setUpdatable(1L);
                        synonym.setName(newSynonym.getName());
                        synonym.setSoftware(newSoftware);
                        newSoftware.getSynonyms().add(synonymService.createSynonym(synonym)); //Нужно удалить все синонимы которых нет у новой программы и у которых свойство updatable = 1
                    }
                List<Synonym> newSynonyms = software.getSynonyms();
                newSoftware.getSynonyms().removeIf(currentSynonym ->
                        (currentSynonym.getUpdatable()==1L) &&
                                newSynonyms.stream().noneMatch(newSynonym -> newSynonym.getName().equalsIgnoreCase(currentSynonym.getName())));

            }
            if(!isNewSoftware){
                try {
                    softwareService.updateSoftware(newSoftware);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        notificationService.sendUpdateNotification();
    }
}
