package ru.pin120.via.SoftwareCatalog;

import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.pin120.via.SoftwareCatalog.Models.Categories;
import ru.pin120.via.SoftwareCatalog.Models.Code;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Models.Synonym;
import ru.pin120.via.SoftwareCatalog.Services.SoftwareService;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class SoftwareParser {
    public static List<Software> readExcelFile(String exelFilePath) {
        disableSSLCertificateChecking();
        List<Software> softwares = new ArrayList<>() ;
        FileInputStream file;
        Workbook workbook = null;
        try {
            file = new FileInputStream(exelFilePath);
            workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            for (Row row : sheet) {
                try {
                    if (row.getRowNum() < 5) { // Пропускаем заголовок
                        continue;
                    }
//                    if((long)row.getCell(0).getNumericCellValue()==22195){
                    i++;
                    if(i>=10)//Ограничение количества данных для дебага
                        break;
                    Software software = new Software();
                    software.setReestr_id((long)row.getCell(0).getNumericCellValue());
                    software.setName(row.getCell(1).getStringCellValue());
                    software.setLink(row.getCell(24).getStringCellValue()!=null?row.getCell(24).getStringCellValue():row.getCell(6).getStringCellValue());
                    if(!row.getCell(2).getStringCellValue().isEmpty()){
                        software.setSynonyms(new ArrayList<Synonym>());
                        String[] synTexts = row.getCell(2).getStringCellValue().replace(";", "").split("\\r?\\n");
                        if(!row.getCell(2).getStringCellValue().isEmpty() && synTexts.length==0){
                            Synonym synonym = new Synonym();
                            synonym.setName(row.getCell(2).getStringCellValue());
                            software.getSynonyms().add(synonym);
                        }
                        for (String synText: synTexts){
                            Synonym synonym = new Synonym();
                            synonym.setSoftware(software);
                            synonym.setName(synText);
                            software.getSynonyms().add(synonym);
                        }
                    }


                    if(!row.getCell(4).getStringCellValue().isEmpty()){
                        software.setCods(new ArrayList<Code>());
                        String[] codeTexts = row.getCell(4).getStringCellValue().replace(";", "").split("\\r?\\n");
                        if(!row.getCell(4).getStringCellValue().isEmpty() && codeTexts.length==0){
                            Code code = new Code();
                            code.setName(row.getCell(4).getStringCellValue());
                            software.getCods().add(code);
                        }
                        else {
                            for (String codeText: codeTexts){
                                if(!codeText.isEmpty()){
                                    Code code = new Code();
                                    code.setName(codeText);
                                    software.getCods().add(code);
                                }
                            }
                        }
                    }

                    if(!row.getCell(3).getStringCellValue().isEmpty()){
                        software.setCategories(new ArrayList<Categories>());
                        String[] categoryTexts = row.getCell(3).getStringCellValue().replace(";", "").split("\\r?\\n");
                        if(!row.getCell(3).getStringCellValue().isEmpty() && categoryTexts.length==0){
                            Categories category = new Categories();
                            category.setName(row.getCell(3).getStringCellValue());
                            software.getCategories().add(category);
                        }
                        else{
                            for (String categoryText: categoryTexts){
                                if(!categoryText.isEmpty()){
                                    Categories category = new Categories();
                                    category.setName(categoryText);
                                    software.getCategories().add(category);
                                }
                            }
                        }
                    }
                    String lincCheck = row.getCell(24).getStringCellValue().toLowerCase();
                    if (!lincCheck.endsWith(".pdf") && !lincCheck.endsWith(".7z") && !lincCheck.endsWith(".docx"))
                    {
                        try {
                            Element doc = Jsoup.connect(row.getCell(24).getStringCellValue()).get().body();
                            String logoUrl = "";
                            Elements logoContainers = doc.select("[class~=(?i).*logo.*], [id~=(?i).*logo.*]");
                            for (Element container : logoContainers) {
                                Element logoElement = container.selectFirst("img");
                                if (logoElement != null) {
                                    logoUrl = logoElement.attr("src");
                                    if (logoUrl.isEmpty()) {
                                        logoUrl = logoElement.attr("data-original");
                                    }
                                    break;
                                }
                            }

                            if (logoUrl.isEmpty()) {
                                Elements logoImages = doc.select("img[class~=(?i).*logo.*], img[id~=(?i).*logo.*]");
                                if (!logoImages.isEmpty()) {
                                    Element logoElement = logoImages.first();
                                    logoUrl = logoElement.attr("src");
                                    if (logoUrl.isEmpty()) {
                                        logoUrl = logoElement.attr("data-original");
                                    }
                                }
                            }

                            // Если логотип все еще не найден, ищем изображения с "logo" в названии файла
                            if (logoUrl.isEmpty()) {
                                Elements images = doc.select("img");
                                for (Element image : images) {
                                    String src = image.attr("src");
                                    if (src.toLowerCase().contains("logo")) {
                                        logoUrl = src;
                                    }
                                    if (logoUrl.isEmpty()) {
                                        String dataOriginal = image.attr("data-original");
                                        if (dataOriginal.toLowerCase().contains("logo")) {
                                            logoUrl = dataOriginal;
                                        }
                                    }
                                    if (!logoUrl.isEmpty()) {
                                        break; // Останавливаем поиск после первой найденной картинки
                                    }
                                }
                            }

                            if (!logoUrl.isEmpty()) {
                                if (logoUrl.charAt(0) != 'h') {
                                    if (logoUrl.charAt(0) == '.') {
                                        logoUrl = getBaseUrl(row.getCell(24).getStringCellValue()) + logoUrl.substring(1);
                                    } else if (logoUrl.charAt(0) == '/') {
                                        logoUrl = getBaseUrl(row.getCell(24).getStringCellValue()) + logoUrl;
                                    } else {
                                        logoUrl = row.getCell(24).getStringCellValue() + logoUrl;
                                    }
                                }
                                //System.out.println("Логотип сайта " + row.getCell(1).getStringCellValue() + ": " + logoUrl);

                                int lastSlashIndex = logoUrl.lastIndexOf('/');
                                String fileName = logoUrl.substring(lastSlashIndex + 1);
                                if (fileName.indexOf('?') != -1) {
                                    fileName = fileName.substring(0, fileName.indexOf('?'));
                                }
                                if (fileName.length() > 30) {
                                    fileName = fileName.substring(fileName.length() - 30);
                                }
                                // Скачивание и сохранение логотипа
                                try (InputStream in = new URL(logoUrl).openStream()) {
                                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                                    String uniqueFileName = timeStamp + fileName;
                                    Path filePath = Paths.get(FileUtil.getUploadsFolderPath(), uniqueFileName);
                                    Files.copy(in, filePath);
                                    software.setImage(uniqueFileName);
                                    //System.out.println("Логотип сохранен как: " + uniqueFileName);
                                    software.setImage(uniqueFileName);
                                } catch (IOException e) {
                                    //System.out.println("Ошибка при скачивании логотипа: " + e.getMessage());
                                    software.setImage("NotFound.png");
                                }
                            } else {
                                software.setImage("NotFound.png");
                                //System.out.println("Логотип сайта: " + row.getCell(1).getStringCellValue() + " не найден");
                            }
                            try {
                                String description = parseCompanyInfo(Jsoup.connect(row.getCell(24).getStringCellValue()).get().body(), 100);
                                if ((description != null)&&(!(description.isEmpty()))){
                                    if(description.length() < 65000){
                                        software.setDescription(description);
                                    }
                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                                //System.out.println("Во время поиска описание продукта: " + row.getCell(1).getStringCellValue() + " произошла ошибка");
                            }
                            //System.out.println("Итоговое описание продукта: " + row.getCell(1).getStringCellValue() + " -  " + software.getDescription());

                            // Парсинг цены продукта
                            String pricePattern = "\\d{1,3}(?:[ \\u00A0]?\\d{3})*(?:[.,]\\d{2})?\\s*(?:₽|руб\\.?|р\\.?|рублей)";
                            Elements priceElements = doc.select("*:matchesOwn(" + pricePattern + ")");
                            if (!priceElements.isEmpty()) {
                                String setPriceText = "";
                                for (Element priceElement : priceElements) {
                                    //System.out.println("Цена продукта:  " + row.getCell(1).getStringCellValue() + " : " + priceElement.text());
                                    setPriceText+=priceElement.text()+"\n\r";
                                }
                                software.setPriceText(setPriceText.length()<65000?setPriceText:null);
                            } else {
                                //System.out.println("Цена продукта: " + row.getCell(1).getStringCellValue() + " не найдена");
                            }
                            //System.out.println(row.getCell(24).getStringCellValue());
                            //System.out.println("\n\r\n\r");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        software.setImage("NotFound.png");
                    }
                    softwares.add(software);//}
                }catch(Exception ex) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return softwares;
    }

    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String parseCompanyInfo(Element doc, int minLength) {
        Elements paragraphs = doc.select("p");
        for (Element paragraph : paragraphs) {
            String text = paragraph.text();
            if (text.length() >= minLength) {
                return text;
            }
        }
        return null;
    }



    public static String getBaseUrl(String fullUrl) {
        try {
            URL url = new URL(fullUrl);
            String protocol = url.getProtocol();
            String host = url.getHost();
            return protocol + "://" + host;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }
}
