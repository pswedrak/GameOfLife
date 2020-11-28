package kis.sspd.jade.game;

import java.util.List;

interface NeighbourhoodCreator {
    public List<Point> createNeighbourhood(int x, int y, int X, int Y);
}
