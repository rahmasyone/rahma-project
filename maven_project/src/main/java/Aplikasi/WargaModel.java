package Aplikasi;
import javafx.beans.property.*;

public class WargaModel {
    private final IntegerProperty idWarga, idWilayah;
    private final StringProperty  nik, namaWarga, noHp, namaWilayah;

    public WargaModel(int id, String nik, String nama, String hp, String wilayah, int idWil) {
        this.idWarga     = new SimpleIntegerProperty(id);
        this.nik         = new SimpleStringProperty(nik);
        this.namaWarga   = new SimpleStringProperty(nama);
        this.noHp        = new SimpleStringProperty(hp != null ? hp : "");
        this.namaWilayah = new SimpleStringProperty(wilayah);
        this.idWilayah   = new SimpleIntegerProperty(idWil);
    }
    public int    getIdWarga()     { return idWarga.get(); }
    public String getNik()         { return nik.get(); }
    public String getNamaWarga()   { return namaWarga.get(); }
    public String getNoHp()        { return noHp.get(); }
    public String getNamaWilayah() { return namaWilayah.get(); }
    public int    getIdWilayah()   { return idWilayah.get(); }
    public IntegerProperty idWargaProperty()     { return idWarga; }
    public StringProperty  nikProperty()         { return nik; }
    public StringProperty  namaWargaProperty()   { return namaWarga; }
    public StringProperty  noHpProperty()        { return noHp; }
    public StringProperty  namaWilayahProperty() { return namaWilayah; }
    public IntegerProperty idWilayahProperty()   { return idWilayah; }
}
