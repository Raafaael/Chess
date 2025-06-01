package model;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(char color,int row,int col) {
        super(color,row,col);
    }

    @Override
    public char getTypeChar() { return 'R'; }

    /* ---------- movimentos ---------- */
    @Override
    public List<int[]> pieceMovement(Board board) {

        List<int[]> safe = new ArrayList<>();
        int[][] dir = { {-1,0},{1,0},{0,-1},{0,1} };

        for (int[] d : dir) {
            int r = row + d[0];
            int c = col + d[1];

            while (board.isValidPosition(r,c)) {

                Piece tgt = board.getPiece(r,c);
                if (tgt!=null && tgt.getColor()==color) break; // próprio

                if (tgt instanceof King) break;               // não mira Rei

                /* ----- simula ----- */
                int fromR = row, fromC = col;
                boolean movedFlag = hasMoved;

                board.makeMove(fromR,fromC,r,c);
                boolean inCheck = board.isInCheck(color);
                board.undoMove(fromR,fromC,r,c,tgt);
                setHasMoved(movedFlag);

                if (!inCheck) safe.add(new int[]{r,c});

                if (tgt!=null) break;                         // captura, pára
                r += d[0]; c += d[1];
            }
        }
        return safe;
    }

    /* ---------- checagem pontual ---------- */
    @Override
    public boolean canMove(int fr,int fc,int tr,int tc,Board board) {

        if (fr!=tr && fc!=tc) return false;            // não é linha/coluna

        /* caminho livre? */
        int dR = Integer.compare(tr,fr);
        int dC = Integer.compare(tc,fc);
        for (int r=fr+dR, c=fc+dC; r!=tr||c!=tc; r+=dR,c+=dC)
            if (!board.isEmpty(r,c)) return false;

        Piece tgt = board.getPiece(tr,tc);
        if (tgt!=null) {
            if (tgt.getColor()==color) return false;
            if (tgt instanceof King)   return false;
        }

        int fromR = fr, fromC = fc;
        boolean movedFlag = hasMoved;

        board.makeMove(fromR,fromC,tr,tc);
        boolean inCheck = board.isInCheck(color);
        board.undoMove(fromR,fromC,tr,tc,tgt);
        setHasMoved(movedFlag);

        return !inCheck;
    }
}
