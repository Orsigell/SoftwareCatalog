package ru.pin120.via.SoftwareCatalog.Controlers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import ru.pin120.via.SoftwareCatalog.FileUtil;
import ru.pin120.via.SoftwareCatalog.Models.*;
import ru.pin120.via.SoftwareCatalog.Services.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
public class SoftwareController {
    private SoftwareService softwareService;
    private CategoriesService categoriesService;
    private CommentsService commentsService;
    private ScreensService screensService;
    private TagsService tagsService;
    private CodeService codeService;
    private OSService osService;
    private UserService userService;
    private NotificationService notificationService;
    @Autowired
    public void setNotificationService(NotificationService notificationService){this.notificationService = notificationService;}
    @Autowired
    public void setOsService(OSService osService){
        this.osService = osService;
    }
    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    @Autowired
    public void setSoftwareService(SoftwareService softwareService){ this.softwareService = softwareService; }
    @Autowired
    public void setCategoriesService(CategoriesService categoriesService){ this.categoriesService = categoriesService; }
    @Autowired
    public void setCommentsService(CommentsService commentsService){
        this.commentsService = commentsService;
    }
    @Autowired
    public void setScreensService(ScreensService screensService){
        this.screensService = screensService;
    }
    @Autowired
    public void setTagsService(TagsService tagsService){
        this.tagsService = tagsService;
    }
    @Autowired
    public void setCodeService(CodeService codeService){
        this.codeService = codeService;
    }
    @GetMapping("/software")
    public String main(@RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       @RequestParam(value = "sort", defaultValue = "name") String sort,
                       @RequestParam(value = "searchText", required = false) String searchText,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Software> softwarePage = softwareService.getAllSoftware(pageable);

        List<Software> filteredSoftware = softwarePage.getContent();


        if (searchText != null && !searchText.isEmpty()) {
            filteredSoftware = filteredSoftware.stream()
                    .filter(software -> software.getName().toLowerCase().contains(searchText.toLowerCase())
                            || software.getDescription().toLowerCase().contains(searchText.toLowerCase())
                            || (((software.getSynonyms() != null) && (!software.getSynonyms().isEmpty())) && software.getSynonyms().stream().anyMatch(synonym -> synonym.getName().toLowerCase().contains(searchText.toLowerCase()))))
                    .collect(Collectors.toList());
        }

        Map<Long, Double> softwareRatings = new HashMap<>();
        Map<Long, Integer> commentCounts = new HashMap<>();
        Map<Long, Integer> subscriptionCounts = new HashMap<>();
        for (Software software : filteredSoftware) {
            double averageRating = software.getComments().stream()
                    .mapToDouble(Comments::getRating)
                    .average()
                    .orElse(0.0);
            softwareRatings.put(software.getId(), Math.round(averageRating * 10) / 10.0);
            commentCounts.put(software.getId(), commentsService.getCommentCount(software.getId()));
            subscriptionCounts.put(software.getId(), software.getSubscribedUsers().size());
        }

        model.addAttribute("softwares", filteredSoftware);
        model.addAttribute("categories", categoriesService.getAllCategories());
        model.addAttribute("tags", tagsService.getAllTags());
        model.addAttribute("codes", codeService.getAllCodes());
        model.addAttribute("oss", osService.getAllOs());
        model.addAttribute("searchText", searchText);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", softwarePage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("softwareRatings", softwareRatings);
        model.addAttribute("commentCounts", commentCounts);
        model.addAttribute("subscriptionCounts", subscriptionCounts);

        return "software/index";
    }


//    @PostMapping("/software")
//    public String filterSoftware(@RequestParam(value = "categories", required = false) List<Long> categoryIds,
//                                 @RequestParam(value = "tags", required = false) List<Long> tagIds,
//                                 @RequestParam(value = "codes", required = false) List<Long> codeIds,
//                                 @RequestParam(value = "os", required = false) List<Long> osIds,
//                                 @RequestParam(value = "page", defaultValue = "0") int page,
//                                 @RequestParam(value = "size", defaultValue = "10") int size,
//                                 @RequestParam(value = "sort", defaultValue = "name") String sort,
//                                 @RequestParam(value = "searchText", required = false) String searchText,
//                                 Model model) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
//        Page<Software> filteredSoftwarePage = softwareService.getFilteredSoftware(osIds, tagIds, categoryIds, codeIds, searchText, sort, pageable);
//        List<Software> filteredSoftware = filteredSoftwarePage.getContent();
    @PostMapping("/software")
    public String filterSoftware(@RequestParam(value = "categories", required = false) List<Long> categoryIds,
                                 @RequestParam(value = "tags", required = false) List<Long> tagIds,
                                 @RequestParam(value = "codes", required = false) List<Long> codeIds,
                                 @RequestParam(value = "os", required = false) List<Long> osIds,
                                 @RequestParam(value = "sort", defaultValue = "name") String sort,
                                 @RequestParam(value = "searchText", required = false) String searchText,
                                 Model model) {
        List<Software> filteredSoftware = softwareService.getAllSoftware();

        if (categoryIds != null && !categoryIds.isEmpty()) {
            filteredSoftware = filteredSoftware.stream()
                    .filter(software -> software.getCategories().stream().map(Categories::getId).collect(Collectors.toList()).containsAll(categoryIds))
                    .collect(Collectors.toList());
        }

        if (tagIds != null && !tagIds.isEmpty()) {
            filteredSoftware = filteredSoftware.stream()
                    .filter(software -> software.getTags().stream().map(Tags::getId).collect(Collectors.toList()).containsAll(tagIds))
                    .collect(Collectors.toList());
        }

        if (codeIds != null && !codeIds.isEmpty()) {
            filteredSoftware = filteredSoftware.stream()
                    .filter(software -> software.getCods().stream().map(Code::getId).collect(Collectors.toList()).containsAll(codeIds))
                    .collect(Collectors.toList());
        }

        if (osIds != null && !osIds.isEmpty()) {
            filteredSoftware = filteredSoftware.stream()
                    .filter(software -> software.getOs().stream().map(OS::getId).collect(Collectors.toList()).containsAll(osIds))
                    .collect(Collectors.toList());
        }

        if (searchText != null && !searchText.isEmpty()) {
            filteredSoftware = filteredSoftware.stream()
                    .filter(software -> software.getName().toLowerCase().contains(searchText.toLowerCase())
                            || (software.getDescription() != null && software.getDescription().toLowerCase().contains(searchText.toLowerCase()))
                                || (((software.getSynonyms()!= null)&&(!software.getSynonyms().isEmpty())) && software.getSynonyms().stream().anyMatch(synonym -> synonym.getName().toLowerCase().contains(searchText.toLowerCase()))))
                    .collect(Collectors.toList());
        }


        Map<Long, Double> softwareRatings = new HashMap<>();
        Map<Long, Integer> commentCounts = new HashMap<>();
        Map<Long, Integer> subscriptionCounts = new HashMap<>();
        for (Software software : filteredSoftware) {
            double averageRating = software.getComments().stream()
                    .mapToDouble(Comments::getRating)
                    .average()
                    .orElse(0.0);
            softwareRatings.put(software.getId(), averageRating);
            commentCounts.put(software.getId(), commentsService.getCommentCount(software.getId()));
            subscriptionCounts.put(software.getId(), software.getSubscribedUsers().size());
        }
        Comparator<Software> comparator;
        switch (sort){
            case "rating":
                comparator = new Comparator<Software>() {
                    @Override
                    public int compare(Software s1, Software s2) {
                        Double rating1 = softwareRatings.get(s1.getId());
                        Double rating2 = softwareRatings.get(s2.getId());
                        // Обратный порядок для сортировки от высшей оценки к низшей
                        return rating2.compareTo(rating1);
                    }
                };
                Collections.sort(filteredSoftware, comparator);
                break;
            case "name":
                comparator = new Comparator<Software>() {
                    @Override
                    public int compare(Software s1, Software s2) {
                        return s1.getName().compareTo(s2.getName());
                    }
                };
                Collections.sort(filteredSoftware, comparator);
                break;
            case "comments":
                comparator = new Comparator<Software>() {
                    @Override
                    public int compare(Software s1, Software s2) {
                        Integer rating1 = commentCounts.get(s1.getId());
                        Integer rating2 = commentCounts.get(s2.getId());
                        return rating2.compareTo(rating1);
                    }
                };
                Collections.sort(filteredSoftware, comparator);
                break;
            case "views":
                comparator = new Comparator<Software>() {
                    @Override
                    public int compare(Software s1, Software s2) {
                        Integer rating1 = s1.getViewCount();
                        Integer rating2 = s2.getViewCount();
                        return rating2.compareTo(rating1);
                    }
                };
                Collections.sort(filteredSoftware, comparator);
                break;
            case "subscriptions":
                comparator = new Comparator<Software>() {
                    @Override
                    public int compare(Software s1, Software s2) {
                        Integer rating1 = subscriptionCounts.get(s1.getId());
                        Integer rating2 = subscriptionCounts.get(s2.getId());
                        return rating2.compareTo(rating1);
                    }
                };
                Collections.sort(filteredSoftware, comparator);
                break;
        }

        filteredSoftware = filteredSoftware.stream().limit(100).collect(Collectors.toList());

        model.addAttribute("softwares", filteredSoftware);
        model.addAttribute("categories", categoriesService.getAllCategories());
        model.addAttribute("tags", tagsService.getAllTags());
        model.addAttribute("codes", codeService.getAllCodes());
        model.addAttribute("oss", osService.getAllOs());
        model.addAttribute("selectedCategories", categoryIds);
        model.addAttribute("selectedTags", tagIds);
        model.addAttribute("selectedCodes", codeIds);
        model.addAttribute("selectedOS", osIds);
        model.addAttribute("searchText", searchText);
        model.addAttribute("softwareRatings", softwareRatings);
        model.addAttribute("commentCounts", commentCounts);
        model.addAttribute("subscriptionCounts", subscriptionCounts);

        return "software/index";
    }

