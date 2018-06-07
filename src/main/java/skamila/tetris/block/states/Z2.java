package skamila.tetris.block.states;

import skamila.tetris.block.BlockState;
import skamila.tetris.block.StatePoint;

public class Z2 implements BlockState {

    StatePoint[] state;

    // X
    // X X
    // X

    Z2() {

        state = new StatePoint[] {
            new StatePoint(2, 0),
            new StatePoint(1, 1),
            new StatePoint(2, 1),
            new StatePoint(1, 2),
        };
    }

    @Override
    public StatePoint[] getPositionValues() {

        return state;
    }
}