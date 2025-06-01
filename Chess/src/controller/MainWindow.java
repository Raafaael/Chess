package controller;

import javax.swing.*;
import view.BoardPanel;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    /* Mantém a única instância visível */
    private static MainWindow current;

    /** Fecha (e invalida) a janela atual, caso exista */
    public static void disposeCurrent() {
        if (current != null) {
            current.dispose();
            current = null;
        }
    }

    public MainWindow() {
        /* fecha qualquer tabuleiro anterior antes de criar um novo */
        disposeCurrent();
        current = this;

        setTitle("Xadrez");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new BoardPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
