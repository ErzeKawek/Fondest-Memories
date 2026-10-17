package net.erzekawek.fondestmemories.levelgen.chkgen.rooms;

import net.erzekawek.fondestmemories.FondestMemories;
import net.minecraft.world.level.block.Blocks;

/**
 * basically where all the rooms are registered
 */
public final class MazeRooms {
    private MazeRooms() {}

    public static void registerPoolrooms() {
        RoomGroup.builder(FondestMemories.id("poolrooms"))
                .guides(Blocks.PRISMARINE, Blocks.COBBLED_DEEPSLATE)

                .single(FondestMemories.id("pools/pool_8x8_nub"), 0, 2)
                .single(FondestMemories.id("pools/pool_8x8_i"), 0, 4)
                .single(FondestMemories.id("pools/pool_8x8_l"), 0, 4)
                .single(FondestMemories.id("pools/pool_8x8_f"), 0, 2)
                .single(FondestMemories.id("pools/pool_8x8_t"), 0, 1)

                .multi(FondestMemories.id("pools/pool_8x16_small"),   1, 2, 0, 3)
                .multi(FondestMemories.id("pools/pool_16x24_skyview"), 2, 3, 0, 2)
                .multi(FondestMemories.id("pools/pool_24x24_lush"), 2, 2, -3, 1)

                .start(FondestMemories.id("pools/pool_24x24_entrance"),3, 3, 0)
                .register();
    }
}
