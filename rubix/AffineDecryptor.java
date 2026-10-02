package rubix;

public class AffineDecryptor implements Decryptor {
    private static final int[] COPRIMES = {1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25};

    @Override
    public String getName() {
        return "Affine Cipher";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        int letterCount = 0;
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) letterCount++;
        }
        return letterCount >= 4;
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        String bestPlaintext = null;
        double bestScore = 0.0;
        int bestA = -1;
        int bestB = -1;

        for (int a : COPRIMES) {
            int aInv = modularInverse(a, 26);
            for (int b = 0; b < 26; b++) {
                if (a == 1 && b == 0) continue; // Skip no-op

                String decrypted = decryptCandidate(input, aInv, b);
                double score = validator.evaluateText(decrypted);

                if (score > bestScore) {
                    bestScore = score;
                    bestPlaintext = decrypted;
                    bestA = a;
                    bestB = b;
                }
            }
        }

        if (bestPlaintext != null && bestScore >= 0.40) {
            String schemeName = String.format("Affine (a=%d, b=%d)", bestA, bestB);
            return new DecryptionResult(schemeName, bestPlaintext, bestScore);
        }

        return null;
    }

    private String decryptCandidate(String input, int aInv, int b) {
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int y = c - base;
                int diff = (y - b) % 26;
                if (diff < 0) diff += 26;
                int x = (aInv * diff) % 26;
                sb.append((char) (base + x));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private int modularInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1;
    }
}