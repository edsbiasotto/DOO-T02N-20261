package com.serieshub.ui;

import com.serieshub.model.Serie;
import com.serieshub.model.Usuario;
import com.serieshub.service.PersistenciaService;
import com.serieshub.service.TVMazeService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private static final Color FUNDO     = Color.decode("#121212");
    private static final Color MENU      = Color.decode("#1E1E1E");
    private static final Color CARD      = Color.decode("#2A2A2A");
    private static final Color DESTAQUE  = Color.decode("#F79E1B");
    private static final Color TEXTO     = Color.decode("#FFFFFF");
    private static final Color TEXTO_SEC = Color.decode("#B3B3B3");

    private Usuario usuario;
    private PersistenciaService persistencia;
    private TVMazeService tvMazeService;
    private JPanel painelConteudo;

    public TelaPrincipal() {
        persistencia = new PersistenciaService();
        tvMazeService = new TVMazeService();
        usuario = persistencia.carregar();
        configurarJanela();
        add(criarNavbar(), BorderLayout.NORTH);
        painelConteudo = criarPainelInicio();
        JScrollPane scroll = new JScrollPane(painelConteudo);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(FUNDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private void configurarJanela() {
        setTitle("SeriesHub");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);
    }

    private JPanel criarNavbar() {
        JPanel navbar = new JPanel(new GridBagLayout());
        navbar.setBackground(MENU);
        navbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, DESTAQUE),
            new EmptyBorder(12, 24, 12, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
        Image logoOriginal = logoIcon.getImage();
        int largura = 130;
        int altura = (int)(logoOriginal.getHeight(null) * (largura / (double) logoOriginal.getWidth(null)));
        JLabel logo = new JLabel(new ImageIcon(logoOriginal.getScaledInstance(largura, altura, Image.SCALE_SMOOTH)));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        navbar.add(logo, gbc);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        painelBusca.setBackground(MENU);

        JTextField campoBusca = new JTextField(28);
        campoBusca.setBackground(CARD);
        campoBusca.setForeground(TEXTO);
        campoBusca.setCaretColor(TEXTO);
        campoBusca.setFont(new Font("Arial", Font.PLAIN, 14));
        campoBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DESTAQUE, 1),
            new EmptyBorder(7, 12, 7, 12)
        ));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(DESTAQUE);
        btnBuscar.setForeground(Color.decode("#121212"));
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 13));
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setPreferredSize(new Dimension(100, 36));

        painelBusca.add(campoBusca);
        painelBusca.add(btnBuscar);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.anchor = GridBagConstraints.CENTER;
        navbar.add(painelBusca, gbc);

        JPanel painelDireita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        painelDireita.setBackground(MENU);

        JButton btnListas = criarBotaoNav("Minhas Listas");
        JButton btnSair   = criarBotaoNav("Sair");
        painelDireita.add(btnListas);
        painelDireita.add(btnSair);

        gbc.gridx = 2;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.EAST;
        navbar.add(painelDireita, gbc);

        btnBuscar.addActionListener(e -> {
            String termo = campoBusca.getText().trim();
            if (!termo.isEmpty()) mostrarResultados(termo);
        });
        campoBusca.addActionListener(e -> btnBuscar.doClick());
        btnListas.addActionListener(e -> new TelaLista(usuario, persistencia).setVisible(true));
        btnSair.addActionListener(e -> { persistencia.salvar(usuario); System.exit(0); });

        return navbar;
    }

    private JPanel criarPainelInicio() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(FUNDO);
        painel.setBorder(new EmptyBorder(28, 28, 28, 28));

        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(CARD);
        banner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DESTAQUE, 1),
            new EmptyBorder(20, 24, 20, 24)
        ));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel bemVindo = new JLabel("Ola, " + usuario.getNome() + "! O que vamos assistir hoje?");
        bemVindo.setFont(new Font("Arial", Font.BOLD, 22));
        bemVindo.setForeground(TEXTO);

        JLabel sub = new JLabel("Busque series, gerencie suas listas e descubra novos titulos.");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(TEXTO_SEC);

        banner.add(bemVindo, BorderLayout.NORTH);
        banner.add(sub, BorderLayout.SOUTH);
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.add(banner);
        painel.add(Box.createVerticalStrut(24));

        JLabel tituloSecao = criarTituloSecao("Series em Destaque");
        painel.add(tituloSecao);
        painel.add(Box.createVerticalStrut(14));

        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 14));
        grid.setBackground(FUNDO);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel carregando = new JLabel("Carregando series...");
        carregando.setForeground(TEXTO_SEC);
        carregando.setFont(new Font("Arial", Font.ITALIC, 14));
        grid.add(carregando);
        painel.add(grid);

        SwingWorker<List<Serie>, Void> worker = new SwingWorker<>() {
            protected List<Serie> doInBackground() {
                return tvMazeService.buscarSeries("the");
            }
            protected void done() {
                try {
                    List<Serie> series = get();
                    grid.removeAll();
                    grid.setLayout(new GridLayout(0, 4, 14, 14));
                    int limite = Math.min(series.size(), 8);
                    for (int i = 0; i < limite; i++) {
                        grid.add(criarCard(series.get(i), false));
                    }
                    grid.revalidate();
                    grid.repaint();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        };
        worker.execute();

        return painel;
    }

    private void mostrarResultados(String termo) {
        painelConteudo.removeAll();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel titulo = criarTituloSecao("Resultados para: \"" + termo + "\"");
        painelConteudo.add(titulo);
        painelConteudo.add(Box.createVerticalStrut(14));

        JPanel grid = new JPanel(new GridLayout(0, 4, 14, 14));
        grid.setBackground(FUNDO);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelConteudo.add(grid);

        JLabel carregando = new JLabel("Buscando...");
        carregando.setForeground(TEXTO_SEC);
        grid.add(carregando);

        painelConteudo.revalidate();
        painelConteudo.repaint();

        SwingWorker<List<Serie>, Void> worker = new SwingWorker<>() {
            protected List<Serie> doInBackground() {
                return tvMazeService.buscarSeries(termo);
            }
            protected void done() {
                try {
                    List<Serie> series = get();
                    grid.removeAll();
                    if (series.isEmpty()) {
                        JLabel vazio = new JLabel("Nenhuma serie encontrada.");
                        vazio.setForeground(TEXTO_SEC);
                        grid.add(vazio);
                    } else {
                        for (Serie s : series) grid.add(criarCard(s, true));
                    }
                    grid.revalidate();
                    grid.repaint();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private JPanel criarCard(Serie serie, boolean comAcoes) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createLineBorder(Color.decode("#3A3A3A"), 1));
        card.setPreferredSize(new Dimension(240, comAcoes ? 410 : 320));
        card.setMaximumSize(new Dimension(240, comAcoes ? 410 : 320));
        card.setMinimumSize(new Dimension(240, comAcoes ? 410 : 320));

        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(240, 180));
        imgLabel.setMinimumSize(new Dimension(240, 180));
        imgLabel.setMaximumSize(new Dimension(240, 180));
        imgLabel.setBackground(Color.decode("#1A1A1A"));
        imgLabel.setOpaque(true);
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imgLabel);

        if (serie.getImageUrl() != null && !serie.getImageUrl().isEmpty()) {
            SwingWorker<ImageIcon, Void> imgWorker = new SwingWorker<>() {
                protected ImageIcon doInBackground() throws Exception {
                    java.net.URL imgUrl = new java.net.URL(serie.getImageUrl());
                    ImageIcon icon = new ImageIcon(imgUrl);
                    Image img = icon.getImage().getScaledInstance(160, 180, Image.SCALE_SMOOTH);
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
            imgWorker.execute();
        } else {
            imgLabel.setText("Sem imagem");
            imgLabel.setForeground(TEXTO_SEC);
        }

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(CARD);
        info.setBorder(new EmptyBorder(10, 14, 14, 14));

        JLabel nome = new JLabel("<html><b>" + serie.getNome() + "</b></html>");
        nome.setForeground(TEXTO);
        nome.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel nota = new JLabel("Nota: " + serie.getNota());
        nota.setForeground(DESTAQUE);
        nota.setFont(new Font("Arial", Font.BOLD, 13));

        String estadoTexto = switch (serie.getEstado()) {
            case "Running"  -> "[ON] ";
            case "Ended"    -> "[FIM] ";
            case "Canceled" -> "[CAN] ";
            default         -> "[?] ";
        };

        JLabel estado = new JLabel(estadoTexto + serie.getEstado());
        estado.setForeground(TEXTO_SEC);
        estado.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel generos = new JLabel("<html><i>" + String.join(", ", serie.getGeneros()) + "</i></html>");
        generos.setForeground(TEXTO_SEC);
        generos.setFont(new Font("Arial", Font.PLAIN, 11));

        info.add(nome);
        info.add(Box.createVerticalStrut(6));
        info.add(nota);
        info.add(Box.createVerticalStrut(4));
        info.add(estado);
        info.add(Box.createVerticalStrut(4));
        info.add(generos);

        if (comAcoes) {
            info.add(Box.createVerticalStrut(10));

            JButton btnFav  = criarBotaoCard("+ Favoritos");
            JButton btnAss  = criarBotaoCard("+ Assistidas");
            JButton btnQuer = criarBotaoCard("+ Quero Ver");

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

            info.add(btnFav);
            info.add(Box.createVerticalStrut(4));
            info.add(btnAss);
            info.add(Box.createVerticalStrut(4));
            info.add(btnQuer);
        }

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new TelaDetalhe(serie, usuario, persistencia).setVisible(true);
            }
        });

        card.add(info);
        return card;
    }

    private JLabel criarTituloSecao(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 17));
        label.setForeground(TEXTO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton criarBotaoCard(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(Color.decode("#333333"));
        btn.setForeground(TEXTO);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return btn;
    }

    private JButton criarBotaoNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(MENU);
        btn.setForeground(TEXTO);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 36));
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