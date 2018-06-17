package com.example.praga.opencv.Praga;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.example.praga.opencv.R;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

public class PasswordActi extends Activity{
    SharedPreferences preferences;
    private DrawerLayout mDrawerLayout;
    XWalkView xWalkWebView;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        pass = preferences.getString("passWiw",null);
        Toast toast = Toast.makeText(getApplicationContext(), pass , Toast.LENGTH_SHORT); toast.show();

        xWalkWebView=(XWalkView)findViewById(R.id.xwalkWebView);
        xWalkWebView.setBackgroundColor(Color.TRANSPARENT);
        xWalkWebView.clearCache(true);
        xWalkWebView.load("file:///android_asset/index.html", null);


        xWalkWebView.setResourceClient(new XWalkResourceClient(xWalkWebView){
            @Override
            public void onLoadFinished(XWalkView view, String url) {
                final XWalkView tmpView = view;
                xWalkWebView.evaluateJavascript("(function() {return outPut; })();"  , new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        SharedPreferences.Editor editor = preferences.edit();
                        // pass = s;
                        if(pass.equals( s) ){
                            PasswordActi.this.finish();
                            Toast toast = Toast.makeText(getApplicationContext(), "okk" , Toast.LENGTH_SHORT); toast.show();
                        }
                    }
                });
                super.onLoadFinished(view, url);

            }


        });

    }

    @Override
    protected void onStart() {
        makeFullScreen();
        super.onStart();
    }

    /**
     * A simple method that sets the screen to fullscreen.
     * It removes the Notifications bar,
     *   the Actionbar and the virtual keys (if they are on the phone)
     */
    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()|0x00200000);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
    }


    @Override
    public void onBackPressed() {
        return; //Do nothing!
    }

    public void unlockScreen(View view) {
        //Instead of using finish(), this totally destroys the process
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).moveTaskToFront(getTaskId(), 0);
    }

    /**
     @Override
     public void onAttachedToWindow()
     {
     this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
     super.onAttachedToWindow();
     }**/

    public  void  restart(View view) {
        xWalkWebView.evaluateJavascript("(function() { output = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR';var board2 = new ChessBoard3('board2', {\n" +
                "\t\t\tdraggable: true,\n" +
                "\t\t\tstart: true,\n" +
                "\t\t\tdropOffBoard: 'trash',\n" +
                "\t\t\tsparePieces: false ,\n" +
                "\t\t\trotateControls : true,\n" +
                "position : 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR',"+
                "\t\t\tonChange: function(oldPos, newPos) {\n" +

                "outPut = ChessBoard3.objToFen(newPos);" +
                "\t\t\t  }\n" +
                "\n" +
                "\t\t\t});" +
                " return board2;})(); ", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Toast toast = Toast.makeText(getApplicationContext(), s , Toast.LENGTH_SHORT); toast.show();
            }
        });
    }
}