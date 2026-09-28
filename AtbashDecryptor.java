package rubix;

public class AtbashDecryptor implements Decryptor {
    @Override
    public String getName() {
        return "Atbash Cipher";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        String clean = input.replaceAll("[^A-Za-z]", "");
        return clean.length() > 3;
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append((char) ('z' - (c - 'a')));
            } else if (c >= 'A' && c <= 'Z') {
                sb.append((char) ('Z' - (c - 'A')));
            } else {
                sb.append(c);
            }
        }

        String decrypted = sb.toString();
        double confidence = validator.evaluateText(decrypted);
        if (confidence >= 0.40) {
            return new DecryptionResult(getName(), decrypted, confidence);
        }
        return null;
    }
}
