package rubix;

public class DecryptionResult {
    private final String scheme;
    private final String plaintext;
    private final double confidence;

    public DecryptionResult(String scheme, String plaintext, double confidence) {
        this.scheme = scheme;
        this.plaintext = plaintext;
        this.confidence = confidence;
    }

    public String getScheme() {
        return scheme;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public double getConfidence() {
        return confidence;
    }
}