package com.mycompany.angkotlampung;

import java.util.ArrayList;

public class Rute {

    private String kodeRute;
    private String namaRute;
    private String titikAwal;
    private String titikAkhir;
    private final ArrayList<String> pemberhentian = new ArrayList<>();

    public static int totalRute = 0;

    public Rute(String kodeRute, String namaRute, String titikAwal, String titikAkhir) {
        setKodeRute(kodeRute);
        setNamaRute(namaRute);
        setTitikAwal(titikAwal);
        setTitikAkhir(titikAkhir);
        totalRute++;
    }

    private static String wajibIsi(String nilai, String namaField) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaField + " tidak boleh kosong!");
        }
        return nilai.trim();
    }

    public String getKodeRute() { return this.kodeRute; }
    public void setKodeRute(String kodeRute) { this.kodeRute = wajibIsi(kodeRute, "Kode rute").toUpperCase(); }

    public String getNamaRute() { return this.namaRute; }
    public void setNamaRute(String namaRute) { this.namaRute = wajibIsi(namaRute, "Nama rute"); }

    public String getTitikAwal() { return this.titikAwal; }
    public void setTitikAwal(String titikAwal) { this.titikAwal = wajibIsi(titikAwal, "Titik awal"); }

    public String getTitikAkhir() { return this.titikAkhir; }
    public void setTitikAkhir(String titikAkhir) { this.titikAkhir = wajibIsi(titikAkhir, "Titik akhir"); }

    public ArrayList<String> getPemberhentian() { return this.pemberhentian; }

    public void tambahPemberhentian(String tempat) {
        this.pemberhentian.add(wajibIsi(tempat, "Nama pemberhentian"));
    }

    public boolean melewati(String tempat) {
        String kata = tempat.toLowerCase();
        for (String p : pemberhentian) {
            if (p.toLowerCase().contains(kata)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + kodeRute + "] " + namaRute;
    }
}
