package rubix;

import java.nio.charset.StandardCharsets;

public class HexDecryptor implements Decryptor {
    @Override
    public String getName() {
        return "Hexadecimal";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        return analyzer.isHexCandidate(input);
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        try {
            String clean = input.trim().replaceAll("(?i)0x|[\\s:,]", "");
            if (clean.length() % 2 != 0 || clean.length() < 2) {
                return null;
            }
            byte[] bytes = new byte[clean.length() / 2];
            for (int i = 0; i < clean.length(); i += 2) {
                bytes[i / 2] = (byte) Integer.parseInt(clean.substring(i, i + 2), 16);
            }
            String decoded = new String(bytes, StandardCharsets.UTF_8);
            if (!validator.isPrintableAscii(decoded)) {
                return null;
            }
            double confidence = validator.evaluateText(decoded);
            if (confidence >= 0.40) {
                return new DecryptionResult(getName(), decoded, confidence);
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}