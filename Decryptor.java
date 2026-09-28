package rubix;

public interface Decryptor {
    String getName();
    boolean canHandle(String input, Analyzer analyzer);
    DecryptionResult decrypt(String input, TextValidator validator);
}