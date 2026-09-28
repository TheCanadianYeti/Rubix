package rubix;

import java.util.ArrayList;
import java.util.List;

public class VigenereDecryptor implements Decryptor {
    @Override
    public String getName() {
        return "Vigenere Cipher";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        String clean = input.replaceAll("[^A-Za-z]", "");
        return clean.length() >= 8;
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String clean = input.toUpperCase().replaceAll("[^A-Z]", "");
        if (clean.length() < 8) {
            return null;
        }

        DecryptionResult bestResult = null;
        double highestConfidence = 0.0;

        for (int k = 2; k <= 7; k++) {
            List<List<Character>> slices = new ArrayList<>();

            // For short inputs, Chi-Square is unreliable on slices.
            // Use full search for k <= 3, and wider beams for k >= 4.
            int topN;
            if (clean.length() < 35) {
                topN = (k <= 3) ? 26 : (k == 4 ? 6 : (k == 5 ? 3 : 2));
            } else {
                topN = (k <= 4) ? 5 : (k == 5 ? 3 : 2);
            }

            for (int i = 0; i < k; i++) {
                StringBuilder slice = new StringBuilder();
                for (int j = i; j < clean.length(); j += k) {
                    slice.append(clean.charAt(j));
                }
                slices.add(getTopShifts(slice.toString(), topN, validator));
            }

            List<String> candidateKeys = new ArrayList<>();
            generateKeys(slices, 0, new StringBuilder(), candidateKeys);

            for (String key : candidateKeys) {
                // Skip monoalphabetic keys (handled by Caesar)
                boolean mono = true;
                for (int i = 1; i < key.length(); i++) {
                    if (key.charAt(i) != key.charAt(0)) {
                        mono = false;
                        break;
                    }
                }
                if (mono) continue;

                String candidate = applyVigenere(input, key);
                double conf = validator.evaluateText(candidate);

                if (conf > highestConfidence && conf >= 0.40) {
                    highestConfidence = conf;
                    bestResult = new DecryptionResult("Vigenere (Key: " + key + ")", candidate, conf);
                    if (conf >= 0.85) {
                        return bestResult;
                    }
                }
            }
        }

        return bestResult;
    }

    private List<Character> getTopShifts(String slice, int topN, TextValidator validator) {
        if (topN >= 26) {
            List<Character> allChars = new ArrayList<>(26);
            for (int i = 0; i < 26; i++) {
                allChars.add((char) ('A' + i));
            }
            return allChars;
        }

        List<int[]> scored = new ArrayList<>();
        for (int shift = 0; shift < 26; shift++) {
            StringBuilder shifted = new StringBuilder();
            for (char c : slice.toCharArray()) {
                shifted.append((char) ('A' + (c - 'A' - shift + 26) % 26));
            }
            double chi = validator.calculateChiSquare(shifted.toString());
            scored.add(new int[] { shift, (int) (chi * 100) });
        }
        scored.sort((a, b) -> Integer.compare(a[1], b[1]));

        List<Character> topChars = new ArrayList<>();
        for (int i = 0; i < Math.min(topN, scored.size()); i++) {
            topChars.add((char) ('A' + scored.get(i)[0]));
        }
        return topChars;
    }

    private void generateKeys(List<List<Character>> slices, int depth, StringBuilder cur, List<String> result) {
        if (depth == slices.size()) {
            result.add(cur.toString());
            return;
        }
        for (char c : slices.get(depth)) {
            cur.append(c);
            generateKeys(slices, depth + 1, cur, result);
            cur.setLength(cur.length() - 1);
        }
    }

    private String applyVigenere(String input, String key) {
        StringBuilder sb = new StringBuilder();
        int keyIndex = 0;
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                boolean isUpper = Character.isUpperCase(c);
                char base = isUpper ? 'A' : 'a';
                int shift = key.charAt(keyIndex % key.length()) - 'A';
                sb.append((char) (base + (c - base - shift + 26) % 26));
                keyIndex++;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}