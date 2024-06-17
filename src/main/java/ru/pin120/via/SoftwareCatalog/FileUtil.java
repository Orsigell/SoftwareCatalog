package ru.pin120.via.SoftwareCatalog;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    public static String getUploadsFolderPath() {
        try {
            Resource resource = new ClassPathResource("static");
            String staticPath = resource.getFile().getAbsolutePath();
            String uploadsPath = staticPath + File.separator + "uploads";
            File uploadsFolder = new File(uploadsPath);
            if (!uploadsFolder.exists()) {
                uploadsFolder.mkdirs();
            }

            return uploadsPath;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось получить путь к папке uploads.", e);
        }
    }
    public static String downloadFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String filePath = getUploadsFolderPath()+timeStamp+"softwareDataset.xlsx";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://reestr.digital.gov.ru/reestr/?export=list");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.addHeader("Accept-Language", "en-US,en;q=0.5");
        httpGet.addHeader("Connection", "keep-alive");

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = client.execute(httpGet)) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    try (InputStream inputStream = entity.getContent();
                         FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                        System.out.println("File downloaded successfully.");
                        return filePath;
                    }
                }
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
