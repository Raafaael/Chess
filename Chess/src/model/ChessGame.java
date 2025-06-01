package model;

import java.util.ArrayList;
import java.util.List;

/* Façade do Model: único ponto de acesso da View/Controller */
public class ChessGame {

    /* ---------- Singleton ---------- */
    private static ChessGame instance;
    public static ChessGame getInstance() {
        if (instance == null) instance = new ChessGame();
        return instance;
    }

    /* ---------- estado ---------- */
    private final Board board;
    private Piece  selectedPiece;
    private char   currentTurn = 'W';

    /* fim de jogo */
    private boolean gameEnded   = false;
    private char    winner      = '\0';   // 'W', 'B', '=' ou '\0'

    /* en-passant: {row,col} atrás do peão que avançou duas casas */
    private int[] enPassantTarget;

    /* promoção */
    private boolean promotionPending = false;
    private int     promoRow, promoCol;

    private ChessGame() {
        board = Board.getInstance();
    }

    /* Brancas = 'W'   Pretas = 'B' */
    public char  getCurrentTurn()        { return currentTurn; }
    public boolean isGameEnded()         { return gameEnded;   }
    public char  getWinner()             { return winner;      }

    /* Reinicia completamente a partida. */
    public void resetGame() {
        board.setupInitialPosition();
        currentTurn      = 'W';
        selectedPiece    = null;
        enPassantTarget  = null;
        promotionPending = false;
        gameEnded        = false;
        winner           = '\0';
    }

    /* Seleciona a peça da vez situada em (row,col) */
    public boolean selectPiece(int row, int col) {
        Piece p = board.getPiece(row, col);
        if (p != null && p.getColor() == currentTurn) {
            selectedPiece = p;
            return true;
        }
        return false;
    }

    /* Executa um lance (ou roque) da peça previamente selecionada */
    public boolean moveTo(int row, int col) {
        if (gameEnded || selectedPiece == null) return false;

        /* -------- roque -------- */
        Piece target = board.getPiece(row, col);
        if (selectedPiece instanceof King && target instanceof Rook
                && target.getColor() == selectedPiece.getColor()) {
            boolean ok = tryCastle((King) selectedPiece, (Rook) target);
            if (ok) {
                endTurn();
                enPassantTarget = null;
            }
            return ok;
        }

        int fr = selectedPiece.getRow();
        int fc = selectedPiece.getCol();

        if (!selectedPiece.canMove(fr, fc, row, col, board)) return false;

        /* -------- en-passant captura -------- */
        Piece captured = board.getPiece(row, col);
        if (captured == null && enPassantTarget != null
                && row == enPassantTarget[0] && col == enPassantTarget[1]
                && selectedPiece instanceof Pawn) {

            int dir = (currentTurn == 'W') ? 1 : -1;   // peão inimigo atrás
            captured = board.getPiece(row + dir, col);
            board.setPiece(row + dir, col, null);      // remove-o
        }

        board.makeMove(fr, fc, row, col);

        /* marca alvo e.p. se peão andou duas casas */
        setEnPassantIfDoublePush(selectedPiece, fr, row, col);

        /* -------- promoção -------- */
        if (selectedPiece instanceof Pawn) {
            boolean promotable = (selectedPiece.getColor() == 'W' && row == 0)
                               || (selectedPiece.getColor() == 'B' && row == 7);
            if (promotable) {
                promotionPending = true;
                promoRow = row;
                promoCol = col;
            }
        }

        endTurn();
        checkEndOfGame();          // <-- avaliação concentrada aqui
        return true;
    }

    /* ---------------- getters auxiliares ---------------- */

    public int[] getEnPassantTarget() { return enPassantTarget; }

    public List<java.awt.Point> getReachableSquares() {
        List<java.awt.Point> res = new ArrayList<>();
        if (selectedPiece == null) return res;
        for (int[] mv : selectedPiece.pieceMovement(board))
            res.add(new java.awt.Point(mv[1], mv[0]));
        return res;
    }

    /* código interno da peça ('p','P','q'…) ou null */
    public String getPieceCode(int row, int col) {
        Piece p = board.getPiece(row, col);
        if (p == null) return null;
        char pc = p.getTypeChar();
        return p.getColor() == 'W'
             ? String.valueOf(Character.toLowerCase(pc))
             : String.valueOf(Character.toUpperCase(pc));
    }

    public boolean isPromotionPending() { return promotionPending; }

    public void promote(char code) {
        Piece nova;
        char  color = board.getPiece(promoRow, promoCol).getColor();

        switch (code) {
            case 'Q' -> nova = new Queen (color, promoRow, promoCol);
            case 'R' -> nova = new Rook  (color, promoRow, promoCol);
            case 'B' -> nova = new Bishop(color, promoRow, promoCol);
            default  -> nova = new Knight(color, promoRow, promoCol);
        }
        board.setPiece(promoRow, promoCol, nova);
        promotionPending = false;
    }

    private void endTurn() {
        currentTurn   = (currentTurn == 'W') ? 'B' : 'W';
        selectedPiece = null;
    }

    /* avalia se a partida terminou após um lance */
    private void checkEndOfGame() {
        char enemy = currentTurn;          // já trocado por endTurn()

        if (board.isCheckmate(enemy)) {
            gameEnded = true;
            winner    = (enemy == 'W') ? 'B' : 'W';
            return;
        }
        if (!anyLegalMoves(enemy)) {
            gameEnded = true;
            winner    = '=';
        }
    }

    private boolean anyLegalMoves(char color) {
        for (Piece p : board.getAllPieces())
            if (p.getColor() == color && !p.pieceMovement(board).isEmpty())
                return true;
        return false;
    }

    /* --------------------------- roque ------------------------ */
    private boolean tryCastle(King king, Rook rook) {
        if (king.hasMoved() || rook.hasMoved()) return false;
        if (board.isInCheck(king.getColor()))   return false;

        int r   = king.getRow();
        int kc  = king.getCol();
        int rc  = rook.getCol();
        int dir = (rc < kc) ? -1 : 1;       // -1 = longo, +1 = curto

        int kingDst = kc + 2 * dir;
        int rookDst = kc + dir;

        /* casas vazias entre rei e torre */
        for (int c = kc + dir; c != rc; c += dir)
            if (!board.isEmpty(r, c)) return false;

        /* rei não atravessa casas atacadas */
        for (int c = kc + dir; c != kingDst + dir; c += dir) {
            Piece cap = board.getPiece(r, c);
            board.makeMove(r, kc, r, c);
            boolean check = board.isInCheck(king.getColor());
            board.undoMove(r, kc, r, c, cap);
            if (check) {
                king.setHasMoved(false);
                rook.setHasMoved(false);
            	return false;
            }
        }

        board.makeMove(r, kc, r, kingDst);
        board.makeMove(r, rc, r, rookDst);
        king.setHasMoved(true);
        rook.setHasMoved(true);
        return true;
    }

    /* ----------------------- en-passant ----------------------- */
    private void setEnPassantIfDoublePush(Piece p,int fromRow,int toRow,int col){
        if (p instanceof Pawn && Math.abs(toRow-fromRow)==2) {
            int dir = (p.getColor()=='W') ? -1 : 1;
            enPassantTarget = new int[]{ toRow - dir, col };
        } else {
            enPassantTarget = null;
        }
    }

    /* setter usado em testes, se necessário */
    public void setEnPassantTarget(int[] value) { enPassantTarget = value; }
}