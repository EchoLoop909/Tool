module com.example.tool {
    requires spring.boot.starter.web;
    exports com.example.demo.ToolXml;
    requires java.base;
    requires java.logging;
    requires java.xml;
    requires spring.boot;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.aop;
    requires spring.boot.autoconfigure;
    requires org.slf4j; // 💥 Bổ sung dòng này để sử dụng được SLF4J

    requires static lombok;
    requires jakarta.annotation;
    requires spring.web;  // Nếu bạn đang sử dụng AOP trong Spring
    exports com.example.demo to spring.beans;  // Mở quyền cho spring.beans truy cập vào com.example.demo
    opens com.example.demo.ToolXml to spring.core;

}
