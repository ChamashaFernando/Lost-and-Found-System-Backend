package lk.chamasha.lost.and.found.config;//package lk.chamasha.lost.and.found.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import jakarta.annotation.PostConstruct;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Configuration
//public class FirebaseConfig {
//
//    @PostConstruct
//    public void init() {
//        try {
//            if (FirebaseApp.getApps().isEmpty()) {
//                FileInputStream serviceAccount =
//                        new FileInputStream("src/main/resources/firebase/google-services.json");
//
//                FirebaseOptions options = FirebaseOptions.builder()
//                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                        .build();
//
//                FirebaseApp.initializeApp(options);
//                System.out.println("✅ Firebase initialized successfully");
//            }
//        } catch (IOException e) {
//            // 🔥 Ignore error in test environment
//            System.out.println("⚠️ Firebase not initialized (no config file found)");
//        }
//    }
//}






import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount = new FileInputStream(
                    "src/main/resources/firebase/firebase-service-account.json"
            );

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("✅ Firebase Initialized Successfully");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
