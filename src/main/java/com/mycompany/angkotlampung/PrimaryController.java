package com.mycompany.angkotlampung;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PrimaryController {

    // ----- Tab Rute -----
    @FXML private TableView<Rute> tabelRute;
    @FXML private TableColumn<Rute, String> kolKode;
    @FXML private TableColumn<Rute, String> kolNama;
    @FXML private TableColumn<Rute, String> kolAwal;
    @FXML private TableColumn<Rute, String> kolAkhir;
    @FXML private TextField fieldCari;
    @FXML private ListView<String> listPemberhentian;
    @FXML private TextField fieldKode;
    @FXML private TextField fieldNamaRute;
    @FXML private TextField fieldAwal;
    @FXML private TextField fieldAkhir;
    @FXML private TextField fieldTengah;
    @FXML private Label labelTotalRute;

    // ----- Tab Angkot -----
    @FXML private TableView<Angkot> tabelAngkot;
    @FXML private TableColumn<Angkot, String> kolJenis;
    @FXML private TableColumn<Angkot, String> kolPlat;
    @FXML private TableColumn<Angkot, String> kolSopir;
    @FXML private TableColumn<Angkot, String> kolRuteAngkot;
    @FXML private TableColumn<Angkot, String> kolTarif;
    @FXML private TableColumn<Angkot, String> kolKeterangan;
    @FXML private TextField fieldPlat;
    @FXML private TextField fieldSopir;
    @FXML private TextField fieldTarif;
    @FXML private TextField fieldExtra;
    @FXML private ComboBox<Rute> comboRute;
    @FXML private ComboBox<String> comboJenis;
    @FXML private Label labelExtra;
    @FXML private Label labelTotalAngkot;

    // ----- Tab Hitung Tarif -----
    @FXML private ComboBox<Angkot> comboAngkotTarif;
    @FXML private TextField fieldPenumpang;
    @FXML private Label labelHasil;

    private final ObservableList<Rute> dataRute = FXCollections.observableArrayList();
    private final ObservableList<Angkot> dataAngkot = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        isiDataAwal();

        kolKode.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKodeRute()));
        kolNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaRute()));
        kolAwal.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitikAwal()));
        kolAkhir.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitikAkhir()));
        tabelRute.setItems(dataRute);
        tabelRute.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> {
            if (baru == null) {
                listPemberhentian.getItems().clear();
            } else {
                listPemberhentian.setItems(FXCollections.observableArrayList(baru.getPemberhentian()));
            }
        });

        kolJenis.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getJenis()));
        kolPlat.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPlatNomor()));
        kolSopir.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaSopir()));
        kolRuteAngkot.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRute().toString()));
        kolTarif.setCellValueFactory(d -> new SimpleStringProperty(rupiah(d.getValue().getTarifDasar())));
        kolKeterangan.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKeterangan()));
        tabelAngkot.setItems(dataAngkot);

        comboRute.setItems(dataRute);
        comboAngkotTarif.setItems(dataAngkot);
        comboJenis.setItems(FXCollections.observableArrayList("Reguler", "Pelajar"));
        comboJenis.getSelectionModel().selectFirst();
        labelExtra.setText("Kapasitas (orang):");
        comboJenis.valueProperty().addListener((obs, lama, baru) -> {
            if ("Pelajar".equals(baru)) {
                labelExtra.setText("Diskon (%):");
            } else {
                labelExtra.setText("Kapasitas (orang):");
            }
        });

        perbaruiLabel();
    }

    // ================= Tab Rute =================

    @FXML
    private void handleCari() {
        String kata = fieldCari.getText().trim();
        if (kata.isEmpty()) {
            tabelRute.setItems(dataRute);
            return;
        }
        ArrayList<Rute> hasil = new ArrayList<>();
        for (Rute r : dataRute) {
            if (r.melewati(kata)) {
                hasil.add(r);
            }
        }
        tabelRute.setItems(FXCollections.observableArrayList(hasil));
        if (hasil.isEmpty()) {
            tampilkan(Alert.AlertType.INFORMATION, "Hasil Pencarian",
                    "Tidak ada rute yang melewati \"" + kata + "\".");
        }
    }

    @FXML
    private void handleSemuaRute() {
        fieldCari.clear();
        tabelRute.setItems(dataRute);
    }

    @FXML
    private void handleTambahRute() {
        try {
            if (cariKode(fieldKode.getText()) != null) {
                throw new IllegalArgumentException("Kode rute sudah dipakai!");
            }
            Rute baru = new Rute(fieldKode.getText(), fieldNamaRute.getText(),
                    fieldAwal.getText(), fieldAkhir.getText());
            baru.tambahPemberhentian(fieldAwal.getText());
            String tengah = fieldTengah.getText().trim();
            if (!tengah.isEmpty()) {
                for (String p : tengah.split(",")) {
                    if (!p.trim().isEmpty()) {
                        baru.tambahPemberhentian(p);
                    }
                }
            }
            baru.tambahPemberhentian(fieldAkhir.getText());
            dataRute.add(baru);

            fieldKode.clear();
            fieldNamaRute.clear();
            fieldAwal.clear();
            fieldAkhir.clear();
            fieldTengah.clear();
            perbaruiLabel();
            tampilkan(Alert.AlertType.INFORMATION, "Berhasil", "Rute berhasil ditambahkan!");
        } catch (IllegalArgumentException e) {
            tampilkan(Alert.AlertType.ERROR, "Input tidak valid", e.getMessage());
        }
    }

    // ================= Tab Angkot =================

    @FXML
    private void handleTambahAngkot() {
        try {
            double tarif = Double.parseDouble(fieldTarif.getText().trim());
            double extra = Double.parseDouble(fieldExtra.getText().trim());
            Rute rute = comboRute.getValue();
            String jenis = comboJenis.getValue();

            Angkot baru;
            if ("Pelajar".equals(jenis)) {
                baru = new AngkotPelajar(fieldPlat.getText(), fieldSopir.getText(), rute, tarif, extra);
            } else {
                baru = new AngkotReguler(fieldPlat.getText(), fieldSopir.getText(), rute, tarif, (int) extra);
            }
            dataAngkot.add(baru);

            fieldPlat.clear();
            fieldSopir.clear();
            fieldTarif.clear();
            fieldExtra.clear();
            perbaruiLabel();
            tampilkan(Alert.AlertType.INFORMATION, "Berhasil", "Angkot berhasil ditambahkan!");
        } catch (NumberFormatException e) {
            tampilkan(Alert.AlertType.ERROR, "Input tidak valid", "Tarif dan kapasitas/diskon harus berupa angka!");
        } catch (IllegalArgumentException e) {
            tampilkan(Alert.AlertType.ERROR, "Input tidak valid", e.getMessage());
        }
    }

    // ================= Tab Hitung Tarif =================

    @FXML
    private void handleHitung() {
        Angkot a = comboAngkotTarif.getValue();
        if (a == null) {
            tampilkan(Alert.AlertType.WARNING, "Peringatan", "Pilih angkot terlebih dahulu.");
            return;
        }
        try {
            int orang = Integer.parseInt(fieldPenumpang.getText().trim());
            if (orang <= 0) {
                tampilkan(Alert.AlertType.WARNING, "Peringatan", "Jumlah penumpang harus lebih dari 0.");
                return;
            }
            labelHasil.setText("Total tarif untuk " + orang + " orang: " + rupiah(a.hitungTarif(orang)));
        } catch (NumberFormatException e) {
            tampilkan(Alert.AlertType.ERROR, "Input tidak valid", "Jumlah penumpang harus berupa angka bulat!");
        }
    }

    // ================= Helper =================

    private Rute cariKode(String kode) {
        for (Rute r : dataRute) {
            if (r.getKodeRute().equalsIgnoreCase(kode.trim())) {
                return r;
            }
        }
        return null;
    }

    private String rupiah(double nilai) {
        return String.format("Rp%,.0f", nilai);
    }

    private void perbaruiLabel() {
        labelTotalRute.setText("Total rute: " + Rute.totalRute);
        labelTotalAngkot.setText("Total angkot terdaftar: " + Angkot.totalAngkot);
    }

    private void tampilkan(Alert.AlertType tipe, String judul, String pesan) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    // Data contoh (ILUSTRASI - ganti dengan data rute asli)
    private void isiDataAwal() {
        Rute r1 = new Rute("R01", "Rajabasa - Teluk Betung", "Terminal Rajabasa", "Teluk Betung");
        for (String p : new String[]{"Terminal Rajabasa", "Kampus Unila", "Kedaton", "Tanjung Karang", "Teluk Betung"}) {
            r1.tambahPemberhentian(p);
        }
        Rute r2 = new Rute("R02", "Kemiling - Tanjung Karang", "Kemiling", "Tanjung Karang");
        for (String p : new String[]{"Kemiling", "Segala Mider", "Pasar Tugu", "Tanjung Karang"}) {
            r2.tambahPemberhentian(p);
        }
        Rute r3 = new Rute("R03", "Panjang - Tanjung Karang", "Panjang", "Tanjung Karang");
        for (String p : new String[]{"Panjang", "Pelabuhan Panjang", "Teluk Betung", "Tanjung Karang"}) {
            r3.tambahPemberhentian(p);
        }
        dataRute.addAll(r1, r2, r3);

        dataAngkot.add(new AngkotReguler("BE 1234 AB", "Pak Budi", r1, 5000, 12));
        dataAngkot.add(new AngkotPelajar("BE 5678 CD", "Pak Joko", r1, 5000, 40));
        dataAngkot.add(new AngkotReguler("BE 4321 EF", "Pak Slamet", r2, 4000, 12));
    }
}
