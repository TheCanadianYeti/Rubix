package rubix;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class RubixGui extends JFrame {

    private final RubixEngine engine;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JTextField schemeField;
    private JTextField confidenceField;

    public RubixGui(RubixEngine engine) {
        this.engine = engine;
        initUi();
    }

    private void initUi() {
        setTitle("Rubix - Cipher Detector & Decryptor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 450));
        setLocationRelativeTo(null);

        Font monoFont = new Font(Font.MONOSPACED, Font.PLAIN, 13);

        inputArea = new JTextArea();
        inputArea.setFont(monoFont);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        setupDragAndDrop();
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder("Ciphertext / Encoded Input"));

        outputArea = new JTextArea();
        outputArea.setFont(monoFont);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Decrypted Plaintext Output"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputScroll, outputScroll);
        splitPane.setResizeWeight(0.5);

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 10, 12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);

        // --- ROW 0: Status & Detection Results ---
        JLabel schemeLabel = new JLabel("Detected Scheme:");
        schemeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        controlPanel.add(schemeLabel, gbc);

        schemeField = new JTextField();
        schemeField.setEditable(false);
        schemeField.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        schemeField.setPreferredSize(new Dimension(220, 28));
        schemeField.setMinimumSize(new Dimension(150, 28));
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlPanel.add(schemeField, gbc);

        JLabel confLabel = new JLabel("Confidence:");
        confLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        controlPanel.add(confLabel, gbc);

        confidenceField = new JTextField(8);
        confidenceField.setEditable(false);
        confidenceField.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        confidenceField.setHorizontalAlignment(JTextField.CENTER);
        confidenceField.setPreferredSize(new Dimension(90, 28));
        confidenceField.setMinimumSize(new Dimension(80, 28));
        gbc.gridx = 3; gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlPanel.add(confidenceField, gbc);

        // --- ROW 1: Action Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 2));
        JButton importBtn = new JButton("Import File");
        JButton decryptBtn = new JButton("Detect & Decrypt");
        JButton clearBtn = new JButton("Clear");

        importBtn.setPreferredSize(new Dimension(110, 30));
        decryptBtn.setPreferredSize(new Dimension(140, 30));
        clearBtn.setPreferredSize(new Dimension(80, 30));

        buttonPanel.add(importBtn);
        buttonPanel.add(decryptBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlPanel.add(buttonPanel, gbc);

        importBtn.addActionListener(e -> importFile());
        decryptBtn.addActionListener(e -> runDecryption());
        clearBtn.addActionListener(e -> {
            inputArea.setText("");
            outputArea.setText("");
            schemeField.setText("");
            confidenceField.setText("");
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
    }

    private void importFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select File to Import");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text and Encoded Files (*.txt, *.enc, *.hex, *.b64, *.log, *.dat)", "txt", "enc", "hex", "b64", "log", "dat"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            loadFileContent(fileChooser.getSelectedFile());
        }
    }

    private void loadFileContent(File file) {
        try {
            String content;
            try {
                content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            } catch (Exception utf8Ex) {
                content = Files.readString(file.toPath(), StandardCharsets.ISO_8859_1);
            }
            inputArea.setText(content);
            inputArea.setCaretPosition(0);
            runDecryption();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error reading file:\n" + ex.getMessage(), "File Import Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupDragAndDrop() {
        inputArea.setDropTarget(new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    Object data = evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (data instanceof List<?> list && !list.isEmpty()) {
                        Object first = list.get(0);
                        if (first instanceof File file) {
                            loadFileContent(file);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });
    }

    private void runDecryption() {
        String input = inputArea.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter ciphertext to decrypt.", "Empty Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DecryptionResult result = engine.process(input);
        if (result != null) {
            schemeField.setText(result.getScheme());
            confidenceField.setText(String.format("%.2f%%", result.getConfidence() * 100));
            outputArea.setText(result.getPlaintext());
        } else {
            schemeField.setText("Unknown / Unsolvable");
            confidenceField.setText("0.00%");
            outputArea.setText("Failed to decrypt: Text could not be identified or validated against English plaintext heuristics.");
        }
    }

    public static void launch(RubixEngine engine) {
        SwingUtilities.invokeLater(() -> {
            RubixGui gui = new RubixGui(engine);
            gui.setVisible(true);
        });
    }
}