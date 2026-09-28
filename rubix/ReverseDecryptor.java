package rubix;

public class ReverseDecryptor implements Decryptor {
    @Override
    public String getName() {
        return "Reverse String";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        return input != null && input.trim().length() > 3;
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        String reversed = new StringBuilder(input.trim()).reverse().toString();
        double confidence = validator.evaluateText(reversed);
        if (confidence >= 0.40) {
            return new DecryptionResult(getName(), reversed, confidence);
        }
        return null;
    }
}
