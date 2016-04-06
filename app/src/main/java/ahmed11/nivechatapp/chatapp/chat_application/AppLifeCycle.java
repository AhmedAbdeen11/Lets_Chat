package ahmed11.nivechatapp.chatapp.chat_application;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by root on 2/20/16.
 */
public class AppLifeCycle extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

    }
}
