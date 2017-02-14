package com.expandenegocio.visorimagenes.forms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.expandenegocio.visorimagenes.DAO.SolicitudesDataSource;
import com.expandenegocio.visorimagenes.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    public static int MILISEGUNDOS_ESPERA = 4000;

    private ViewPager viewPager;

    private boolean hasVideo=false;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        SolicitudesDataSource dataSource = new SolicitudesDataSource(this);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);

        Button buttonFormulario = (Button) findViewById(R.id.buttonFormulario);

        if (buttonFormulario != null) {
            buttonFormulario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), FormActivity.class);
                    startActivity(intent);
                }
            });
        }

        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(viewPager), 0, MILISEGUNDOS_ESPERA);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }

    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);

    }

    private ArrayList<String> getImages(){

        ArrayList<String> output = new ArrayList<String>();

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/Pictures/";

      //  Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);

        if (isExternalStorageAvailable()){
            File[] files = targetDirector.listFiles();
            for (File file : files){
                if (getExtension(file.getAbsolutePath().toString()).toUpperCase().equals("JPG") ||
                    getExtension(file.getAbsolutePath().toString()).toUpperCase().equals("PNG")){
                    output.add(file.getAbsolutePath());
                }
            }
        }

        String nombreVideo=getVideo();

        if (!nombreVideo.equals("")){
            hasVideo=true;
            output.add(nombreVideo);
        }

        return output;
    }

    private String getVideo(){

        String output = "";

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/Pictures/";

        //  Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);

        if (isExternalStorageAvailable()){
            File[] files = targetDirector.listFiles();

            for (File file : files){
                if (getExtension(file.getAbsolutePath().toString()).toUpperCase().equals("MP4")){
                    output = file.getAbsolutePath().toString();

                }
            }
        }
        return output;
    }

    private String getExtension(String filename){
        String filenameArray[] = filename.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        return extension;
    }

    private boolean isExternalStorageAvailable(){

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        return mExternalStorageAvailable;
    }

    private class ImagePagerAdapter extends PagerAdapter {

        ArrayList<String> listaURIImages = getImages();

        @Override
        public int getCount() {
            return listaURIImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Context context = MainActivity.this;

            try {

                String fichero =listaURIImages.get(position);

                if (getExtension(fichero).toUpperCase().equals("PNG")||
                    getExtension(fichero).toUpperCase().equals("JPG")){


                    ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    Bitmap myBitmap = BitmapFactory.decodeFile(fichero);

                    imageView.setImageBitmap(myBitmap);
                    ((ViewPager) container).addView(imageView, 0);
                    return imageView;

                }else if (getExtension(fichero).toUpperCase().equals("MP4")){

                    getWindow().setFormat(PixelFormat.UNKNOWN);

                    //Displays a video file.
                    VideoView videoView = new VideoView(context);

                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            timer = new Timer();
                            timer.scheduleAtFixedRate(new RemindTask(viewPager), 0, MILISEGUNDOS_ESPERA);
                        }
                    });

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        public void onPrepared(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                          //  timer.cancel();
                        }
                    });

                    String uriPath = fichero;
                    Uri uri = Uri.parse(uriPath);
                    videoView.setVideoURI(uri);
                    videoView.requestFocus();

                    videoView.start();

                    ((ViewPager) container).addView(videoView, 0);
                    return videoView;
                }
            }
            catch(Exception ex){
            }
            return  new ImageView(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }

    class RemindTask extends TimerTask {

        ViewPager viewpager;

        public RemindTask(ViewPager viewpager){
            this.viewpager=viewpager;
        }

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    if (viewpager!=null){



                        if (viewpager.getCurrentItem()==viewpager.getAdapter().getCount()-1) {
                            viewpager.setCurrentItem(0, true);
                        } else {
                            viewpager.setCurrentItem(viewpager.getCurrentItem()+1,true);
                        }

                        if (viewpager.getCurrentItem()==viewpager.getAdapter().getCount()-1 &&
                                hasVideo){
                            timer.cancel();
                        }
                        Integer num=viewpager.getCurrentItem();
                       // Toast.makeText(getApplicationContext(),num.toString()+"-"+(viewpager.getAdapter().getCount()-1), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        View decorView = setSystemUiVisibilityMode();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                setSystemUiVisibilityMode(); // Needed to avoid exiting immersive_sticky when keyboard is displayed
            }
        });

    }

    private View setSystemUiVisibilityMode() {
        View decorView = getWindow().getDecorView();
        int options;
        options =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;

        decorView.setSystemUiVisibility(options);
        return decorView;
    }

    @Override
    public void onBackPressed() {

    }

}