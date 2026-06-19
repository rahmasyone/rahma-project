package Aplikasi;

import Kelas.koneksi;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblStatus;

    static final Color BIRU_TUA         = new Color(30, 100, 180);
    static final Color BIRU_MUDA        = new Color(100, 170, 240);
    static final Color BIRU_SANGAT_MUDA = new Color(220, 237, 255);
    static final Color PUTIH            = Color.WHITE;
    static final Color ABU              = new Color(120, 120, 120);

    public FormLogin() {
        setTitle("Login - Kasir Restoran");
        setSize(420, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }
    public void isiankosong {


    }
    private void initComponents() {
        JPanel mainPanel = new JPanel() {
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
        mainPanel.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PUTIH);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(320, 380));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // ===== LOGO =====
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;

                // Lingkaran biru
                g2.setColor(BIRU_TUA);
                g2.fillOval(cx - 32, 0, 64, 64);

                // Garpu & sendok putih
                g2.setColor(PUTIH);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Garpu (kiri dari tengah)
                int gx = cx - 10;
                g2.drawLine(gx, 14, gx, 50);
                g2.drawLine(gx - 4, 14, gx - 4, 24);
                g2.drawLine(gx,     14, gx,     24);
                g2.drawLine(gx + 4, 14, gx + 4, 24);
                g2.drawLine(gx - 4, 24, gx + 4, 24);

                // Sendok (kanan dari tengah)
                int sx = cx + 10;
                g2.drawLine(sx, 32, sx, 50);
                g2.drawOval(sx - 5, 14, 10, 16);
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(280, 70));

        // Judul
        JLabel lblTitle = new JLabel("SISTEM KASIR RESTORAN RRR", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(BIRU_TUA);

        JLabel lblSub = new JLabel("Silakan login untuk melanjutkan", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 11));
        lblSub.setForeground(ABU);

        // Username
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Arial", Font.BOLD, 12));
        lblUser.setForeground(BIRU_TUA);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUsername.setPreferredSize(new Dimension(280, 36));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(BIRU_MUDA, 10),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        txtUsername.setBackground(BIRU_SANGAT_MUDA);

        // Password
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Arial", Font.BOLD, 12));
        lblPass.setForeground(BIRU_TUA);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPassword.setPreferredSize(new Dimension(280, 36));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(BIRU_MUDA, 10),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        txtPassword.setBackground(BIRU_SANGAT_MUDA);

        // Status
        lblStatus = new JLabel(" ", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(200, 50, 50));

        // Tombol Login
        btnLogin = new JButton("LOGIN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(20, 80, 160));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(50, 130, 220));
                } else {
                    g2.setColor(BIRU_TUA);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(PUTIH);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };
        btnLogin.setPreferredSize(new Dimension(280, 40));
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ===== SUSUN KOMPONEN =====
        gbc.gridy = 0; gbc.insets = new Insets(20, 20, 4, 20);
        card.add(logoPanel, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(4, 20, 2, 20);
        card.add(lblTitle, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 20, 12, 20);
        card.add(lblSub, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(4, 20, 2, 20);
        card.add(lblUser, gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 20, 4, 20);
        card.add(txtUsername, gbc);

        gbc.gridy = 5; gbc.insets = new Insets(4, 20, 2, 20);
        card.add(lblPass, gbc);

        gbc.gridy = 6; gbc.insets = new Insets(0, 20, 4, 20);
        card.add(txtPassword, gbc);

        gbc.gridy = 7; gbc.insets = new Insets(2, 20, 4, 20);
        card.add(lblStatus, gbc);

        gbc.gridy = 8; gbc.insets = new Insets(4, 20, 20, 20);
        card.add(btnLogin, gbc);

        // Footer
        JLabel lblFooter = new JLabel("© 2026 Kasir Restoran RRR", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(200, 220, 255));

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(card, gbcMain);

        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(lblFooter, gbcMain);

        setContentPane(mainPanel);

        btnLogin.addActionListener(e -> doLogin());
        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        });
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Username dan password tidak boleh kosong!");
            return;
        }

        try {
            Connection conn = koneksi.configDB();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                int idUser = rs.getInt("id_user");
                this.dispose();
                if (role.equals("admin")) {
                    new FormDashboardAdmin(idUser, username).setVisible(true);
                } else {
                    new FormDashboardKasir(idUser, username).setVisible(true);
                }
            } else {
                lblStatus.setText("Username atau password salah!");
            }

        } catch (SQLException ex) {
            lblStatus.setText("Koneksi database gagal!");
            ex.printStackTrace();
        }
    }

    static class RoundBorder extends AbstractBorder {
        private Color color;
        private int radius;
        RoundBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormLogin().setVisible(true));
    }
}