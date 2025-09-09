package lk.chamasha.lost.and.found.service;

import lk.chamasha.lost.and.found.controller.response.NotificationResponse;
import lk.chamasha.lost.and.found.exception.NotificationNotFoundException;

import java.util.List;

public interface NotificationService {
    public List<NotificationResponse> getUserNotifications(Long userId);
    public void markAsRead(Long id)throws NotificationNotFoundException;

}
