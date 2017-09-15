package com.soosy.lagosdevandela;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by hubuk on 15/09/2017 AD.
 */

public class infoDev extends AppCompatActivity {
    FloatingActionButton btn;
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.info_developer);
            Intent intent = getIntent();
            final String devName = intent.getStringExtra("dev");
            TextView texttView = (TextView)findViewById(R.id.devName);
            texttView.setText(devName);
            ImageView imageView = (ImageView)(findViewById(R.id.dImage));
            String dImage = intent.getStringExtra("avatar");
            final String urlDev = intent.getStringExtra("html_url");
            TextView txtview = (TextView)findViewById(R.id.urlDev);
            txtview.setText(urlDev);
            Picasso.with(getApplicationContext()).load(dImage).into(imageView);

            btn = (FloatingActionButton) findViewById(R.id.fab);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Check this awesome developer " + "@"+devName +" "+ urlDev);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });
        }

    }

