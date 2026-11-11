package com.mycompany.angkotlampung;

import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private static final ArrayList<Rute> dataRute = new ArrayList<>();
    private static final ArrayList<Angkot> dataAngkot = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        isiDataAwal();

        boolean berjalan = true;
        while (berjalan) {
            System.out.println("\n==========================================");
            System.out.println("   SISTEM INFORMASI ANGKOT BANDAR LAMPUNG ");
            System.out.println("==========================================");
            System.out.println("1. Tampilkan Semua Rute");
            System.out.println("2. Cari Rute (berdasarkan lokasi)");
            System.out.println("3. Tambah Rute Baru");
            System.out.println("4. Tampilkan Semua Angkot");
            System.out.println("5. Tambah Angkot Baru");
            System.out.println("6. Hitung Tarif Penumpang");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu (0-6): ");

            String pilihan = scanner.nextLine().trim();
            System.out.println();

            switch (pilihan) {
                case "1":
                    tampilkanRute();
                    break;
                case "2":
                    cariRute();
                    break;
                case "3":
                    tambahRute();
                    break;
                case "4":
                    tampilkanAngkot();
                    break;
                case "5":
                    tambahAngkot();
                    break;
                case "6":
                    hitungTarif();
                    break;
                case "0":
                    berjalan = false;
                    System.out.println("Terima kasih telah menggunakan layanan ini!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid, silakan coba lagi.");
                    break;
            }
        }
    }

    // ================= DATA AWAL =================
    private static void isiDataAwal() {
        Rute r1 = new Rute("R01", "Rajabasa - Teluk Betung", "Terminal Rajabasa", "Teluk Betung");
        for (String p : new String[]{"Terminal Rajabasa", "Kampus Unila", "Kedaton", "Tanjung Karang", "Teluk Betung"}) {
            r1.tambahPemberhentian(p);
        }
        Rute r2 = new Rute("R02", "Kemiling - Tanjung Karang", "Kemiling", "Tanjung Karang");
        for (String p : new String[]{"Kemiling", "Segala Mider", "Pasar Tugu", "Tanjung Karang"}) {
            r2.tambahPemberhentian(p);
        }
        dataRute.add(r1);
        dataRute.add(r2);

        dataAngkot.add(new AngkotReguler("BE 1234 AB", "Pak Budi", r1, 5000, 12));
        dataAngkot.add(new AngkotPelajar("BE 5678 CD", "Pak Joko", r1, 5000, 40));
        dataAngkot.add(new AngkotReguler("BE 4321 EF", "Pak Slamet", r2, 4000, 12));
    }

    // ================= MENU RUTE =================
    private static void tampilkanRute() {
        System.out.println("--- DAFTAR RUTE ANGKOT ---");
        if (dataRute.isEmpty()) {
            System.out.println("Belum ada data rute.");
            return;
        }
        for (Rute r : dataRute) {
            System.out.printf("[%s] %s (%s -> %s)\n", r.getKodeRute(), r.getNamaRute(), r.getTitikAwal(), r.getTitikAkhir());
            System.out.println("    Pemberhentian: " + String.join(" -> ", r.getPemberhentian()));
        }
        System.out.println("Total Rute: " + Rute.totalRute);
    }

    private static void cariRute() {
        System.out.print("Masukkan nama tempat/lokasi yang dicari: ");
        String kata = scanner.nextLine().trim();
        boolean ditemukan = false;

        System.out.println("\nHasil Pencarian Rute me-lewati \"" + kata + "\":");
        for (Rute r : dataRute) {
            if (r.melewati(kata)) {
                System.out.printf("- [%s] %s\n", r.getKodeRute(), r.getNamaRute());
                ditemukan = true;
            }
        }
        if (!ditemukan) {
            System.out.println("Tidak ada rute yang melewati lokasi tersebut.");
        }
    }

    private static void tambahRute() {
        try {
            System.out.print("Kode Rute (misal: R03): ");
            String kode = scanner.nextLine().trim();
            if (cariKode(kode) != null) {
                System.out.println("Error: Kode rute sudah digunakan!");
                return;
            }
            System.out.print("Nama Rute: ");
            String nama = scanner.nextLine().trim();
            System.out.print("Titik Awal: ");
            String awal = scanner.nextLine().trim();
            System.out.print("Titik Akhir: ");
            String akhir = scanner.nextLine().trim();

            Rute ruteBaru = new Rute(kode, nama, awal, akhir);
            ruteBaru.tambahPemberhentian(awal);

            System.out.print("Pemberhentian transit (pisahkan dengan koma, atau kosongkan): ");
            String transit = scanner.nextLine().trim();
            if (!transit.isEmpty()) {
                for (String t : transit.split(",")) {
                    if (!t.trim().isEmpty()) {
                        ruteBaru.tambahPemberhentian(t.trim());
                    }
                }
            }
            ruteBaru.tambahPemberhentian(akhir);
            dataRute.add(ruteBaru);
            System.out.println("Berhasil menambahkan rute baru!");
        } catch (IllegalArgumentException e) {
            System.out.println("Gagal menambahkan rute: " + e.getMessage());
        }
    }

    // ================= MENU ANGKOT =================
    private static void tampilkanAngkot() {
        System.out.println("--- DAFTAR ANGKOT TERDAFTAR ---");
        if (dataAngkot.isEmpty()) {
            System.out.println("Belum ada data angkot.");
            return;
        }
        for (int i = 0; i < dataAngkot.size(); i++) {
            Angkot a = dataAngkot.get(i);
            System.out.printf("%d. %s | Sopir: %s | Jenis: %s | Tarif Dasar: Rp%,.0f | Ket: %s\n",
                    (i + 1), a.getPlatNomor(), a.getNamaSopir(), a.getJenis(), a.getTarifDasar(), a.getKeterangan());
            System.out.println("   Rute: " + a.getRute().toString());
        }
        System.out.println("Total Angkot Terdaftar: " + Angkot.totalAngkot);
    }

    private static void tambahAngkot() {
        if (dataRute.isEmpty()) {
            System.out.println("Error: Tambahkan rute terlebih dahulu sebelum menambah angkot.");
            return;
        }
        try {
            System.out.println("Pilih Jenis Angkot:");
            System.out.println("1. Angkot Reguler");
            System.out.println("2. Angkot Pelajar");
            System.out.print("Pilihan (1/2): ");
            String jenis = scanner.nextLine().trim();

            System.out.print("Plat Nomor: ");
            String plat = scanner.nextLine().trim();
            System.out.print("Nama Sopir: ");
            String sopir = scanner.nextLine().trim();

            tampilkanPilihanRute();
            System.out.print("Pilih Nomor Rute (1-" + dataRute.size() + "): ");
            int idxRute = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idxRute < 0 || idxRute >= dataRute.size()) {
                System.out.println("Error: Pilihan rute tidak valid!");
                return;
            }
            Rute rutePilihan = dataRute.get(idxRute);

            System.out.print("Tarif Dasar (Rp): ");
            double tarif = Double.parseDouble(scanner.nextLine().trim());

            Angkot angkotBaru;
            if ("2".equals(jenis)) {
                System.out.print("Diskon Pelajar (%): ");
                double diskon = Double.parseDouble(scanner.nextLine().trim());
                angkotBaru = new AngkotPelajar(plat, sopir, rutePilihan, tarif, diskon);
            } else {
                System.out.print("Kapasitas Penumpang (orang): ");
                int kapasitas = Integer.parseInt(scanner.nextLine().trim());
                angkotBaru = new AngkotReguler(plat, sopir, rutePilihan, tarif, kapasitas);
            }

            dataAngkot.add(angkotBaru);
            System.out.println("Berhasil menambahkan angkot baru!");
        } catch (NumberFormatException e) {
            System.out.println("Gagal: Tarif/Kapasitas/Diskon harus berupa angka!");
        } catch (IllegalArgumentException e) {
            System.out.println("Gagal: " + e.getMessage());
        }
    }

    // ================= MENU HITUNG TARIF =================
    private static void hitungTarif() {
        if (dataAngkot.isEmpty()) {
            System.out.println("Belum ada angkot terdaftar.");
            return;
        }
        tampilkanAngkot();
        try {
            System.out.print("\nPilih Nomor Angkot (1-" + dataAngkot.size() + "): ");
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= dataAngkot.size()) {
                System.out.println("Nomor angkot tidak valid!");
                return;
            }
            Angkot terpilih = dataAngkot.get(idx);

            System.out.print("Masukkan Jumlah Penumpang: ");
            int orang = Integer.parseInt(scanner.nextLine().trim());
            if (orang <= 0) {
                System.out.println("Jumlah penumpang harus lebih dari 0.");
                return;
            }

            double total = terpilih.hitungTarif(orang);
            System.out.printf("--> Total Tarif untuk %d penumpang (%s): Rp%,.0f\n", orang, terpilih.getJenis(), total);
        } catch (NumberFormatException e) {
            System.out.println("Error: Input angka tidak valid!");
        }
    }

    // ================= HELPER =================
    private static Rute cariKode(String kode) {
        for (Rute r : dataRute) {
            if (r.getKodeRute().equalsIgnoreCase(kode.trim())) {
                return r;
            }
        }
        return null;
    }

    private static void tampilkanPilihanRute() {
        System.out.println("Daftar Rute Tersedia:");
        for (int i = 0; i < dataRute.size(); i++) {
            System.out.printf("  %d. %s\n", (i + 1), dataRute.get(i).toString());
        }
    }
}