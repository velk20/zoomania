package com.zoomania.zoomania.config;

import com.zoomania.zoomania.service.OfferService;
import com.zoomania.zoomania.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableScheduling
public class LogConfig {
    private final UserService userService;
    private final OfferService offerService;

    public LogConfig(UserService userService, OfferService offerService) {
        this.userService = userService;
        this.offerService = offerService;
    }

//    @Scheduled(cron="0 0/30 * * * ?") // 30 minutes
    @Scheduled(cron="0 0/15 * * * ?") // 15 minutes
//    @Scheduled(cron = "0 * * * * *") // every minutes
    public void LogCurrentApplicationStatus() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("log.txt"));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append(System.lineSeparator());
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"));
        writer.write(stringBuilder.toString());

        String format = String.format("{%s} - It has total USERS[%d] and total OFFERS[%d]",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        this.userService.getAllUsersCount(),
                        this.offerService.getAllOffersCount()
                );

        writer.write(format);

        writer.close();
    }
}
