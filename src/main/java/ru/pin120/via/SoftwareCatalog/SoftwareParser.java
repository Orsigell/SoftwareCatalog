package ru.pin120.via.SoftwareCatalog;

import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.pin120.via.SoftwareCatalog.Models.Software;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SoftwareParser {
    public static List<Software> readExcelFile(String filePath) {
        List<Software> softwares = new ArrayList<>() ;
        FileInputStream file;
        Workbook workbook = null;
        try {
            file = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Пропускаем заголовок
                    continue;
                }
                Software software = new Software();
                software.setName(row.getCell(1).getStringCellValue());


                SoftwareEntry entry = new SoftwareEntry();
                entry.setRegistrationNumber((int) row.getCell(0).getNumericCellValue());
                entry.setSoftwareName(row.getCell(1).getStringCellValue());
                entry.setAlternativeNames(row.getCell(2).getStringCellValue());
                // Продолжайте аналогично для остальных полей

                softwares.add(software);
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

        return null;
    }
}
class SoftwareEntry {
    // Регистрационный номер
    private int registrationNumber;
    // Наименование ПО
    private String softwareName;
    // Альтернативные названия ПО
    private String alternativeNames;
    // Класс ПО
    private String softwareClass;
    // Код продукции
    private String productCode;
    // Дата исключения
    private String exclusionDate;
    // Сайт с документацией по установке и эксплуатации ПО
    private String documentationWebsite;
    // Поддержка работы пользователей с ограничениями по зрению
    private String visualImpairmentSupport;
    // Поддержка работы пользователей с ограничениями по слуху
    private String hearingImpairmentSupport;
    // Соответствие ПП №325 от 23.03.2017
    private String complianceWithRegulation;
    // Программное обеспечение относится к сфере искусственного интеллекта
    private String aiSoftware;
    // Наименование (ФИО) правообладателя
    private String rightsOwnerFullName;
    // Сокращенное наименование (ФИО) правообладателя
    private String rightsOwnerShortName;
    // Статус правообладателя
    private String rightsOwnerStatus;
    // Идентификационный номер (ИНН)
    private String inn;
    // Основной государственный регистрационный номер (ОГРН)
    private String ogrn;
    // Сведения об основаниях возникновения у правообладателя исключительного права на ПО
    private String rightsOriginInfo;
    // Номер решения уполномоченного органа о включении сведений о ПО в реестр
    private String decisionNumber;
    // Дата решения уполномоченного органа о включении сведений о ПО в реестр
    private String decisionDate;
    // Ссылка на решения уполномоченного органа о включении сведений о ПО в реестр
    private String decisionLink;
    // Номер заявления о включении
    private String inclusionRequestNumber;
    // Дата регистрации заявления о включении
    private String inclusionRequestDate;
    // Номер гос. регистрации
    private String govRegistrationNumber;
    // Дата гос. регистрации
    private String govRegistrationDate;
    // Ссылка на стоимость
    private String priceLink;

    public SoftwareEntry(int registrationNumber, String softwareName, String alternativeNames, String softwareClass, String productCode, String exclusionDate, String documentationWebsite, String visualImpairmentSupport, String hearingImpairmentSupport, String complianceWithRegulation, String aiSoftware, String rightsOwnerFullName, String rightsOwnerShortName, String rightsOwnerStatus, String inn, String ogrn, String rightsOriginInfo, String decisionNumber, String decisionDate, String decisionLink, String inclusionRequestNumber, String inclusionRequestDate, String govRegistrationNumber, String govRegistrationDate, String priceLink) {
        this.registrationNumber = registrationNumber;
        this.softwareName = softwareName;
        this.alternativeNames = alternativeNames;
        this.softwareClass = softwareClass;
        this.productCode = productCode;
        this.exclusionDate = exclusionDate;
        this.documentationWebsite = documentationWebsite;
        this.visualImpairmentSupport = visualImpairmentSupport;
        this.hearingImpairmentSupport = hearingImpairmentSupport;
        this.complianceWithRegulation = complianceWithRegulation;
        this.aiSoftware = aiSoftware;
        this.rightsOwnerFullName = rightsOwnerFullName;
        this.rightsOwnerShortName = rightsOwnerShortName;
        this.rightsOwnerStatus = rightsOwnerStatus;
        this.inn = inn;
        this.ogrn = ogrn;
        this.rightsOriginInfo = rightsOriginInfo;
        this.decisionNumber = decisionNumber;
        this.decisionDate = decisionDate;
        this.decisionLink = decisionLink;
        this.inclusionRequestNumber = inclusionRequestNumber;
        this.inclusionRequestDate = inclusionRequestDate;
        this.govRegistrationNumber = govRegistrationNumber;
        this.govRegistrationDate = govRegistrationDate;
        this.priceLink = priceLink;
    }

