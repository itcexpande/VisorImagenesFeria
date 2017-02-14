package com.expandenegocio.visorimagenes.forms;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.expandenegocio.visorimagenes.DAO.SolicitudesDataSource;
import com.expandenegocio.visorimagenes.R;
import com.expandenegocio.visorimagenes.model.Solicitud;
import com.expandenegocio.visorimagenes.utils.BackFranqReminderTask;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

public class FranquicisasActivity extends AppCompatActivity {

    Hashtable<String,String> listaFranquicias=new Hashtable<String,String>();
    Timer timer;

    Solicitud solicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        solicitud = (Solicitud)getIntent().getExtras().getSerializable("Solicitud");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_franquicisas);

        timer = new Timer();
        timer.schedule(new BackFranqReminderTask(this),BackFranqReminderTask.MILISEGUNDOS_ESPERA);
    }

    public void onCheckboxClicked(View view) {

        CheckBox check = (CheckBox)view;

        if (check.isChecked()){
            listaFranquicias.put(check.getTag().toString(),"");
        }else{
            listaFranquicias.remove(check.getTag().toString());
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

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

    public void saveFranquicia(View view) {

        SolicitudesDataSource dataSource = new SolicitudesDataSource(this);

        Enumeration<String> enumeration = listaFranquicias.keys();
        while (enumeration.hasMoreElements()) {
            dataSource.insertSolicitudFranquicia(solicitud,enumeration.nextElement());
        }

        dataSource.insertSolicitudFranquicia(solicitud,dataSource.buscaFranquiciaAct());
        Toast.makeText(getApplicationContext(), "Gracias por su tiempo!! \n \n En breve le enviaremos información sobre las franquicias solicitadas", Toast.LENGTH_LONG).show();

        volverInicio();
    }

    private void volverInicio(){

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getBaseContext().startActivity(intent);
    }
}
