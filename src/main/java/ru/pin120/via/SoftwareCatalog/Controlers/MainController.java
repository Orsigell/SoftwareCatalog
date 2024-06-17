package ru.pin120.via.SoftwareCatalog.Controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.pin120.via.SoftwareCatalog.FileUtil;
import ru.pin120.via.SoftwareCatalog.Models.Categories;
import ru.pin120.via.SoftwareCatalog.Models.Code;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Models.Synonym;
import ru.pin120.via.SoftwareCatalog.Services.*;
import ru.pin120.via.SoftwareCatalog.SoftwareParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private ParsingService parsingService;
    @GetMapping("/")
    public String main(Model model){
        return "redirect:/software";
    }
    @GetMapping("/about")
    public String about(){
        return "main/about";
    }

    @PostMapping("/uploadData")
    @ResponseBody
    public ModelAndView exportData() {
        String exportPath = FileUtil.downloadFile();
        if (exportPath != null){
            List<Software> newSoftwares = SoftwareParser.readExcelFile(exportPath);
            parsingService.parseAndUpdate(newSoftwares);
        }
        return new ModelAndView("redirect:/software");
    }
}
