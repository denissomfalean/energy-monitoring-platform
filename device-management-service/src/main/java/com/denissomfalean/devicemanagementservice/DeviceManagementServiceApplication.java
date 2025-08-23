package com.denissomfalean.devicemanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class DeviceManagementServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DeviceManagementServiceApplication.class, args);
  }
}
