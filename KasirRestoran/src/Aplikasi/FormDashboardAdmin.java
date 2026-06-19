package Aplikasi;

import Kelas.koneksi;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormDashboardAdmin extends JFrame {

    private int idUser;
    private String username;
    private JPanel contentArea;

    static final Color BIRU_TUA         = new Color(30, 100, 180);
    static final Color BIRU_MUDA        = new Color(100, 170, 240);
    static final Color BIRU_SANGAT_MUDA = new Color(220, 237, 255);
    static final Color PUTIH            = Color.WHITE;
    static final Color ABU_MUDA         = new Color(245, 248, 255);

    public FormDashboardAdmin(int idUser, String username) {
        this.idUser = idUser;
        this.username = username;
        setTitle("Dashboard Admin - " + username);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ABU_MUDA);

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, BIRU_TUA, 0, getHeight(), BIRU_MUDA);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(210, 0));

        JPanel logoSidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2;
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillOval(cx - 25, 5, 50, 50);
                g2.setColor(PUTIH);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx - 8, 14, cx - 8, 45);
                g2.drawLine(cx - 11, 14, cx - 11, 22);
                g2.drawLine(cx - 8,  14, cx - 8,  22);
                g2.drawLine(cx - 5,  14, cx - 5,  22);
                g2.drawLine(cx - 11, 22, cx - 5,  22);
                g2.drawLine(cx + 8, 28, cx + 8, 45);
                g2.drawOval(cx + 3, 14, 10, 14);
            }
        };
        logoSidebar.setOpaque(false);
        logoSidebar.setBounds(0, 15, 210, 60);

        JLabel lblApp = new JLabel("KasirApp", SwingConstants.CENTER);
        lblApp.setFont(new Font("Arial", Font.BOLD, 15));
        lblApp.setForeground(PUTIH);
        lblApp.setBounds(0, 75, 210, 25);

        JPanel garis1 = buatGaris();
        garis1.setBounds(20, 108, 170, 1);

        JLabel lblUserSidebar = new JLabel(username, SwingConstants.CENTER);
        lblUserSidebar.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUserSidebar.setForeground(new Color(200, 225, 255));
        lblUserSidebar.setBounds(0, 115, 210, 20);

        JLabel lblRole = new JLabel("[ ADMIN ]", SwingConstants.CENTER);
        lblRole.setFont(new Font("Arial", Font.BOLD, 11));
        lblRole.setForeground(new Color(150, 210, 255));
        lblRole.setBounds(0, 133, 210, 18);

        JPanel garis2 = buatGaris();
        garis2.setBounds(20, 158, 170, 1);

        JLabel lblMenuLabel = new JLabel("MENU", SwingConstants.LEFT);
        lblMenuLabel.setFont(new Font("Arial", Font.BOLD, 10));
        lblMenuLabel.setForeground(new Color(180, 210, 255));
        lblMenuLabel.setBounds(20, 165, 170, 20);

        contentArea = new JPanel(new CardLayout());

        String[] namaMenu = {"Dashboard", "Kelola User", "Kelola Menu", "Pelanggan", "Pesanan"};
        for (int i = 0; i < namaMenu.length; i++) {
            final int idx = i;
            JButton btn = buatTombolSidebar(namaMenu[i], i);
            btn.setBounds(10, 190 + i * 48, 190, 40);
            btn.addActionListener(e -> {
                CardLayout cl = (CardLayout) contentArea.getLayout();
                cl.show(contentArea, String.valueOf(idx));
            });
            sidebar.add(btn);
        }

        JPanel garis3 = buatGaris();
        garis3.setBounds(20, 440, 170, 1);

        JButton btnLogout = buatTombolLogout();
        btnLogout.setBounds(10, 450, 190, 40);
        btnLogout.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Yakin mau logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                this.dispose();
                new FormLogin().setVisible(true);
            }
        });

        sidebar.add(logoSidebar);
        sidebar.add(lblApp);
        sidebar.add(garis1);
        sidebar.add(lblUserSidebar);
        sidebar.add(lblRole);
        sidebar.add(garis2);
        sidebar.add(lblMenuLabel);
        sidebar.add(garis3);
        sidebar.add(btnLogout);

        // ===== CONTENT =====
        contentArea.add(buatPanelDashboard(), "0");
        contentArea.add(new PanelUser(), "1");
        contentArea.add(new PanelMenu(), "2");
        contentArea.add(new PanelPelanggan(), "3");
        contentArea.add(new PanelPesanan(), "4");


        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentArea, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private int[] hitungData() {
        int[] hasil = {0, 0, 0, 0};
        try {
            Connection conn = koneksi.configDB();
            String[] tabel = {"users", "menu", "pelanggan", "pesanan"};
            for (int i = 0; i < tabel.length; i++) {
                ResultSet rs = conn.createStatement()
                        .executeQuery("SELECT COUNT(*) FROM " + tabel[i]);
                if (rs.next()) hasil[i] = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasil;
    }

    private JPanel buatPanelDashboard() {
        JPanel p = new JPanel(null);
        p.setBackground(ABU_MUDA);

        JLabel title = new JLabel("Selamat Datang, " + username + "!");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(BIRU_TUA);
        title.setBounds(30, 30, 600, 35);

        JLabel sub = new JLabel("Kelola restoran kamu dari sini.");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(new Color(100, 100, 100));
        sub.setBounds(30, 65, 400, 25);

        int[] jumlah = hitungData();

        String[] judul  = {"Users", "Menu", "Pelanggan", "Pesanan"};
        String[] nilai  = {
                jumlah[0] + " Akun",
                jumlah[1] + " Item",
                jumlah[2] + " Orang",
                jumlah[3] + " Transaksi"
        };
        int[] tipe      = {0, 1, 2, 3};
        String[] idxStr = {"1", "2", "3", "4"};

        for (int i = 0; i < judul.length; i++) {
            final String idx = idxStr[i];
            JPanel card = buatKartu(tipe[i], judul[i], nilai[i]);
            card.setBounds(30 + i * 185, 110, 160, 140);
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    CardLayout cl = (CardLayout) contentArea.getLayout();
                    cl.show(contentArea, idx);
                }
            });
            p.add(card);
        }

        p.add(title);
        p.add(sub);
        return p;
    }

    private JPanel buatKartu(int tipe, String judul, String nilai) {
        JPanel card = new JPanel(null) {
            private boolean hover = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? BIRU_SANGAT_MUDA : PUTIH);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(hover ? BIRU_MUDA : new Color(200, 225, 255));
                g2.setStroke(new BasicStroke(hover ? 2f : 1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);

                int cx = getWidth() / 2;
                g2.setColor(BIRU_TUA);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (tipe == 0) {
                    g2.fillOval(cx - 12, 18, 24, 24);
                    g2.fillRoundRect(cx - 18, 46, 36, 20, 12, 12);
                } else if (tipe == 1) {
                    g2.drawOval(cx - 18, 24, 36, 26);
                    g2.drawLine(cx - 14, 37, cx + 14, 37);
                    g2.drawLine(cx - 26, 16, cx - 26, 52);
                    g2.drawLine(cx - 29, 16, cx - 29, 26);
                    g2.drawLine(cx - 26, 16, cx - 26, 26);
                    g2.drawLine(cx - 23, 16, cx - 23, 26);
                    g2.drawLine(cx - 29, 26, cx - 23, 26);
                    g2.drawLine(cx + 26, 30, cx + 26, 52);
                    g2.drawOval(cx + 21, 16, 10, 16);
                } else if (tipe == 2) {
                    g2.fillOval(cx - 20, 18, 18, 18);
                    g2.fillRoundRect(cx - 24, 38, 26, 14, 8, 8);
                    g2.fillOval(cx + 2,  18, 18, 18);
                    g2.fillRoundRect(cx - 2, 38, 26, 14, 8, 8);
                } else if (tipe == 3) {
                    g2.fillRoundRect(cx - 18, 16, 36, 44, 6, 6);
                    g2.setColor(PUTIH);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawLine(cx - 12, 27, cx + 12, 27);
                    g2.drawLine(cx - 12, 35, cx + 12, 35);
                    g2.drawLine(cx - 12, 43, cx + 4,  43);
                }
            }
        };
        card.setOpaque(false);

        JLabel lJudul = new JLabel(judul, SwingConstants.CENTER);
        lJudul.setFont(new Font("Arial", Font.BOLD, 13));
        lJudul.setForeground(BIRU_TUA);
        lJudul.setBounds(0, 75, 160, 22);

        JLabel lNilai = new JLabel(nilai, SwingConstants.CENTER);
        lNilai.setFont(new Font("Arial", Font.PLAIN, 11));
        lNilai.setForeground(new Color(120, 120, 120));
        lNilai.setBounds(0, 97, 160, 18);

        card.add(lJudul);
        card.add(lNilai);
        return card;
    }

    private JPanel buatGaris() {
        JPanel garis = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 255, 255, 60));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        garis.setOpaque(false);
        return garis;
    }

    private JButton buatTombolSidebar(String teks, int tipe) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                g2.setColor(PUTIH);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int ix = 14, iy = getHeight() / 2;
                if (tipe == 0) {
                    g2.drawRect(ix,     iy - 8, 7, 7);
                    g2.drawRect(ix + 9, iy - 8, 7, 7);
                    g2.drawRect(ix,     iy + 2, 7, 7);
                    g2.drawRect(ix + 9, iy + 2, 7, 7);
                } else if (tipe == 1) {
                    g2.fillOval(ix + 2, iy - 10, 12, 12);
                    g2.fillRoundRect(ix, iy + 4, 16, 8, 6, 6);
                } else if (tipe == 2) {
                    g2.drawLine(ix + 4, iy - 9, ix + 4, iy + 9);
                    g2.drawLine(ix + 2, iy - 9, ix + 2, iy - 3);
                    g2.drawLine(ix + 4, iy - 9, ix + 4, iy - 3);
                    g2.drawLine(ix + 6, iy - 9, ix + 6, iy - 3);
                    g2.drawLine(ix + 2, iy - 3, ix + 6, iy - 3);
                    g2.drawLine(ix + 13, iy - 2, ix + 13, iy + 9);
                    g2.drawOval(ix + 9,  iy - 9, 8, 10);
                } else if (tipe == 3) {
                    g2.fillOval(ix,     iy - 9, 10, 10);
                    g2.fillRoundRect(ix - 2, iy + 2, 14, 7, 4, 4);
                    g2.fillOval(ix + 8, iy - 9, 10, 10);
                    g2.fillRoundRect(ix + 6, iy + 2, 14, 7, 4, 4);
                } else if (tipe == 4) {
                    g2.fillRoundRect(ix + 1, iy - 9, 14, 18, 3, 3);
                    g2.setColor(new Color(30, 100, 180));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawLine(ix + 4, iy - 5, ix + 12, iy - 5);
                    g2.drawLine(ix + 4, iy,     ix + 12, iy);
                    g2.drawLine(ix + 4, iy + 5, ix + 9,  iy + 5);
                }
                g2.setColor(PUTIH);
                g2.setFont(new Font("Arial", Font.PLAIN, 13));
                g2.drawString(teks, ix + 26, iy + 5);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton buatTombolLogout() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 100, 100, 50));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                int ix = 14, iy = getHeight() / 2;
                g2.setColor(new Color(255, 180, 180));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(ix + 6, iy - 6, ix + 14, iy);
                g2.drawLine(ix + 6, iy + 6, ix + 14, iy);
                g2.drawLine(ix,     iy,      ix + 14, iy);
                g2.drawLine(ix,     iy - 8,  ix,      iy + 8);
                g2.setFont(new Font("Arial", Font.BOLD, 13));
                g2.drawString("Logout", ix + 26, iy + 5);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel buatPanelInfo(String judul, String deskripsi) {
        JPanel p = new JPanel(null);
        p.setBackground(ABU_MUDA);
        JLabel lbl = new JLabel(judul);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(BIRU_TUA);
        lbl.setBounds(30, 30, 500, 35);
        JLabel desc = new JLabel(deskripsi);
        desc.setFont(new Font("Arial", Font.PLAIN, 13));
        desc.setForeground(new Color(100, 100, 100));
        desc.setBounds(30, 65, 500, 25);
        p.add(lbl);
        p.add(desc);
        return p;
    }
}