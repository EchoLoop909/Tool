package com.example.demo.ToolXml;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

@Slf4j
@SpringBootApplication
@EnableScheduling
@Configuration(proxyBeanMethods = false) // Tắt CGLIB proxy cho configuration class
public class ToolXmlApplication {

    private static final String BASE_DIR = "ToolXml";

    public static void main(String[] args) {
        SpringApplication.run(ToolXmlApplication.class, args);
    }

    @PostConstruct
    public void init() {
        Path basePath = Paths.get(System.getProperty("user.dir"), BASE_DIR);
        Path pendingDir = basePath.resolve("pending");
        Path successDir = basePath.resolve("success");
        Path failedDir = basePath.resolve("error");
        Path logDir = basePath.resolve("log");

        try {
            Files.createDirectories(pendingDir);
            Files.createDirectories(successDir);
            Files.createDirectories(failedDir);
            Files.createDirectories(logDir);
            log.info("Đã tạo thư mục pending, success, error, logs tại: {}", basePath.toAbsolutePath());
        } catch (Exception e) {
            log.error("Không thể tạo các thư mục làm việc", e);
        }
    }

    @Bean
    public String startupMessage() {
        log.info("Tool đã khởi động và sẵn sàng làm việc.");
        return "Started";
    }

    @Slf4j
    @RestController
    @RequestMapping("/api/hadoop")
    public static class FakeHadoopApiController {

        @RequestMapping("/upload")
        public ResponseEntity<?> uploadFile(@RequestBody String xmlContent) {
            log.info("📥 Nhận file XML:\n{}", xmlContent);
            return ResponseEntity.ok("✅ Nhận thành công");
        }
    }

    @Slf4j
    @Service
    public static class FileProcessorService {
        private static final String API_URL = "http://localhost:9998/api/hadoop/upload";
        private final Path pendingDir;
        private final Path successDir;
        private final Path failedDir;

        private final RestTemplate restTemplate = new RestTemplate();

        public FileProcessorService() {
            Path basePath = Paths.get(System.getProperty("user.dir"), BASE_DIR);
            this.pendingDir = basePath.resolve("pending");
            this.successDir = basePath.resolve("success");
            this.failedDir = basePath.resolve("error");
        }

        @Scheduled(fixedRate = 300000)
        public void scanAndSendFiles() {
            try {
                log.info("Đang kiểm tra file trong thư mục pending...");
                log.info("Đường dẫn thư mục pending: {}", pendingDir.toAbsolutePath());

                File[] allFiles = pendingDir.toFile().listFiles();

                if (allFiles == null || allFiles.length == 0) {
                    log.info("Không có file nào trong thư mục pending.");
                    return;
                }

                for (File file : allFiles) {
                    if (!file.getName().toLowerCase().endsWith(".xml")) {
                        log.warn("File {} không phải định dạng XML. Di chuyển sang thư mục error.", file.getName());
                        Path errorPath = failedDir.resolve(file.getName());
                        Files.move(file.toPath(), errorPath, StandardCopyOption.REPLACE_EXISTING);
                        continue;
                    }

                    try {
                        log.info("Đang xử lý file XML: {}", file.getName());
                        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                        log.info("Gửi tới API Hadoop...");
                        restTemplate.postForEntity(API_URL, content, String.class);
                        Path successPath = successDir.resolve(file.getName());
                        Files.move(file.toPath(), successPath, StandardCopyOption.REPLACE_EXISTING);
                        log.info("Gửi file thành công: {}", file.getName());
                    } catch (Exception e) {
                        log.error("Lỗi khi xử lý file XML {}: {}", file.getName(), e.getMessage());
                        Path targetPath = failedDir.resolve(file.getName());
                        Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            } catch (Exception e) {
                log.error("Lỗi trong quá trình kiểm tra và gửi file: {}", e.getMessage());
            }
        }
    }
}
