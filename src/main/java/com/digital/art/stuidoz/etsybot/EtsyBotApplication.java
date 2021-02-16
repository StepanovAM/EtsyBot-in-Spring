package com.digital.art.stuidoz.etsybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootApplication
@EnableAsync
public class EtsyBotApplication {

	@Bean
	public ExecutorService threadPool(){
		var boundedQueue = new ArrayBlockingQueue<Runnable>(1000);
		return new ThreadPoolExecutor(4, 4, 60, SECONDS, boundedQueue, new ThreadPoolExecutor.AbortPolicy());
	}

	public static void main(String[] args) {
		SpringApplication.run(EtsyBotApplication.class, args);
	}

}
