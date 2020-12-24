import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MatheImAdvent extends JFrame {

    public static void main(String[] args) {
        // day11(1000000);
        // day21();
        EventQueue.invokeLater(() -> {
            try {
                Day22 frame = new Day22();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * School classes should be limited to 22 students. In Level 8 there are 132 students in whole.
     * They formed smaller groups of 11 * 5 students and 11 * 7 students. These will stay together.
     * Question: How many classes are at least needed to create classes of a maximum of 22 students?
     */
    private static void day21() {
        // Combinations to form max 22: 7|7|7 ; 7|7|5 ; 7|7 ; 7|5|5|5 ; 7 ; 5|5|5|5 ; 5|5 ; 5
        while (true) {
            int sum = 0;
            int groupsOf5 = 11;
            int groupsOf7 = 11;
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                int random = (int) (Math.random() * 9);
                if (random == 0) {
                    output.append("7|7|7 (1) ");
                    groupsOf7 -= 3;
                } else if (random == 1) {
                    output.append("7|7|5 (2) ");
                    groupsOf7 -= 2;
                    groupsOf5 -= 1;
                } else if (random == 2) {
                    output.append("7|5|5|5 (3) ");
                    groupsOf7 -= 1;
                    groupsOf5 -= 3;
                } else if (random == 3) {
                    output.append("5|5|5|5 (4) ");
                    groupsOf5 -= 4;
                } else if (random == 4) {
                    output.append("7|7 (5) ");
                    groupsOf7 -= 2;
                } else if (random == 5) {
                    output.append("5|5 (6) ");
                    groupsOf5 -= 2;
                } else if (random == 6) {
                    output.append("5|5|5 (7) ");
                    groupsOf5 -= 3;
                } else if (random == 7) {
                    output.append("5 (8) ");
                    groupsOf5 -= 1;
                } else if (random == 8) {
                    output.append("7 (9) ");
                    groupsOf7 -= 1;
                }
                sum++;
                if ((groupsOf5 < 0 || groupsOf7 < 0) || (groupsOf5 == 0 && groupsOf7 == 0)) {
                    break;
                }
            }
            if (groupsOf5 == 0 && groupsOf7 == 0 && sum <= 7) {
                System.out.println("Finished, " + sum + " classes needed: " + output);
            }
        }
    }

    /**
     * Martin has 6 plates (numbered form 1 to 6) and a dice. He rolls it and checks the appearing number:
     * 1. If the plate of this number is empty, he places a Bounty on it.
     * 2. If there is a Bounty on the plate already, he eats it and the plate is empty again.
     * The game ends, if all plates are filled with Bounty, in this case he eats all 6 and the game is over.
     * Question: How many Bounty will Martin eat in the mean, if he repeats this again and again?
     *
     * @param iterations The number of total iterations
     */
    private static void day11(int iterations) {
        int allBounty = 0;
        double variance = 0;
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            int bountyCounter = 0;
            boolean[] plates = {false, false, false, false, false, false};
            while (true) {
                if (plates[0] && plates[1] && plates[2] && plates[3] && plates[4] && plates[5]) {
                    // Goal state
                    bountyCounter += 6;
                    // System.out.println("Eaten Bounty: " + bountyCounter);
                    allBounty += bountyCounter;
                    results.add(bountyCounter);
                    break;
                }
                int random = (int) (Math.random() * 6);
                plates[random] = !plates[random];
                // System.out.print("{" + plates[0] + "," + plates[1] + "," + plates[2] + "," + plates[3] + "," + plates[4] + "," + plates[5] + "} ");

                if (plates[random]) {
                    // System.out.println("Diced number: " + (random + 1) + ". Teller " + (random + 1) + " gets a Bounty!");
                } else {
                    // System.out.println("Diced number: " + (random + 1) + ". Teller " + (random + 1) + " has a Bounty already, Martin is going to eat it!");
                    bountyCounter++;
                }
            }
        }
        int mean = allBounty / iterations;
        for (int i = 0; i < iterations; i++) {
            int sum = results.get(i) - mean;
            variance += sum * sum;
        }
        variance /= (iterations - 1);
        System.out.println("Sum: " + allBounty + " and mean: " + mean + " with variance: " + variance + " and standard deviation: " + Math.sqrt(variance));
    }

}
