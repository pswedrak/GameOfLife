package kis.sspd.jade.game;

import java.util.List;

interface Rule {
    public List<Integer> getDeadToAlive();
    public List<Integer> getRemainAlive();
}