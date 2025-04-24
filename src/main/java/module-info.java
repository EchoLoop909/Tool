module com.example.demo {
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires spring.context;
    requires jakarta.annotation;
    requires static lombok;
    requires org.slf4j;

    exports com.example.demo.ToolXml;
    opens com.example.demo.ToolXml; // Mở package cho tất cả module, bao gồm unnamed module
}