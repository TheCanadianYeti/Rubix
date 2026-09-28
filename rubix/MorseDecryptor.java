package rubix;

import java.util.HashMap;
import java.util.Map;

public class MorseDecryptor implements Decryptor {
    private static final Map<String, Character> MORSE_MAP = new HashMap<>();

    static {
        MORSE_MAP.put(".-", 'A'); MORSE_MAP.put("-...", 'B'); MORSE_MAP.put("-.-.", 'C');
        MORSE_MAP.put("-..", 'D'); MORSE_MAP.put(".", 'E'); MORSE_MAP.put("..-.", 'F');
        MORSE_MAP.put("--.", 'G'); MORSE_MAP.put("....", 'H'); MORSE_MAP.put("..", 'I');
        MORSE_MAP.put(".---", 'J'); MORSE_MAP.put("-.-", 'K'); MORSE_MAP.put(".-..", 'L');
        MORSE_MAP.put("--", 'M'); MORSE_MAP.put("-.", 'N'); MORSE_MAP.put("---", 'O');
        MORSE_MAP.put(".--.", 'P'); MORSE_MAP.put("--.-", 'Q'); MORSE_MAP.put(".-.", 'R');
        MORSE_MAP.put("...", 'S'); MORSE_MAP.put("-", 'T'); MORSE_MAP.put("..-", 'U');
        MORSE_MAP.put("...-", 'V'); MORSE_MAP.put(".--", 'W'); MORSE_MAP.put("-..-", 'X');
        MORSE_MAP.put("-.--", 'Y'); MORSE_MAP.put("--..", 'Z');
        MORSE_MAP.put("-----", '0'); MORSE_MAP.put(".----", '1'); MORSE_MAP.put("..---", '2');
        MORSE_MAP.put("...--", '3'); MORSE_MAP.put("....-", '4'); MORSE_MAP.put(".....", '5');
        MORSE_MAP.put("-....", '6'); MORSE_MAP.put("--...", '7'); MORSE_MAP.put("---..", '8');
        MORSE_MAP.put("----.", '9');
    }

    @Override
    public String getName() {
        return "Morse Code";
    }

    @Override
    public boolean canHandle(String input, Analyzer analyzer) {
        if (input == null) return false;
        String trimmed = input.trim();
        if (trimmed.isEmpty()) return false;
        int morseChars = 0;
        for (char c : trimmed.toCharArray()) {
            if (c == '.' || c == '-' || c == '/' || Character.isWhitespace(c)) {
                morseChars++;
            }
        }
        return ((double) morseChars / trimmed.length()) >= 0.85 && (trimmed.contains(".") || trimmed.contains("-"));
    }

    @Override
    public DecryptionResult decrypt(String input, TextValidator validator) {
        // Words separated by " / " or multiple spaces
        String[] words = input.trim().split(" / | {2,}");
        StringBuilder sb = new StringBuilder();
        int recognizedTokens = 0;
        int totalTokens = 0;

        for (int w = 0; w < words.length; w++) {
            String[] letters = words[w].trim().split("\\s+");
            for (String letter : letters) {
                if (letter.isEmpty()) continue;
                totalTokens++;
                Character ch = MORSE_MAP.get(letter);
                if (ch != null) {
                    sb.append(ch);
                    recognizedTokens++;
                } else if (letter.equals("/")) {
                    sb.append(" ");
                }
            }
            if (w < words.length - 1) {
                sb.append(" ");
            }
        }

        if (totalTokens == 0 || (double) recognizedTokens / totalTokens < 0.80) {
            return null;
        }

        String decoded = sb.toString().trim();
        double confidence = validator.evaluateText(decoded);
        if (confidence < 0.40) {
            // If it's valid morse token-wise (e.g. producing hex or code for layered decryptions), give base confidence 0.70
            confidence = ((double) recognizedTokens / totalTokens) * 0.70;
        }

        return new DecryptionResult(getName(), decoded, confidence);
    }
}
