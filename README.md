# Language Based Push Notification with FCM & Firestore
## Requirements
- A Firebase Project to use FCM & Firestore [Create a project on Firebase Console](https://console.firebase.google.com)
- Firebase Admin SDK Library For Java   [com.google.firebase:firebase-admin:(latest-version) ](https://mvnrepository.com/artifact/com.google.firebase/firebase-admin) or you can use Maven Artifact Search in IntelliJ IDEA [Generate Maven Dependency with Maven Artifact Search](https://www.jetbrains.com/help/idea/work-with-maven-dependencies.html#generate_maven_dependency)
- Firebase Admin SDK JSON file [How to Generate Firebase Admin SDK JSON file](https://firebase.google.com/docs/admin/setup?hl=en#initialize-sdk)
- (Optional, I used this for getting some JSON data from the Internet) JSON Libraries for parsing JSON data [JSON Libraries - Maven Repository ](https://mvnrepository.com/open-source/json-libraries)

I used this data representation on Firestore
```sh
users (collection)
    - random string (document)
        - lang (field)
        - device token id (field)
```
You must collect and store device token IDs  (I used Firestore for storing device IDs)

Retrieve FCM token of the device [Read here](https://firebase.google.com/docs/cloud-messaging/android/client?hl=en#retrieve-the-current-registration-token)
