package com.bolsadeideas.springboot.app;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import  org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
  // public void addResourceHandlers(ResourceHandlerRegistry registry) {
  //   WebMvcConfigurer.super.addResourceHandlers(registry);
  //   String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
    
  //   registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
  // }
}
