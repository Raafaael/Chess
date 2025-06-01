package model;

import java.util.ArrayList;
import java.util.List;

/* Peão – inclui en-passant e filtragem contra xeque descoberto. */
public class Pawn extends Piece {

    public Pawn(char color,int row,int col) {
        super(color,row,col);
    }

    @Override
    public char getTypeChar() { return 'P'; }

    @Override
    public List<int[]> pieceMovement(Board board) {

        List<int[]> candidates = new ArrayList<>();
        int dir      = (color == 'W') ? -1 : 1;       // brancas sobem
        int startRow = (color == 'W') ? 6 : 1;
        int nextRow  = row + dir;

        /* 1. avança 1 casa */
        if (board.isValidPosition(nextRow,col) && board.isEmpty(nextRow,col))
            candidates.add(new int[]{nextRow,col});

        /* 2. avança 2 casas da linha inicial */
        int twoRow = row + 2*dir;
        if (row == startRow &&
            board.isEmpty(nextRow,col) &&
            board.isEmpty(twoRow ,col))
            candidates.add(new int[]{twoRow,col});

        /* 3. capturas diagonais normais */
        addCaptureIfEnemy(board, candidates, nextRow, col-1);
        addCaptureIfEnemy(board, candidates, nextRow, col+1);

        /* 4. en-passant */
        int[] ep = ChessGame.getInstance().getEnPassantTarget();
        if (ep != null && ep[0] == nextRow && Math.abs(ep[1]-col) == 1)
            candidates.add(new int[]{ep[0], ep[1]});

        /* ---------- filtra lances que deixam o rei em xeque ---------- */
        List<int[]> safe = new ArrayList<>();
        for (int[] mv : candidates)
            if (moveKeepsKingSafe(board, mv[0], mv[1])) safe.add(mv);

        return safe;
    }

    /* auxiliar – adiciona diagonal se inimigo e não for rei */
    private void addCaptureIfEnemy(Board b,List<int[]> list,int r,int c){
        if (!b.isValidPosition(r,c)) return;
        Piece tgt = b.getPiece(r,c);
        if (tgt != null && tgt.getColor() != color && !(tgt instanceof King))
            list.add(new int[]{r,c});
    }

    /* simula o lance e verifica se o rei permanece seguro */
    private boolean moveKeepsKingSafe(Board b,int toRow,int toCol) {

        ChessGame game = ChessGame.getInstance();
        int[] oldEP = game.getEnPassantTarget();          // preserva alvo EP

        /* ---- identifica en-passant ---- */
        boolean enPassant = oldEP != null &&
                            toRow == oldEP[0] &&
                            toCol == oldEP[1] &&
                            b.getPiece(toRow,toCol) == null;

        /* ---- prepara captura ---- */
        Piece capturedOnTarget = b.getPiece(toRow,toCol);
        Piece capturedBehind   = null;
        int   capRow = -1, capCol = -1;
        boolean capturedMovedFlag = false;

        if (enPassant) {
            capRow = toRow + (color=='W'?1:-1);
            capCol = toCol;
            capturedBehind = b.getPiece(capRow,capCol);
            if (capturedBehind != null) {
                capturedMovedFlag = capturedBehind.hasMoved();
                b.setPiece(capRow,capCol,null);           // remove-o
            }
            capturedOnTarget = null;                      // alvo vazio
        }

        /* ---- guarda origem antes de mover ---- */
        int fromRow = row;
        int fromCol = col;
        boolean originalMovedFlag = this.hasMoved;

        /* ---- simula ---- */
        b.makeMove(fromRow,fromCol,toRow,toCol);

        boolean inCheck = b.isInCheck(color);

        /* ---- desfaz ---- */
        b.undoMove(fromRow,fromCol,toRow,toCol,capturedOnTarget);
        this.setHasMoved(originalMovedFlag);              // restaura flag

        if (enPassant && capturedBehind != null) {
            capturedBehind.setHasMoved(capturedMovedFlag);
            b.setPiece(capRow,capCol,capturedBehind);     // devolve peão
        }

        /* restaura alvo en-passant */
        game.setEnPassantTarget(oldEP);

        return !inCheck;
    }


    @Override
    public boolean canMove(int fr,int fc,int tr,int tc,Board board) {
        for (int[] mv : pieceMovement(board))
            if (mv[0]==tr && mv[1]==tc) return true;
        return false;
    }
}
