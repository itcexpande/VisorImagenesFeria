package com.expandenegocio.visorimagenes.utils;

import android.app.Activity;
import android.content.Intent;

import com.expandenegocio.visorimagenes.forms.MainActivity;

import java.util.TimerTask;

/**
 * Created by Penlopjo on 18/04/2016.
 */
public class BackFormReminderTask  extends TimerTask {

    public static int MILISEGUNDOS_ESPERA = 40000;
    Activity activity;

   public BackFormReminderTask(Activity activity){
        this.activity=activity;
    }

    @Override
    public void run() {
        volverInicio();
    }

    private void volverInicio(){

        Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.getBaseContext().startActivity(intent);
    }

}
