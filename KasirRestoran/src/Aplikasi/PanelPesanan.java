package Aplikasi;

import Kelas.koneksi;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class PanelPesanan extends JPanel {

    static final Color BIRU_TUA         = new Color(30, 100, 180);
    static final Color BIRU_MUDA        = new Color(100, 170, 240);
    static final Color BIRU_SANGAT_MUDA = new Color(220, 237, 255);
    static final Color PUTIH            = Color.WHITE;
    static final Color ABU_MUDA         = new Color(245, 248, 255);

    private JTable tabel;
    private DefaultTableModel modelTabel;
    private JLabel lblStatus;

    public PanelPesanan() {
        setLayout(new BorderLayout());
        setBackground(ABU_MUDA);
        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel header = new JPanel(null);
        header.setBackground(PUTIH);
        header.setPreferredSize(new Dimension(0, 70));

        JLabel lblJudul = new JLabel("Riwayat Pesanan");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
        lblJudul.setForeground(BIRU_TUA);
        lblJudul.setBounds(25, 15, 300, 30);

        JLabel lblSub = new JLabel("Lihat semua riwayat transaksi pesanan");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(new Color(120, 120, 120));
        lblSub.setBounds(25, 42, 400, 20);

        header.add(lblJudul);
        header.add(lblSub);

        String[] kolom = {"ID", "Tanggal", "Kasir", "Pelanggan", "Total Harga"};
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
        tabel.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabel.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabel.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabel.getColumnModel().getColumn(4).setPreferredWidth(130);

        tabel.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                           boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(col == 0 || col == 4 ? CENTER : LEFT);
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

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(200, 50, 50));

        JPanel toolbar = new JPanel(null);
        toolbar.setBackground(PUTIH);
        toolbar.setPreferredSize(new Dimension(0, 50));

        JButton btnRefresh = buatTombol("Refresh", new Color(80, 170, 80));
        btnRefresh.setBounds(15, 10, 90, 30);
        btnRefresh.addActionListener(e -> loadData());
        toolbar.add(btnRefresh);

        JPanel tengah = new JPanel(new BorderLayout(0, 10));
        tengah.setBackground(ABU_MUDA);
        tengah.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        tengah.add(scroll, BorderLayout.CENTER);
        tengah.add(toolbar, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tengah, BorderLayout.CENTER);
    }

    private void loadData() {
        modelTabel.setRowCount(0);
        try {
            Connection conn = koneksi.configDB();
            String sql = "SELECT p.id_pesanan, p.tanggal, u.username, pl.nama_pelanggan, p.total_harga " +
                    "FROM pesanan p " +
                    "JOIN users u ON p.id_user = u.id_user " +
                    "JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan " +
                    "ORDER BY p.tanggal DESC";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                modelTabel.addRow(new Object[]{
                        rs.getInt("id_pesanan"),
                        rs.getString("tanggal"),
                        rs.getString("username"),
                        rs.getString("nama_pelanggan"),
                        "Rp " + String.format("%,.0f", rs.getDouble("total_harga"))
                });
            }
        } catch (SQLException e) {
            lblStatus.setText("Gagal load data: " + e.getMessage());
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