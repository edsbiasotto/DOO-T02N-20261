package com.serieshub.ui;

import com.serieshub.model.Serie;
import com.serieshub.model.Usuario;
import com.serieshub.service.PersistenciaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaDetalhe extends JFrame {

    private static final Color FUNDO     = Color.decode("#121212");
    private static final Color MENU      = Color.decode("#1E1E1E");
    private static final Color CARD      = Color.decode("#2A2A2A");
    private static final Color DESTAQUE  = Color.decode("#F79E1B");
    private static final Color TEXTO     = Color.decode("#FFFFFF");
    private static final Color TEXTO_SEC = Color.decode("#B3B3B3");

    private Usuario usuario;
    private PersistenciaService persistencia;
    private Serie serie;

    public TelaDetalhe(Serie serie, Usuario usuario, PersistenciaService persistencia) {
        this.serie = serie;
        this.usuario = usuario;
        this.persistencia = persistencia;

        setTitle(serie.getNome() + " - SeriesHub");
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);

        add(criarNavbar(), BorderLayout.NORTH);
        add(criarConteudo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(MENU);
        navbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, DESTAQUE),
            new EmptyBorder(12, 24, 12, 24)
        ));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
        Image logoOriginal = logoIcon.getImage();
        int largura = 120;
        int altura = (int)(logoOriginal.getHeight(null) * (largura / (double) logoOriginal.getWidth(null)));
        JLabel logo = new JLabel(new ImageIcon(logoOriginal.getScaledInstance(largura, altura, Image.SCALE_SMOOTH)));

        JLabel lblDetalhe = new JLabel("Detalhes da Serie");
        lblDetalhe.setForeground(DESTAQUE);
        lblDetalhe.setFont(new Font("Arial", Font.BOLD, 13));

        navbar.add(logo, BorderLayout.WEST);
        navbar.add(lblDetalhe, BorderLayout.EAST);

        return navbar;
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel(new BorderLayout(0, 0));
        conteudo.setBackground(FUNDO);
        conteudo.setBorder(new EmptyBorder(24, 24, 24, 24));

        conteudo.add(criarPainelImagem(), BorderLayout.WEST);
        conteudo.add(criarPainelInfo(), BorderLayout.CENTER);

        return conteudo;
    }

    private JPanel criarPainelImagem() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(FUNDO);
        painel.setBorder(new EmptyBorder(0, 0, 0, 24));

        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(280, 400));
        imgLabel.setBackground(Color.decode("#1A1A1A"));
        imgLabel.setOpaque(true);
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setBorder(BorderFactory.createLineBorder(DESTAQUE, 2));

        if (serie.getImageUrl() != null && !serie.getImageUrl().isEmpty()) {
            SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
                protected ImageIcon doInBackground() throws Exception {
                    java.net.URL imgUrl = new java.net.URL(serie.getImageUrl());
                    ImageIcon icon = new ImageIcon(imgUrl);
                    Image img = icon.getImage().getScaledInstance(280, 400, Image.SCALE_SMOOTH);
                    return new ImageIcon(img);
                }
                protected void done() {
                    try {
                        imgLabel.setIcon(get());
                        imgLabel.revalidate();
                        imgLabel.repaint();
                    } catch (Exception ex) {
                        imgLabel.setText("Sem imagem");
                        imgLabel.setForeground(TEXTO_SEC);
                    }
                }
            };
            worker.execute();
        } else {
            imgLabel.setText("Sem imagem");
            imgLabel.setForeground(TEXTO_SEC);
        }

        painel.add(imgLabel, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelInfo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(FUNDO);

        JLabel nome = new JLabel(serie.getNome());
        nome.setFont(new Font("Arial", Font.BOLD, 28));
        nome.setForeground(TEXTO);
        nome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(DESTAQUE);
        sep.setBackground(DESTAQUE);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(nome);
        painel.add(Box.createVerticalStrut(8));
        painel.add(sep);
        painel.add(Box.createVerticalStrut(16));

        painel.add(criarLinhaBadge());
        painel.add(Box.createVerticalStrut(20));

        painel.add(criarInfo("Idioma", serie.getIdioma()));
        painel.add(Box.createVerticalStrut(10));
        painel.add(criarInfo("Generos", String.join(", ", serie.getGeneros())));
        painel.add(Box.createVerticalStrut(10));
        painel.add(criarInfo("Emissora", serie.getEmissora()));
        painel.add(Box.createVerticalStrut(10));
        painel.add(criarInfo("Estreia", serie.getDataEstreia()));
        painel.add(Box.createVerticalStrut(10));
        painel.add(criarInfo("Termino", serie.getDataTermino().equals("N/A") ? "Em andamento" : serie.getDataTermino()));
        painel.add(Box.createVerticalStrut(24));

        JLabel lblDescTitulo = new JLabel("Descricao");
        lblDescTitulo.setForeground(TEXTO_SEC);
        lblDescTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descArea = new JTextArea(serie.getDescricao());
        descArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descArea.setForeground(TEXTO);
        descArea.setBackground(CARD);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setOpaque(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#3A3A3A"), 1),
            new EmptyBorder(10, 14, 10, 14)
        ));
        descArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(Box.createVerticalStrut(10));
        painel.add(lblDescTitulo);
        painel.add(Box.createVerticalStrut(4));
        painel.add(descArea);
        painel.add(Box.createVerticalStrut(24));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelBotoes.setBackground(FUNDO);
        painelBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnFav  = criarBotaoAcao("+ Favoritos", DESTAQUE, Color.decode("#121212"));
        JButton btnAss  = criarBotaoAcao("+ Assistidas", Color.decode("#2ECC71"), Color.decode("#121212"));
        JButton btnQuer = criarBotaoAcao("+ Quero Ver", Color.decode("#3498DB"), TEXTO);

        btnFav.addActionListener(e -> {
            usuario.adicionarFavorita(serie);
            persistencia.salvar(usuario);
            JOptionPane.showMessageDialog(this, serie.getNome() + " adicionada aos favoritos!");
        });
        btnAss.addActionListener(e -> {
            usuario.adicionarAssistida(serie);
            persistencia.salvar(usuario);
            JOptionPane.showMessageDialog(this, serie.getNome() + " adicionada as assistidas!");
        });
        btnQuer.addActionListener(e -> {
            usuario.adicionarParaAssistir(serie);
            persistencia.salvar(usuario);
            JOptionPane.showMessageDialog(this, serie.getNome() + " adicionada a lista!");
        });

        painelBotoes.add(btnFav);
        painelBotoes.add(btnAss);
        painelBotoes.add(btnQuer);
        painel.add(painelBotoes);

        return painel;
    }

    private JPanel criarLinhaBadge() {
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        linha.setBackground(FUNDO);
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nota = new JLabel("Nota: " + serie.getNota());
        nota.setFont(new Font("Arial", Font.BOLD, 16));
        nota.setForeground(DESTAQUE);
        nota.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DESTAQUE, 1),
            new EmptyBorder(4, 12, 4, 12)
        ));

        Color corEstado = switch (serie.getEstado()) {
            case "Running"  -> Color.decode("#2ECC71");
            case "Ended"    -> Color.decode("#E74C3C");
            case "Canceled" -> Color.decode("#95A5A6");
            default         -> Color.decode("#F39C12");
        };

        String textoEstado = switch (serie.getEstado()) {
            case "Running"  -> "Em exibicao";
            case "Ended"    -> "Encerrada";
            case "Canceled" -> "Cancelada";
            default         -> serie.getEstado();
        };

        JLabel estado = new JLabel(textoEstado);
        estado.setFont(new Font("Arial", Font.BOLD, 13));
        estado.setForeground(Color.decode("#121212"));
        estado.setBackground(corEstado);
        estado.setOpaque(true);
        estado.setBorder(new EmptyBorder(4, 12, 4, 12));

        linha.add(nota);
        linha.add(estado);
        return linha;
    }

    private JPanel criarInfo(String label, String valor) {
        JPanel linha = new JPanel(new BorderLayout());
        linha.setBackground(CARD);
        linha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#3A3A3A"), 1),
            new EmptyBorder(10, 14, 10, 14)
        ));
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setForeground(TEXTO_SEC);
        lblLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lblLabel.setPreferredSize(new Dimension(90, 20));

        JLabel lblValor = new JLabel(valor.isEmpty() ? "N/A" : valor);
        lblValor.setForeground(TEXTO);
        lblValor.setFont(new Font("Arial", Font.BOLD, 13));

        linha.add(lblLabel, BorderLayout.WEST);
        linha.add(lblValor, BorderLayout.CENTER);

        return linha;
    }

    private JButton criarBotaoAcao(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 38));
        return btn;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rodape.setBackground(MENU);
        rodape.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, DESTAQUE),
            new EmptyBorder(8, 0, 8, 0)
        ));

        JLabel txt = new JLabel("SeriesHub  |  Dados fornecidos por TVMaze API");
        txt.setForeground(TEXTO_SEC);
        txt.setFont(new Font("Arial", Font.PLAIN, 11));
        rodape.add(txt);

        return rodape;
    }
}