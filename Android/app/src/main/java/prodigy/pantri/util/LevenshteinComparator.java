package prodigy.pantri.util;

import java.util.Comparator;

/**
 * Created by Quinn on 9/18/2016.
 */
public class LevenshteinComparator implements Comparator<String> {
    private String mMatch;

    public LevenshteinComparator(String toMatch) {
        mMatch = toMatch;
    }

    @Override
    public int compare(String o1, String o2) {
        int o1Distance = calcLevenshteinDistance(mMatch, o1);
        int o2Distance = calcLevenshteinDistance(mMatch, o2);
        if (o1Distance == o2Distance) {
            return 0;
        }
        else if (o1Distance < o2Distance) {
            return -1;
        }
        return 1;
    }

    private static int calcLevenshteinDistance(String a, String b) {
        if (a == null || b == null || a.isEmpty() || b.isEmpty()) {
            return 0;
        }

        int lengthA = a.length();
        int lengthB = b.length();
        int[][] distances = new int[lengthA + 1][lengthB + 1];
        for (int i = 0;  i <= lengthA;  distances[i][0] = i++);
        for (int j = 0;  j <= lengthB;  distances[0][j] = j++);

        for (int i = 1;  i <= lengthA;  i++)
            for (int j = 1;  j <= lengthB;  j++) {
                int cost = (b.charAt(j - 1) == a.charAt(i - 1)) ? 0 : 1;
                distances[i][j] = Math.min(Math.min(distances[i - 1][j] + 1, distances[i][j - 1] + 1), distances[i - 1][j - 1] + cost);
            }
        return distances[lengthA][lengthB];
    }
}
