package com.example.stu.glego28; /**
 * Created by Stu on 23/07/2015.
 */
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;
import com.example.stu.glego28.*;

/**
 * Our OpenGL program's main activity
 */
public class MyGLActivity extends Activity {

    private GLSurfaceView glView;   // Use GLSurfaceView

    // Call back when the activity is started, to initialize the view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //setContentView(R.layout.main_layout);
       // stage = (Stage)findViewById(R.id.);

       // TextureManager.add(new int[]{R.drawable.slime});
//        stage.setScene(new MyScene(stage));

        glView = new MyGLSurfaceView(this);           // Allocate a GLSurfaceView
        //glView.setRenderer(new MyGLRenderer(this)); // Use a custom renderer
        this.setContentView(glView);                // This activity sets to GLSurfaceView
    }

    // Call back when the activity is going into the background
    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
    }

    // Call back after onPause()
    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }
}