package skamila.tetris.block;

import java.util.Random;

import skamila.tetris.block.states.Point;
import skamila.tetris.board.Board;

public class BlockImp implements Block {

    private BlockState[] states;

    private int activeStateIndex;

    private int shiftVertical;

    private int shiftHorizontal;

    public BlockImp(BlockState[] states) {

        this.states = states;

    }

    @Override
    public BlockState getActiveState() {

        return states[activeStateIndex];
    }

    @Override
    public void rotate(Board board) {

        if (!isBlockVisible())
            return;

        int newStateIndex = activeStateIndex + 1;
        if (newStateIndex == states.length)
            newStateIndex = 0;

        int newShiftHorizontal = shiftHorizontal;
        boolean rightShift = false;
        boolean leftShift = false;

        Point[] points = states[newStateIndex].getPositionValues();

        for (int i = 0; i < points.length; i++) {

            if (points[i].getX() + newShiftHorizontal < 0 || isLeftOccupied(board, points[i])) {
                if (leftShift == true)
                    return;
                newShiftHorizontal++;
                points = movePoints(points, 1);
                rightShift = true;
                i = -1;
            }
            if (
                points[i].getX() + newShiftHorizontal >= board.getWidth() || isRightOccupied(
                    board,
                    points[i]
                )
            ) {
                if (rightShift == true)
                    return;
                newShiftHorizontal--;
                points = movePoints(points, -1);
                leftShift = true;
                i = -1;
            }

        }

        shiftHorizontal = newShiftHorizontal;
        activeStateIndex = newStateIndex;
    }

    @Override
    public void randomizeActiveState() {

        Random generator = new Random();
        activeStateIndex = generator.nextInt(states.length);
    }

    @Override
    public void moveLeft(Board board) {

        if (!isBlockVisible())
            return;

        Point[] points = states[activeStateIndex].getPositionValues();

        for (int i = 0; i < points.length; i++) {
            if (points[i].getX() + shiftHorizontal - 1 < 0)
                return;
            if (isLeftOccupied(board, points[i]))
                return;
        }
        shiftHorizontal--;
    }

    @Override
    public void moveRight(Board board) {

        if (!isBlockVisible())
            return;

        Point[] points = states[activeStateIndex].getPositionValues();

        for (int i = 0; i < points.length; i++) {
            if (points[i].getX() + shiftHorizontal + 1 >= board.getWidth())
                return;
            if (isRightOccupied(board, points[i]))
                return;
        }

        shiftHorizontal++;
    }

    @Override
    public void moveDown(Board board) {

        Point[] points = states[activeStateIndex].getPositionValues();

        for (int i = 0; i < points.length; i++) {

            if (points[i].getY() + shiftVertical + 1 >= board.getHeight())
                return;
            if (points[i].getY() + shiftVertical <= -2)
                continue;
            if (isUnderOccupied(board, points[i]))
                return;
        }

        shiftVertical++;

        // zatapianie. tu czy nie tu?
    }

    private boolean isBlockVisible() {

        Point[] points = states[activeStateIndex].getPositionValues();

        for (int i = 0; i < points.length; i++) {
            if (points[i].getY() + shiftVertical >= 0)
                return true;
        }

        return false;
    }

    private boolean isUnderOccupied(Board board, Point point) {

        return board
            .getField(
                point.getX() + shiftHorizontal,
                point.getY() + shiftVertical + 1
            )
            .isOccupied();
    }

    private boolean isLeftOccupied(Board board, Point point) {

        if (point.getY() + shiftVertical < 0)
            return false;

        return board
            .getField(
                point.getX() + shiftHorizontal - 1,
                point.getY() + shiftVertical
            )
            .isOccupied();
    }

    private boolean isRightOccupied(Board board, Point point) {

        if (point.getY() + shiftVertical < 0)
            return false;

        return board
            .getField(
                point.getX() + shiftHorizontal + 1,
                point.getY() + shiftVertical
            )
            .isOccupied();
    }

    private Point[] movePoints(Point[] points, int shiftHorizontal) {

        Point[] movedPoints = {
            new Point(points[0].getX() + shiftHorizontal, points[0].getY()),
            new Point(points[1].getX() + shiftHorizontal, points[1].getY()),
            new Point(points[2].getX() + shiftHorizontal, points[2].getY()),
            new Point(points[3].getX() + shiftHorizontal, points[3].getY())
        };

        return movedPoints;

    }

    public BlockState getShiftedActiveState() {

        BlockState activeState = states[activeStateIndex];
        Point[] points = activeState.getPositionValues();
        Point[] shiftedPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            shiftedPoints[i] = new Point(
                points[i].getX() + shiftHorizontal,
                points[i].getY() + shiftVertical
            );
        }

        return new BlockStateImp(shiftedPoints);
    }

    public void countInitialShift(Board board) {

        shiftHorizontal = (board.getWidth() - countWidth()) / 2 - firstindexX();
        shiftVertical -= countHeight() + firstindexY();
    }

    private int firstindexX() {

        Point[] points = states[activeStateIndex].getPositionValues();
        int firstindexX = points[0].getX();

        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() < firstindexX)
                firstindexX = points[i].getX();
        }

        return firstindexX;
    }

    private int firstindexY() {

        Point[] points = states[activeStateIndex].getPositionValues();
        int firstindexY = points[0].getY();

        for (int i = 1; i < points.length; i++) {
            if (points[i].getY() < firstindexY)
                firstindexY = points[i].getY();
        }

        return firstindexY;
    }

    private int countWidth() {

        Point[] points = states[activeStateIndex].getPositionValues();
        boolean[] blockWidth = new boolean[4];
        int width = 0;

        for (int i = 0; i < points.length; i++) {
            blockWidth[points[i].getX()] = true;
        }

        for (int i = 0; i < blockWidth.length; i++) {
            if (blockWidth[i] == true)
                width++;
        }
        return width;
    }

    private int countHeight() {

        Point[] points = states[activeStateIndex].getPositionValues();
        boolean[] blockHeight = new boolean[4];
        int height = 0;

        for (int i = 0; i < points.length; i++) {
            blockHeight[points[i].getY()] = true;
        }

        for (int i = 0; i < blockHeight.length; i++) {
            if (blockHeight[i] == true)
                height++;
        }
        return height;
    }

}
