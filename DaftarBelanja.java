import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DaftarBelanja {

    // ===== Atribut (komponen GUI) =====
    private JFrame frame;
    private JTextField fieldBarang;
    private JComboBox<String> comboKategori;
    private JButton btnTambah, btnHapus, btnReset;
    private JList<String> listBarang;
    private DefaultListModel<String> model;
    private JLabel lblStatus;

    // ===== Constructor: membuat komponen =====
    public DaftarBelanja() {
        frame = new JFrame("Daftar Belanja");
        fieldBarang = new JTextField(15);
        comboKategori = new JComboBox<String>(
                new String[] { "Makanan", "Minuman", "Alat Tulis", "Lainnya" });
        btnTambah = new JButton("Tambah");
        btnHapus = new JButton("Hapus");
        btnReset = new JButton("Reset");
        model = new DefaultListModel<String>();
        listBarang = new JList<String>(model);
        lblStatus = new JLabel("Siap. Total barang: 0");
    }

    // ===== Menyusun tampilan & mendaftarkan listener =====
    public void launchFrame() {
        frame.setLayout(new BorderLayout(10, 10));

        // Panel atas: judul
        JLabel judul = new JLabel("Daftar Belanja", SwingConstants.CENTER);
        judul.setFont(new Font("SansSerif", Font.BOLD, 20));
        frame.add(judul, BorderLayout.NORTH);

        // Panel tengah: form input + daftar
        JPanel panelForm = new JPanel(new FlowLayout());
        panelForm.add(new JLabel("Barang:"));
        panelForm.add(fieldBarang);
        panelForm.add(new JLabel("Kategori:"));
        panelForm.add(comboKategori);
        panelForm.add(btnTambah);

        JPanel panelTengah = new JPanel(new BorderLayout(5, 5));
        panelTengah.add(panelForm, BorderLayout.NORTH);
        panelTengah.add(new JScrollPane(listBarang), BorderLayout.CENTER);
        frame.add(panelTengah, BorderLayout.CENTER);

        // Panel bawah: tombol + status
        JPanel panelBawah = new JPanel(new BorderLayout());
        JPanel panelTombol = new JPanel(new FlowLayout());
        panelTombol.add(btnHapus);
        panelTombol.add(btnReset);
        panelBawah.add(panelTombol, BorderLayout.NORTH);
        panelBawah.add(lblStatus, BorderLayout.SOUTH);
        frame.add(panelBawah, BorderLayout.SOUTH);

        // --- Registrasi listener ke event source ---
        TambahHandler tambah = new TambahHandler();
        btnTambah.addActionListener(tambah);
        fieldBarang.addActionListener(tambah);          // tekan Enter = Tambah
        btnHapus.addActionListener(new HapusHandler());
        btnReset.addActionListener(new ResetHandler());

        fieldBarang.addKeyListener(new KetikHandler());

        HoverHandler hover = new HoverHandler();
        btnTambah.addMouseListener(hover);
        btnHapus.addMouseListener(hover);
        btnReset.addMouseListener(hover);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new TutupHandler());

        frame.setSize(520, 380);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void updateStatus() {
        lblStatus.setText("Total barang: " + model.getSize());
    }

    // ===== INNER CLASS: Event Handler =====
    // ActionListener: tombol Tambah / Enter
    private class TambahHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String barang = fieldBarang.getText().trim();
            if (barang.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nama barang tidak boleh kosong!",
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String kategori = (String) comboKategori.getSelectedItem();
            model.addElement(barang + "  [" + kategori + "]");
            fieldBarang.setText("");
            fieldBarang.requestFocus();
            updateStatus();
        }
    }

    // ActionListener: tombol Hapus
    private class HapusHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = listBarang.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(frame, "Pilih barang yang ingin dihapus dulu.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                model.remove(index);
                updateStatus();
            }
        }
    }

    // ActionListener: tombol Reset
    private class ResetHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.clear();
            fieldBarang.setText("");
            updateStatus();
        }
    }

    // KeyAdapter: hanya override method yang dipakai (keyReleased)
    private class KetikHandler extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            lblStatus.setText("Mengetik... " + fieldBarang.getText().length() + " karakter");
        }
    }

    // MouseAdapter: hanya override mouseEntered & mouseExited
    private class HoverHandler extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            JButton b = (JButton) e.getSource();
            lblStatus.setText("Klik untuk: " + b.getText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            updateStatus();
        }
    }

    // WindowAdapter: hanya override windowClosing
    private class TutupHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            int pilih = JOptionPane.showConfirmDialog(frame, "Yakin ingin keluar?",
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (pilih == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    // ===== main =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DaftarBelanja app = new DaftarBelanja();
                app.launchFrame();
            }
        });
    }
}
