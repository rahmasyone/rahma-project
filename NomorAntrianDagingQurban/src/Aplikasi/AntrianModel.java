package Aplikasi;
import javafx.beans.property.*;

public class AntrianModel {
    private final IntegerProperty nomorAntrian;
    private final StringProperty  namaWarga, namaWilayah, status, waktuDaftar, waktuDipanggil;

    public AntrianModel(int nomor, String nama, String wilayah, String status, String daftar, String panggil) {
        this.nomorAntrian   = new SimpleIntegerProperty(nomor);
        this.namaWarga      = new SimpleStringProperty(nama);
        this.namaWilayah    = new SimpleStringProperty(wilayah);
        this.status         = new SimpleStringProperty(status);
        this.waktuDaftar    = new SimpleStringProperty(daftar   != null ? daftar   : "-");
        this.waktuDipanggil = new SimpleStringProperty(panggil  != null ? panggil  : "-");
    }
    public int    getNomorAntrian()   { return nomorAntrian.get(); }
    public String getNamaWarga()      { return namaWarga.get(); }
    public String getNamaWilayah()    { return namaWilayah.get(); }
    public String getStatus()         { return status.get(); }
    public String getWaktuDaftar()    { return waktuDaftar.get(); }
    public String getWaktuDipanggil() { return waktuDipanggil.get(); }
    public IntegerProperty nomorAntrianProperty()   { return nomorAntrian; }
    public StringProperty  namaWargaProperty()      { return namaWarga; }
    public StringProperty  namaWilayahProperty()    { return namaWilayah; }
    public StringProperty  statusProperty()         { return status; }
    public StringProperty  waktuDaftarProperty()    { return waktuDaftar; }
    public StringProperty  waktuDipanggilProperty() { return waktuDipanggil; }
}
