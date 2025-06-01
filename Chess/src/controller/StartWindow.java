package controller;

import javax.swing.*;
import java.awt.*;
import model.ChessGame;

/** Tela inicial (Controller externo) */
public class StartWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    public StartWindow() {
        setTitle("Xadrez – Início");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JButton btnNew  = new JButton("Nova Partida");
        JButton btnCont = new JButton("Continuar…");

        btnNew.addActionListener(_ -> {
            ChessGame.getInstance().resetGame();
            new MainWindow();
            dispose();
        });

        btnCont.addActionListener(_ -> JOptionPane.showMessageDialog(
            this,
            "Carregar partida estará disponível na 4ª iteração.",
            "Função ainda não implementada",
            JOptionPane.INFORMATION_MESSAGE));

        JPanel p = new JPanel(new GridLayout(2,1,10,10));
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        p.add(btnNew);
        p.add(btnCont);

        add(p);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartWindow::new);
    }
}
