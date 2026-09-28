package rubix;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Analyzer {
    private static final Pattern BASE64_PATTERN = Pattern.compile("^[A-Za-z0-9+/]+={0,2}$");
    private static final Pattern HEX_PATTERN = Pattern.compile("^[0-9a-fA-F]+$");

    public boolean isBase64Candidate(String input) {
        String trimmed = input.trim();
        return trimmed.length() % 4 == 0 && BASE64_PATTERN.matcher(trimmed).matches();
    }

    public boolean isHexCandidate(String input) {
        if (input == null) return false;
        String clean = input.trim().replaceAll("(?i)0x|[\\s:,]", "");
        return clean.length() >= 2 && clean.length() % 2 == 0 && HEX_PATTERN.matcher(clean).matches();
    }

    public double calculateEntropy(String input) {
        if (input == null || input.isEmpty()) {
            return 0.0;
        }
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        double entropy = 0.0;
        double len = input.length();
        for (int count : freqMap.values()) {
            double prob = count / len;
            entropy -= prob * (Math.log(prob) / Math.log(2));
        }
        return entropy;
    }

    public double calculateIndexOrCoincidence(String input) {
        String clean = input.toUpperCase().replaceAll("[^A-Z]", "");
        int len = clean.length();
        if (len <= 1) {
            return 0.0;
        }

        int[] counts = new int[26];
        for (char c : clean.toCharArray()) {
            counts[c - 'A']++;
        }

        int sum = 0;
        for (int count : counts) {
            sum += count * (count - 1);
        }

        return (double) sum / (len * (len - 1));
    }
}