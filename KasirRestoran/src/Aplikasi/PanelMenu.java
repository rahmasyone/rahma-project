package Aplikasi;

import Kelas.koneksi;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PanelMenu extends JPanel {

    static final Color BIRU_TUA         = new Color(30, 100, 180);
    static final Color BIRU_MUDA        = new Color(100, 170, 240);
    static final Color BIRU_SANGAT_MUDA = new Color(220, 237, 255);
    static final Color PUTIH            = Color.WHITE;
    static final Color ABU_MUDA         = new Color(245, 248, 255);

    private JTable tabel;
    private DefaultTableModel modelTabel;
    private JTextField txtNama, txtHarga;
    private JComboBox<String> cmbKategori;
    private JButton btnTambah, btnHapus, btnRefresh;
    private JLabel lblStatus;

    public PanelMenu() {
        setLayout(new BorderLayout());
        setBackground(ABU_MUDA);
        initComponents();
        loadData();
    }

    public void isiankosong{
    txtHarga.setText();
    txtNama.setText();
    }

    public void Aktif(){
        txtHarga.setEnabled();
        txtNama.setEnabled();
    }

    private void initComponents() {
        // ===== HEADER =====
        JPanel header = new JPanel(null);
        header.setBackground(PUTIH);
        header.setPreferredSize(new Dimension(0, 70));

        JLabel lblJudul = new JLabel("Kelola Menu");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
        lblJudul.setForeground(BIRU_TUA);
        lblJudul.setBounds(25, 15, 300, 30);

        JLabel lblSub = new JLabel("Tambah, lihat, dan hapus data menu restoran");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(new Color(120, 120, 120));
        lblSub.setBounds(25, 42, 400, 20);

        header.add(lblJudul);
        header.add(lblSub);

        // ===== FORM =====
        JPanel formPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PUTIH);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(BIRU_MUDA);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setPreferredSize(new Dimension(0, 155));

        JLabel lblFormJudul = new JLabel("Tambah Menu Baru");
        lblFormJudul.setFont(new Font("Arial", Font.BOLD, 13));
        lblFormJudul.setForeground(BIRU_TUA);
        lblFormJudul.setBounds(20, 12, 200, 22);

        JLabel lblN = new JLabel("Nama Menu");
        lblN.setFont(new Font("Arial", Font.BOLD, 12));
        lblN.setForeground(new Color(80, 80, 80));
        lblN.setBounds(20, 42, 100, 18);

        txtNama = new JTextField();
        txtNama.setBounds(20, 62, 200, 30);
        txtNama.setBorder(BorderFactory.createCompoundBorder(
                new FormLogin.RoundBorder(BIRU_MUDA, 8),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        txtNama.setBackground(BIRU_SANGAT_MUDA);
        txtNama.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblH = new JLabel("Harga (Rp)");
        lblH.setFont(new Font("Arial", Font.BOLD, 12));
        lblH.setForeground(new Color(80, 80, 80));
        lblH.setBounds(240, 42, 100, 18);

        txtHarga = new JTextField();
        txtHarga.setBounds(240, 62, 160, 30);
        txtHarga.setBorder(BorderFactory.createCompoundBorder(
                new FormLogin.RoundBorder(BIRU_MUDA, 8),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        txtHarga.setBackground(BIRU_SANGAT_MUDA);
        txtHarga.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblK = new JLabel("Kategori");
        lblK.setFont(new Font("Arial", Font.BOLD, 12));
        lblK.setForeground(new Color(80, 80, 80));
        lblK.setBounds(420, 42, 80, 18);

        cmbKategori = new JComboBox<>(new String[]{"Makanan Berat", "Minuman", "Cemilan"});
        cmbKategori.setBounds(420, 62, 150, 30);
        cmbKategori.setBackground(BIRU_SANGAT_MUDA);
        cmbKategori.setFont(new Font("Arial", Font.PLAIN, 12));

        btnTambah = buatTombol("+ Tambah", new Color(52, 152, 219));
        btnTambah.setBounds(590, 62, 120, 30);
        btnTambah.addActionListener(e -> tambahMenu());

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setBounds(20, 105, 600, 20);

        formPanel.add(lblFormJudul);
        formPanel.add(lblN);
        formPanel.add(txtNama);
        formPanel.add(lblH);
        formPanel.add(txtHarga);
        formPanel.add(lblK);
        formPanel.add(cmbKategori);
        formPanel.add(btnTambah);
        formPanel.add(lblStatus);

        // ===== TABEL =====
        String[] kolom = {"ID", "Nama Menu", "Harga", "Kategori"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabel = new JTable(modelTabel);
        tabel.setFont(new Font("Arial", Font.PLAIN, 13));
        tabel.setRowHeight(35);
        tabel.setShowVerticalLines(false);
        tabel.setGridColor(new Color(220, 230, 245));
        tabel.setSelectionBackground(BIRU_SANGAT_MUDA);
        tabel.setSelectionForeground(BIRU_TUA);
        tabel.setBackground(PUTIH);

        JTableHeader th = tabel.getTableHeader();
        th.setBackground(BIRU_TUA);
        th.setForeground(PUTIH);
        th.setFont(new Font("Arial", Font.BOLD, 13));
        th.setReorderingAllowed(false);
        th.setPreferredSize(new Dimension(0, 38));

        tabel.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabel.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabel.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabel.getColumnModel().getColumn(3).setPreferredWidth(120);

        tabel.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                           boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(col == 0 ? CENTER : LEFT);
                if (sel) {
                    setBackground(BIRU_SANGAT_MUDA);
                    setForeground(BIRU_TUA);
                } else {
                    setBackground(row % 2 == 0 ? PUTIH : new Color(240, 247, 255));
                    setForeground(new Color(50, 50, 50));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(PUTIH);

        // ===== TOOLBAR =====
        JPanel toolbar = new JPanel(null);
        toolbar.setBackground(PUTIH);
        toolbar.setPreferredSize(new Dimension(0, 50));

        btnHapus = buatTombol("Hapus Menu", new Color(220, 60, 60));
        btnHapus.setBounds(15, 10, 120, 30);
        btnHapus.addActionListener(e -> hapusMenu());

        btnRefresh = buatTombol("Refresh", new Color(80, 170, 80));
        btnRefresh.setBounds(145, 10, 90, 30);
        btnRefresh.addActionListener(e -> loadData());

        toolbar.add(btnHapus);
        toolbar.add(btnRefresh);

        JPanel tengah = new JPanel(new BorderLayout(0, 10));
        tengah.setBackground(ABU_MUDA);
        tengah.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        tengah.add(formPanel, BorderLayout.NORTH);
        tengah.add(scroll, BorderLayout.CENTER);
        tengah.add(toolbar, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tengah, BorderLayout.CENTER);
    }

    private void loadData() {
        modelTabel.setRowCount(0);
        try {
            Connection conn = koneksi.configDB();
            String sql = "SELECT m.id_menu, m.nama_menu, m.harga, k.nama_kategori " +
                    "FROM menu m JOIN kategori k ON m.id_kategori = k.id_kategori " +
                    "ORDER BY m.id_menu";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                modelTabel.addRow(new Object[]{
                        rs.getInt("id_menu"),
                        rs.getString("nama_menu"),
                        "Rp " + String.format("%,.0f", rs.getDouble("harga")),
                        rs.getString("nama_kategori")
                });
            }
        } catch (SQLException e) {
            lblStatus.setText("Gagal load data: " + e.getMessage());
        }
    }

    private void tambahMenu() {
        String nama  = txtNama.getText().trim();
        String harga = txtHarga.getText().trim();
        String kat   = (String) cmbKategori.getSelectedItem();

        if (nama.isEmpty() || harga.isEmpty()) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Nama dan harga tidak boleh kosong!");
            return;
        }

        try {
            double hargaVal = Double.parseDouble(harga);
            Connection conn = koneksi.configDB();

            // Ambil id_kategori
            PreparedStatement psKat = conn.prepareStatement(
                    "SELECT id_kategori FROM kategori WHERE nama_kategori = ?");
            psKat.setString(1, kat);
            ResultSet rsKat = psKat.executeQuery();
            if (!rsKat.next()) {
                lblStatus.setForeground(new Color(200, 50, 50));
                lblStatus.setText("Kategori tidak ditemukan!");
                return;
            }
            int idKat = rsKat.getInt("id_kategori");

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO menu (nama_menu, harga, id_kategori) VALUES (?, ?, ?)");
            ps.setString(1, nama);
            ps.setDouble(2, hargaVal);
            ps.setInt(3, idKat);
            ps.executeUpdate();

            lblStatus.setForeground(new Color(50, 150, 50));
            lblStatus.setText("Menu berhasil ditambahkan!");
            txtNama.setText("");
            txtHarga.setText("");
            loadData();

        } catch (NumberFormatException ex) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Harga harus berupa angka!");
        } catch (SQLException ex) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Gagal tambah: " + ex.getMessage());
        }
    }

    private void hapusMenu() {
        int baris = tabel.getSelectedRow();
        if (baris == -1) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Pilih menu yang ingin dihapus dulu!");
            return;
        }
        int id   = (int) modelTabel.getValueAt(baris, 0);
        String nama = (String) modelTabel.getValueAt(baris, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin hapus menu \"" + nama + "\"?", "Hapus Menu",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = koneksi.configDB();
                PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM menu WHERE id_menu = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
                lblStatus.setForeground(new Color(50, 150, 50));
                lblStatus.setText("Menu \"" + nama + "\" berhasil dihapus!");
                loadData();
            } catch (SQLException ex) {
                lblStatus.setForeground(new Color(200, 50, 50));
                lblStatus.setText("Gagal hapus: " + ex.getMessage());
            }
        }
    }

    private JButton buatTombol(String teks, Color warna) {
        JButton btn = new JButton(teks) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? warna.darker() :
                        getModel().isRollover() ? warna.brighter() : warna);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(PUTIH);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}