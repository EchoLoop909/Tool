//package com.example.demo.ToolXml;
//
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@SpringBootApplication
//@Slf4j
//@EnableScheduling
//public class FileToolApplication {
//    public static void main(String[] args){
//        SpringApplication.run(FileToolApplication.class, args);
//    }
//
//    @PostConstruct
//    public void init(){
//        // Lấy thư mục gốc của ứng dụng (chứa package ToolXml)
//        String baseDir = System.getProperty("user.dir") + "/src/main/java/bkav/com/springboot/ToolXml";
//
//        // Đường dẫn đến các thư mục cần tạo trong thư mục ToolXml
//        Path pendingDir = Paths.get(baseDir, "pending");
//        Path successDir = Paths.get(baseDir, "success");
//        Path failedDir = Paths.get(baseDir, "error");
//        Path logDir = Paths.get(baseDir, "log");
//
//        try{
//            //tao cac thu muc de xu ly
//            Files.createDirectories(pendingDir);
//            Files.createDirectories(successDir);
//            Files.createDirectories(failedDir);
//            Files.createDirectories(logDir);
//            log.info("Đã tạo thư mục pending, success, error, logs tại: {}", baseDir);
//        }catch (Exception e){
//            log.error("Không thể tạo các thư mục làm việc", e);
//        }
//    }
//
//    @Bean
//    public String startupMessage() {
//        log.info("Tool đã khởi động và sẵn sàng làm việc.");
//        return "Started";
//    }
//}
