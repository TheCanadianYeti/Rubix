package rubix;

import java.nio.charset.StandardCharsets;

public class SingleByteXorDecryptor implements Decryptor {

    @Override
    public String getName() {
        return "Single-Byte XOR";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        return analyzer.isHexCandidate(input);
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        byte[] rawBytes = parseHex(input);
        if (rawBytes == null || rawBytes.length < 4) {
            return null;
        }

        String bestPlaintext = null;
        double bestScore = 0.0;
        int bestKey = -1;

        for (int key = 1; key < 256; key++) {
            byte[] decrypted = new byte[rawBytes.length];
            for (int i = 0; i < rawBytes.length; i++) {
                decrypted[i] = (byte) (rawBytes[i] ^ key);
            }

            String candidate = new String(decrypted, StandardCharsets.UTF_8);
            if (!validator.isPrintableAscii(candidate)) {
                continue;
            }

            double score = validator.evaluateText(candidate);
            double wordScore = validator.calculateWordScore(candidate);
            double finalScore = Math.max(score, wordScore >= 0.50 ? 0.85 : 0.0);

            if (finalScore > bestScore) {
                bestScore = finalScore;
                bestPlaintext = candidate;
                bestKey = key;
            }
        }

        if (bestPlaintext != null && bestScore >= 0.40) {
            return new DecryptionResult(String.format("Single-Byte XOR (Key: 0x%02X)", bestKey), bestPlaintext, bestScore);
        }

        return null;
    }

    private byte[] parseHex(String input) {
        String clean = input.trim().replaceAll("(?i)0x|[\\s:,]", "");
        if (clean.length() % 2 != 0) return null;

        byte[] data = new byte[clean.length() / 2];
        try {
            for (int i = 0; i < clean.length(); i += 2) {
                data[i / 2] = (byte) Integer.parseInt(clean.substring(i, i + 2), 16);
            }
            return data;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}