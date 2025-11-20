//package lk.chamasha.lost.and.found.service.impl;
//
//import jakarta.transaction.Transactional;
//import lk.chamasha.lost.and.found.model.Item;
//import lk.chamasha.lost.and.found.model.Notification;
//import lk.chamasha.lost.and.found.model.User;
//import lk.chamasha.lost.and.found.model.ItemStatus;
//import lk.chamasha.lost.and.found.repository.NotificationRepository;
//import lk.chamasha.lost.and.found.repository.UserRepository;
//import lk.chamasha.lost.and.found.service.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationServiceImpl implements NotificationService {
//
//    private final NotificationRepository notificationRepository;
//    private final UserRepository userRepository;
//    private final FcmService fcmService;
//
//    @Override
//    public void createNearbyAlerts(Item lostItem) {
//        List<User> allUsers = userRepository.findAll();
//
//        for (User user : allUsers) {
//            // දැන් if check නැතුව සියලුම usersට notification යවන්නෙමු
//            Notification notification = Notification.builder()
//                    .user(user)
//                    .item(lostItem)
//                    .title("Lost Item Alert")
//                    .message("A " + lostItem.getCategory() + " titled '" + lostItem.getTitle() +
//                            "' was reported lost near " + lostItem.getLocation())
//                    .seen(false)
//                    .createdAt(LocalDateTime.now())
//                    .build();
//            notificationRepository.save(notification);
//
//            fcmService.sendNotification(user.getFcmToken(), notification.getTitle(), notification.getMessage());
//        }
//    }
//
//
//    @Transactional
//    @Override
//    public List<Notification> getUserNotifications(Long userId) {
//        return userRepository.findById(userId)
//                .map(notificationRepository::findByUserOrderByCreatedAtDesc)
//                .orElse(List.of());
//    }
//
//    @Override
//    public void markNotificationAsSeen(Long notificationId) {
//        notificationRepository.findById(notificationId).ifPresent(notification -> {
//            notification.setSeen(true);
//            notificationRepository.save(notification);
//        });
//    }
//
//}



package lk.chamasha.lost.and.found.service.impl;

import jakarta.transaction.Transactional;
import lk.chamasha.lost.and.found.model.Item;
import lk.chamasha.lost.and.found.model.Notification;
import lk.chamasha.lost.and.found.model.User;
import lk.chamasha.lost.and.found.repository.NotificationRepository;
import lk.chamasha.lost.and.found.repository.UserRepository;
import lk.chamasha.lost.and.found.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;

    /**
     * Create notifications for all users when a lost item is reported
     */
    @Transactional
    @Override
    public void createNearbyAlerts(Item lostItem) {
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            Notification notification = Notification.builder()
                    .user(user)
                    .item(lostItem)
                    .title("Lost Item Alert")
                    .message("A " + lostItem.getCategory() + " titled '" + lostItem.getTitle() +
                            "' was reported lost near " + lostItem.getLocation())
                    .seen(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            // ✅ Only send FCM if token is available
            if (user.getFcmToken() != null && !user.getFcmToken().isBlank()) {
                try {
                    fcmService.sendNotification(
                            user.getFcmToken(),
                            notification.getTitle(),
                            notification.getMessage()
                    );
                } catch (Exception e) {
                    System.err.println("❌ Failed to send FCM notification to " + user.getEmail());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Retrieve user notifications
     */
    @Transactional
    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return userRepository.findById(userId)
                .map(notificationRepository::findByUserOrderByCreatedAtDesc)
                .orElse(List.of());
    }

    /**
     * Mark notification as seen
     */
    @Override
    public void markNotificationAsSeen(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setSeen(true);
            notificationRepository.save(notification);
        });
    }


}
