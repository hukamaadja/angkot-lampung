package com.mycompany.angkotlampung;

// Subclass 2
public class AngkotPelajar extends Angkot {

    private double diskonPersen;

    public AngkotPelajar(String platNomor, String namaSopir, Rute rute, double tarifDasar, double diskonPersen) {
        super(platNomor, namaSopir, rute, tarifDasar);
        setDiskonPersen(diskonPersen);
    }

    public double getDiskonPersen() { return this.diskonPersen; }
    public void setDiskonPersen(double diskonPersen) {
        if (diskonPersen < 0 || diskonPersen > 100) {
            throw new IllegalArgumentException("Diskon harus antara 0 - 100%!");
        }
        this.diskonPersen = diskonPersen;
    }

    @Override
    public String getJenis() {
        return "Angkot Pelajar";
    }

    // Method Overriding: tarif dipotong diskon
    @Override
    public double hitungTarif(int jumlahPenumpang) {
        double total = super.hitungTarif(jumlahPenumpang);
        return total - (total * diskonPersen / 100);
    }

    @Override
    public String getKeterangan() {
        return String.format("Diskon %.0f%%", diskonPersen);
    }
}
