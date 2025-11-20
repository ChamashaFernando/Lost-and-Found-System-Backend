package lk.chamasha.lost.and.found.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class FcmService {

    public void sendNotification(String fcmToken, String title, String body) throws Exception {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream("/home/nethmi/Downloads/lostandfoundapp-e5cdf-firebase-adminsdk-fbsvc-ca9c918438.json"))
                .createScoped("https://www.googleapis.com/auth/firebase.messaging");

        googleCredentials.refreshIfExpired();
        String accessToken = googleCredentials.getAccessToken().getTokenValue();

        Map<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);

        Map<String, Object> message = Map.of(
                "message", Map.of(
                        "token", fcmToken,
                        "notification", notification
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(message, headers);

        RestTemplate restTemplate = new RestTemplate();
        String fcmUrl = "https://fcm.googleapis.com/v1/projects/lostandfoundapp-e5cdf/messages:send";

        restTemplate.postForObject(fcmUrl, request, String.class);
    }
}
