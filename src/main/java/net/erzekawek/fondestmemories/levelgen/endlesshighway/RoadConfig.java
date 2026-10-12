package net.erzekawek.fondestmemories.levelgen.endlesshighway;

import net.minecraft.world.level.block.Rotation;

import java.util.List;

public final class RoadConfig {
    private RoadConfig() {}

    public static final List<RoadLoader> PIECES = List.of(
            new RoadLoader("road_basic", 90),
            new RoadLoader("road_bus_stop", 2),
            new RoadLoader("road_lightened", 2),
            new RoadLoader("road_with_plants", 2)
    );

    public static final String START_PIECE = "road_basic";

    public static final int ORIGIN_Y = 1;

    public static final Rotation ROTATION = Rotation.CLOCKWISE_90;

    public static final boolean BOTH_DIRECTIONS = true;

    public static final int RADIUS_CHUNKS = 10;

    public static final int MARKER_Y = -64;
}
