package ru.pin120.via.SoftwareCatalog;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static String getUploadsFolderPath() {
        try {
            // Получаем путь к папке static внутри JAR/WAR-файла
            Resource resource = new ClassPathResource("static");
            String staticPath = resource.getFile().getAbsolutePath();

            // Создаем путь к папке uploads внутри static
            String uploadsPath = staticPath + File.separator + "uploads";

            // Создаем папку, если ее нет
            File uploadsFolder = new File(uploadsPath);
            if (!uploadsFolder.exists()) {
                uploadsFolder.mkdirs();
            }

            return uploadsPath;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось получить путь к папке uploads.", e);
        }
    }
}
