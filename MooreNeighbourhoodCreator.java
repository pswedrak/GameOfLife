package kis.sspd.jade.game;

import java.util.ArrayList;
import java.util.List;

public class MooreNeighbourhoodCreator implements NeighbourhoodCreator {
    @Override
    public List<Point> createNeighbourhood(int x, int y, int X, int Y) {
        List<Point> neighbours = new ArrayList<>();

        int decremented_x = x-1;
        int incremented_x = x+1;
        int decremented_y = y-1;
        int incremented_y = y+1;

        if(decremented_x < 0){
            decremented_x = X - 1;
        }

        if(decremented_y < 0){
            decremented_y = Y - 1;
        }

        if(incremented_x > X - 1){
            incremented_x = 0;
        }

        if(incremented_y > Y - 1){
            incremented_y = 0;
        }

        neighbours.add(new Point(decremented_x, decremented_y));
        neighbours.add(new Point(x, decremented_y));
        neighbours.add(new Point(incremented_x, decremented_y));
        neighbours.add(new Point(incremented_x, y));
        neighbours.add(new Point(incremented_x, incremented_y));
        neighbours.add(new Point(x, incremented_y));
        neighbours.add(new Point(decremented_x, incremented_y));
        neighbours.add(new Point(decremented_x, y));

        return neighbours;
    }
}
