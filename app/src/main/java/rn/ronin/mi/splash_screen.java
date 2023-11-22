package rn.ronin.mi;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/** @noinspection ALL*/
public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(1024,1024);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();


        VideoView splash=findViewById(R.id.videoView);
        Uri splashFile =Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.nin);
        splash.setVideoURI(splashFile);


        splash.start();
        splash.setOnCompletionListener(mediaPlayer -> {
            splash.stopPlayback();
            splash.setVisibility(View.GONE);

            Intent intent = new Intent(splash_screen.this, MainActivity.class);
            startActivity(intent); // Use 'intent' instead of 'Intent'
            finish();
        });

    }



}