/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.block;

import lombok.Getter;
import net.redstonelamp.item.Items;

/**
 * Wooden planks block implementation.
 *
 * @author RedstoneLamp Team
 */
public class WoodPlanks extends Block {
    public static final int ID = Items.WOODEN_PLANKS;
    @Getter private PlankType type;

    public WoodPlanks(int id, short meta, int count) {
        super(id, meta, count);
        this.type = PlankType.OAK;
    }

    public WoodPlanks(PlankType type, int count) {
        super(ID, (short) type.getMetaId(), count);
    }

    public static enum PlankType {
        OAK(0),
        SPRUCE(1),
        BIRCH(2),
        JUNGLE(3),
        ACACIA(4),
        DARK_OAK(5);

        @Getter private int metaId;

        PlankType(int metaId) {
            this.metaId = metaId;
        }
    }
}
