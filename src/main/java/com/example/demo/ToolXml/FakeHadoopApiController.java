package com.example.demo.ToolXml;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hadoop")
public class FakeHadoopApiController {

    @RequestMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestBody String xmlContent){
        System.out.println("📥 Nhận file XML:\n" + xmlContent);
        return ResponseEntity.ok("✅ Nhận thành công");
    }
}
