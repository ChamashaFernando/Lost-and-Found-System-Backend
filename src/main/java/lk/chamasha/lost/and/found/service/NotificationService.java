package lk.chamasha.lost.and.found.service;

import lk.chamasha.lost.and.found.controller.response.NotificationResponse;
import lk.chamasha.lost.and.found.exception.NotificationNotFoundException;
import lk.chamasha.lost.and.found.model.Item;
import lk.chamasha.lost.and.found.model.Notification;

import java.util.List;

public interface NotificationService {
//    public List<NotificationResponse> getUserNotifications(Long userId);
//    public void markAsRead(Long id)throws NotificationNotFoundException;

    void createNearbyAlerts(Item lostItem);
    List<Notification> getUserNotifications(Long userId);
    void markNotificationAsSeen(Long notificationId);

}
