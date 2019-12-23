package fr.axelallain.borrowings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BorrowingsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BorrowingsApplication.class, args);
    }

}
