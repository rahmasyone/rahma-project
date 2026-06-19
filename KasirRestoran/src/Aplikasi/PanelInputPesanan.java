package Aplikasi;

import Kelas.koneksi;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class PanelInputPesanan extends JPanel {

    static final Color BIRU_TUA         = new Color(30, 100, 180);
    static final Color BIRU_MUDA        = new Color(100, 170, 240);
    static final Color BIRU_SANGAT_MUDA = new Color(220, 237, 255);
    static final Color PUTIH            = Color.WHITE;
    static final Color ABU_MUDA         = new Color(245, 248, 255);
    static final Color HIJAU            = new Color(52, 168, 83);

    private int idUser;
    private JTextField txtNamaPelanggan, txtTelpPelanggan;
    private JComboBox<String> cmbMenu;
    private JSpinner spnJumlah;
    private JTable tabelKeranjang;
    private DefaultTableModel modelKeranjang;
    private JLabel lblTotal, lblStatus;

    private Map<String, double[]> mapMenu = new LinkedHashMap<>();
    private double totalHarga = 0;

    public PanelInputPesanan(int idUser) {
        this.idUser = idUser;
        setLayout(new BorderLayout());
        setBackground(ABU_MUDA);
        initComponents();
        loadMenu();
    }

    private void initComponents() {
        // ===== HEADER =====
        JPanel header = new JPanel(null);
        header.setBackground(PUTIH);
        header.setPreferredSize(new Dimension(0, 70));

        JLabel lblJudul = new JLabel("Input Pesanan");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
        lblJudul.setForeground(BIRU_TUA);
        lblJudul.setBounds(25, 15, 300, 30);

        JLabel lblSub = new JLabel("Buat pesanan baru untuk pelanggan");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(new Color(120, 120, 120));
        lblSub.setBounds(25, 42, 400, 20);

        header.add(lblJudul);
        header.add(lblSub);

        // ===== PANEL KIRI =====
        JPanel kiri = new JPanel(null) {
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
        kiri.setOpaque(false);
        kiri.setPreferredSize(new Dimension(320, 0));

        JLabel lblDataPelanggan = new JLabel("Data Pelanggan");
        lblDataPelanggan.setFont(new Font("Arial", Font.BOLD, 13));
        lblDataPelanggan.setForeground(BIRU_TUA);
        lblDataPelanggan.setBounds(15, 15, 200, 20);

        JLabel lblNama = new JLabel("Nama Pelanggan");
        lblNama.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNama.setForeground(new Color(80, 80, 80));
        lblNama.setBounds(15, 42, 150, 18);

        txtNamaPelanggan = new JTextField();
        txtNamaPelanggan.setBounds(15, 62, 280, 32);
        txtNamaPelanggan.setBorder(BorderFactory.createCompoundBorder(
                new FormLogin.RoundBorder(BIRU_MUDA, 8),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        txtNamaPelanggan.setBackground(BIRU_SANGAT_MUDA);
        txtNamaPelanggan.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblTelp = new JLabel("No. Telepon");
        lblTelp.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTelp.setForeground(new Color(80, 80, 80));
        lblTelp.setBounds(15, 100, 150, 18);

        txtTelpPelanggan = new JTextField();
        txtTelpPelanggan.setBounds(15, 120, 280, 32);
        txtTelpPelanggan.setBorder(BorderFactory.createCompoundBorder(
                new FormLogin.RoundBorder(BIRU_MUDA, 8),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        txtTelpPelanggan.setBackground(BIRU_SANGAT_MUDA);
        txtTelpPelanggan.setFont(new Font("Arial", Font.PLAIN, 12));

        JSeparator sep1 = new JSeparator();
        sep1.setForeground(new Color(220, 235, 255));
        sep1.setBounds(15, 165, 280, 1);

        JLabel lblPilihMenu = new JLabel("Pilih Menu");
        lblPilihMenu.setFont(new Font("Arial", Font.BOLD, 13));
        lblPilihMenu.setForeground(BIRU_TUA);
        lblPilihMenu.setBounds(15, 175, 200, 20);

        cmbMenu = new JComboBox<>();
        cmbMenu.setBounds(15, 198, 280, 32);
        cmbMenu.setBackground(BIRU_SANGAT_MUDA);
        cmbMenu.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblJumlah = new JLabel("Jumlah");
        lblJumlah.setFont(new Font("Arial", Font.BOLD, 13));
        lblJumlah.setForeground(BIRU_TUA);
        lblJumlah.setBounds(15, 242, 100, 20);

        spnJumlah = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spnJumlah.setBounds(15, 265, 100, 32);
        spnJumlah.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton btnTambahItem = buatTombol("+ Tambah ke Keranjang", BIRU_TUA);
        btnTambahItem.setBounds(15, 312, 280, 36);
        btnTambahItem.addActionListener(e -> tambahKeKeranjang());

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(220, 235, 255));
        sep2.setBounds(15, 360, 280, 1);

        JLabel lblTotalLabel = new JLabel("Total Harga:");
        lblTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalLabel.setForeground(BIRU_TUA);
        lblTotalLabel.setBounds(15, 370, 150, 25);

        lblTotal = new JLabel("Rp 0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(HIJAU);
        lblTotal.setBounds(15, 395, 280, 30);

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setBounds(15, 430, 280, 20);

        JButton btnSimpan = buatTombol("Simpan & Bayar", HIJAU);
        btnSimpan.setBounds(15, 455, 280, 38);
        btnSimpan.addActionListener(e -> simpanPesanan());

        JButton btnBatal = buatTombol("Batal / Reset", new Color(180, 180, 180));
        btnBatal.setBounds(15, 502, 280, 36);
        btnBatal.addActionListener(e -> resetForm());

        kiri.add(lblDataPelanggan);
        kiri.add(lblNama);
        kiri.add(txtNamaPelanggan);
        kiri.add(lblTelp);
        kiri.add(txtTelpPelanggan);
        kiri.add(sep1);
        kiri.add(lblPilihMenu);
        kiri.add(cmbMenu);
        kiri.add(lblJumlah);
        kiri.add(spnJumlah);
        kiri.add(btnTambahItem);
        kiri.add(sep2);
        kiri.add(lblTotalLabel);
        kiri.add(lblTotal);
        kiri.add(lblStatus);
        kiri.add(btnSimpan);
        kiri.add(btnBatal);

        // ===== PANEL KANAN =====
        JPanel kanan = new JPanel(new BorderLayout(0, 8));
        kanan.setOpaque(false);

        JLabel lblKeranjang = new JLabel("Keranjang Pesanan");
        lblKeranjang.setFont(new Font("Arial", Font.BOLD, 14));
        lblKeranjang.setForeground(BIRU_TUA);
        lblKeranjang.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

        String[] kolom = {"Menu", "Harga", "Jumlah", "Subtotal"};
        modelKeranjang = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabelKeranjang = new JTable(modelKeranjang);
        tabelKeranjang.setFont(new Font("Arial", Font.PLAIN, 13));
        tabelKeranjang.setRowHeight(35);
        tabelKeranjang.setShowVerticalLines(false);
        tabelKeranjang.setGridColor(new Color(220, 230, 245));
        tabelKeranjang.setSelectionBackground(BIRU_SANGAT_MUDA);
        tabelKeranjang.setBackground(PUTIH);

        JTableHeader th = tabelKeranjang.getTableHeader();
        th.setBackground(BIRU_TUA);
        th.setForeground(PUTIH);
        th.setFont(new Font("Arial", Font.BOLD, 13));
        th.setReorderingAllowed(false);
        th.setPreferredSize(new Dimension(0, 38));

        tabelKeranjang.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabelKeranjang.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabelKeranjang.getColumnModel().getColumn(2).setPreferredWidth(60);
        tabelKeranjang.getColumnModel().getColumn(3).setPreferredWidth(110);

        tabelKeranjang.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                           boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(col == 0 ? LEFT : CENTER);
                if (sel) {
                    setBackground(BIRU_SANGAT_MUDA);
                    setForeground(BIRU_TUA);
                } else {
                    setBackground(row % 2 == 0 ? PUTIH : new Color(240, 247, 255));
                    setForeground(new Color(50, 50, 50));
                }
                if ((col == 1 || col == 3) && v instanceof Double) {
                    setText("Rp " + String.format("%,.0f", (Double) v));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(tabelKeranjang);
        scroll.setBorder(BorderFactory.createLineBorder(BIRU_MUDA, 1));
        scroll.getViewport().setBackground(PUTIH);

        JButton btnHapusItem = buatTombol("Hapus Item", new Color(220, 60, 60));
        btnHapusItem.setPreferredSize(new Dimension(120, 32));
        btnHapusItem.addActionListener(e -> hapusItemKeranjang());

        JPanel toolbarKanan = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolbarKanan.setOpaque(false);
        toolbarKanan.add(btnHapusItem);

        kanan.add(lblKeranjang, BorderLayout.NORTH);
        kanan.add(scroll, BorderLayout.CENTER);
        kanan.add(toolbarKanan, BorderLayout.SOUTH);

        JPanel tengah = new JPanel(new BorderLayout(15, 0));
        tengah.setBackground(ABU_MUDA);
        tengah.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        tengah.add(kiri, BorderLayout.WEST);
        tengah.add(kanan, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(tengah, BorderLayout.CENTER);
    }

    private void loadMenu() {
        mapMenu.clear();
        cmbMenu.removeAllItems();
        try {
            Connection conn = koneksi.configDB();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT id_menu, nama_menu, harga FROM menu ORDER BY nama_menu");
            while (rs.next()) {
                String nama = rs.getString("nama_menu");
                mapMenu.put(nama, new double[]{
                        rs.getDouble("harga"),
                        rs.getInt("id_menu")
                });
                cmbMenu.addItem(nama);
            }
        } catch (SQLException e) {
            lblStatus.setText("Gagal load menu!");
        }
    }

    private void tambahKeKeranjang() {
        String namaMenu = (String) cmbMenu.getSelectedItem();
        if (namaMenu == null) return;

        double[] info   = mapMenu.get(namaMenu);
        double harga    = info[0];
        int jumlah      = (int) spnJumlah.getValue();
        double subtotal = harga * jumlah;

        for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
            if (modelKeranjang.getValueAt(i, 0).equals(namaMenu)) {
                int jmlLama    = (int) modelKeranjang.getValueAt(i, 2);
                int jmlBaru    = jmlLama + jumlah;
                double subBaru = harga * jmlBaru;
                modelKeranjang.setValueAt(jmlBaru, i, 2);
                modelKeranjang.setValueAt(subBaru, i, 3);
                hitungTotal();
                lblStatus.setForeground(BIRU_TUA);
                lblStatus.setText("Jumlah " + namaMenu + " diupdate!");
                return;
            }
        }

        modelKeranjang.addRow(new Object[]{namaMenu, harga, jumlah, subtotal});
        hitungTotal();
        lblStatus.setForeground(BIRU_TUA);
        lblStatus.setText(namaMenu + " ditambahkan ke keranjang!");
    }

    private void hapusItemKeranjang() {
        int baris = tabelKeranjang.getSelectedRow();
        if (baris == -1) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Pilih item yang ingin dihapus dulu!");
            return;
        }
        modelKeranjang.removeRow(baris);
        hitungTotal();
        lblStatus.setForeground(new Color(200, 50, 50));
        lblStatus.setText("Item dihapus dari keranjang.");
    }

    private void hitungTotal() {
        totalHarga = 0;
        for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
            totalHarga += (double) modelKeranjang.getValueAt(i, 3);
        }
        lblTotal.setText("Rp " + String.format("%,.0f", totalHarga));
    }

    private void simpanPesanan() {
        String nama = txtNamaPelanggan.getText().trim();
        String telp = txtTelpPelanggan.getText().trim();

        if (nama.isEmpty()) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Nama pelanggan tidak boleh kosong!");
            return;
        }
        if (modelKeranjang.getRowCount() == 0) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Keranjang masih kosong!");
            return;
        }

        try {
            Connection conn = koneksi.configDB();
            conn.setAutoCommit(false);

            PreparedStatement psCek = conn.prepareStatement(
                    "SELECT id_pelanggan FROM pelanggan WHERE nama_pelanggan = ?");
            psCek.setString(1, nama);
            ResultSet rsCek = psCek.executeQuery();

            int idPelanggan;
            if (rsCek.next()) {
                idPelanggan = rsCek.getInt("id_pelanggan");
            } else {
                PreparedStatement psAddPel = conn.prepareStatement(
                        "INSERT INTO pelanggan (nama_pelanggan, no_telepon) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                psAddPel.setString(1, nama);
                psAddPel.setString(2, telp);
                psAddPel.executeUpdate();
                ResultSet rsNewPel = psAddPel.getGeneratedKeys();
                rsNewPel.next();
                idPelanggan = rsNewPel.getInt(1);
            }

            PreparedStatement psPesanan = conn.prepareStatement(
                    "INSERT INTO pesanan (tanggal, id_user, id_pelanggan, total_harga) VALUES (NOW(), ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            psPesanan.setInt(1, idUser);
            psPesanan.setInt(2, idPelanggan);
            psPesanan.setDouble(3, totalHarga);
            psPesanan.executeUpdate();

            ResultSet rsKey = psPesanan.getGeneratedKeys();
            rsKey.next();
            int idPesanan = rsKey.getInt(1);

            for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                String namaMenu = modelKeranjang.getValueAt(i, 0).toString();
                int jumlah      = (int) modelKeranjang.getValueAt(i, 2);
                double subtotal = (double) modelKeranjang.getValueAt(i, 3);
                int idMenu      = (int) mapMenu.get(namaMenu)[1];

                PreparedStatement psDetail = conn.prepareStatement(
                        "INSERT INTO detail_pesanan (id_pesanan, id_menu, jumlah, subtotal) VALUES (?, ?, ?, ?)");
                psDetail.setInt(1, idPesanan);
                psDetail.setInt(2, idMenu);
                psDetail.setInt(3, jumlah);
                psDetail.setDouble(4, subtotal);
                psDetail.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);

            lblStatus.setForeground(HIJAU);
            lblStatus.setText("Pesanan disimpan! Lanjut pembayaran...");

            List<String[]> itemKeranjang = new ArrayList<>();
            for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                itemKeranjang.add(new String[]{
                        modelKeranjang.getValueAt(i, 0).toString(),
                        String.valueOf(modelKeranjang.getValueAt(i, 1)),
                        modelKeranjang.getValueAt(i, 2).toString(),
                        String.valueOf(modelKeranjang.getValueAt(i, 3))
                });
            }

            tampilDialogBayar(conn, idPesanan, totalHarga, nama, itemKeranjang);

        } catch (SQLException ex) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Gagal simpan: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void tampilDialogBayar(Connection conn, int idPesanan, double total,
                                   String namaPelanggan, List<String[]> items) {
        JDialog dialog = new JDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), "Pembayaran", true);
        dialog.setSize(450, 490);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(PUTIH);

        // Judul
        JLabel lblJudul = new JLabel("Pembayaran Pesanan");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 16));
        lblJudul.setForeground(BIRU_TUA);
        lblJudul.setBounds(20, 15, 400, 28);

        JPanel garis = new JPanel();
        garis.setBackground(BIRU_MUDA);
        garis.setBounds(20, 48, 400, 1);

        // Total
        JLabel lblTotalLabel = new JLabel("Total Harga:");
        lblTotalLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTotalLabel.setForeground(new Color(80, 80, 80));
        lblTotalLabel.setBounds(20, 60, 150, 22);

        JLabel lblTotalNilai = new JLabel("Rp " + String.format("%,.0f", total));
        lblTotalNilai.setFont(new Font("Arial", Font.BOLD, 15));
        lblTotalNilai.setForeground(HIJAU);
        lblTotalNilai.setBounds(175, 60, 260, 22);

        // Metode
        JLabel lblMetode = new JLabel("Metode Bayar:");
        lblMetode.setFont(new Font("Arial", Font.PLAIN, 13));
        lblMetode.setForeground(new Color(80, 80, 80));
        lblMetode.setBounds(20, 95, 150, 22);

        JRadioButton rbCash     = new JRadioButton("Cash");
        JRadioButton rbTransfer = new JRadioButton("Transfer");
        rbCash.setBounds(175, 93, 80, 26);
        rbTransfer.setBounds(265, 93, 100, 26);
        rbCash.setBackground(PUTIH);
        rbTransfer.setBackground(PUTIH);
        rbCash.setFont(new Font("Arial", Font.PLAIN, 13));
        rbTransfer.setFont(new Font("Arial", Font.PLAIN, 13));
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbCash);
        bg.add(rbTransfer);
        rbCash.setSelected(true);

        // Pilih bank
        JLabel lblBank = new JLabel("Tujuan Transfer:");
        lblBank.setFont(new Font("Arial", Font.PLAIN, 13));
        lblBank.setForeground(new Color(80, 80, 80));
        lblBank.setBounds(20, 130, 150, 22);
        lblBank.setVisible(false);

        JComboBox<String> cmbBank = new JComboBox<>(new String[]{
                "BCA", "Mandiri", "BRI", "QRIS", "E-Wallet (GoPay/OVO/Dana)"
        });
        cmbBank.setBounds(175, 128, 240, 28);
        cmbBank.setBackground(BIRU_SANGAT_MUDA);
        cmbBank.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbBank.setVisible(false);

        // Uang Bayar
        JLabel lblBayar = new JLabel("Uang Bayar:");
        lblBayar.setFont(new Font("Arial", Font.PLAIN, 13));
        lblBayar.setForeground(new Color(80, 80, 80));
        lblBayar.setBounds(20, 130, 150, 22);

        JTextField txtBayar = new JTextField();
        txtBayar.setBounds(175, 128, 240, 30);
        txtBayar.setBorder(BorderFactory.createCompoundBorder(
                new FormLogin.RoundBorder(BIRU_MUDA, 8),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        txtBayar.setBackground(BIRU_SANGAT_MUDA);
        txtBayar.setFont(new Font("Arial", Font.PLAIN, 12));

        // Kembalian
        JLabel lblKembaliLabel = new JLabel("Kembalian:");
        lblKembaliLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        lblKembaliLabel.setForeground(new Color(80, 80, 80));
        lblKembaliLabel.setBounds(20, 173, 150, 22);

        JLabel lblKembaliNilai = new JLabel("Rp 0");
        lblKembaliNilai.setFont(new Font("Arial", Font.BOLD, 15));
        lblKembaliNilai.setForeground(BIRU_TUA);
        lblKembaliNilai.setBounds(175, 173, 240, 22);

        // Panel QRIS
        JPanel qrisPanel = new JPanel(null);
        qrisPanel.setBackground(PUTIH);
        qrisPanel.setBounds(20, 162, 400, 230);
        qrisPanel.setVisible(false);

        JLabel lblQrisJudul = new JLabel("Scan QRIS untuk Pembayaran", SwingConstants.CENTER);
        lblQrisJudul.setFont(new Font("Arial", Font.BOLD, 13));
        lblQrisJudul.setForeground(BIRU_TUA);
        lblQrisJudul.setBounds(0, 0, 400, 25);

        JLabel lblQrisTotal = new JLabel(
                "Total: Rp " + String.format("%,.0f", total), SwingConstants.CENTER);
        lblQrisTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblQrisTotal.setForeground(HIJAU);
        lblQrisTotal.setBounds(0, 28, 400, 25);

        // Load gambar QR
        JLabel lblQR = new JLabel("", SwingConstants.CENTER);
        try {
            java.net.URL imgUrl = getClass().getResource("/Gambar/qris.png");
            if (imgUrl != null) {
                ImageIcon icon   = new ImageIcon(imgUrl);
                Image scaled     = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
                lblQR.setIcon(new ImageIcon(scaled));
            } else {
                lblQR.setText("File qris.png tidak ditemukan di folder Gambar!");
                lblQR.setForeground(Color.RED);
                lblQR.setFont(new Font("Arial", Font.PLAIN, 11));
            }
        } catch (Exception ex) {
            lblQR.setText("Gagal load QR!");
            lblQR.setForeground(Color.RED);
        }
        lblQR.setBounds(120, 58, 160, 165);

        qrisPanel.add(lblQrisJudul);
        qrisPanel.add(lblQrisTotal);
        qrisPanel.add(lblQR);

        // Hitung kembalian otomatis
        txtBayar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    double bayar     = Double.parseDouble(txtBayar.getText().trim());
                    double kembalian = bayar - total;
                    if (kembalian >= 0) {
                        lblKembaliNilai.setForeground(BIRU_TUA);
                        lblKembaliNilai.setText("Rp " + String.format("%,.0f", kembalian));
                    } else {
                        lblKembaliNilai.setForeground(new Color(200, 50, 50));
                        lblKembaliNilai.setText("Uang kurang!");
                    }
                } catch (NumberFormatException ex) {
                    lblKembaliNilai.setText("Rp 0");
                }
            }
        });

        // Toggle saat pilih bank
        cmbBank.addActionListener(e -> {
            boolean isQris = "QRIS".equals(cmbBank.getSelectedItem());
            qrisPanel.setVisible(isQris);
            dialog.setSize(450, isQris ? 590 : 490);
            dialog.revalidate();
            dialog.repaint();
        });

        // Toggle Cash/Transfer
        rbTransfer.addActionListener(e -> {
            lblBayar.setVisible(false);
            txtBayar.setVisible(false);
            lblKembaliLabel.setVisible(false);
            lblKembaliNilai.setVisible(false);
            lblBank.setVisible(true);
            cmbBank.setVisible(true);
            qrisPanel.setVisible(false);
        });
        rbCash.addActionListener(e -> {
            lblBayar.setVisible(true);
            txtBayar.setVisible(true);
            lblKembaliLabel.setVisible(true);
            lblKembaliNilai.setVisible(true);
            lblBank.setVisible(false);
            cmbBank.setVisible(false);
            qrisPanel.setVisible(false);
            dialog.setSize(450, 490);
        });

        JLabel lblStatusDialog = new JLabel(" ");
        lblStatusDialog.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatusDialog.setForeground(new Color(200, 50, 50));
        lblStatusDialog.setBounds(20, 210, 400, 20);

        JButton btnKonfirmasi = buatTombol("Konfirmasi & Cetak Struk", HIJAU);
        btnKonfirmasi.setBounds(20, 240, 400, 42);

        JButton btnBatalDialog = buatTombol("Batal", new Color(180, 180, 180));
        btnBatalDialog.setBounds(20, 292, 400, 36);
        btnBatalDialog.addActionListener(e -> dialog.dispose());

        btnKonfirmasi.addActionListener(e -> {
            String metode      = rbCash.isSelected() ? "Cash" :
                    (String) cmbBank.getSelectedItem();
            double jumlahBayar = total;
            double kembalian   = 0;

            if (rbCash.isSelected()) {
                try {
                    jumlahBayar = Double.parseDouble(txtBayar.getText().trim());
                    kembalian   = jumlahBayar - total;
                    if (kembalian < 0) {
                        lblStatusDialog.setText("Uang bayar kurang dari total harga!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    lblStatusDialog.setText("Masukkan jumlah uang bayar!");
                    return;
                }
            }

            final double kembalianFinal = kembalian;
            final double bayarFinal     = jumlahBayar;
            final String metodeFinal    = metode;

            try {
                String metodeDB = rbCash.isSelected() ? "cash" : "transfer";
                PreparedStatement psBayar = conn.prepareStatement(
                        "INSERT INTO pembayaran (id_pesanan, metode, jumlah_bayar, kembalian, tanggal) " +
                                "VALUES (?, ?, ?, ?, NOW())");
                psBayar.setInt(1, idPesanan);
                psBayar.setString(2, metodeDB);
                psBayar.setDouble(3, bayarFinal);
                psBayar.setDouble(4, kembalianFinal);
                psBayar.executeUpdate();

                dialog.dispose();

                cetakStruk(idPesanan, namaPelanggan, items,
                        total, metodeFinal, bayarFinal, kembalianFinal);

                resetForm();

            } catch (SQLException ex) {
                lblStatusDialog.setText("Gagal simpan: " + ex.getMessage());
            }
        });

        dialog.add(lblJudul);
        dialog.add(garis);
        dialog.add(lblTotalLabel);
        dialog.add(lblTotalNilai);
        dialog.add(lblMetode);
        dialog.add(rbCash);
        dialog.add(rbTransfer);
        dialog.add(lblBank);
        dialog.add(cmbBank);
        dialog.add(lblBayar);
        dialog.add(txtBayar);
        dialog.add(lblKembaliLabel);
        dialog.add(lblKembaliNilai);
        dialog.add(qrisPanel);
        dialog.add(lblStatusDialog);
        dialog.add(btnKonfirmasi);
        dialog.add(btnBatalDialog);

        dialog.setVisible(true);
    }

    private void cetakStruk(int idPesanan, String namaPelanggan,
                            List<String[]> items, double total,
                            String metode, double bayar, double kembalian) {

        StringBuilder sb = new StringBuilder();
        sb.append("================================\n");
        sb.append("      KASIR RESTORAN RRR        \n");
        sb.append("================================\n");
        sb.append("No. Pesanan  : #").append(idPesanan).append("\n");
        sb.append("Pelanggan    : ").append(namaPelanggan).append("\n");
        sb.append("--------------------------------\n");
        for (String[] item : items) {
            String nama     = item[0];
            double harga    = Double.parseDouble(item[1]);
            int jumlah      = Integer.parseInt(item[2]);
            double subtotal = Double.parseDouble(item[3]);
            sb.append(String.format("%-18s x%d\n", nama, jumlah));
            sb.append(String.format("  Rp%,.0f x %d = Rp%,.0f\n", harga, jumlah, subtotal));
        }
        sb.append("--------------------------------\n");
        sb.append(String.format("Total        : Rp %,.0f\n", total));
        sb.append(String.format("Metode       : %s\n", metode));
        sb.append(String.format("Bayar        : Rp %,.0f\n", bayar));
        sb.append(String.format("Kembalian    : Rp %,.0f\n", kembalian));
        sb.append("================================\n");
        sb.append("        Terima kasih!           \n");
        sb.append("================================\n");

        String strukText = sb.toString();

        // Preview struk
        JTextArea preview = new JTextArea(strukText);
        preview.setFont(new Font("Monospaced", Font.PLAIN, 12));
        preview.setEditable(false);
        preview.setBackground(new Color(250, 250, 250));

        JScrollPane scrollPreview = new JScrollPane(preview);
        scrollPreview.setPreferredSize(new Dimension(400, 300));

        int pilihan = JOptionPane.showConfirmDialog(this,
                scrollPreview, "Preview Struk - Cetak?",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (pilihan == JOptionPane.YES_OPTION) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Struk #" + idPesanan);

            job.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
                Graphics2D g2 = (Graphics2D) graphics;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
                g2.setColor(Color.BLACK);
                String[] baris = strukText.split("\n");
                int y = 20;
                for (String line : baris) {
                    g2.drawString(line, 10, y);
                    y += 15;
                }
                return Printable.PAGE_EXISTS;
            });

            if (job.printDialog()) {
                try {
                    job.print();
                    JOptionPane.showMessageDialog(this,
                            "Struk berhasil dicetak!",
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Gagal cetak: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void resetForm() {
        modelKeranjang.setRowCount(0);
        totalHarga = 0;
        lblTotal.setText("Rp 0");
        lblStatus.setText(" ");
        spnJumlah.setValue(1);
        txtNamaPelanggan.setText("");
        txtTelpPelanggan.setText("");
        if (cmbMenu.getItemCount() > 0) cmbMenu.setSelectedIndex(0);
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