package ru.pin120.via.SoftwareCatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Services.ParsingService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ScheduledTasks {

    @Autowired
    private ParsingService parsingService;

    @Scheduled(cron = "0 0 0 * * SUN")
    public void scheduleTaskWithCronExpression() {
        String exportPath = FileUtil.downloadFile();
        if (exportPath != null){
            List<Software> newSoftwares = SoftwareParser.readExcelFile(exportPath);
            parsingService.parseAndUpdate(newSoftwares);
        }
    }
}

