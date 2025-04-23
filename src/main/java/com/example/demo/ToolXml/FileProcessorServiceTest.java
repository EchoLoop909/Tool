package com.example.demo.ToolXml;//package bkav.com.springboot.ToolXml;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.*;
//import java.nio.file.attribute.BasicFileAttributes;
//
//@Service
//public class FileProcessorServiceTest {
//
//    // Thư mục gốc chứa các thư mục con
//    private final Path baseDir = Paths.get(System.getProperty("user.home"), "src", "main", "java", "bkav.com.springboot", "ToolXml");
//
//    // Các thư mục con
//    private final Path pendingDir = baseDir.resolve("pending");
//    private final Path successDir = baseDir.resolve("success");
//    private final Path failedDir = baseDir.resolve("error");
//
//    // RestTemplate để gửi yêu cầu HTTP
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    // URL của API (có thể thay đổi sau này)
//    private final String API_URL = "http://localhost:9998/api/hadoop/upload";
//
//    @PostConstruct
//    public void init() throws Exception {
//        // Đảm bảo các thư mục con đã tồn tại, nếu chưa thì tạo mới
//        Files.createDirectories(pendingDir);
//        Files.createDirectories(successDir);
//        Files.createDirectories(failedDir);
//
//        // Bắt đầu theo dõi thư mục pending trong một luồng riêng
//        new Thread(this::watchDirectory).start();
//    }
//
//    // Theo dõi thư mục pending để xem có file mới được thêm vào
//    private void watchDirectory() {
//        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
//
//            // Đăng ký sự kiện theo dõi khi có file mới được tạo trong thư mục pending
//            pendingDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
//            System.out.println("Bắt đầu theo dõi thư mục pending");
//
//            // Vòng lặp vô tận để xử lý các sự kiện
//            while (true) {
//                WatchKey key = watchService.take(); // Chờ sự kiện
//
//                // Lặp qua các sự kiện được phát hiện
//                for (WatchEvent<?> event : key.pollEvents()) {
//                    Path fileName = (Path) event.context(); // Lấy tên file
//                    Path filePath = pendingDir.resolve(fileName); // Tạo đường dẫn tuyệt đối cho file
//
//                    // Kiểm tra xem file có đuôi .xml hay không
//                    if (fileName.toString().endsWith(".xml")) {
//                        System.out.println("Có file mới được thêm vào: " + filePath);
//                        processFile(filePath); // Gọi phương thức xử lý file
//                    }
//                }
//
//                // Reset key để tiếp tục theo dõi
//                key.reset();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Lỗi trong quá trình theo dõi thư mục: " + e.getMessage());
//        }
//    }
//
//    // Xử lý file khi có file mới được thêm vào thư mục pending
//    private void processFile(Path filePath) {
//        try {
//            // Đọc nội dung file thành chuỗi
//            String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
//            System.out.println("Đang xử lý file: " + filePath.getFileName());
//
//            // Gửi nội dung file tới API
//            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, content, String.class);
//
//            // Nếu gửi thành công, di chuyển file vào thư mục success
//            if (response.getStatusCode().is2xxSuccessful()) {
//                System.out.println("Chuyển file thành công: " + filePath.getFileName());
//                Files.move(filePath, successDir.resolve(filePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
//            } else {
//                // Nếu có lỗi, di chuyển file vào thư mục error
//                throw new RuntimeException("API trả về lỗi: " + response.getStatusCode());
//            }
//        } catch (Exception e) {
//            // Xử lý lỗi khi gửi file -> chuyển file vào thư mục error
//            System.err.println("❌ Gửi thất bại file: " + filePath.getFileName() + " - " + e.getMessage());
//            try {
//                Files.move(filePath, failedDir.resolve(filePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
//        }
//    }
//}
