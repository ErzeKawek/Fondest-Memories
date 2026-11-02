package net.erzekawek.fondestmemories.registry;

import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.veyl.levelgen.chunkgen.rooms.RoomGroup;
import net.erzekawek.veyl.utils.finder.Find;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;

import static net.erzekawek.fondestmemories.FondestMemories.MOD_ID;

/**
 * basically where all the rooms are registered
 */
public final class FondestMemoriesRoomRegistry {
    private FondestMemoriesRoomRegistry() {}

    public static void registerPoolrooms() {
        RoomGroup.builder(FondestMemories.id("poolrooms"))
                .guides(Blocks.PRISMARINE, Blocks.DARK_PRISMARINE)

                // le singlet rooms
                .single(FondestMemories.id("pools/pool_8x8_nub"), 0, 2)
                .single(FondestMemories.id("pools/pool_8x8_i"), 0, 4)
                .single(FondestMemories.id("pools/pool_8x8_l"), 0, 4)
                .single(FondestMemories.id("pools/pool_8x8_f"), 0, 2)
                .single(FondestMemories.id("pools/pool_8x8_t"), 0, 1)
                .single(FondestMemories.id("pools/pool_8x8_deadendtall"), -8, 2)

                // dis are big rooms
                .multi(FondestMemories.id("pools/pool_8x16_small"),   1, 2, 0, 3)
                .multi(FondestMemories.id("pools/pool_8x16_tallhall"),   1, 2, 0, 3)
                .multi(FondestMemories.id("pools/pool_16x24_skyview"), 2, 3, 0, 2)
                .multi(FondestMemories.id("pools/pool_24x24_lush"), 3, 3, 0, 1)
                .multi(FondestMemories.id("pools/pool_24x32_arches"), 3, 4, 0, 1)
                .multi(FondestMemories.id("pools/pool_24x32_archwalkway"), 3, 4, 0, 1)
                .multi(FondestMemories.id("pools/pool_24x32_tunneling"), 3, 4, 0, 1)

                // de starting room
                .start(FondestMemories.id("pools/pool_24x24_entrance"),3, 3, -8)
                .register();
    }

    public static void registerLightsOut() {
        RoomGroup.builder(FondestMemories.id("cardinal_office"))
                .guides(Blocks.SULFUR, Blocks.CINNABAR)
                .stairGuides(Blocks.RAW_GOLD_BLOCK, Blocks.RAW_IRON_BLOCK)
                .single(findCardinal("lightrooms_hall_i"), 0, 1)
                .single(findCardinal("lightrooms_hall_f"), 0, 1)
                .single(findCardinal("lightrooms_hall_l"), 0, 1)
                .single(findCardinal("lightrooms_hall_t"), 0, 1)
                .single(findCardinal("lightrooms_hall_nub"), 0, 1)

                .multi(findCardinal("lightrooms_bathrooms"), 4, 4, 0, 5)
                .multi(findCardinal("lightrooms_huge_meeting"), 4, 3, 0, 1)
                .multi(findCardinal("lightrooms_meeting_room"), 4, 4, 0, 5)
                .multi(findCardinal("lightrooms_office_bloc"), 4, 3, 0, 4)
                .multi(findCardinal("lightrooms_office_bloc2"), 4, 4, 0, 5)

                .stairway(findStair("stairway_1"), 2, 2, 0, 1)
                .stairway(findStair("stairway_2"), 2, 2, 0, 1)
                .stairway(findStair("stairway_3"), 2, 3, 0, 1)

                .start(findCardinal("lightrooms_starting_room"), 4, 4, 0)
                .register();
    }

    public static Identifier findHalls(String roomId) {
        return Find.findNBT(MOD_ID,"lights_out", roomId);
    }

    public static Identifier findCardinal(String roomId) {
        return Find.findNBT(MOD_ID,"lightrooms", roomId);
    }

    public static Identifier findStair(String roomId) {
        return Find.findNBT(MOD_ID,"cardinalstairways", roomId);
    }
}
