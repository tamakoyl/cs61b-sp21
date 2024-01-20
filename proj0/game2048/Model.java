//package game2048;
//
//import java.util.Formatter;
//import java.util.Observable;
//
//
///**
// * The state of a game of 2048.
// *
// * @author tamako
// */
//public class Model extends Observable {
//    /**
//     * Current contents of the board.
//     */
//    private Board board;
//    /**
//     * Current score.
//     */
//    private int score;
//    /**
//     * Maximum score so far.  Updated when game ends.
//     */
//    private int maxScore;
//    /**
//     * True iff game is ended.
//     */
//    private boolean gameOver;
//
//    /* Coordinate System: column C, row R of the board (where row 0,
//     * column 0 is the lower-left corner of the board) will correspond
//     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
//     */
//
//    /**
//     * Largest piece value.
//     */
//    public static final int MAX_PIECE = 2048;
//
//    /**
//     * A new 2048 game on a board of size SIZE with no pieces
//     * and score 0.
//     */
//    public Model(int size) {
//        board = new Board(size);
//        score = maxScore = 0;
//        gameOver = false;
//    }
//
//    /**
//     * A new 2048 game where RAWVALUES contain the values of the tiles
//     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
//     * to the bottom-left corner. Used for testing purposes.
//     */
//    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
//        int size = rawValues.length;
//        board = new Board(rawValues, score);
//        this.score = score;
//        this.maxScore = maxScore;
//        this.gameOver = gameOver;
//    }
//
//    /**
//     * Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
//     * 0 <= COL < size(). Returns null if there is no tile there.
//     * Used for testing. Should be deprecated and removed.
//     * tile: 是瓦砖的意思
//     */
//    public Tile tile(int col, int row) {
//        return board.tile(col, row);
//    }
//
//    /**
//     * Return the number of squares on one side of the board.
//     * Used for testing. Should be deprecated and removed.
//     */
//    public int size() {
//        return board.size();
//    }
//
//    /**
//     * Return true iff the game is over (there are no moves, or
//     * there is a tile with value 2048 on the board).
//     */
//    public boolean gameOver() {
//        checkGameOver();
//        if (gameOver) {
//            maxScore = Math.max(score, maxScore);
//        }
//        return gameOver;
//    }
//
//    /**
//     * Return the current score.
//     */
//    public int score() {
//        return score;
//    }
//
//    /**
//     * Return the current maximum game score (updated at end of game).
//     */
//    public int maxScore() {
//        return maxScore;
//    }
//
//    /**
//     * Clear the board to empty and reset the score.
//     */
//    public void clear() {
//        score = 0;
//        gameOver = false;
//        board.clear();
//        setChanged();
//    }
//
//    /**
//     * Add TILE to the board. There must be no Tile currently at the
//     * same position.
//     */
//    public void addTile(Tile tile) {
//        board.addTile(tile);
//        checkGameOver();
//        setChanged();
//    }
//
//    /**
//     * Tilt the board toward SIDE. Return true iff this changes the board.
//     * <p>
//     * 1. If two Tile objects are adjacent in the direction of motion and have
//     * the same value, they are merged into one Tile of twice the original
//     * value and that new value is added to the score instance variable
//     * 2. A tile that is the result of a merge will not merge again on that
//     * tilt. So each move, every tile will only ever be part of at most one
//     * merge (perhaps zero).
//     * 3. When three adjacent tiles in the direction of motion have the same
//     * value, then the leading two tiles in the direction of motion merge,
//     * and the trailing tile does not.
//     */
//
//    public boolean tilt(Side side) {
//        boolean changed;
//        changed = false;
//        // System.out.println(board.toString());
//        // TODO: Modify this.board (and perhaps this.score) to account
//        // for the tilt to the Side SIDE. If the board changed, set the
//        // changed locald variable to true.
//
//        // }
//        changed = handleNorth(side);
//        board.setViewingPerspective(Side.NORTH);
//        checkGameOver();
//        if (changed) {
//            setChanged();
//        }
//        return changed;
//    }
//
//    /**
//     * 找到tile在tile.col这一列，需要移动的最终位置
//     *
//     * @param from
//     * @param to
//     * @param tile
//     * @return
//     */
//    private int getRowDesPos(int from, int to, Tile tile) {
//        int colNo = tile.col();
//        int lastEmptyTile = -1;
//        for (int i = from + 1; i <= to; i++) {
//            Tile cur = board.tile(colNo, i);
//            //合并
//            if (cur != null && cur.value() == tile.value()) {
//                return i;
//                //空位要移动
//            } else if (cur == null) {
//                lastEmptyTile = i;
//            } else {
//                //不动
//                return -1;
//            }
//        }
//        return lastEmptyTile;
//    }
//
//    private void handleColMerge(int rowNo) {
//
//    }
//
//
//    private boolean handleOneDir(Side side) {
//        board.setViewingPerspective(side);
//        int size = board.size();
//        for (int c = 0; c < size; c++) {
//            for (int r = size - 1; r >= 0; r--) {
//                Tile tile = board.tile(side.col(c, r, size), side.row(c, r, size));
//                if (tile != null) {
//                    int rowDesPos = getRowDesPos(tile.row(), board.size() - 1, tile);
//                    if (rowDesPos != -1) board.move(tile.col(), rowDesPos, tile);
//                }
//            }
//        }
//        return true;
//    }
//
//
//    private boolean handleWest(Side side) {
//        board.setViewingPerspective(side);
//        int size = board.size();
//        for (int j = 0; j < size; j++) {
//            for (int i = size - 1; i >= 0; i--) {
//                Tile tile = board.tile(j, i);
//                if (tile != null) {
//                    int lastEmptyTile = -1;
//                    boolean sameTilt = false;
//                    int tmp = tile.row();
//                    while (--tmp > 0) {
//                        Tile target = board.tile(tile.col(), tmp);
//                        if (target != null && target.value() == tile.value()) {
//                            board.move(tile.col(), tmp,tile);
//                            sameTilt = true;
//                            break;
//                        } else if (target == null) {
//                            lastEmptyTile = tmp;
//                        } else {
//                            break;
//                        }
//                    }
//                    if (!sameTilt && lastEmptyTile != -1) {
//                        board.move(tile.col(), lastEmptyTile, tile);
//                    }
//                }
//            }
//        }
//        return true;
//    }
//    private boolean handleNorth(Side side) {
//        board.setViewingPerspective(side);
//        int size = board.size();
//        for (int j = 0; j < size; j++) {
//            for (int i = size - 1; i >= 0; i--) {
//                Tile tile = board.tile(j, i);
//                if (tile != null) {
//                    int lastEmptyTile = -1;
//                    boolean sameTilt = false;
//                    int tmp = tile.row();
//                    while (++tmp < size) {
//                        Tile target = board.tile(tile.col(), tmp);
//                        if (target != null && target.value() == tile.value()) {
//                            board.move(tile.col(), tmp,tile);
//                            sameTilt = true;
//                            break;
//                        } else if (target == null) {
//                            lastEmptyTile = tmp;
//                        } else {
//                            break;
//                        }
//                    }
//                    if (!sameTilt && lastEmptyTile != -1) {
//                        board.move(tile.col(), lastEmptyTile, tile);
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//
//    /**
//     * Checks if the game is over and sets the gameOver variable
//     * appropriately.
//     */
//    private void checkGameOver() {
//        gameOver = checkGameOver(board);
//    }
//
//    /**
//     * Determine whether game is over.
//     */
//    private static boolean checkGameOver(Board b) {
//        return maxTileExists(b) || !atLeastOneMoveExists(b);
//    }
//
//    /**
//     * Returns true if at least one space on the Board is empty.
//     * Empty spaces are stored as null.
//     * 检查board的values是否存在空的
//     */
//    public static boolean emptySpaceExists(Board b) {
//        int size = b.size();
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                if (b.tile(i, j) == null) return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Returns true if any tile is equal to the maximum valid value.
//     * Maximum valid value is given by MAX_PIECE. Note that
//     * given a Tile object t, we get its value with t.value().
//     */
//    public static boolean maxTileExists(Board b) {
//        int size = b.size();
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                Tile tile = b.tile(i, j);
//                if (tile != null && tile.value() == Model.MAX_PIECE) return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Returns true if there are any valid moves on the board.
//     * There are two ways that there can be valid moves:
//     * 1. There is at least one empty space on the board.
//     * 2. There are two adjacent tiles with the same value.
//     */
//    public static boolean atLeastOneMoveExists(Board b) {
//        if (emptySpaceExists(b)) return true;
//        int size = b.size();
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                int val = b.tile(i, j).value();
//                //up
//                int upY = j - 1;
//                if (upY >= 0 && b.tile(i, upY).value() == val) return true;
//                //down
//                int downY = j + 1;
//                if (downY < size && b.tile(i, downY).value() == val) return true;
//                //left
//                int leftX = i - 1;
//                if (leftX > 0 && b.tile(leftX, j).value() == val) return true;
//                //right
//                int rightX = i + 1;
//                if (rightX < size && b.tile(rightX, j).value() == val) return true;
//            }
//        }
//        return false;
//    }
//
//
//    @Override
//    /** Returns the model as a string, used for debugging. */
//    public String toString() {
//        Formatter out = new Formatter();
//        out.format("%n[%n");
//        for (int row = size() - 1; row >= 0; row -= 1) {
//            for (int col = 0; col < size(); col += 1) {
//                if (tile(col, row) == null) {
//                    out.format("|    ");
//                } else {
//                    out.format("|%4d", tile(col, row).value());
//                }
//            }
//            out.format("|%n");
//        }
//        String over = gameOver() ? "over" : "not over";
//        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
//        return out.toString();
//    }
//
//    @Override
//    /** Returns whether two models are equal. */
//    public boolean equals(Object o) {
//        if (o == null) {
//            return false;
//        } else if (getClass() != o.getClass()) {
//            return false;
//        } else {
//            return toString().equals(o.toString());
//        }
//    }
//
//    @Override
//    /** Returns hash code of Model’s string. */
//    public int hashCode() {
//        return toString().hashCode();
//    }
//}


