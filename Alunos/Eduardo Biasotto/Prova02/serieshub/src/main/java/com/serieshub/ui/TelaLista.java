package com.serieshub.ui;

import com.serieshub.model.Serie;
import com.serieshub.model.Usuario;
import com.serieshub.service.PersistenciaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class TelaLista extends JFrame {

    private static final Color FUNDO     = Color.decode("#121212");
    private static final Color MENU      = Color.decode("#1E1E1E");
    private static final Color CARD      = Color.decode("#2A2A2A");
    private static final Color DESTAQUE  = Color.decode("#F79E1B");
    private static final Color TEXTO     = Color.decode("#FFFFFF");
    private static final Color TEXTO_SEC = Color.decode("#B3B3B3");
    private static final Color REMOVER   = Color.decode("#C0392B");

    private Usuario usuario;
    private PersistenciaService persistencia;

    public TelaLista(Usuario usuario, PersistenciaService persistencia) {
        this.usuario = usuario;
        this.persistencia = persistencia;

        setTitle("Minhas Listas - SeriesHub");
        setSize(1200, 800);
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

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        direita.setBackground(MENU);

        JLabel lblUsuario = new JLabel("Ola, " + usuario.getNome());
        lblUsuario.setForeground(TEXTO_SEC);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblSub = new JLabel("  |  Minhas Listas");
        lblSub.setForeground(DESTAQUE);
        lblSub.setFont(new Font("Arial", Font.BOLD, 13));

        direita.add(lblUsuario);
        direita.add(lblSub);

        navbar.add(logo, BorderLayout.WEST);
        navbar.add(direita, BorderLayout.EAST);

        return navbar;
    }

    private JPanel criarConteudo() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(FUNDO);
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel tituloSecao = new JLabel("Minhas Listas");
        tituloSecao.setFont(new Font("Arial", Font.BOLD, 22));
        tituloSecao.setForeground(TEXTO);
        tituloSecao.setBorder(new EmptyBorder(0, 0, 16, 0));

        JPanel colunas = new JPanel(new GridLayout(1, 3, 14, 0));
        colunas.setBackground(FUNDO);

        colunas.add(criarColuna("Favoritas", usuario.getSeriesFavoritas(), "favoritas"));
        colunas.add(criarColuna("Assistidas", usuario.getSeriesAssistidas(), "assistidas"));
        colunas.add(criarColuna("Quero Ver", usuario.getSeriesParaAssistir(), "querassistir"));

        wrapper.add(tituloSecao, BorderLayout.NORTH);
        wrapper.add(colunas, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel criarColuna(String titulo, List<Serie> lista, String tipo) {
        JPanel coluna = new JPanel(new BorderLayout());
        coluna.setBackground(CARD);
        coluna.setBorder(BorderFactory.createLineBorder(Color.decode("#3A3A3A"), 1));

        JPanel headerCol = new JPanel(new BorderLayout());
        headerCol.setBackground(Color.decode("#1A1A1A"));
        headerCol.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel headerEsq = new JPanel();
        headerEsq.setLayout(new BoxLayout(headerEsq, BoxLayout.Y_AXIS));
        headerEsq.setBackground(Color.decode("#1A1A1A"));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 15));
        lblTitulo.setForeground(DESTAQUE);

        JLabel lblCount = new JLabel(lista.size() + " series");
        lblCount.setFont(new Font("Arial", Font.PLAIN, 11));
        lblCount.setForeground(TEXTO_SEC);

        headerEsq.add(lblTitulo);
        headerEsq.add(lblCount);
        headerCol.add(headerEsq, BorderLayout.WEST);

        JPanel painelOrdem = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        painelOrdem.setBackground(Color.decode("#222222"));
        painelOrdem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#3A3A3A")),
            new EmptyBorder(2, 8, 2, 8)
        ));

        JLabel lblOrdenar = new JLabel("Ordenar:");
        lblOrdenar.setForeground(TEXTO_SEC);
        lblOrdenar.setFont(new Font("Arial", Font.PLAIN, 11));

        JButton btnNome   = criarBotaoOrdem("Nome");
        JButton btnNota   = criarBotaoOrdem("Nota");
        JButton btnEstado = criarBotaoOrdem("Estado");
        JButton btnData   = criarBotaoOrdem("Data");

        painelOrdem.add(lblOrdenar);
        painelOrdem.add(btnNome);
        painelOrdem.add(btnNota);
        painelOrdem.add(btnEstado);
        painelOrdem.add(btnData);

        JPanel painelCards = new JPanel();
        painelCards.setLayout(new BoxLayout(painelCards, BoxLayout.Y_AXIS));
        painelCards.setBackground(CARD);
        painelCards.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(painelCards);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CARD);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        atualizarColuna(painelCards, lista, tipo, lblCount);

        btnNome.addActionListener(e -> {
            lista.sort(Comparator.comparing(Serie::getNome));
            atualizarColuna(painelCards, lista, tipo, lblCount);
        });
        btnNota.addActionListener(e -> {
            lista.sort(Comparator.comparingDouble(Serie::getNota).reversed());
            atualizarColuna(painelCards, lista, tipo, lblCount);
        });
        btnEstado.addActionListener(e -> {
            lista.sort(Comparator.comparing(Serie::getEstado));
            atualizarColuna(painelCards, lista, tipo, lblCount);
        });
        btnData.addActionListener(e -> {
            lista.sort(Comparator.comparing(Serie::getDataEstreia));
            atualizarColuna(painelCards, lista, tipo, lblCount);
        });

        JPanel topColuna = new JPanel(new BorderLayout());
        topColuna.setBackground(Color.decode("#1A1A1A"));
        topColuna.add(headerCol, BorderLayout.NORTH);
        topColuna.add(painelOrdem, BorderLayout.SOUTH);

        coluna.add(topColuna, BorderLayout.NORTH);
        coluna.add(scroll, BorderLayout.CENTER);

        return coluna;
    }

    private void atualizarColuna(JPanel painelCards, List<Serie> lista, String tipo, JLabel lblCount) {
        painelCards.removeAll();

        if (lista.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma serie ainda");
            vazio.setForeground(TEXTO_SEC);
            vazio.setFont(new Font("Arial", Font.ITALIC, 13));
            vazio.setAlignmentX(Component.CENTER_ALIGNMENT);
            painelCards.add(Box.createVerticalStrut(20));
            painelCards.add(vazio);
        } else {
            for (Serie serie : lista) {
                painelCards.add(criarCardColuna(serie, lista, tipo, painelCards, lblCount));
                painelCards.add(Box.createVerticalStrut(8));
            }
        }

        lblCount.setText(lista.size() + " series");
        painelCards.revalidate();
        painelCards.repaint();
    }

    private JPanel criarCardColuna(Serie serie, List<Serie> lista, String tipo, JPanel painelCards, JLabel lblCount) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.decode("#333333"));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#444444"), 1),
            new EmptyBorder(0, 0, 0, 0)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setMinimumSize(new Dimension(0, 90));

        // Imagem pequena na esquerda
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(60, 90));
        imgLabel.setMinimumSize(new Dimension(60, 90));
        imgLabel.setMaximumSize(new Dimension(60, 90));
        imgLabel.setBackground(Color.decode("#1A1A1A"));
        imgLabel.setOpaque(true);
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if (serie.getImageUrl() != null && !serie.getImageUrl().isEmpty()) {
            SwingWorker<ImageIcon, Void> imgWorker = new SwingWorker<>() {
                protected ImageIcon doInBackground() throws Exception {
                    java.net.URL imgUrl = new java.net.URL(serie.getImageUrl());
                    ImageIcon icon = new ImageIcon(imgUrl);
                    Image img = icon.getImage().getScaledInstance(60, 90, Image.SCALE_SMOOTH);
                    return new ImageIcon(img);
                }
                protected void done() {
                    try {
                        imgLabel.setIcon(get());
                        imgLabel.revalidate();
                        imgLabel.repaint();
                    } catch (Exception ex) {
                        imgLabel.setText("...");
                        imgLabel.setForeground(TEXTO_SEC);
                    }
                }
            };
            imgWorker.execute();
        }

        // Info central
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.decode("#333333"));
        info.setBorder(new EmptyBorder(10, 12, 10, 8));

        JLabel nome = new JLabel("<html><b>" + serie.getNome() + "</b></html>");
        nome.setForeground(TEXTO);
        nome.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel nota = new JLabel("Nota: " + serie.getNota());
        nota.setForeground(DESTAQUE);
        nota.setFont(new Font("Arial", Font.PLAIN, 11));

        String estadoTexto = switch (serie.getEstado()) {
            case "Running"  -> "[ON] ";
            case "Ended"    -> "[FIM] ";
            case "Canceled" -> "[CAN] ";
            default         -> "[?] ";
        };

        JLabel estado = new JLabel(estadoTexto + serie.getEstado());
        estado.setForeground(TEXTO_SEC);
        estado.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel data = new JLabel("Estreia: " + serie.getDataEstreia());
        data.setForeground(TEXTO_SEC);
        data.setFont(new Font("Arial", Font.PLAIN, 11));

        info.add(nome);
        info.add(Box.createVerticalStrut(3));
        info.add(nota);
        info.add(Box.createVerticalStrut(2));
        info.add(estado);
        info.add(Box.createVerticalStrut(2));
        info.add(data);

        // Botao remover
        JButton btnRemover = new JButton("X");
        btnRemover.setBackground(REMOVER);
        btnRemover.setForeground(TEXTO);
        btnRemover.setFont(new Font("Arial", Font.BOLD, 12));
        btnRemover.setBorderPainted(false);
        btnRemover.setFocusPainted(false);
        btnRemover.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemover.setPreferredSize(new Dimension(60, 90));
        btnRemover.setToolTipText("Remover da lista");

        btnRemover.addActionListener(e -> {
            switch (tipo) {
                case "favoritas"    -> usuario.removerFavorita(serie);
                case "assistidas"   -> usuario.removerAssistida(serie);
                case "querassistir" -> usuario.removerParaAssistir(serie);
            }
            persistencia.salvar(usuario);
            lista.remove(serie);
            atualizarColuna(painelCards, lista, tipo, lblCount);
        });

        card.add(imgLabel, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(btnRemover, BorderLayout.EAST);

        return card;
    }

    private JButton criarBotaoOrdem(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(Color.decode("#2A2A2A"));
        btn.setForeground(TEXTO_SEC);
        btn.setFont(new Font("Arial", Font.PLAIN, 11));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(4, 10, 4, 10));
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