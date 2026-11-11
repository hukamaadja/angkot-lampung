/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.angkotlampung;

/**
 *
 * @author YONOKOMOO
 */
public class Angkot {
    private String platNomor;
    private String namaSopir;
    private Rute rute;
    protected double tarifDasar;

    public static int totalAngkot = 0;

    public Angkot(String platNomor, String namaSopir, Rute rute, double tarifDasar) {
        setPlatNomor(platNomor);
        setNamaSopir(namaSopir);
        setRute(rute);
        setTarifDasar(tarifDasar);
        totalAngkot++;
    }

    public String getPlatNomor() { return this.platNomor; }
    public void setPlatNomor(String platNomor) {
        if (platNomor == null || platNomor.trim().isEmpty()) {
            throw new IllegalArgumentException("Plat nomor tidak boleh kosong!");
        }
        this.platNomor = platNomor.trim().toUpperCase();
    }

    public String getNamaSopir() { return this.namaSopir; }
    public void setNamaSopir(String namaSopir) {
        if (namaSopir == null || namaSopir.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama sopir tidak boleh kosong!");
        }
        this.namaSopir = namaSopir.trim();
    }

    public Rute getRute() { return this.rute; }
    public void setRute(Rute rute) {
        if (rute == null) {
            throw new IllegalArgumentException("Rute harus dipilih!");
        }
        this.rute = rute;
    }

    public double getTarifDasar() { return this.tarifDasar; }
    public void setTarifDasar(double tarifDasar) {
        if (tarifDasar <= 0) {
            throw new IllegalArgumentException("Tarif harus lebih dari 0!");
        }
        this.tarifDasar = tarifDasar;
    }

    public String getJenis() {
        return "Angkot Umum";
    }

    // Akan di-override oleh subclass
    public double hitungTarif(int jumlahPenumpang) {
        return this.tarifDasar * jumlahPenumpang;
    }

    public String getKeterangan() {
        return "-";
    }

    @Override
    public String toString() {
        return platNomor + " (" + getJenis() + ")";
    }
}
