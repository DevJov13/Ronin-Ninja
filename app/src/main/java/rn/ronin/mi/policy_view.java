package rn.ronin.mi;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.Manifest;

public class policy_view extends AppCompatActivity {

    private WebView webPolicy;
    private Button agreeButton;
    private Button disAgreeButton;

    private static final int PERMISSION_REQUEST_CODE = 83334;

    private SharedPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_view);


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        appPreferences = getSharedPreferences(GlobalConfig.appCode, MODE_PRIVATE);

        boolean hasAcceptedPolicy = appPreferences.getBoolean("doneUserConsent", false);

        if (hasAcceptedPolicy) {
            gameContent();
            return;
        }

        webPolicy = findViewById(R.id.policy_view);
        agreeButton = findViewById(R.id.button);
        disAgreeButton = findViewById(R.id.button2);

        webPolicy.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webPolicy.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webPolicy.loadUrl(GlobalConfig.policyURL);

        // Add onClick listeners to your buttons
        agreeButton.setOnClickListener(v -> handleAgree());
        disAgreeButton.setOnClickListener(v -> handleDisagree());
    }

    private void handleAgree() {
        sendDataDialog();
    }

    private void handleDisagree() {
        finishAffinity();
    }

    private void sendDataDialog() {
        AlertDialog.Builder userConsent = new AlertDialog.Builder(policy_view.this);

        userConsent.setTitle("User Data Consent");
        userConsent.setMessage("We may collect information based on your activities during the usage of the app to provide a better experience");

        userConsent.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handleUserConsent(true);

                dialogInterface.dismiss();
                gameContent();
            }
        });

        userConsent.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handleUserConsent(false);

                dialogInterface.dismiss();
            }
        });

        AlertDialog userConsentDialog = userConsent.create();
        userConsentDialog.show();
    }

    private void handleUserConsent(boolean consent) {
        GlobalConfig.permitSendData = consent;
        appPreferences.edit().putBoolean("permitSendData", consent).apply();
        appPreferences.edit().putBoolean("doneUserConsent", true).apply();

        if (consent) {
            // Call permission data
            if (GlobalConfig.permitSendData && !checkPermissions()) {
                requestPermission();
            }
        }
        gameContent();
    }

    private boolean checkPermissions() {
        // Check location permission
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        return locationPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // You can request location permission here
        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                appPreferences.edit().putBoolean("grantLocation", true).apply();
            } else {
                appPreferences.edit().putBoolean("grantLocation", false).apply();
            }
        }
    }

    private void gameContent() {
        Intent webapp = new Intent(this, splash_screen.class);
        webapp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(webapp);
        finish();
    }
}
