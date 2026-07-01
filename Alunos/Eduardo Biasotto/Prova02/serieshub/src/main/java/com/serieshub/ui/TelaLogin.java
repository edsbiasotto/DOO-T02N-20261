package com.serieshub.ui;

import com.serieshub.model.Usuario;
import com.serieshub.service.PersistenciaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaLogin extends JFrame {

    private static final Color FUNDO    = Color.decode("#121212");
    private static final Color MENU     = Color.decode("#1E1E1E");
    private static final Color CARD     = Color.decode("#2A2A2A");
    private static final Color DESTAQUE = Color.decode("#F79E1B");
    private static final Color TEXTO    = Color.decode("#FFFFFF");
    private static final Color TEXTO_SEC = Color.decode("#B3B3B3");

    public TelaLogin() {
        setTitle("SeriesHub - Bem vindo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(FUNDO);

        add(criarConteudo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarConteudo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(FUNDO);
        painel.setBorder(new EmptyBorder(40, 50, 40, 50));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
        Image logoOriginal = logoIcon.getImage();
        int largura = 180;
        int altura = (int)(logoOriginal.getHeight(null) * (largura / (double) logoOriginal.getWidth(null)));
        JLabel logo = new JLabel(new ImageIcon(logoOriginal.getScaledInstance(largura, altura, Image.SCALE_SMOOTH)));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBemVindo = new JLabel("Bem vindo ao SeriesHub!");
        lblBemVindo.setFont(new Font("Arial", Font.BOLD, 18));
        lblBemVindo.setForeground(TEXTO);
        lblBemVindo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Como podemos te chamar?");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSub.setForeground(TEXTO_SEC);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField campoNome = new JTextField();
        campoNome.setBackground(CARD);
        campoNome.setForeground(TEXTO);
        campoNome.setCaretColor(TEXTO);
        campoNome.setFont(new Font("Arial", Font.PLAIN, 14));
        campoNome.setHorizontalAlignment(JTextField.CENTER);
        campoNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DESTAQUE, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        campoNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campoNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(DESTAQUE);
        btnEntrar.setForeground(Color.decode("#121212"));
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEntrar.setBorderPainted(false);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEntrar.addActionListener(e -> entrar(campoNome.getText().trim()));
        campoNome.addActionListener(e -> entrar(campoNome.getText().trim()));

        painel.add(logo);
        painel.add(Box.createVerticalStrut(20));
        painel.add(lblBemVindo);
        painel.add(Box.createVerticalStrut(6));
        painel.add(lblSub);
        painel.add(Box.createVerticalStrut(20));
        painel.add(campoNome);
        painel.add(Box.createVerticalStrut(12));
        painel.add(btnEntrar);

        return painel;
    }

    private void entrar(String nome) {
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, digite seu nome!",
                "Campo obrigatorio",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        PersistenciaService persistencia = new PersistenciaService();
        Usuario usuario = persistencia.carregar();
        usuario = new Usuario(nome);
        persistencia.salvar(usuario);

        TelaPrincipal telaPrincipal = new TelaPrincipal();
        telaPrincipal.setVisible(true);
        dispose();
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rodape.setBackground(MENU);
        rodape.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, DESTAQUE),
            new EmptyBorder(8, 0, 8, 0)
        ));

        JLabel txt = new JLabel("SeriesHub  |  Powered by TVMaze API");
        txt.setForeground(TEXTO_SEC);
        txt.setFont(new Font("Arial", Font.PLAIN, 11));
        rodape.add(txt);

        return rodape;
    }
}