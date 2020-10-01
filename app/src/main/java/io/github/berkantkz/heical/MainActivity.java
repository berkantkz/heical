package io.github.berkantkz.heical;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tinydb = new TinyDB(getApplicationContext());

        MobileAds.initialize(this, "ca-app-pub-2951689275458403~7278075869");

        final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2951689275458403/6678970750");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });

        AdView adView_top = findViewById(R.id.adView_top);
        adView_top.loadAd(new AdRequest.Builder().build());

        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        final EditText input = findViewById(R.id.input);
        findViewById(R.id.calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(input.getText().toString())) {
                    input.setError("Cannot be empty");
                    return;
                }
                dialog.setTitle("Result")
                        .setMessage("Your height is " + input.getText() + " cm!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!tinydb.getBoolean("share_dialog_never_show"))
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Share this app!")
                                        .setMessage("If you enjoyed this app, please help me to spread it and share with your friends!")
                                        .setPositiveButton("OKAY, I'M IN", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(Intent.createChooser(new Intent()
                                                        .setAction(Intent.ACTION_SEND)
                                                        .putExtra(Intent.EXTRA_TEXT, "Ever wanted to know your height? Check this app out! \n\nhttps://play.google.com/store/apps/details?id=io.github.berkantkz.heical")
                                                        .setType("text/plain"), null));
                                            }
                                        })
                                        .setNeutralButton("NEVER SHOW AGAIN", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                tinydb.putBoolean("share_dialog_never_show", true);
                                            }
                                        })
                                        .show();
                            }
                        })
                        .show();
            }
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TITLE, "Help spreading Height Calculator!")
                        .putExtra(Intent.EXTRA_TEXT, "Ever wanted to know your height? Check this app out! \n\nhttps://play.google.com/store/apps/details?id=io.github.berkantkz.heical")
                        .setType("text/plain"), null));
            }
        });

        findViewById(R.id.source).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/berkantkz/heical")));
            }
        });
    }
}