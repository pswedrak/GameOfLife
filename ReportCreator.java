package kis.sspd.jade.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReportCreator {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("report.txt"));

        String x = reader.readLine();
        String y = reader.readLine();

        System.out.println("Board size: " + x + "x" + y);

        Long start = Long.parseLong(reader.readLine());
        Long iteration = Long.parseLong(reader.readLine());

        System.out.println("Initialization time: " + (iteration - start) + "ms");

        ArrayList<Long> iterationTimes = new ArrayList();

        while(true){
            String nextIterationString = reader.readLine();
            if(nextIterationString == null){
                break;
            }
            Long nextIteration = Long.parseLong(nextIterationString);
            iterationTimes.add(nextIteration - iteration);
            iteration = nextIteration;
        }

        System.out.println();
        System.out.println("Mean iteration time: " + getMean(iterationTimes) + "ms");
        System.out.println("Standard deviation: " + getSd(iterationTimes) + "ms");

    }

    private static double getMean(ArrayList<Long> times){
        long sum = 0;
        for(long time: times){
            sum += time;
        }
        return sum / times.size();
    }

    private static double getSd(ArrayList<Long> times){
        double mean = getMean(times);
        double sum = 0L;
        for(long time: times){
            sum = sum + Math.pow((time - mean), 2);
        }

        return Math.sqrt(sum/times.size());
    }
}
