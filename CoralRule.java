package kis.sspd.jade.game;

import java.util.ArrayList;
import java.util.List;

public class CoralRule implements Rule {
    @Override
    public List<Integer> getDeadToAlive() {
        List<Integer> deadToAlive = new ArrayList<>();
        deadToAlive.add(3);

        return deadToAlive;
    }

    @Override
    public List<Integer> getRemainAlive() {
        List<Integer> remainAlive = new ArrayList<>();
        remainAlive.add(4);
        remainAlive.add(5);
        remainAlive.add(6);
        remainAlive.add(7);
        remainAlive.add(8);

        return remainAlive;
    }
}