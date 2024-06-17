package ru.pin120.via.SoftwareCatalog.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Models.User;
import ru.pin120.via.SoftwareCatalog.Repositories.SoftwareRepository;
import ru.pin120.via.SoftwareCatalog.Repositories.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;


    @Autowired
    private SoftwareRepository softwareRepository;
    private List<Software> updatedSoftware = new ArrayList<>();

    @Transactional
    public void addSoftwareToUpdateList(Software software){
        updatedSoftware.add(software);
    }

    @Transactional
    public void sendUpdateNotification() {
        List<Software> updatedSoftwareWithSubscribedUsers = new ArrayList<>();
        for(Software software: updatedSoftware){
            Optional<Software> softwareWithUsers = softwareRepository.findByIdWithSubscribedUsers(software.getId());
            if (softwareWithUsers.isPresent()) {
                updatedSoftwareWithSubscribedUsers.add(software);
            }
        }

        Map<User, List<Software>> userSoftwareMap = new HashMap<>();
        for (Software software : updatedSoftware) {
            Optional<Software> softwareWithUsers = softwareRepository.findByIdWithSubscribedUsers(software.getId());
            if (softwareWithUsers.isPresent()) {
                Software softwareEntity = softwareWithUsers.get();
                for (User subscriber : softwareEntity.getSubscribedUsers()) {
                    userSoftwareMap.computeIfAbsent(subscriber, k -> new ArrayList<>()).add(softwareEntity);
                }
            }
        }
        for (Map.Entry<User, List<Software>> entry : userSoftwareMap.entrySet()) {
            User user = entry.getKey();
            List<Software> softwareList = entry.getValue();
            sendEmailWithAttachment(user.getEmail(), "Программное обеспечение обновлено", "Список обновленного ПО в файле.", softwareList);
        }
    }

    private void sendEmailWithAttachment(String to, String subject, String body, List<Software> softwareList) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("ilyant2222@mail.ru");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Updated Software");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Список обновленного ПО");
            int rowNum = 1;
            for (Software software : softwareList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(software.getName());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            byte[] bytes = outputStream.toByteArray();
            helper.addAttachment("UpdatedSoftware.xlsx", new ByteArrayResource(bytes));
            mailSender.send(message);
            updatedSoftware.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendOneUpdateNotification(Software software) {
        Optional<Software> softwareWithUsers = softwareRepository.findByIdWithSubscribedUsers(software.getId());
        if (softwareWithUsers.isPresent()) {
            Set<User> subscribedUsers = softwareWithUsers.get().getSubscribedUsers();
            for (User user : subscribedUsers) {
                sendEmail(user.getEmail(), "Программное обеспечение обновлено", "Программное обеспечение " + software.getName() + " было обновлено.");
            }
        }
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("ilyant2222@mail.ru");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
