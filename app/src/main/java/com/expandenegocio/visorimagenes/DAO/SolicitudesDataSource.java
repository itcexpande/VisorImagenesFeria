package com.expandenegocio.visorimagenes.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.expandenegocio.visorimagenes.model.Provincia;
import com.expandenegocio.visorimagenes.model.Solicitud;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Penlopjo on 31/03/2016.
 */
public class SolicitudesDataSource {

    public static final String SOLICITUD_TABLE_NAME = "Solicitudes";
    public static final String PROVINCIA_TABLE_NAME = "Provincias";
    public static final String FRANQUICIA_TABLE_NAME = "Franquicias";
    public static final String SOLICITUD_FRANQUICIA_TABLE_NAME = "SolicitudFranquicia";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";

    //Campos de la tabla Solicitudes
    public static class ColumnSolicitud{
        public static final String ID = "id";
        public static final String NOMBRE = "Nombre";
        public static final String APELLIDOS = "Apellidos";
        public static final String CORREO = "Correo";
        public static final String TELEFONO = "Telefono";
        public static final String PROVINCIA = "Provincia";
        public static final String FECHA="Fecha";
        public static final String FRANQUICIA_PRINCIPAL="franq_principal";
    }

    public static class ColumnProvincia{
        public static final String ID = "c_prov";
        public static final String NOMBRE = "d_prov";
    }

    public static class ColumnFranquicia{
        public static final String ID = "id";
        public static final String NOMBRE = "nombre";
        public static final String ACTIVO = "activo";
    }

    public static class ColumnSolicitudFranquicia{
        public static final String IDSol = "IDSol";
        public static final String IDFran = "IDFran";
    }

    public static final String CREATE_SOLICITUDES_SCRIPT =
            "create table "+SOLICITUD_TABLE_NAME+"(" +
                    ColumnSolicitud.ID+" "+STRING_TYPE+" not null, " +
                    ColumnSolicitud.NOMBRE+" "+STRING_TYPE+" ," +
                    ColumnSolicitud.APELLIDOS+" "+STRING_TYPE+" not null, " +
                    ColumnSolicitud.CORREO+" "+STRING_TYPE+" not null, " +
                    ColumnSolicitud.TELEFONO+" "+STRING_TYPE+" not null, " +
                    ColumnSolicitud.PROVINCIA+" "+INT_TYPE+" not null, "+
                    ColumnSolicitud.FECHA+" "+STRING_TYPE+" not null, " +
                    ColumnSolicitud.FRANQUICIA_PRINCIPAL+" "+STRING_TYPE+" not null)";


    private SolicitudesDbHelper openHelper;
    private SQLiteDatabase database;

    public SolicitudesDataSource(Context context) {
        //Creando una instancia hacia la base de datos
        openHelper = new SolicitudesDbHelper(context);
        database = openHelper.getWritableDatabase();
    }

    public void insertSolicitud(Solicitud solicitud){

        String insertSQL= "Insert into " + SOLICITUD_TABLE_NAME +
                "("+
                ColumnSolicitud.ID+","+
                ColumnSolicitud.NOMBRE+","+
                ColumnSolicitud.APELLIDOS+", "+
                ColumnSolicitud.CORREO+", "+
                ColumnSolicitud.TELEFONO+", "+
                ColumnSolicitud.PROVINCIA+", "+
                ColumnSolicitud.FECHA+", "+
                ColumnSolicitud.FRANQUICIA_PRINCIPAL+
                ") VALUES"+ "("+
                "'"+ solicitud.getId()+"',"+
                "'"+ solicitud.getNombre()+"',"+
                "'"+ solicitud.getApellidos()+"',"+
                "'"+ solicitud.getCorreo()+"',"+
                "'"+ solicitud.getTelefono()+"',"+
                + solicitud.getProvincia()+","+
                "'"+ solicitud.getFecha()+"',"+
                "'"+ solicitud.getFranq_principal()+"')";

        try{
            database.execSQL(insertSQL);
        }catch(Exception ex ){
            Log.d("Error insertar Recinto",ex.toString());
        }
    }

    public void insertSolicitudFranquicia(Solicitud solicitud,String IdFranquicia){

        String insertSQL= "Insert into " + SOLICITUD_FRANQUICIA_TABLE_NAME +
                "("+ColumnSolicitudFranquicia.IDSol +","+
                ColumnSolicitudFranquicia.IDFran +") VALUES"+ "("+
                "'"+ solicitud.getId()+"',"+
                "'"+ IdFranquicia+"')";
        try{
            database.execSQL(insertSQL);
        }catch(Exception ex ){
            Log.d("Error insertar Recinto",ex.toString());
        }
    }

    public String buscaFranquiciaAct(){

        String output="";
        try{

            String query = "SELECT "+ColumnFranquicia.ID+
                    " FROM " + FRANQUICIA_TABLE_NAME +
                    " WHERE activo=1";

            Cursor cursor = database.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    output=cursor.getString(0);
                } while (cursor.moveToNext());
            }

        }catch(Exception ex){
            Log.d("Error recog FranquiAct",ex.toString());
        }

        return output;

    }

    public ArrayList<Provincia> getProvincias(){

        ArrayList<Provincia> output=new ArrayList<Provincia>();

        try{

            String query = "SELECT "+ColumnProvincia.ID+","+
                                     ColumnProvincia.NOMBRE+" "+
                            " FROM " + PROVINCIA_TABLE_NAME +
                            " ORDER BY "+ ColumnProvincia.ID;

            Cursor cursor = database.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Provincia provincia=new Provincia();
                    provincia.setCodigoProv(Integer.parseInt(cursor.getString(0)));
                    provincia.setDescProv(cursor.getString(1));

                    // Add book to books
                    output.add(provincia);
                } while (cursor.moveToNext());
            }

        }catch(Exception ex){
            Log.d("Error getProvincias",ex.toString());
        }

        return output;
    }
}