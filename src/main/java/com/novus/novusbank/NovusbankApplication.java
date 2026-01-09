package com.novus.novusbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.novus.novusbank.notification.services.NotificationService;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class NovusbankApplication {

	// private final NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(NovusbankApplication.class, args);
	}

	/*
	 * @Bean
	 * CommandLineRunner run() {
	 * return args -> {
	 * NotificationDTO notificationDTO = NotificationDTO.builder()
	 * .recipient("memarez@gmail.com")
	 * .subject("Hello from Novus Bank")
	 * .body("This is a test email sent from Novus Bank application.")
	 * .type(NotificationType.EMAIL)
	 * .build();
	 * // You can use the notificationService here if needed
	 * notificationService.sendEmail(notificationDTO, new User());
	 * };
	 * }
	 */
}
