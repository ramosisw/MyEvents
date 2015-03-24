package mx.itson.myevents.views;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import mx.itson.myevents.R;


public class Splash extends ActionBarActivity {
    private static final int SPLASH_DURATION = 3000; // 2 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();

        final ImageView imgIcono = (ImageView)findViewById(R.id.img_icono);
        final ImageView imgTicketLeft = (ImageView)findViewById(R.id.img_ticket_left);
        final ImageView imgTicketRight = (ImageView)findViewById(R.id.img_ticket_right);
        final TextView tvInsoft = (TextView)findViewById(R.id.lbl_insoft);
        final TextView tvITSON = (TextView)findViewById(R.id.lbl_itson);

        Animation animIcono = AnimationUtils.loadAnimation(this, R.anim.splash_icono);
        Animation animTicketLeft = AnimationUtils.loadAnimation(this, R.anim.splash_ticket_left);
        Animation animTicketRight = AnimationUtils.loadAnimation(this, R.anim.splash_ticket_right);
        Animation animTV = AnimationUtils.loadAnimation(this, R.anim.splash_tv);

        imgIcono.setAnimation(animIcono);
        tvInsoft.setAnimation(animTV);
        tvITSON.setAnimation(animTV);
        imgTicketLeft.setAnimation(animTicketLeft);
        imgTicketRight.setAnimation(animTicketRight);
        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // make sure we close the splash screen so the user won't come back when it presses back key
                finish();
                // start the home screen if the back button wasn't pressed already
                Intent intent = new Intent(Splash.this, LoginActivity.class);
                Splash.this.startActivity(intent);

            }
        }, SPLASH_DURATION); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
    }



}

