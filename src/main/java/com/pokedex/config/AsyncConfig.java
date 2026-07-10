package com.pokedex.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/** Virtual Threads de Java 21 (Project Loom) para operaciones I/O-bound. */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor applicationTaskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
