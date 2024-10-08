import java.util.*;

public class HolidayOptimizer {
    public static void main(String[] args) {
        Map<String, List<String>> officeMap = new HashMap<>();
        officeMap.put("Noida", Arrays.asList("Delhi", "Gurugram", "Faridabad"));
        officeMap.put("Delhi", Arrays.asList("Noida", "Gurugram", "Sonipat", "Faridabad"));
        officeMap.put("Sonipat", Arrays.asList("Delhi", "Panipat", "Gurugram"));
        officeMap.put("Gurugram", Arrays.asList("Noida", "Delhi", "Sonipat", "Panipat", "Faridabad"));
        officeMap.put("Panipat", Arrays.asList("Sonipat", "Gurugram"));
        officeMap.put("Faridabad", Arrays.asList("Delhi", "Noida", "Gurugram"));

        Map<String, List<Integer>> holidays = new HashMap<>();
        holidays.put("Noida", Arrays.asList(1, 3, 4, 2, 1, 5, 6, 5, 1, 7, 2, 1));
        holidays.put("Delhi", Arrays.asList(5, 1, 8, 2, 1, 7, 2, 6, 2, 8, 2, 6));
        holidays.put("Sonipat", Arrays.asList(2, 5, 8, 2, 1, 6, 9, 3, 2, 1, 5, 7));
        holidays.put("Gurugram", Arrays.asList(6, 4, 1, 6, 3, 4, 7, 3, 2, 5, 7, 8));
        holidays.put("Panipat", Arrays.asList(2, 4, 3, 1, 7, 2, 6, 8, 2, 1, 4, 6));
        holidays.put("Faridabad", Arrays.asList(2, 4, 6, 7, 2, 1, 3, 6, 3, 1, 6, 8));

        List<String> sequence = maximizeHolidays(officeMap, holidays);
        System.out.println("Optimal Office Transitions: " + sequence);
        System.out.println("Total Holidays: " + calculateTotalHolidays(sequence, holidays));
    }

    private static int getHolidays(String office, int month, Map<String, List<Integer>> holidays) {
        return holidays.get(office).get(month);
    }

    private static boolean isValidMove(String currentOffice, String newOffice, Map<String, List<String>> officeMap, int relocations, int[] quarterRelocations, int month) {
        if (!officeMap.get(currentOffice).contains(newOffice)) {
            return false;
        }
        if (relocations >= 8) {
            return false;
        }
        if (quarterRelocations[month / 3] >= 2) {
            return false;
        }
        return true;
    }

    private static List<String> maximizeHolidays(Map<String, List<String>> officeMap, Map<String, List<Integer>> holidays) {
        int nMonths = 12;
        List<String> offices = new ArrayList<>(holidays.keySet());
        int[][] dp = new int[nMonths][offices.size()];
        int[][] path = new int[nMonths][offices.size()];

        for (int i = 0; i < offices.size(); i++) {
            dp[0][i] = getHolidays(offices.get(i), 0, holidays);
        }

        for (int month = 1; month < nMonths; month++) {
            for (int i = 0; i < offices.size(); i++) {
                String currentOffice = offices.get(i);
                for (int j = 0; j < offices.size(); j++) {
                    String newOffice = offices.get(j);
                    if (isValidMove(currentOffice, newOffice, officeMap, month, path[month], month)) {
                        int holidaysCount = dp[month - 1][i] + getHolidays(newOffice, month, holidays);
                        if (holidaysCount > dp[month][j]) {
                            dp[month][j] = holidaysCount;
                            path[month][j] = i;
                        }
                    }
                }
            }
        }

        int maxHolidays = 0;
        int maxIndex = 0;
        for (int i = 0; i < offices.size(); i++) {
            if (dp[nMonths - 1][i] > maxHolidays) {
                maxHolidays = dp[nMonths - 1][i];
                maxIndex = i;
            }
        }

        List<String> sequence = new ArrayList<>();
        for (int month = nMonths - 1; month >= 0; month--) {
            sequence.add(offices.get(maxIndex));
            maxIndex = path[month][maxIndex];
        }
        Collections.reverse(sequence);
        return sequence;
    }

    private static int calculateTotalHolidays(List<String> sequence, Map<String, List<Integer>> holidays) {
        int totalHolidays = 0;
        for (int month = 0; month < sequence.size(); month++) {
            totalHolidays += getHolidays(sequence.get(month), month, holidays);
        }
        return totalHolidays;
    }
}
