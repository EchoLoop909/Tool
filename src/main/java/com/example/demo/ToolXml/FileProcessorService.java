//package com.example.demo.ToolXml;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.File;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.*;
//
//@Slf4j
//@Service
//public class FileProcessorService {
//    private static final String API_URL = "http://localhost:9998/api/hadoop/upload";
//
////    // Thư mục gốc chứa các thư mục con
////    private final Path baseDir = Paths.get(System.getProperty("user.home"), "src", "main", "java", "bkav.com.springboot", "ToolXml");
////
////    // Các thư mục con
////    private final Path pendingDir = baseDir.resolve("pending");
////    private final Path successDir = baseDir.resolve("success");
////    private final Path failedDir = baseDir.resolve("error");
//
////     Lấy thư mục gốc của ứng dụng (chứa package ToolXml)
//    String baseDir = System.getProperty("user.dir") + "/src/main/java/bkav/com/springboot/ToolXml";
//
//    // Đường dẫn đến các thư mục cần tạo trong thư mục ToolXml
//    Path pendingDir = Paths.get(baseDir, "pending");
//    Path successDir = Paths.get(baseDir, "success");
//    Path failedDir = Paths.get(baseDir, "error");
//    Path logDir = Paths.get(baseDir, "log");
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    // Tạo các thư mục nếu chưa tồn tại
//    public FileProcessorService() {
//        try {
//            if (!pendingDir.toFile().exists()) {
//                pendingDir.toFile().mkdirs();
//                log.info("Đã tạo thư mục pending.");
//            }
//            if (!successDir.toFile().exists()) {
//                successDir.toFile().mkdirs();
//                log.info("Đã tạo thư mục success.");
//            }
//            if (!failedDir.toFile().exists()) {
//                failedDir.toFile().mkdirs();
//                log.info("Đã tạo thư mục error.");
//            }
//        } catch (Exception e) {
//            log.error("Lỗi khi tạo thư mục: {}", e.getMessage());
//        }
//    }
//
//    // Tự động chạy mỗi 5 phút
//    @Scheduled(fixedRate = 5 * 60 * 1000)
//    public void scanAndSendFiles() {
//        try {
//            log.info("Đang kiểm tra file trong thư mục pending...");
//            log.info("Đường dẫn thư mục pending: {}", pendingDir.toAbsolutePath());
//
//            // Lấy toàn bộ file trong thư mục pending
//            File[] allFiles = pendingDir.toFile().listFiles();
//
//            if (allFiles == null || allFiles.length == 0) {
//                log.info("Không có file nào trong thư mục pending.");
//                return;
//            }
//
//            // Duyệt từng file
//            for (File file : allFiles) {
//
//                // Nếu file không phải .xml thì chuyển sang thư mục error
//                if (!file.getName().toLowerCase().endsWith(".xml")) {
//                    log.warn("File {} không phải định dạng XML. Di chuyển sang thư mục error.", file.getName());
//                    Path errorPath = failedDir.resolve(file.getName());
//                    Files.move(file.toPath(), errorPath, StandardCopyOption.REPLACE_EXISTING);
//                    continue;
//                }
//
//                try {
//                    log.info("Đang xử lý file XML: {}", file.getName());
//
//                    // Đọc nội dung file XML
//                    String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
//
//                    // Gửi nội dung file đến API Hadoop
//                    log.info("Gửi tới API Hadoop...");
//                    restTemplate.postForEntity(API_URL, content, String.class);
//
//                    // Nếu gửi thành công, chuyển file sang thư mục success
//                    Path successPath = successDir.resolve(file.getName());
//                    Files.move(file.toPath(), successPath, StandardCopyOption.REPLACE_EXISTING);
//                    log.info("Gửi file thành công: {}", file.getName());
//
//                } catch (Exception e) {
//                    // Nếu có lỗi trong quá trình gửi API, log lỗi và chuyển file sang thư mục error
//                    log.error("Lỗi khi xử lý file XML {}: {}", file.getName(), e.getMessage());
//                    Path targetPath = failedDir.resolve(file.getName());
//                    Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//                }
//            }
//
//        } catch (Exception e) {
//            // Log lỗi chung nếu có sự cố trong toàn bộ quá trình
//            log.error("Lỗi trong quá trình kiểm tra và gửi file: {}", e.getMessage());
//        }
//    }
//}