package game2048;

import java.util.*;
import java.util.function.Predicate;

/** The state of a game of 2048.
 *  @author Exuanbo
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        Set<Tile> changedTiles = new HashSet<>();
        board.setViewingPerspective(side);
        for (int row = board.size() - 2; row >= 0; row--) {
            for (int col = 0; col < board.size(); col++) {
                Tile tile = board.tile(col, row);
                if (tile == null) {
                    continue;
                }
                VTile vTile = new VTile(new Coordinate(col, row), tile);
                boolean isChangedAfterMove = moveTileUp(vTile, changedTiles);
                if (isChangedAfterMove) {
                    changed = true;
                }
            }
        }
        board.setViewingPerspective(Side.NORTH);

        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    private boolean moveTileUp(VTile vTile, Set<Tile> changedTiles) {
        int targetRow;
        VTile nearest = findNearestTileAbove(vTile);
        if (nearest == null) {
            targetRow = board.size() - 1;
        } else if (vTile.actualTile.value() == nearest.actualTile.value() && !changedTiles.contains(nearest.actualTile)) {
            targetRow = nearest.row;
        } else if (nearest.row == vTile.row + 1) {
            return false;
        } else {
            targetRow = nearest.row - 1;
        }
        boolean isMerged = board.move(vTile.col, targetRow, vTile.actualTile);
        if (isMerged) {
            score += vTile.actualTile.next().value();
            changedTiles.add(board.tile(vTile.col, targetRow));
        }
        return true;
    }

    private VTile findNearestTileAbove(VTile vTile) {
        for (int row = vTile.row + 1; row < board.size(); row++) {
            Tile actualTile = board.tile(vTile.col, row);
            if (actualTile != null) {
                return new VTile(new Coordinate(vTile.col, row), actualTile);
            }
        }
        return null;
    }

    private static class VTile {
        int col;
        int row;
        Tile actualTile;
        public VTile(Coordinate c, Tile t) {
            col = c.col;
            row = c.row;
            actualTile = t;
        }
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        return TileUtils.some(Objects::isNull, b);
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        return TileUtils.some(tile -> tile != null && tile.value() == MAX_PIECE, b);
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if (emptySpaceExists(b)) {
            return true;
        }
        Predicate<Tile> hasAdjacentTileWithSameValue = (Tile tile) -> {
            for (Coordinate coordinate : getAdjacentCoordinates(tile, b)) {
                Tile adjacentTile = b.tile(coordinate.col, coordinate.row);
                if (tile.value() == adjacentTile.value()) {
                    return true;
                }
            }
            return false;
        };
        return TileUtils.some(hasAdjacentTileWithSameValue, b);
    }

    private static Coordinate[] getAdjacentCoordinates(Tile tile, Board b) {
        int col = tile.col();
        int row = tile.row();
        Coordinate[] coordinates = Coordinate.of(new int[][]{
                {col, row + 1}, {col + 1, row},
                {col, row - 1}, {col - 1, row}
        });
        return Arrays.stream(coordinates).filter(coordinate -> isValidCoordinate(coordinate, b)).toArray(Coordinate[]::new);
    }

    private static boolean isValidCoordinate(Coordinate coordinate, Board b) {
        int col = coordinate.col;
        int row = coordinate.row;
        return col >= 0 && col < b.size() && row >= 0 && row < b.size();
    }

    private static class Coordinate {
        int col;
        int row;
        public Coordinate(int c, int r) {
            col = c;
            row = r;
        }
        public static Coordinate[] of(int[][] values) {
            Coordinate[] coordinates = new Coordinate[values.length];
            for (int i = 0; i < values.length; i++) {
                int[] coordinate = values[i];
                coordinates[i] = new Coordinate(coordinate[0], coordinate[1]);
            }
            return coordinates;
        }
    }

    private static class TileUtils {
        public static boolean some(Predicate<Tile> tilePredicate, Board b) {
            for (int col = 0; col < b.size(); col++) {
                for (int row = 0; row < b.size(); row++) {
                    if (tilePredicate.test(b.tile(col, row))) {
                        return true;
                    }
                }
            }
            return false;
        }
    }



    @Override
    /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
