package com.nikki.jwt.data_loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(value = "dev")
public class DataLoader implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello from DataLoader running in Dev Profile! 🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐");
    }
}
