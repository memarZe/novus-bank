package com.novus.novusbank.notification.services;

import com.novus.novusbank.auth_users.entity.User;
import com.novus.novusbank.notification.dto.NotificationDTO;

public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO, User user);

}