    public SoftwareEntry() {
    }

    public int getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getAlternativeNames() {
        return alternativeNames;
    }

    public void setAlternativeNames(String alternativeNames) {
        this.alternativeNames = alternativeNames;
    }

    public String getSoftwareClass() {
        return softwareClass;
    }

    public void setSoftwareClass(String softwareClass) {
        this.softwareClass = softwareClass;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getExclusionDate() {
        return exclusionDate;
    }

    public void setExclusionDate(String exclusionDate) {
        this.exclusionDate = exclusionDate;
    }

    public String getDocumentationWebsite() {
        return documentationWebsite;
    }

    public void setDocumentationWebsite(String documentationWebsite) {
        this.documentationWebsite = documentationWebsite;
    }

    public String getVisualImpairmentSupport() {
        return visualImpairmentSupport;
    }

    public void setVisualImpairmentSupport(String visualImpairmentSupport) {
        this.visualImpairmentSupport = visualImpairmentSupport;
    }

    public String getHearingImpairmentSupport() {
        return hearingImpairmentSupport;
    }

    public void setHearingImpairmentSupport(String hearingImpairmentSupport) {
        this.hearingImpairmentSupport = hearingImpairmentSupport;
    }

    public String getComplianceWithRegulation() {
        return complianceWithRegulation;
    }

    public void setComplianceWithRegulation(String complianceWithRegulation) {
        this.complianceWithRegulation = complianceWithRegulation;
    }

    public String getAiSoftware() {
        return aiSoftware;
    }

    public void setAiSoftware(String aiSoftware) {
        this.aiSoftware = aiSoftware;
    }

    public String getRightsOwnerFullName() {
        return rightsOwnerFullName;
    }

    public void setRightsOwnerFullName(String rightsOwnerFullName) {
        this.rightsOwnerFullName = rightsOwnerFullName;
    }

    public String getRightsOwnerShortName() {
        return rightsOwnerShortName;
    }

    public void setRightsOwnerShortName(String rightsOwnerShortName) {
        this.rightsOwnerShortName = rightsOwnerShortName;
    }

    public String getRightsOwnerStatus() {
        return rightsOwnerStatus;
    }

    public void setRightsOwnerStatus(String rightsOwnerStatus) {
        this.rightsOwnerStatus = rightsOwnerStatus;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getRightsOriginInfo() {
        return rightsOriginInfo;
    }

    public void setRightsOriginInfo(String rightsOriginInfo) {
        this.rightsOriginInfo = rightsOriginInfo;
    }

    public String getDecisionNumber() {
        return decisionNumber;
    }

    public void setDecisionNumber(String decisionNumber) {
        this.decisionNumber = decisionNumber;
    }

    public String getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(String decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getDecisionLink() {
        return decisionLink;
    }

    public void setDecisionLink(String decisionLink) {
        this.decisionLink = decisionLink;
    }

    public String getInclusionRequestNumber() {
        return inclusionRequestNumber;
    }

    public void setInclusionRequestNumber(String inclusionRequestNumber) {
        this.inclusionRequestNumber = inclusionRequestNumber;
    }

    public String getInclusionRequestDate() {
        return inclusionRequestDate;
    }

    public void setInclusionRequestDate(String inclusionRequestDate) {
        this.inclusionRequestDate = inclusionRequestDate;
    }

    public String getGovRegistrationNumber() {
        return govRegistrationNumber;
    }

    public void setGovRegistrationNumber(String govRegistrationNumber) {
        this.govRegistrationNumber = govRegistrationNumber;
    }

    public String getGovRegistrationDate() {
        return govRegistrationDate;
    }

    public void setGovRegistrationDate(String govRegistrationDate) {
        this.govRegistrationDate = govRegistrationDate;
    }

    public String getPriceLink() {
        return priceLink;
    }

    public void setPriceLink(String priceLink) {
        this.priceLink = priceLink;
    }
}
