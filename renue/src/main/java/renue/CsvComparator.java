package renue;

import java.util.Comparator;

public class CsvComparator implements Comparator<String[]> {

    private final int columnToMatch;

    public CsvComparator(int columnToMatch) {
        this.columnToMatch = columnToMatch;
    }

    public int compare(String[] s1, String[] s2) {
        if (columnToMatch == 5 || columnToMatch == 8 || columnToMatch == 9) {
            if (Double.parseDouble(s1[columnToMatch]) < Double.parseDouble(s2[columnToMatch])) {
                return -1;
            } else if (Double.parseDouble(s1[columnToMatch]) < Double.parseDouble(s2[columnToMatch])) {
                return 1;
            } else {
                return 0;
            }
        }

        return Integer.compare(s1[columnToMatch].compareTo(s2[columnToMatch]), 0);
    }
}
