package Aplikasi;

import Kelas.koneksi;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PanelUser extends JPanel {

    static final Color BIRU_TUA         = new Color(30, 100, 180);
    static final Color BIRU_MUDA        = new Color(100, 170, 240);
    static final Color BIRU_SANGAT_MUDA = new Color(220, 237, 255);
    static final Color PUTIH            = Color.WHITE;
    static final Color ABU_MUDA         = new Color(245, 248, 255);

    private JTable tabel;
    private DefaultTableModel modelTabel;
    private JTextField txtUsername, txtPassword;
    private JComboBox<String> cmbRole;
    private JButton btnTambah, btnHapus, btnRefresh;
    private JLabel lblStatus;

    public PanelUser() {
        setLayout(new BorderLayout());
        setBackground(ABU_MUDA);
        initComponents();
        loadData();
    }

    private void initComponents() {
        // ===== HEADER =====
        JPanel header = new JPanel(null);
        header.setBackground(PUTIH);
        header.setPreferredSize(new Dimension(0, 70));

        JLabel lblJudul = new JLabel("Kelola User");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
        lblJudul.setForeground(BIRU_TUA);
        lblJudul.setBounds(25, 15, 300, 30);

        JLabel lblSub = new JLabel("Tambah, lihat, dan hapus data user/kasir");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(new Color(120, 120, 120));
        lblSub.setBounds(25, 42, 400, 20);

        header.add(lblJudul);
        header.add(lblSub);

        // ===== FORM TAMBAH (layout 2 baris) =====
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
        formPanel.setPreferredSize(new Dimension(0, 160));

        JLabel lblFormJudul = new JLabel("Tambah User Baru");
        lblFormJudul.setFont(new Font("Arial", Font.BOLD, 13));
        lblFormJudul.setForeground(BIRU_TUA);
        lblFormJudul.setBounds(20, 12, 200, 22);

        // --- Baris 1: Username ---
        JLabel lblU = new JLabel("Username");
        lblU.setFont(new Font("Arial", Font.BOLD, 12));
        lblU.setForeground(new Color(80, 80, 80));
        lblU.setBounds(20, 42, 80, 18);

        txtUsername = new JTextField();
        txtUsername.setBounds(20, 62, 180, 30);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                new FormLogin.RoundBorder(BIRU_MUDA, 8),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        txtUsername.setBackground(BIRU_SANGAT_MUDA);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 12));

        // --- Baris 1: Password ---
        JLabel lblP = new JLabel("Password");
        lblP.setFont(new Font("Arial", Font.BOLD, 12));
        lblP.setForeground(new Color(80, 80, 80));
        lblP.setBounds(220, 42, 80, 18);

        txtPassword = new JTextField();
        txtPassword.setBounds(220, 62, 180, 30);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new FormLogin.RoundBorder(BIRU_MUDA, 8),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        txtPassword.setBackground(BIRU_SANGAT_MUDA);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));

        // --- Baris 1: Role ---
        JLabel lblR = new JLabel("Role");
        lblR.setFont(new Font("Arial", Font.BOLD, 12));
        lblR.setForeground(new Color(80, 80, 80));
        lblR.setBounds(420, 42, 50, 18);

        cmbRole = new JComboBox<>(new String[]{"kasir", "admin"});
        cmbRole.setBounds(420, 62, 120, 30);
        cmbRole.setBackground(BIRU_SANGAT_MUDA);
        cmbRole.setFont(new Font("Arial", Font.PLAIN, 12));

        // --- Tombol Tambah ---
        btnTambah = buatTombol("+ Tambah", new Color(52, 152, 219));
        btnTambah.setBounds(560, 62, 120, 30);
        btnTambah.addActionListener(e -> tambahUser());

        // --- Status ---
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setBounds(20, 105, 600, 20);

        formPanel.add(lblFormJudul);
        formPanel.add(lblU);
        formPanel.add(txtUsername);
        formPanel.add(lblP);
        formPanel.add(txtPassword);
        formPanel.add(lblR);
        formPanel.add(cmbRole);
        formPanel.add(btnTambah);
        formPanel.add(lblStatus);

        // ===== TABEL =====
        String[] kolom = {"ID", "Username", "Role"};
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

        tabel.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabel.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabel.getColumnModel().getColumn(2).setPreferredWidth(120);

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

        // ===== TOOLBAR BAWAH =====
        JPanel toolbar = new JPanel(null);
        toolbar.setBackground(PUTIH);
        toolbar.setPreferredSize(new Dimension(0, 50));

        btnHapus = buatTombol("Hapus User", new Color(220, 60, 60));
        btnHapus.setBounds(15, 10, 120, 30);
        btnHapus.addActionListener(e -> hapusUser());

        btnRefresh = buatTombol("Refresh", new Color(80, 170, 80));
        btnRefresh.setBounds(145, 10, 90, 30);
        btnRefresh.addActionListener(e -> loadData());

        toolbar.add(btnHapus);
        toolbar.add(btnRefresh);

        // ===== SUSUN LAYOUT =====
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
            String sql = "SELECT id_user, username, role FROM users ORDER BY id_user";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                modelTabel.addRow(new Object[]{
                        rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("role")
                });
            }
        } catch (SQLException e) {
            lblStatus.setText("Gagal load data: " + e.getMessage());
        }
    }

    private void tambahUser() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String role = (String) cmbRole.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Username dan password tidak boleh kosong!");
            return;
        }

        try {
            Connection conn = koneksi.configDB();
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();

            lblStatus.setForeground(new Color(50, 150, 50));
            lblStatus.setText("User berhasil ditambahkan!");
            txtUsername.setText("");
            txtPassword.setText("");
            loadData();

        } catch (SQLException e) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Gagal tambah: " + e.getMessage());
        }
    }

    private void hapusUser() {
        int baris = tabel.getSelectedRow();
        if (baris == -1) {
            lblStatus.setForeground(new Color(200, 50, 50));
            lblStatus.setText("Pilih user yang ingin dihapus dulu!");
            return;
        }

        int id = (int) modelTabel.getValueAt(baris, 0);
        String nama = (String) modelTabel.getValueAt(baris, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin hapus user \"" + nama + "\"?", "Hapus User",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = koneksi.configDB();
                String sql = "DELETE FROM users WHERE id_user = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();

                lblStatus.setForeground(new Color(50, 150, 50));
                lblStatus.setText("User \"" + nama + "\" berhasil dihapus!");
                loadData();

            } catch (SQLException e) {
                lblStatus.setForeground(new Color(200, 50, 50));
                lblStatus.setText("Gagal hapus: " + e.getMessage());
            }
        }
    }

    private JButton buatTombol(String teks, Color warna) {
        JButton btn = new JButton(teks) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(warna.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(warna.brighter());
                } else {
                    g2.setColor(warna);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(PUTIH);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}