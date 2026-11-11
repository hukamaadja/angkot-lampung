package com.mycompany.angkotlampung;

// Subclass 1
public class AngkotReguler extends Angkot {

    private int kapasitas;

    public AngkotReguler(String platNomor, String namaSopir, Rute rute, double tarifDasar, int kapasitas) {
        super(platNomor, namaSopir, rute, tarifDasar);
        setKapasitas(kapasitas);
    }

    public int getKapasitas() { return this.kapasitas; }
    public void setKapasitas(int kapasitas) {
        if (kapasitas <= 0) {
            throw new IllegalArgumentException("Kapasitas harus lebih dari 0!");
        }
        this.kapasitas = kapasitas;
    }

    @Override
    public String getJenis() {
        return "Angkot Reguler";
    }

    @Override
    public String getKeterangan() {
        return "Kapasitas " + kapasitas + " penumpang";
    }
}
