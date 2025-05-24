package nldoko.game.java;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v7.appcompat.BuildConfig;
import nldoko.game.R;

import nldoko.game.java.game.NewGameActivity;
import nldoko.game.java.game.SavedGameListActivity;


public class SplashscreenActivity extends Activity {

    private static final int SPLASH_TIME = 1000; // ms
    private static final int SPLASH_TIME_DEBUG = 100; // ms

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.splashscreeen_layout);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        int time = SPLASH_TIME;
        if (isDebug()) {
            time = SPLASH_TIME_DEBUG;
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashscreenActivity.this,NewGameActivity.class);
                SplashscreenActivity.this.startActivity(mainIntent);
                SplashscreenActivity.this.finish();
            }
       }, time);

    }

    private boolean isDebug() {
        return ( 0 != ( getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
    }


}
