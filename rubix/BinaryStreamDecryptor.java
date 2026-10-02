package rubix;

public class BinaryStreamDecryptor implements Decryptor {

    @Override
    public String getName() {
        return "Binary (ASCII)";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        if (input == null) return false;
        String clean = input.trim();
        if (clean.isEmpty()) return false;

        if (clean.contains(" ")) {
            String[] tokens = clean.split("\\s+");
            if (tokens.length < 2) return false;
            for (String t : tokens) {
                if (!t.matches("[01]{7,8}")) return false;
            }
            return true;
        }

        return clean.length() >= 8 && clean.length() % 8 == 0 && clean.matches("^[01]+$");
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        String clean = input.trim();
        StringBuilder decoded = new StringBuilder();

        try {
            if (clean.contains(" ")) {
                String[] tokens = clean.split("\\s+");
                for (String t : tokens) {
                    decoded.append((char) Integer.parseInt(t, 2));
                }
            } else {
                for (int i = 0; i < clean.length(); i += 8) {
                    decoded.append((char) Integer.parseInt(clean.substring(i, i + 8), 2));
                }
            }
        } catch (Exception ex) {
            return null;
        }

        String plaintext = decoded.toString();
        if (!validator.isPrintableAscii(plaintext)) {
            return null;
        }

        double wordScore = validator.calculateWordScore(plaintext);
        double evalScore = validator.evaluateText(plaintext);
        double finalScore = Math.max(evalScore, wordScore >= 0.50 ? 0.90 : 0.45);

        return new DecryptionResult("Binary (ASCII)", plaintext, finalScore);
    }
}