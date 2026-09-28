package rubix;

public class CaesarDecryptor implements Decryptor {
    @Override
    public String getName() {
        return "Caesar / ROT Shift";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        String clean = input.replaceAll("[^A-Za-z]", "");
        return clean.length() > 5;
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        DecryptionResult bestResult = null;
        double highestConfidence = 0.0;

        for (int shift = 1; shift < 26; shift++) {
            String candidate = shiftString(input, shift);
            double confidence = validator.evaluateText(candidate);

            if (confidence > highestConfidence) {
                highestConfidence = confidence;
                String schemeName = (shift == 13) ? "ROT13" : "Caesar (Shift " + (26 - shift) + ")";
                bestResult = new DecryptionResult(schemeName, candidate, confidence);
            }
        }

        if (highestConfidence >= 0.40) {
            return bestResult;
        }
        return null;
    }

    private String shiftString(String text, int shift) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append((char) ('a' + (c - 'a' + shift) % 26));
            } else if (c >= 'A' && c <= 'Z') {
                sb.append((char) ('A' + (c - 'A' + shift) % 26));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}