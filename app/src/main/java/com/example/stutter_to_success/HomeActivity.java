package com.example.stutter_to_success;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    TextView t1;
    TextView t2;
    TextView t3;
    Button render;

    void blog(TextView textView, String description, String url, int img){
        SpannableString spannableString = new SpannableString(description);

        // Load your thumbnail drawable
        Drawable thumbnail = ContextCompat.getDrawable(this, img);
        if (thumbnail != null) {
            int parentWidth = getResources().getDisplayMetrics().widthPixels;
            int parentHeight = getResources().getDisplayMetrics().heightPixels;
            thumbnail.setBounds(0, 0, 1050, 550);

            // Create an ImageSpan to display the thumbnail
            ImageSpan imageSpan = new ImageSpan(thumbnail);

            // Set the ImageSpan to the appropriate position in the SpannableString
            spannableString.setSpan(imageSpan, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        // Create a ClickableSpan to handle the click on the hyperlink
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Handle the click action (open a URL, start an activity, etc.)
                openUrl(url);
                //openUrl("https://www.businessideashindi.com/government-subsidy-small-business-india-hindi-%E0%A4%B8%E0%A4%B0%E0%A4%95%E0%A4%BE%E0%A4%B0-%E0%A4%B8%E0%A4%AC%E0%A5%8D%E0%A4%B8%E0%A4%BF%E0%A4%A1%E0%A5%80/");
            }
        };

        // Set the ClickableSpan to the hyperlink text in the SpannableString
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        // Set the SpannableString to the TextView
        textView.setText(spannableString);

        // Make the TextView clickable and handle link clicks
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void openUrl(String url) {
        // Open the URL using an Intent
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);
        render = findViewById(R.id.render);

        render.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });




        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splash_anim);
        t1.setVisibility(View.VISIBLE);
        t1.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                t2.setVisibility(View.VISIBLE);
                t2.setAnimation(animation);
            }
        },3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                t3.setVisibility(View.VISIBLE);
                t3.setAnimation(animation);
            }
        },4000);

        TextView textView = findViewById(R.id.blog1);
        blog(textView,"Ed Sheeran Story of stuttering.","https://www.stutteringhelp.org/content/ed_sheeran",R.drawable.blog1);
        TextView textView2 = findViewById(R.id.blog2);
        blog(textView2,"How my stammer became my success !","https://isad.live/isad-2017/papers-presented-by/stories-and-experiences-with-stuttering-by-pws/how-my-stammer-became-my-success/",R.drawable.blog3);
        TextView textView3 = findViewById(R.id.blog3);
        blog(textView3,"Emily Blunt stuttering story","https://www.stutteringhelp.org/famous-people/emily-blunt",R.drawable.blog2);

        }



    }