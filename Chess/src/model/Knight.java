package model;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(char color,int row,int col) {
        super(color,row,col);
    }

    @Override
    public char getTypeChar() { return 'N'; }

    /* ---------- movimentos ---------- */
    @Override
    public List<int[]> pieceMovement(Board board) {

        List<int[]> safe = new ArrayList<>();
        int[][] step = {
            {-2,-1},{-2, 1},{-1,-2},{-1, 2},
            { 1,-2},{ 1, 2},{ 2,-1},{ 2, 1}
        };

        for (int[] d : step) {
            int toR = row + d[0];
            int toC = col + d[1];

            if (!board.isValidPosition(toR,toC)) continue;

            Piece tgt = board.getPiece(toR,toC);
            if (tgt != null) {
                if (tgt.getColor()==color) continue;
                if (tgt instanceof King)   continue;
            }

            int fromR = row, fromC = col;
            boolean movedFlag = hasMoved;

            board.makeMove(fromR,fromC,toR,toC);
            boolean inCheck = board.isInCheck(color);
            board.undoMove(fromR,fromC,toR,toC,tgt);
            setHasMoved(movedFlag);

            if (!inCheck) safe.add(new int[]{toR,toC});
        }
        return safe;
    }

    /* ---------- checagem pontual ---------- */
    @Override
    public boolean canMove(int fr,int fc,int tr,int tc,Board board) {

        int dR = Math.abs(tr-fr), dC = Math.abs(tc-fc);
        if (!((dR==2 && dC==1)||(dR==1 && dC==2))) return false;

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
