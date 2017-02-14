package com.expandenegocio.visorimagenes.forms;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expandenegocio.visorimagenes.DAO.SolicitudesDataSource;
import com.expandenegocio.visorimagenes.R;
import com.expandenegocio.visorimagenes.model.Provincia;
import com.expandenegocio.visorimagenes.model.Solicitud;
import com.expandenegocio.visorimagenes.utils.BackFormReminderTask;
import com.expandenegocio.visorimagenes.utils.ValidatorUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.UUID;

public class FormActivity extends AppCompatActivity {

    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private Provincia provincia;
    private Spinner spnProvincia;
    private EditText txtNombre;
    private EditText txtApellidos;
    private EditText txtCorreo;
    private EditText txtTelefono;

    private Solicitud solicitud;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_form);
        loadSpinner();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        timer = new Timer();
        timer.schedule(new BackFormReminderTask(this),BackFormReminderTask.MILISEGUNDOS_ESPERA);

        setupMainWindowDisplayMode();

        createListeners();

    }

    private void createListeners(){

        TextWatcher textWatcher= new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            public void onTextChanged(CharSequence s, int start, int before, int count){
                reiniciarTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        };

        EditText txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtNombre.addTextChangedListener(textWatcher);

        EditText txtApellidos = (EditText)findViewById(R.id.txtApellidos);
        txtApellidos.addTextChangedListener(textWatcher);

        EditText txtCorreo = (EditText)findViewById(R.id.txtCorreo);
        txtCorreo.addTextChangedListener(textWatcher);

        EditText txtTelefono = (EditText)findViewById(R.id.txtTelefono);
        txtTelefono.addTextChangedListener(textWatcher);

    }

    private void reiniciarTimer(){
        timer.cancel();
        timer = new Timer();
        timer.schedule(new BackFormReminderTask(this),BackFormReminderTask.MILISEGUNDOS_ESPERA);
    }

    private void loadSpinner() {

        SolicitudesDataSource dataSource = new SolicitudesDataSource(this);
        ArrayList<Provincia> listaProv=dataSource.getProvincias();

        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaProv);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnProvincia = (Spinner) findViewById(R.id.spnProvinciaInter);

        spnProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       setupMainWindowDisplayMode();
                       reiniciarTimer();
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {
                       setupMainWindowDisplayMode();
                   }
               }
            );

        spnProvincia.setAdapter(spinner_adapter);

    }

    public void saveForm(View view){

        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        spnProvincia = (Spinner) findViewById(R.id.spnProvinciaInter);

        nombre=txtNombre.getText().toString();
        apellidos=txtApellidos.getText().toString();
        correo=txtCorreo.getText().toString();
        telefono=txtTelefono.getText().toString();
        provincia =(Provincia) spnProvincia.getSelectedItem();

        String val=validate();

        if (val==null){

            solicitud= createSolicitud();

            SolicitudesDataSource dataSource = new SolicitudesDataSource(this);
            dataSource.insertSolicitud(solicitud);

            Intent intent = new Intent(this, FranquicisasActivity.class);
            intent.putExtra("Solicitud",solicitud);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), val, Toast.LENGTH_LONG).show();
        }
    }

    private String validate(){

        String output=null;

        if (nombre.trim().equals("")){
            output="El nombre no puede estar vacío";
        }

        if (apellidos.trim().equals("")){
            output="El campo Apellidos no puede estar vacío";
        }

        if (correo.trim().equals("")){
            output="El correo no puede estar vacío";
        }

        if (!ValidatorUtil.validateEmail(correo)){
            output="El correo no es válido";
        }

        if (telefono.trim().equals("")){
            output="El telefono no puede estar vacío";
        }

        if(!ValidatorUtil.validateTel(telefono.trim(),"34")){
            output="El formato de teléfono no es válido";
        }

        if (provincia==null || provincia.getCodigoProv()==-1){
            output="El campo provincia no puede estar vacío";
        }

        return output;
    }

    private Solicitud createSolicitud(){

        Solicitud solicitud=new Solicitud();

        solicitud.setId(UUID.randomUUID().toString());
        solicitud.setNombre(nombre);
        solicitud.setApellidos(apellidos);
        solicitud.setCorreo(correo);
        solicitud.setTelefono(telefono);
        solicitud.setProvincia(provincia.getCodigoProv());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        solicitud.setFecha(currentDateandTime);

        SolicitudesDataSource dataSource = new SolicitudesDataSource(this);
        String franqAct= dataSource.buscaFranquiciaAct();

        solicitud.setFranq_principal(franqAct);

        return solicitud;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setSystemUiVisibilityMode();
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
        View decorView = setSystemUiVisibilityMode();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                setSystemUiVisibilityMode(); // Needed to avoid exiting immersive_sticky when keyboard is displayed
            }
        });
    }

    private void setupMainWindowDisplayMode() {
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
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;

        decorView.setSystemUiVisibility(options);
        return decorView;
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

}