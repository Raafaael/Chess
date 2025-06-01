package model;

import java.util.ArrayList;
import java.util.List;

/** Tabuleiro 8 × 8 – Singleton. */
public class Board {

    private static Board instance;

    /** matriz [linha][coluna] de casas */
    private final Square[][] squares = new Square[8][8];

    /** construtor privado – singleton */
    private Board() {
        initializeEmptyBoard();
        setupInitialPosition();
    }

    /* ---------- Singleton ---------- */

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    /* ---------- inicialização ---------- */

    private void initializeEmptyBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                squares[r][c] = new Square(null);
            }
        }
    }

    /* posiciona as peças na orientação “brancas embaixo” */
    public void setupInitialPosition() {
        initializeEmptyBoard();

        /* peões */
        for (int c = 0; c < 8; c++) {
            squares[6][c].setPiece(new Pawn('W', 6, c)); // brancos
            squares[1][c].setPiece(new Pawn('B', 1, c)); // pretos
        }

        /* linha de peças maiores – brancos */
        placePiece(new Rook  ('W', 7, 0));
        placePiece(new Knight('W', 7, 1));
        placePiece(new Bishop('W', 7, 2));
        placePiece(new Queen ('W', 7, 3));
        placePiece(new King  ('W', 7, 4));
        placePiece(new Bishop('W', 7, 5));
        placePiece(new Knight('W', 7, 6));
        placePiece(new Rook  ('W', 7, 7));

        /* pretas em cima */
        placePiece(new Rook  ('B', 0, 0));
        placePiece(new Knight('B', 0, 1));
        placePiece(new Bishop('B', 0, 2));
        placePiece(new Queen ('B', 0, 3));
        placePiece(new King  ('B', 0, 4));
        placePiece(new Bishop('B', 0, 5));
        placePiece(new Knight('B', 0, 6));
        placePiece(new Rook  ('B', 0, 7));
    }

    /* utilitário */
    private void placePiece(Piece p) {
        squares[p.getRow()][p.getCol()].setPiece(p);
    }

    /* ---------- consultas simples ---------- */

    public boolean isValidPosition(int r, int c) {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    public boolean isEmpty(int r, int c) {
        return getPiece(r, c) == null;
    }

    public boolean hasEnemyPiece(int r, int c, char myColor) {
        Piece p = getPiece(r, c);
        return p != null && p.getColor() != myColor;
    }

    public Piece getPiece(int r, int c) {
        return isValidPosition(r, c) ? squares[r][c].getPiece() : null;
    }

    public void setPiece(int r, int c, Piece p) {
        if (isValidPosition(r, c)) squares[r][c].setPiece(p);
    }

    /* ---------- movimentação bruta ---------- */

    public void makeMove(int fromR, int fromC, int toR, int toC) {
        Piece moving   = getPiece(fromR, fromC);

        setPiece(toR,   toC, moving);
        setPiece(fromR, fromC, null);

        if (moving != null) {
            moving.setPosition(toR, toC);
            moving.setHasMoved(true);
        }
    }

    public void undoMove(int fromR, int fromC, int toR, int toC,
                         Piece captured) {

        Piece moving = getPiece(toR, toC);
        setPiece(fromR, fromC, moving);
        setPiece(toR,   toC, captured);

        if (moving != null) {
            moving.setPosition(fromR, fromC);
        }
    }

    /* ---------- detecção de xeque ---------- */

    /** true se qualquer rei da cor estiver atacado */
    public boolean isInCheck(char color) {

        /* coleta todas as posições de rei dessa cor */
        List<int[]> kings = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = getPiece(r, c);
                if (p instanceof King && p.getColor() == color) {
                    kings.add(new int[]{r, c});
                }
            }
        }
        if (kings.isEmpty()) return false;

        char enemy = color == 'W' ? 'B' : 'W';
        for (int[] k : kings) {
            if (squareAttacked(k[0], k[1], enemy)) return true;
        }
        return false;
    }

    /* verifica ataques inimigos à casa (row,col) */
    private boolean squareAttacked(int r, int c, char byColor) {

        /* 1. cavalo */
        int[][] jump = {{-2,-1},{-2,1},{-1,-2},{-1,2},
                        { 1,-2},{ 1,2},{ 2,-1},{ 2,1}};
        for (int[] d : jump) {
            int nr = r + d[0], nc = c + d[1];
            Piece p = getPiece(nr, nc);
            if (p instanceof Knight && p.getColor() == byColor) return true;
        }

        /* 2. peões */
        int dir = byColor == 'W' ? -1 : 1;
        int[] dc = {-1, 1};
        for (int d : dc) {
            Piece p = getPiece(r - dir, c + d);
            if (p instanceof Pawn && p.getColor() == byColor) return true;
        }

        /* 3. linhas e colunas (torre / dama) */
        int[][] straight = {{1,0},{-1,0},{0,1},{0,-1}};
        if (rayHits(r, c, straight, byColor, true)) return true;

        /* 4. diagonais (bispo / dama) */
        int[][] diag = {{1,1},{1,-1},{-1,1},{-1,-1}};
        return rayHits(r, c, diag, byColor, false);
    }

    /* varre cada direção até encontrar peça que ataque essa reta */
    private boolean rayHits(int r, int c, int[][] dirs, char byColor,
                            boolean rookLike) {

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            while (isValidPosition(nr, nc)) {
                Piece p = getPiece(nr, nc);

                if (p != null) {
                    if (p.getColor() == byColor) {
                        if (rookLike) {
                            if (p instanceof Rook || p instanceof Queen)
                                return true;
                        } else {
                            if (p instanceof Bishop || p instanceof Queen)
                                return true;
                        }
                    }
                    break;  // primeira peça bloqueia
                }
                nr += d[0];
                nc += d[1];
            }
        }
        return false;
    }

    /* ---------- xeque-mate (usado apenas para popup) ---------- */

    public boolean isCheckmate(char color) {
        if (!isInCheck(color)) return false;

        /* tenta todos os lances para escapar */
        for (Piece p : getAllPieces()) {
            if (p.getColor() != color) continue;

            for (int[] mv : p.pieceMovement(this)) {
                int fr = p.getRow(), fc = p.getCol();
                int tr = mv[0], tc = mv[1];
                Piece captured = getPiece(tr, tc);

                makeMove(fr, fc, tr, tc);
                boolean stillInCheck = isInCheck(color);
                undoMove(fr, fc, tr, tc, captured);

                if (!stillInCheck) return false;
            }
        }
        return true;
    }

    /* ---------- util ---------- */

    public List<Piece> getAllPieces() {
        List<Piece> list = new ArrayList<>();
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (getPiece(r,c) != null) list.add(getPiece(r,c));
        return list;
    }
}
