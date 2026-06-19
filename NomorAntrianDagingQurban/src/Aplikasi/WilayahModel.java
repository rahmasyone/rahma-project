package Aplikasi;
import javafx.beans.property.*;

public class WilayahModel {
    private final IntegerProperty idWilayah, jumlahWarga;
    private final StringProperty  namaWilayah, keterangan;

    public WilayahModel(int id, String nama, String ket, int jumlah) {
        this.idWilayah   = new SimpleIntegerProperty(id);
        this.namaWilayah = new SimpleStringProperty(nama);
        this.keterangan  = new SimpleStringProperty(ket != null ? ket : "");
        this.jumlahWarga = new SimpleIntegerProperty(jumlah);
    }
    public int    getIdWilayah()   { return idWilayah.get(); }
    public String getNamaWilayah() { return namaWilayah.get(); }
    public String getKeterangan()  { return keterangan.get(); }
    public int    getJumlahWarga() { return jumlahWarga.get(); }
    public IntegerProperty idWilayahProperty()   { return idWilayah; }
    public StringProperty  namaWilayahProperty() { return namaWilayah; }
    public StringProperty  keteranganProperty()  { return keterangan; }
    public IntegerProperty jumlahWargaProperty() { return jumlahWarga; }
    @Override public String toString() { return namaWilayah.get(); }
}
