package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import model.ChessGame;

/** Painel Java2D responsável por desenhar o tabuleiro e capturar cliques. */
public class BoardPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int TILE = 64;
    private static final int SIZE = 8;

    /* [bank][type]  bank 0 = ciano (brancas), 1 = roxo (pretas) */
    private final BufferedImage[][] img = new BufferedImage[2][6];

    private final ChessGame game  = ChessGame.getInstance();

    private java.util.List<Point> reachable = java.util.Collections.emptyList();
    private Point selected = null;

    public BoardPanel() {
        setPreferredSize(new Dimension(TILE * SIZE, TILE * SIZE));
        loadImages();

        /* listener único para cliques de jogo / popup de salvar */
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent evt) {

                /* botão direito → salvar (4ª iteração) */
                if (SwingUtilities.isRightMouseButton(evt)) {
                    showSavePopup(evt.getX(), evt.getY());
                    return;
                }

                int col = evt.getX() / TILE;
                int row = evt.getY() / TILE;

                /* ---------- lógica de seleção / movimento ---------- */
                if (selected == null) {
                    if (game.selectPiece(row, col)) {
                        selected  = new Point(col, row);
                        reachable = game.getReachableSquares();
                    }
                } else {
                	// BoardPanel.java  (dentro do mouseClicked)
                	if (game.moveTo(row, col)) {

                	    /* --- promoção --- */
                	    if (game.isPromotionPending()) {
                	        showPromotionMenu(row, col);
                	    }

                	    /* --- fim de jogo --- */
                	    if (game.isGameEnded()) {
                	        String msg = switch (game.getWinner()) {
                	            case 'W' -> "Brancas vencem por xeque-mate!";
                	            case 'B' -> "Pretas vencem por xeque-mate!";
                	            default  -> "Empate por stalemate.";
                	        };

                	        // diálogo pertence à camada Controller/View, então pode usar JOptionPane aqui
                	        javax.swing.JOptionPane.showMessageDialog(
                	        	    BoardPanel.this,              // ← o componente “dono” do diálogo
                	        	    msg,
                	        	    "Fim de jogo",
                	        	    JOptionPane.INFORMATION_MESSAGE);

                	        game.resetGame();          // zera modelo
                	        controller.MainWindow.disposeCurrent();   // fecha a janela do tabuleiro
                	        new controller.StartWindow();             // volta ao menu inicial
                	        return;                    // nada mais a fazer neste clique
                	    }

                	    selected  = null;
                	    reachable = java.util.Collections.emptyList();
                    } else if (game.selectPiece(row, col)) {
                        /* troca de peça selecionada */
                        selected  = new Point(col, row);
                        reachable = game.getReachableSquares();
                    } else {
                        selected  = null;
                        reachable = java.util.Collections.emptyList();
                    }
                }

                repaint();
            }
        });
    }

    /* ---------- Menus pop-up ---------- */
    private void showPromotionMenu(int boardRow, int boardCol) {
        JPopupMenu menu = new JPopupMenu();
        String[] txt  = {"Queen", "Rook", "Bishop", "Knight"};
        char[]   code = {'Q',     'R',    'B',     'N'};

        for (int i = 0; i < txt.length; i++) {
            char pieceCode = code[i];
            JMenuItem item = new JMenuItem(txt[i]);
            item.addActionListener(_ -> {
                game.promote(pieceCode);
                repaint();
            });
            menu.add(item);
        }
        menu.show(this,
                  boardCol * TILE + TILE / 2,
                  boardRow * TILE + TILE / 2);
    }

    private void showSavePopup(int x, int y) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem("Salvar partida…");
        item.addActionListener(_ ->
            JOptionPane.showMessageDialog(
                this,
                "Função de salvar será implementada na 4ª iteração."));
        menu.add(item);
        menu.show(this, x, y);
    }

    /* ---------- imagens ---------- */
    private void loadImages() {
        try {
            img[0][0] = ImageIO.read(getClass().getResource("/images/CyanP.png"));
            img[0][1] = ImageIO.read(getClass().getResource("/images/CyanR.png"));
            img[0][2] = ImageIO.read(getClass().getResource("/images/CyanN.png"));
            img[0][3] = ImageIO.read(getClass().getResource("/images/CyanB.png"));
            img[0][4] = ImageIO.read(getClass().getResource("/images/CyanQ.png"));
            img[0][5] = ImageIO.read(getClass().getResource("/images/CyanK.png"));

            img[1][0] = ImageIO.read(getClass().getResource("/images/PurpleP.png"));
            img[1][1] = ImageIO.read(getClass().getResource("/images/PurpleR.png"));
            img[1][2] = ImageIO.read(getClass().getResource("/images/PurpleN.png"));
            img[1][3] = ImageIO.read(getClass().getResource("/images/PurpleB.png"));
            img[1][4] = ImageIO.read(getClass().getResource("/images/PurpleQ.png"));
            img[1][5] = ImageIO.read(getClass().getResource("/images/PurpleK.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private BufferedImage imageFor(String code) {
        if (code == null) return null;

        boolean black  = Character.isUpperCase(code.charAt(0));
        int bank       = black ? 1 : 0;
        char pieceChar = Character.toUpperCase(code.charAt(0));

        switch (pieceChar) {
            case 'P': return img[bank][0];
            case 'R': return img[bank][1];
            case 'N': return img[bank][2];
            case 'B': return img[bank][3];
            case 'Q': return img[bank][4];
            case 'K': return img[bank][5];
            default : return null;
        }
    }

    /* ---------- Desenho ---------- */
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {

                /* casa base */
                Color base = (r + c) % 2 == 0 ? Color.LIGHT_GRAY
                                              : Color.DARK_GRAY;
                g2.setColor(base);
                g2.fillRect(c * TILE, r * TILE, TILE, TILE);

                /* destinos possíveis */
                if (reachable.contains(new Point(c, r))) {
                    g2.setColor(new Color(255, 105, 180, 130)); // rosa
                    g2.fillRect(c * TILE, r * TILE, TILE, TILE);
                }

                /* peça */
                BufferedImage imgPiece = imageFor(game.getPieceCode(r, c));
                if (imgPiece != null) {
                    g2.drawImage(imgPiece,
                                 c * TILE, r * TILE,
                                 TILE, TILE, null);
                }
            }
        }
    }
}