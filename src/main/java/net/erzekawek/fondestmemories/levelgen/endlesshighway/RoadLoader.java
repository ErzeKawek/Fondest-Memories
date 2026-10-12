package net.erzekawek.fondestmemories.levelgen.endlesshighway;

public record RoadLoader(String id, int weight) {
    public RoadLoader {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Road Loader : id must not be empty");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Road Loader : weight must be >= 0 (" + id + ")");
        }
    }
}