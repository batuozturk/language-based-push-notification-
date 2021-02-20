import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.List;

public class LanguageBasedFCMWithFirestore {

    public static void main(String[] args) throws Exception{

        List<String> listtr; // A list for users whose system language is turkish
        List<String> listen; // A list for users whose system language is english
        FileInputStream firebaseJSONFile = new FileInputStream("YOUR_FIREBASE_PROJECT_ADMIN_SDK.json"); // getting Firebase Admin SDK json file

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseJSONFile))
                .build(); // creating firebase options object for initializing the app which is connected to firebase

        FirebaseApp.initializeApp(options); // inititalize firebase app
        Firestore firestore = FirestoreClient.getFirestore(); // getting firestore object

        ApiFuture<QuerySnapshot> fut = firestore.collection("users").get(); // getting querysnapshot object which is in the collection named users. By the way you can customize your firestore whatever you want, you can change key names etc.
        listtr = new ArrayList<>();
        listen = new ArrayList<>();
        for(DocumentSnapshot d:fut.get()){

            if(d.get("lang") !=null){ // if lang field is not null, then add fcm token of the device to the list based on the language
            if(d.get("lang").equals("tr")){
            listtr.add(d.get("id").toString());}
            else if(d.get("lang").equals("en")){
                listen.add(d.get("id").toString());
            }}

        }

        // you can customize your notification here, below I sent a notification with based on json-related data from the internet
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = jsonParser.getJSONFromUrl("https://api.coincap.io/v2/assets"); // create JSONObject with the data from the internet
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        int i = (int)(Math.random()*20); // I randomly selected which cryptocurrency will be selected for notification body and title
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
        String title = jsonObject1.getString("name") +" price"; // notification title (english)
        String body;
        // notification body (english)
        if(jsonObject1.getString("changePercent24Hr").charAt(0) == '-'){ // if change percent is below zero
        body = jsonObject1.getString("name") + " is $" + jsonObject1.getString("priceUsd").substring(0,7) +" now, down %" + jsonObject1.getString("changePercent24Hr").substring(1,5);}
        else if(jsonObject1.getString("changePercent24Hr").substring(0,4).equals("0.0000")){ // if change percent is zero
            body = jsonObject1.getString("name") + " is $" + jsonObject1.getString("priceUsd").substring(0,7) +" now, not changed"; // I take a substring of price , because it's almost the same value
        }
        else{ // if change percent is above zero
             body = jsonObject1.getString("name") + " is $" + jsonObject1.getString("priceUsd").substring(0,7) +" now, up %" + jsonObject1.getString("changePercent24Hr").substring(0,4); // I take substrings of price and percent, because it's almost the same value
        }

        String title2 = jsonObject1.getString("name") +" fiyatı"; // notification title (turkish)
        String body2;
        String price = jsonObject1.getString("priceUsd").substring(0,7);
        String updatedPrice = String.valueOf(Float.parseFloat(price)*6.98); // price is converted USD to TRY

        if(jsonObject1.getString("changePercent24Hr").charAt(0) == '-'){
            body2 = jsonObject1.getString("name") + " " + updatedPrice +"₺den işlem görüyor, son 24 saatte %" + jsonObject1.getString("changePercent24Hr").substring(1,5) +" değer kaybetti";}
        else if(jsonObject1.getString("changePercent24Hr").substring(0,4).equals("0.0000")){
            body2 = jsonObject1.getString("name") + " " + updatedPrice +"₺den işlem görüyor , son 24 saatte değişmedi";
        }
        else{
            body2 = jsonObject1.getString("name") + " is " + updatedPrice +"₺den işlem görüyor, son 24 saatte %" + jsonObject1.getString("changePercent24Hr").substring(0,4) +" değer kazandı";
        }

        // sending notification to english users , you can set image also
        Notification androidNotification = Notification.builder().setTitle(title).setBody(body).setImage("https://lh3.googleusercontent.com/EL9jrzVap6bE87y5lye_52cWheuJUyMYFl822ySnvRki-1yphhL2nqr7jiqFoHZX3Wo=s180-rw").build();
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(androidNotification).addAllTokens(listen)
                .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);

        System.out.println("Successfully sent message: " + response.getSuccessCount() +" " +response.getFailureCount()); // you can see the statistics of sent notification, how many did users receive the notification etc.

        // sending notification to turkish users
        MulticastMessage message2 = MulticastMessage.builder()
                .setNotification(new Notification(
                        title2,
                        body2,"https://lh3.googleusercontent.com/EL9jrzVap6bE87y5lye_52cWheuJUyMYFl822ySnvRki-1yphhL2nqr7jiqFoHZX3Wo=s180-rw")).addAllTokens(listtr)
                .build();

        BatchResponse response2 = FirebaseMessaging.getInstance().sendMulticast(message2);
        
        System.out.println("Bildirim gönderildi: " + response2.getSuccessCount() +" " +response2.getFailureCount());



}

}
