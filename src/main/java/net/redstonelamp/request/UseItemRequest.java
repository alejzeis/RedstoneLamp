package net.redstonelamp.request;

import net.redstonelamp.level.position.Position;
import net.redstonelamp.math.Vector3;

/**
 * Represents a Request to use an item.
 *
 * @author RedstoneLamp Team
 */
@Deprecated
public class UseItemRequest extends Request {

    public Vector3 blockVector;
    public byte face;
    public Position fPosition;

    public UseItemRequest(Vector3 blockVector, byte face, Position fPosition) {
        this.blockVector = blockVector;
        this.face = face;
        this.fPosition = fPosition;
    }

    @Override
    public void execute() {

    }

    @Override
    public String toString() {
        return "UseItemRequest{" +
                blockVector.toString() + ", " +
                "face: "+face + ", " +
                fPosition.toString() +
                '}';
    }
}
