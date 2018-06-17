package com.example.praga.opencv.Praga;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.example.praga.opencv.R;

import org.xwalk.core.XWalkView;

public class PasswordSet extends Activity {

    XWalkView xWalkWebView;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_password_set);
        xWalkWebView=(XWalkView)findViewById(R.id.xwalkWebView);
        xWalkWebView.clearCache(true);

        //xWalkWebView.load("file:///android_asset/index.html", null);
        xWalkWebView.load("file:///android_asset/cube.html", null);
        xWalkWebView.setBackgroundColor(Color.TRANSPARENT);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


    }

    public  void  save(View view){
        xWalkWebView.evaluateJavascript("(function() {return outPut; })();"                       , new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("passWiw" , s);
                editor.apply();
                Toast toast = Toast.makeText(getApplicationContext(), s , Toast.LENGTH_SHORT); toast.show();

                startService(new Intent(getApplicationContext(),LockScreenService.class));
            }
        });
    }

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
