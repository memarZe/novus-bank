package com.novus.novusbank.notification.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novus.novusbank.notification.entity.Notification;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

}
