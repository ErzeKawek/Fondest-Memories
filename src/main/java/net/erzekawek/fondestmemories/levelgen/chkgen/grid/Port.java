package net.erzekawek.fondestmemories.levelgen.chkgen.grid;

/** room openings */
public record Port(int cx, int cz, Side side) {
    /** rotate */
    public Port rotateCw(int w, int d) { return new Port(d - 1 - cz, cx, side.cw()); }
}
