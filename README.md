# Rubix 🧩
### Intelligent Cipher Detector & Multi-Layer Decryptor

![Java](https://img.shields.io/badge/Java-21%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20macOS-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**Rubix** is a cryptographic identification and decryption tool written in Java. It automatically detects, scores, and decrypts classic ciphers, binary encodings, and nested multi-layer payloads (e.g. Morse Code &rarr; Hexadecimal &rarr; English) without requiring the user to know the cipher type or key in advance.

Powered by statistical heuristics—including **Chi-Square ($\chi^2$) frequency analysis**, **Index of Coincidence (IC)**, and **dictionary verification**—Rubix distinguishes genuine plaintexts from noise, preventing false-positive hallucinations on corrupted inputs.

---

## ✨ Features

- **Automated Scheme Detection**: Automatically classifies ciphertext and determines the best decryption candidate with a confidence percentage.
- **Multi-Layer / Recursive Pipeline**: Automatically unpacks nested ciphers (e.g., `Morse Code -> Hexadecimal -> Plaintext` or `Base64 -> ROT13 -> Plaintext`).
- **Statistical & Lexical Validation**:
  - Unigram English frequency distribution testing via Chi-Square ($\chi^2$).
  - Fast dictionary-based word matching with a comprehensive 780+ word core lexicon.
  - Binary corruption rejection: Discards unprintable ASCII and malformed byte substitutions.
- **Dual Interface**:
  - **Desktop GUI**: Swing application with split-pane view, dedicated file import, and drag-and-drop file support.
  - **Command-Line Interface (CLI)**: Direct decryption and built-in demo test suite.
- **Zero-Build Source Execution**: Runs directly from source in-memory on modern Java (22+) without generating stray `.class` files.

---

## 🔐 Supported Ciphers & Encodings

| Scheme | Description | Key Recovery / Method |
| :--- | :--- | :--- |
| **Caesar Cipher** | Monoalphabetic shift substitution | Brute-force search (all 25 shifts) evaluated by frequency heuristics |
| **ROT13** | Symmetric 13-character rotation | Direct transformation verified by dictionary matching |
| **Atbash Cipher** | Classical Hebrew alphabet-reversal cipher (`A` &harr; `Z`) | Symmetric reciprocal substitution |
| **Vigenère Cipher** | Polyalphabetic substitution | Automated key recovery using beam search and Chi-Square slice optimization (lengths 2–7) |
| **Reverse String** | Reversed character sequencing | Reverses text order and validates against dictionary |
| **Hexadecimal** | ASCII hex-encoded strings | Parses continuous hex, spaced bytes (`53 65 72 ...`), colons, and `0x` prefixes |
| **Base64** | Standard Base64 RFC 4648 | Decodes binary payloads with printable ASCII validation |
| **Morse Code** | International Morse code (`.` and `-`) | Tokenizes letters and words (`/` or double spaces) into ASCII text |
| **Multi-Layer Pipeline** | Chained/nested encodings | Automatically chains decoders when intermediate layers yield valid encodings |

---

## 🚀 Getting Started

### Prerequisites
- [Java SE Development Kit (JDK) 21+](https://www.oracle.com/java/technologies/downloads/) (Java 22 or 25 recommended for direct source launching).

### Installation
Clone the repository:
```bash
git clone https://github.com/TheCanadianYeti/rubix.git
cd rubix
```

---

## 💻 Usage

### 1. Launching the GUI
Run directly from source:
```powershell
java Main.java
```
Or run using the executable JAR / helper script:
```powershell
.\run.bat
# or
java -jar rubix.jar
```

#### GUI Highlights:
- **Ciphertext Input**: Type, paste, or **drag & drop** any text or encoded file directly into the input area.
- **Import File**: Click **Import File** to browse `.txt`, `.enc`, `.hex`, `.b64`, `.dat`, or any arbitrary file.
- **Auto-Detection**: Results populate automatically upon file import or when clicking **Detect & Decrypt**.
- **Responsive Layout**: Dedicated status and action bars designed to adapt seamlessly across standard and high-DPI displays.

---

### 2. Command-Line Interface (CLI)

#### Direct Decryption (`-c` or `--ciphertext`):
```powershell
java Main.java -c "Wkh txlfn eurzq ira mxpsv ryhu wkh odcb grj."
```
**Output:**
```text
Processing: Wkh txlfn eurzq ira mxpsv ryhu wkh odcb grj.
Detected Scheme: Caesar (Shift 3)
Confidence:      100.00%
Decrypted Text:  The quick brown fox jumps over the lazy dog.
```

#### Multi-Layer Nested Decryption:
```powershell
java Main.java -c "..... ...--   -.... .....   --... ..---   --... -....   -.... .....   --... ..---"
```
**Output:**
```text
Processing: ..... ...--   -.... .....   --... ..---   --... -....   -.... .....   --... ..---
Detected Scheme: Morse Code -> Hexadecimal
Confidence:      100.00%
Decrypted Text:  Server
```

#### Run the Built-in Test Suite (`--demo`):
```powershell
java Main.java --demo
```

#### Help Flag (`-h` or `--help`):
```powershell
java Main.java --help
```

---

## 🛠️ Building & Packaging

To compile and package Rubix into a standalone executable JAR:

```powershell
.\build.bat
```
This compiles the classes into an isolated temporary directory, packages `rubix.jar` with `rubix.Main` as the entrypoint, and cleans up intermediate `.class` files.

---

## 📂 Project Architecture

```
rubix/
├── Analyzer.java            # Fast regex & statistical candidate filters (entropy, IC, hex, b64)
├── AtbashDecryptor.java      # Atbash reciprocal substitution cipher implementation
├── Base64Decryptor.java      # Base64 decoder with printable-ASCII heuristics
├── CaesarDecryptor.java      # Caesar & ROT13 solver with shift scoring
├── DecryptionResult.java     # Immutable result carrier (scheme, plaintext, confidence)
├── Decryptor.java            # Standard interface for all decryption modules
├── HexDecryptor.java         # Tolerant hexadecimal decoder (spaces, colons, 0x)
├── Main.java                 # Entrypoint routing CLI arguments and GUI launch
├── MorseDecryptor.java       # International Morse code decoder
├── ReverseDecryptor.java     # String reversal cipher solver
├── RubixEngine.java          # Pipeline engine orchestrating decryption & multi-layer recursion
├── RubixGui.java             # Swing desktop interface with drag-and-drop & file picker
├── TextValidator.java        # Chi-Square frequency analyzer & dictionary evaluator
├── VigenereDecryptor.java    # Automated Vigenère solver using beam search slice analysis
├── build.bat                 # Clean build script for packaging standalone JAR
├── run.bat                   # Convenience runner shortcut
└── README.md                 # Project documentation
```

---

## 👤 Author

**Marcus "Yeti" Podnar**
- GitHub: [@TheCanadianYeti](https://github.com/TheCanadianYeti)
- LinkedIn: [marcus-podnar](https://www.linkedin.com/in/marcus-podnar/)

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
