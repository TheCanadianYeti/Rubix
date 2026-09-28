package rubix;

public class Main {
    public static void main(String[] args) {
        RubixEngine engine = new RubixEngine();

        if (args.length >= 2 && (args[0].equals("-c") || args[0].equals("--ciphertext"))) {
            runCliDirect(engine, args[1]);
            return;
        }

        if (args.length >= 1 && (args[0].equals("--demo") || args[0].equals("-d"))) {
            runCliDemo(engine);
            return;
        }

        if (args.length >= 1 && (args[0].equals("--help") || args[0].equals("-h"))) {
            printUsage();
            return;
        }

        RubixGui.launch(engine);
    }

    private static void runCliDirect(RubixEngine engine, String input) {
        System.out.println("Processing: " + input);
        DecryptionResult result = engine.process(input);
        if (result != null) {
            System.out.println("Detected Scheme: " + result.getScheme());
            System.out.println("Confidence:      " + String.format("%.2f%%", result.getConfidence() * 100));
            System.out.println("Decrypted Text:  " + result.getPlaintext());
        } else {
            System.out.println("Failed to decrypt: Text could not be identified or validated.");
        }
    }

    private static void runCliDemo(RubixEngine engine) {
        String[] samples = {
            "Wkh txlfn eurzq ira mxpsv ryhu wkh odcb grj.",
            "Flfgrzf bcrengvbany naq ernql sbe grfgvat.",
            "VmVyaWZ5IGRldGVjdGlvbiBhbGdvcml0aG0gZml4ZWQu",
            ".gninraw tuohtiw noitpyrcne lairt etelpmoc ot deriuqer snoitcurtsnI",
            "Rijvs uy eomvh, wsmhn dy kckecr!",
            "Rijvs ry ambpb, biync ry hcdiad!"
        };

        for (String sample : samples) {
            System.out.println("--------------------------------------------------");
            System.out.println("Input: " + sample);
            DecryptionResult result = engine.process(sample);
            if (result != null) {
                System.out.println("Detected Scheme: " + result.getScheme());
                System.out.println("Confidence:      " + String.format("%.2f%%", result.getConfidence() * 100));
                System.out.println("Decrypted Text:  " + result.getPlaintext());
            } else {
                System.out.println("Failed to decrypt: Text could not be identified or validated.");
            }
        }
    }

    private static void printUsage() {
        System.out.println("Rubix - Cipher Detector and Decryptor");
        System.out.println("Usage:");
        System.out.println("  java rubix.Main                  Launch the desktop GUI");
        System.out.println("  java rubix.Main -c <string>       Decrypt ciphertext directly in terminal");
        System.out.println("  java rubix.Main --demo            Run built-in test suite in terminal");
        System.out.println("  java rubix.Main --help            Display this help message");
    }
}