# Rubix
### Intelligent Cipher Detector & Multi-Layer Decryptor

Rubix is a cryptographic identification and decryption engine written in Java. It automatically detects, scores, and decrypts classical ciphers, modern encodings, and nested multi-layer payloads without requiring the cipher type or key in advance.

Powered by statistical heuristics including Chi-Square frequency analysis, Index of Coincidence, digram scoring, and dictionary verification, Rubix distinguishes genuine plaintexts from noise and prevents false positives on corrupted inputs.

---

## Features

- Automated Scheme Detection: Classifies ciphertext and determines the best decryption candidate with an exact confidence percentage.
- Multi-Layer Pipeline: Unpacks nested ciphers automatically (for example, `Morse Code -> Hexadecimal -> Plaintext`).
- Statistical and Lexical Validation:
  - Unigram English frequency distribution testing via Chi-Square.
  - English digram proximity matching.
  - Core lexicon dictionary matching across spaced sentences and continuous unspaced text.
  - Binary corruption rejection for unprintable ASCII and malformed byte substitutions.
- Dual Interface:
  - Desktop GUI: Swing application with split-pane view, dedicated file import, and drag-and-drop file support.
  - Command-Line Interface (CLI): Direct flag decryption and built-in demo test suite.
- Zero-Build Source Execution: Runs directly from source in-memory on modern Java (21+) without generating stray class files.

---

## Supported Ciphers & Encodings

| Scheme | Description | Key Recovery / Method |
| :--- | :--- | :--- |
| **Affine Cipher** | Classical monoalphabetic substitution ($E(x) = (ax + b) \bmod 26$) | Coprime key search ($a \in \{1, 3, 5, \dots, 25\}$, $b \in [0, 25]$) via modular inverse |
| **Rail Fence Cipher** | Transposition cipher along zig-zag rail paths | Automated cycle solver testing rails 2 through 15 with space preservation and continuous stream support |
| **Single-Byte XOR** | Byte-level XOR cipher common in CTF challenges | Brute-forces keys `0x01` through `0xFF` on hexadecimal payloads scored against English frequency distributions |
| **Binary (ASCII)** | 7-bit and 8-bit binary stream encoding | Decodes continuous bit sequences and space-delimited byte tokens |
| **Caesar Cipher** | Monoalphabetic shift substitution | Brute-force search across all 25 shifts evaluated by frequency heuristics |
| **ROT13** | Symmetric 13-character rotation | Direct transformation verified by dictionary matching |
| **Atbash Cipher** | Classical Hebrew alphabet-reversal cipher (`A` to `Z`) | Symmetric reciprocal substitution |
| **Vigenère Cipher** | Polyalphabetic substitution | Automated key recovery using beam search and Chi-Square slice optimization (lengths 2–7) |
| **Reverse String** | Reversed character sequencing | Reverses text order and validates against dictionary |
| **Hexadecimal** | ASCII hex-encoded strings | Parses continuous hex, spaced bytes, colons, and `0x` prefixes |
| **Base64** | Standard Base64 RFC 4648 | Decodes binary payloads with printable ASCII validation |
| **Morse Code** | International Morse code (`.` and `-`) | Tokenizes letters and words (`/` or spaces) into ASCII text |
| **Multi-Layer Pipeline** | Chained and nested encodings | Automatically chains decoders when intermediate layers yield valid encodings |

---

## Getting Started

### Prerequisites
- Java SE Development Kit (JDK) 21+

### Installation
Clone the repository:
```powershell
git clone [https://github.com/TheCanadianYeti/rubix.git](https://github.com/TheCanadianYeti/rubix.git)
cd rubix
