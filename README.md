# Manchester United App

Capstone project for the Udacity Android Developer Nanodegree.

The application uses data from the official website of Manchester United (http://www.manutd.com/).

The application is NOT intended for commercial use.

# Instructions

1. Copy your serviceAccountKey.json to backend/src/main/resources
2. Start Google App Engine Server. IMPORTANT: Due to Google App Engine restrictions (no threads, no writing to file system), the Push Notification tokens are stored in memory (which means if you restart the App Engine, your tokens will be lost and the push notifications will not work until you reinstall the Android app to trigger the token refresh)
3. Start the Android application
4. You can trigger a push notification by visiting localhost:8080: fill in the form and click send.