    /**
     * Удаление скриншота
     */
    @GetMapping("/software/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        softwareService.deleteSoftware(id);
        return "redirect:/software";
    }

    @GetMapping("software/adding")
    public String showAddSoftwareForm(Model model) {
        model.addAttribute("software", new Software());
        return "software/addSoftware";
    }

    @PostMapping("software/adding")
    public String addSoftware(
            @Valid Software newSoftware,
            BindingResult result,
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("software", newSoftware);
            return "software/addSoftware";
        }

        // Обработка изображения
        try {

            String uploadDir = FileUtil.getUploadsFolderPath();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());
            newSoftware.setImage(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        softwareService.createSoftware(newSoftware);

        return "redirect:/software";
    }
    @GetMapping("/software/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        Optional<Software> softwareOptional = softwareService.getSoftwareById(id);
        if (softwareOptional.isPresent()) {
            Software software = softwareOptional.get();
            software.setViewCount(software.getViewCount() + 1);
            softwareService.updateSoftware(software);

            String categoryIds = software.getCategories().stream()
                    .map(Categories::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            String tagIds = software.getTags().stream()
                    .map(Tags::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            String codeIds = software.getCods().stream()
                    .map(Code::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            String osIds = software.getOs().stream()
                    .map(OS::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            model.addAttribute("software", software);
            model.addAttribute("synonyms", software.getSynonyms());
            model.addAttribute("screens", software.getScreens());
            model.addAttribute("comments", software.getComments());
            model.addAttribute("allTags", tagsService.getAllTags());
            model.addAttribute("allOs", osService.getAllOs());
            model.addAttribute("categoryIds", categoryIds);
            model.addAttribute("tagIds", tagIds);
            model.addAttribute("codeIds", codeIds);
            model.addAttribute("osIds", osIds);
            return "software/details";
        } else {
            return "redirect:/software";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/software/details/{id}/addTag")
    public String addTagToSoftware(@PathVariable("id") long softwareId, @RequestParam("tagId") long tagId) {
        Software software = softwareService.getSoftwareById(softwareId).orElse(null);
        Tags tag = tagsService.getTagById(tagId).orElse(null);
        if (software != null && tag != null) {
            if(!software.getTags().contains(tag)){
                software.getTags().add(tag);
                softwareService.updateSoftware(software);
            }
        }
        return "redirect:/software/details/" + softwareId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/software/details/{id}/removeTag/{tagId}")
    public String removeTagFromSoftware(@PathVariable("id") long softwareId, @PathVariable("tagId") long tagId) {
        Software software = softwareService.getSoftwareById(softwareId).orElse(null);
        Tags tag = tagsService.getTagById(tagId).orElse(null);
        if (software != null && tag != null) {
            software.getTags().remove(tag);
            softwareService.updateSoftware(software);
        }
        return "redirect:/software/details/" + softwareId;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/software/details/{id}/addOs")
    public String addOsToSoftware(@PathVariable("id") long softwareId, @RequestParam("osId") long osId) {
        Software software = softwareService.getSoftwareById(softwareId).orElse(null);
        OS os = osService.getOsById(osId).orElse(null);
        if (software != null && os != null) {
            if(!software.getOs().contains(os)){
                software.getOs().add(os);
                softwareService.updateSoftware(software);
            }
        }
        return "redirect:/software/details/" + softwareId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/software/details/{id}/removeOs/{osId}")
    public String removeOsFromSoftware(@PathVariable("id") long softwareId, @PathVariable("osId") long osId) {
        Software software = softwareService.getSoftwareById(softwareId).orElse(null);
        OS os = osService.getOsById(osId).orElse(null);
        if (software != null && os != null) {
            software.getOs().remove(os);
            softwareService.updateSoftware(software);
        }
        return "redirect:/software/details/" + softwareId;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/software/editing/{id}")
    public String editSoftware(@PathVariable Long id, Model model) {
        Optional<Software> softwareOptional = softwareService.getSoftwareById(id);

        if (softwareOptional.isPresent()) {
            Software software = softwareOptional.get();
            model.addAttribute("software", software);
            model.addAttribute("screens", software.getScreens());
            return "software/editSoftware";
        } else {
            return "redirect:/software";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/software/editing/{id}")
    public String updateSoftware(@PathVariable Long id, @ModelAttribute @Valid Software updatedSoftware,
                                 @RequestParam("file") MultipartFile file, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("software", updatedSoftware);
            return "software/editSoftware";
        }

        Optional<Software> existingSoftwareOptional = softwareService.getSoftwareById(id);

        if (existingSoftwareOptional.isPresent()) {
            Software existingSoftware = existingSoftwareOptional.get();
            try {
                if (!file.isEmpty()) {
                    String uploadDir = FileUtil.getUploadsFolderPath();
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName);
                    Files.write(filePath, file.getBytes());
                    existingSoftware.setImage(fileName);
                    existingSoftware.setImageUpdatable(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            existingSoftware.setName(updatedSoftware.getName());
            if((existingSoftware.getDescription() == null) || !existingSoftware.getDescription().equals(updatedSoftware.getDescription())){
                existingSoftware.setDescription(updatedSoftware.getDescription());
                existingSoftware.setDescriptionUpdatable(false);
            }
            if((existingSoftware.getPriceText() == null) ||  !existingSoftware.getPriceText().equals(updatedSoftware.getPriceText())){
                existingSoftware.setPriceText(updatedSoftware.getPriceText());
                existingSoftware.setPriceUpdatable(false);
            }
            existingSoftware.setLink(updatedSoftware.getLink());
            softwareService.updateSoftware(existingSoftware);
            notificationService.sendOneUpdateNotification(existingSoftware);
        } else {
            return "redirect:/software";
        }

        return "redirect:/software/details/{id}";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/software/editing/{softwareId}/deletescreen/{screenId}")
    public String deleteScreen(@PathVariable Long softwareId, @PathVariable Long screenId) {
        try {
            String filePath = FileUtil.getUploadsFolderPath() + "/uploads/" + screensService.getScreensById(screenId).getScreen();
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        screensService.deleteScreens(screenId);
        return "redirect:/software/editing/" + softwareId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/software/editing/{softwareId}/addscreen")
    public String addScreen(@PathVariable Long softwareId, @RequestParam("screen") MultipartFile screen) {
        try {
            String uploadDir = FileUtil.getUploadsFolderPath();
            String fileName = System.currentTimeMillis() + "_" + screen.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, screen.getBytes());

            Screens screens = new Screens();
            screens.setScreen(fileName);

            Software software = softwareService.getSoftwareById(softwareId).orElse(null);

            if (software != null) {
                screens.setSoftware(software);
                software.getScreens().add(screensService.createScreens(screens));

                softwareService.updateSoftware(software);
            } else {
                return "redirect:/software";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/software/editing/" + softwareId;
    }


    @GetMapping("/software/favorites")
    public String favorites(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("favorites", user.getFavorites());
        return "software/favorites";
    }

    @PostMapping("/software/{id}/addFavorite")
    public String addFavorite(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        userService.addFavorite(user.getId(), id);
        return "redirect:/software";
    }

    @PostMapping("/software/{id}/removeFavorite")
    public String removeFavorite(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        userService.removeFavorite(user.getId(), id);
        return "redirect:/software/favorites";
    }


//    @PostMapping("/comments/adding/{softwareId}")
//    public String createComment(@PathVariable Long softwareId, @RequestParam String comment, Model model) {
//        Software software = softwareService.getSoftwareById(softwareId).orElse(null);
//        if (software != null) {
//            Comments newComment = new Comments();
//            newComment.setSoftware(software);
//            newComment.setComment(comment);
//            Comments createdComment = commentsService.createComment(newComment);
//            software.getComments().add(createdComment);
//            softwareService.updateSoftware(software);
//        }
//
//        return "redirect:/software/details/" + softwareId;
//    }

}
