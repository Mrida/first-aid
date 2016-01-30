package yam.root;

import android.app.Application;
import android.widget.Toast;

public class ApplicationContext extends Application {
    private static Application sApplication;

    public static void showShortMessage(String message) {
        Toast.makeText(sApplication.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
