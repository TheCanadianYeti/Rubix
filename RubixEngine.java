package rubix;

import java.util.ArrayList;
import java.util.List;

public class RubixEngine {
    private final Analyzer analyzer;
    private final TextValidator validator;
    private final List<Decryptor> decryptors;

    public RubixEngine() {
        this.analyzer = new Analyzer();
        this.validator = new TextValidator();
        this.decryptors = new ArrayList<>();

        decryptors.add(new HexDecryptor());
        decryptors.add(new Base64Decryptor());
        decryptors.add(new CaesarDecryptor());
        decryptors.add(new VigenereDecryptor());
        decryptors.add(new ReverseDecryptor());
        decryptors.add(new AtbashDecryptor());
        decryptors.add(new MorseDecryptor());
    }

    public DecryptionResult process(String ciphertext) {
        return process(ciphertext, 0);
    }

    private DecryptionResult process(String ciphertext, int depth) {
        if (ciphertext == null || depth > 2) {
            return null;
        }

        DecryptionResult bestCandidate = null;

        for (Decryptor decryptor : decryptors) {
            if (decryptor.canHandle(ciphertext, analyzer)) {
                DecryptionResult result = decryptor.decrypt(ciphertext, validator);
                if (result != null && result.getConfidence() >= 0.40) {
                    // Check for nested/multi-layer decryption (e.g. Morse -> Hex -> Text)
                    if (depth < 2 && result.getConfidence() < 0.85 && !result.getPlaintext().trim().equalsIgnoreCase(ciphertext.trim())) {
                        DecryptionResult nextLayer = process(result.getPlaintext(), depth + 1);
                        if (nextLayer != null && nextLayer.getConfidence() >= result.getConfidence()) {
                            result = new DecryptionResult(
                                result.getScheme() + " -> " + nextLayer.getScheme(),
                                nextLayer.getPlaintext(),
                                nextLayer.getConfidence()
                            );
                        }
                    }

                    if (bestCandidate == null || result.getConfidence() > bestCandidate.getConfidence()) {
                        bestCandidate = result;
                    }
                }
            }
        }

        return bestCandidate;
    }
}