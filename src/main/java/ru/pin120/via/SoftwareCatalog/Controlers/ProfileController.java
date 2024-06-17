package ru.pin120.via.SoftwareCatalog.Controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.pin120.via.SoftwareCatalog.FileUtil;
import ru.pin120.via.SoftwareCatalog.Models.User;
import ru.pin120.via.SoftwareCatalog.Services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class ProfileController {

    @Autowired
    private UserService userService;
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", userService.getUserByEmail(userDetails.getUsername()));
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("file") MultipartFile file,
                                @ModelAttribute("user") User currentUser,
                                Model model) {
        try {
            User user = userService.getUserByEmail(userDetails.getUsername());
            if (!file.isEmpty()) {
                String uploadDir = FileUtil.getUploadsFolderPath();
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.write(filePath, file.getBytes());
                user.setProfilePhotoUrl(fileName);
            }
            user.setLastName(currentUser.getLastName());
            user.setLogin(currentUser.getLogin());
            user.setFirstName(currentUser.getFirstName());
            userService.updateUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/profile";
    }
}