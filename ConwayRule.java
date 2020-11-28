package kis.sspd.jade.game;

import java.util.ArrayList;
import java.util.List;

public class ConwayRule implements Rule {
    @Override
    public List<Integer> getDeadToAlive() {
        List<Integer> deadToAlive = new ArrayList<>();
        deadToAlive.add(3);

        return deadToAlive;
    }

    @Override
    public List<Integer> getRemainAlive() {
        List<Integer> remainAlive = new ArrayList<>();
        remainAlive.add(2);
        remainAlive.add(3);

        return remainAlive;
    }
}
