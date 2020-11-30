package com.files.api.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.servlet.multipart")
@Data
public class FileConstants {
    private String maxFileSize;
    private String maxRequestSize;
}
