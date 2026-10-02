package rubix;

public class RailFenceDecryptor implements Decryptor {

    @Override
    public String getName() {
        return "Rail Fence Cipher";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        if (input == null) return false;
        String clean = input.trim();
        return clean.length() >= 6 && !analyzer.isHexCandidate(clean) && !analyzer.isBase64Candidate(clean);
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        String bestPlaintext = null;
        double bestScore = 0.0;
        int bestRails = -1;

        // Try raw input
        int maxRailsRaw = Math.min(15, input.length() - 1);
        for (int rails = 2; rails <= maxRailsRaw; rails++) {
            String decrypted = decryptWithRails(input, rails);
            double score = validator.evaluateText(decrypted);

            if (score > bestScore) {
                bestScore = score;
                bestPlaintext = decrypted;
                bestRails = rails;
            }
        }

        // Try stripped-space input if spaces exist
        if (input.contains(" ")) {
            String stripped = input.replaceAll("\\s+", "");
            if (stripped.length() >= 6) {
                int maxRailsStripped = Math.min(15, stripped.length() - 1);
                for (int rails = 2; rails <= maxRailsStripped; rails++) {
                    String decrypted = decryptWithRails(stripped, rails);
                    double score = validator.evaluateText(decrypted);

                    if (score > bestScore) {
                        bestScore = score;
                        bestPlaintext = decrypted;
                        bestRails = rails;
                    }
                }
            }
        }

        if (bestPlaintext != null && bestScore >= 0.40) {
            return new DecryptionResult(String.format("Rail Fence (%d rails)", bestRails), bestPlaintext, bestScore);
        }

        return null;
    }

    public static String encrypt(String text, int rails) {
        if (text == null || rails <= 1 || text.length() <= rails) return text;
        int len = text.length();
        StringBuilder[] rows = new StringBuilder[rails];
        for (int i = 0; i < rails; i++) rows[i] = new StringBuilder();

        int row = 0;
        int dir = 1;
        for (int i = 0; i < len; i++) {
            rows[row].append(text.charAt(i));
            if (row == 0) {
                dir = 1;
            } else if (row == rails - 1) {
                dir = -1;
            }
            row += dir;
        }

        StringBuilder result = new StringBuilder(len);
        for (StringBuilder sb : rows) {
            result.append(sb);
        }
        return result.toString();
    }

    private String decryptWithRails(String cipher, int rails) {
        int len = cipher.length();
        boolean[][] marker = new boolean[rails][len];
        int row = 0;
        int dir = 1;

        for (int col = 0; col < len; col++) {
            marker[row][col] = true;
            if (row == 0) {
                dir = 1;
            } else if (row == rails - 1) {
                dir = -1;
            }
            row += dir;
        }

        char[][] grid = new char[rails][len];
        int idx = 0;
        for (int r = 0; r < rails; r++) {
            for (int c = 0; c < len; c++) {
                if (marker[r][c] && idx < len) {
                    grid[r][c] = cipher.charAt(idx++);
                }
            }
        }

        StringBuilder result = new StringBuilder(len);
        row = 0;
        dir = 1;
        for (int col = 0; col < len; col++) {
            result.append(grid[row][col]);
            if (row == 0) {
                dir = 1;
            } else if (row == rails - 1) {
                dir = -1;
            }
            row += dir;
        }

        return result.toString();
    }
}