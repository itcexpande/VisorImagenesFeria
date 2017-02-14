package com.expandenegocio.visorimagenes.model;

/**
 * Created by Penlopjo on 12/04/2016.
 */
public class Provincia {

    private int codigoProv;
    private String descProv;

    public int getCodigoProv() {
        return codigoProv;
    }

    public void setCodigoProv(int codigoProv) {
        this.codigoProv = codigoProv;
    }

    public String getDescProv() {
        return descProv;
    }

    public void setDescProv(String descProv) {
        this.descProv = descProv;
    }


    public String toString(){
        return descProv;
    }
}
