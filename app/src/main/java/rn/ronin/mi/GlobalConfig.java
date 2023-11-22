package rn.ronin.mi;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.LogLevel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class GlobalConfig extends Application {
    public static boolean permitSendData;
    private static GlobalConfig aroundCFG;
    public static String gameURL = "";
    public static String policyURL = "https://win11apps.site/policy";
    public static String urlAPI = "";
    public static String appCode = "WB998539";
    public static Boolean navStatus = true;
    public static String success = "";

    @Override
    public void onCreate() {
        super.onCreate();
        aroundCFG = this;

        String appToken = "{YourAppToken}";
        String environment = AdjustConfig.ENVIRONMENT_SANDBOX;
        AdjustConfig config = new AdjustConfig(this, appToken, environment);
        config.setLogLevel(LogLevel.WARN);
        Adjust.onCreate(config);

        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());

        // Initialize FirebaseApp only once in your Application class
        FirebaseApp.initializeApp(aroundCFG);

        initConfig();
    }

    public static synchronized GlobalConfig getInstance() {
        return aroundCFG;
    }

    private void initConfig() {
        FirebaseRemoteConfig remoteCFG = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings settingsCFG = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();

        remoteCFG.setConfigSettingsAsync(settingsCFG);

    }

    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }


        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }
    }
}