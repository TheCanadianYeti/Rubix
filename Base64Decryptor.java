package rubix;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Decryptor implements Decryptor {
    @Override
    public String getName() {
        return "Base64";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        return analyzer.isBase64Candidate(input);
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(input.trim());
            String decoded = new String(decodedBytes, StandardCharsets.UTF_8);
            if (!validator.isPrintableAscii(decoded)) {
                return null;
            }
            double confidence = validator.evaluateText(decoded);
            if (confidence < 0.40) {
                confidence = 0.70;
            }
            return new DecryptionResult(getName(), decoded, confidence);
        } catch (Exception ignored) {
        }
        return null;
    }
}