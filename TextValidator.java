package rubix;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TextValidator {
    private final Set<String> commonWords;

    private static final double[] ENGLISH_FREQS = {
        8.167, 1.492, 2.782, 4.253, 12.702, 2.228, 2.015, 6.094, 6.966, 0.153,
        0.772, 4.025, 2.406, 6.749, 7.507, 1.929, 0.095, 5.987, 6.327, 9.056,
        2.758, 0.978, 2.360, 0.150, 1.974, 0.074
    };

    public TextValidator() {
        commonWords = new HashSet<>(Arrays.asList(
            "a", "about", "above", "action", "active", "add", "added", "admin", "after", "again", "air", "algorithm", "algorithms", "all", "allow",
            "allowed", "allows", "almost", "along", "already", "also", "although", "always", "amount", "an", "and", "another", "answer", "answered", "answers",
            "any", "applied", "applies", "apply", "are", "area", "areas", "around", "as", "ascii", "ask", "asked", "asks", "assist", "at",
            "ate", "attack", "away", "back", "balance", "base", "basic", "be", "became", "because", "become", "becomes", "been", "before", "began",
            "begin", "begins", "begun", "behind", "being", "believe", "below", "best", "better", "between", "beyond", "big", "black", "blue", "body",
            "book", "books", "both", "boy", "boys", "brown", "build", "building", "builds", "built", "business", "but", "by", "bye", "byte",
            "bytes", "caesar", "call", "called", "came", "can", "car", "carry", "cars", "case", "cases", "cause", "caused", "causes", "certain",
            "certainly", "change", "changed", "changes", "char", "character", "characters", "check", "checked", "checking", "checks", "children", "choose", "cipher", "ciphers",
            "ciphertext", "city", "clear", "clearly", "close", "closed", "code", "coded", "codes", "come", "comes", "common", "communication", "communications", "complete",
            "completed", "completes", "completing", "condition", "confidential", "confidentially", "confirm", "confirmation", "confirmed", "confirming", "confirms", "connect", "connected", "connection", "connections",
            "control", "could", "country", "course", "cover", "create", "created", "current", "day", "days", "decide", "decode", "decoded", "decoder", "decodes",
            "decoding", "detect", "detected", "detecting", "detection", "detects", "did", "different", "direct", "directly", "discover", "do", "does", "dog", "dogs",
            "down", "each", "early", "earth", "easily", "easy", "eat", "effect", "eight", "either", "element", "encrypt", "encrypted", "encrypting", "encryption",
            "encrypts", "end", "ended", "ends", "engine", "enough", "enter", "entered", "entry", "error", "errors", "establish", "established", "establishes", "establishing",
            "establishment", "even", "event", "every", "exact", "exactly", "example", "examples", "expect", "expected", "explain", "eyes", "face", "fact", "facts",
            "failure", "false", "family", "far", "fast", "father", "feel", "few", "figure", "final", "finally", "find", "finds", "first", "five",
            "fix", "fixed", "fixes", "fixing", "follow", "followed", "food", "for", "form", "format", "forms", "found", "four", "fox", "foxes",
            "free", "from", "front", "full", "future", "gave", "general", "girl", "give", "given", "gives", "go", "goes", "gone", "good",
            "goodbye", "got", "great", "green", "group", "had", "hand", "hands", "hard", "has", "have", "he", "head", "hear", "heard",
            "hello", "help", "helped", "helps", "her", "here", "hex", "hexadecimal", "hey", "hi", "hidden", "high", "him", "his", "hold",
            "home", "hope", "house", "how", "human", "i", "idea", "ideas", "if", "image", "important", "in", "include", "included", "includes",
            "indeed", "inside", "instance", "instead", "instruction", "instructions", "into", "is", "issue", "it", "its", "job", "jump", "jumped", "jumping",
            "jumps", "just", "keep", "keeps", "kept", "key", "keyed", "keys", "kind", "kinds", "knew", "know", "known", "knows", "land",
            "large", "last", "later", "lazy", "lead", "learn", "learned", "learns", "least", "leave", "let", "letter", "letters", "level", "life",
            "light", "like", "liked", "likes", "line", "lines", "list", "lists", "little", "live", "login", "logout", "long", "look", "looked",
            "looks", "made", "main", "major", "make", "makes", "man", "many", "match", "matched", "matches", "me", "mean", "means", "meant",
            "measure", "men", "message", "messages", "method", "methods", "might", "miss", "modern", "moment", "more", "most", "move", "moved", "moves",
            "much", "must", "my", "name", "names", "nature", "near", "need", "needed", "needs", "never", "new", "next", "night", "nine",
            "no", "not", "note", "notes", "notice", "now", "null", "number", "numbers", "object", "objects", "occur", "occurs", "of", "off",
            "office", "often", "oil", "ok", "okay", "old", "on", "once", "one", "only", "open", "opened", "operate", "operating", "operation",
            "operational", "or", "order", "original", "other", "our", "out", "over", "own", "page", "pages", "paper", "part", "parts", "pass",
            "password", "pattern", "people", "perform", "performance", "performed", "period", "person", "picture", "piece", "place", "plaintext", "plan", "plans", "plant",
            "play", "point", "points", "possible", "power", "present", "printable", "privacy", "private", "problem", "problems", "process", "processed", "produce", "program",
            "programs", "project", "provide", "provided", "provides", "public", "purpose", "put", "puts", "quality", "question", "quick", "quickly", "ran", "range",
            "rate", "reach", "read", "reading", "reads", "ready", "real", "really", "reason", "receive", "record", "red", "reference", "region", "release",
            "remain", "report", "require", "required", "requires", "requiring", "resolve", "resolved", "respect", "respond", "responded", "responding", "response", "responses", "result",
            "results", "return", "right", "river", "rot", "rubix", "rule", "rules", "run", "running", "runs", "safe", "said", "same", "sample",
            "samples", "saw", "say", "says", "school", "score", "sea", "second", "secret", "secrets", "section", "secure", "security", "see", "seek",
            "seem", "seemed", "seems", "seen", "sees", "sense", "sentence", "sentences", "serve", "server", "servers", "service", "set", "sets", "seven",
            "several", "share", "she", "shift", "shifted", "shifts", "should", "show", "showed", "shown", "shows", "side", "sides", "sign", "simple",
            "simply", "single", "six", "size", "small", "so", "solution", "solutions", "solve", "solved", "solves", "solving", "some", "something", "soon",
            "sound", "source", "space", "special", "specific", "speed", "standard", "start", "started", "starts", "state", "states", "still", "stop", "stopped",
            "stops", "story", "string", "strings", "structure", "study", "subject", "success", "successful", "successfully", "such", "suggest", "support", "system", "systems",
            "table", "take", "taken", "takes", "talk", "talked", "talks", "tell", "tells", "ten", "term", "terms", "test", "tested", "testing",
            "tests", "text", "texts", "than", "that", "the", "their", "them", "then", "there", "these", "they", "thing", "things", "think",
            "thinks", "this", "those", "thought", "three", "through", "time", "times", "to", "together", "told", "too", "took", "total", "train",
            "tree", "trial", "trials", "tried", "tries", "true", "try", "turn", "turned", "turns", "two", "type", "types", "under", "unit",
            "units", "until", "up", "us", "use", "used", "user", "users", "uses", "value", "values", "verification", "verified", "verifies", "verify",
            "verifying", "very", "view", "vigenere", "visit", "wait", "walk", "walked", "walks", "want", "wanted", "wants", "warn", "warned", "warning",
            "warnings", "warns", "was", "watch", "water", "way", "ways", "we", "week", "welcome", "well", "went", "were", "what", "when",
            "where", "which", "while", "white", "who", "whole", "why", "wide", "will", "with", "without", "word", "words", "work", "worked",
            "working", "works", "world", "would", "write", "written", "wrote", "year", "years", "yellow", "yes", "you", "young", "your", "zero"
        ));
    }

    public boolean isPrintableAscii(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        int printableCount = 0;
        for (char c : text.toCharArray()) {
            if ((c >= 32 && c <= 126) || c == '\n' || c == '\r' || c == '\t') {
                printableCount++;
            }
        }
        return ((double) printableCount / text.length()) >= 0.95;
    }

    public double calculateChiSquare(String text) {
        String clean = text.toUpperCase().replaceAll("[^A-Z]", "");
        int len = clean.length();
        if (len == 0) {
            return 999.0;
        }

        int[] counts = new int[26];
        for (char c : clean.toCharArray()) {
            counts[c - 'A']++;
        }

        double chiSquare = 0.0;
        for (int i = 0; i < 26; i++) {
            double expected = (ENGLISH_FREQS[i] / 100.0) * len;
            double diff = counts[i] - expected;
            chiSquare += (diff * diff) / expected;
        }

        return chiSquare;
    }

    public double calculateWordScore(String text) {
        if (text == null || text.isEmpty()) {
            return 0.0;
        }
        String[] tokens = text.toLowerCase().split("[^a-z]+");
        if (tokens.length == 0) {
            return 0.0;
        }

        int matches = 0;
        int validTokens = 0;
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            validTokens++;
            if (token.length() == 1) {
                if (token.equals("a") || token.equals("i")) {
                    matches++;
                }
            } else {
                if (commonWords.contains(token)) {
                    matches++;
                }
            }
        }
        if (validTokens == 0) {
            return 0.0;
        }
        return (double) matches / validTokens;
    }

    public double evaluateText(String text) {
        if (!isPrintableAscii(text)) {
            return 0.0;
        }

        // Require at least 40% of non-whitespace characters to be letters
        int letterCount = 0;
        int nonWhitespaceCount = 0;
        for (char c : text.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                nonWhitespaceCount++;
                if (Character.isLetter(c)) {
                    letterCount++;
                }
            }
        }
        if (letterCount < 3 || (nonWhitespaceCount > 0 && (double) letterCount / nonWhitespaceCount < 0.40)) {
            return 0.0;
        }

        double wordScore = calculateWordScore(text);
        double chiSquare = calculateChiSquare(text);

        // If text has space-separated words, require genuine English word matches
        String[] tokens = text.toLowerCase().split("[^a-z]+");
        int wordCount = 0;
        for (String t : tokens) {
            if (t.length() > 1) wordCount++;
        }

        if (wordCount >= 3) {
            if (wordScore >= 0.35) {
                return Math.min(1.0, 0.40 + (wordScore * 0.60));
            } else {
                return 0.0;
            }
        }

        // For short text (< 20 characters), letter frequency is not statistically reliable; require word matches
        String clean = text.toUpperCase().replaceAll("[^A-Z]", "");
        if (clean.length() < 20 && wordScore == 0.0) {
            return 0.0;
        }

        if (wordScore >= 0.35) {
            return Math.min(1.0, 0.40 + (wordScore * 0.60));
        }

        if (chiSquare < 35.0) {
            return Math.max(0.40, 1.0 - (chiSquare / 100.0));
        }

        return wordScore;
    }
}
