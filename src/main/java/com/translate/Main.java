package com.translate;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Main extends JFrame {
    // Audio control buttons
    private JButton startButton;
    private JButton stopButton;
    // Translate control button
    private JButton translateButton;
    
    private Recorder recorder;
    private JTextArea transcriptionTextArea;
    private JTextArea translationTextArea;
    
    // Language selection dropdowns
    private JComboBox<Language> sourceLanguageDropdown;
    private JComboBox<Language> targetLanguageDropdown;

    public Main() {
        super("Audio Recorder, Speech-to-Text and Translation");
        setLayout(new BorderLayout());
        
        // Create the top panel with left and right sections.
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Left side: Panel for button groups.
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        
        // Audio Tools group
        JPanel audioToolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        audioToolsPanel.setBorder(BorderFactory.createTitledBorder("Audio Tools"));
        startButton = new JButton("Start Recording");
        stopButton = new JButton("Stop Recording");
        stopButton.setEnabled(false);
        audioToolsPanel.add(startButton);
        audioToolsPanel.add(stopButton);
        
        // Translate Tools group
        JPanel translateToolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        translateToolsPanel.setBorder(BorderFactory.createTitledBorder("Translate Tools"));
        translateButton = new JButton("Translate Text");
        translateToolsPanel.add(translateButton);
        
        leftPanel.add(audioToolsPanel);
        leftPanel.add(translateToolsPanel);
        
        // Right side: Translation Options panel (vertical layout).
        JPanel translationOptionsPanel = new JPanel();
        translationOptionsPanel.setLayout(new BoxLayout(translationOptionsPanel, BoxLayout.Y_AXIS));
        translationOptionsPanel.setBorder(BorderFactory.createTitledBorder("Translation Options"));
        
        sourceLanguageDropdown = new JComboBox<>(Language.values());
        sourceLanguageDropdown.setSelectedItem(Language.ENGLISH);
        sourceLanguageDropdown.setToolTipText("Select source language for speech recognition and translation");
        targetLanguageDropdown = new JComboBox<>(Language.values());
        targetLanguageDropdown.setSelectedItem(Language.SPANISH);
        targetLanguageDropdown.setToolTipText("Select target language for translation");
        
        // Create separate panels for each dropdown to align labels and components.
        JPanel sourcePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sourcePanel.add(new JLabel("Source Language:"));
        sourcePanel.add(sourceLanguageDropdown);
        
        JPanel targetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        targetPanel.add(new JLabel("Target Language:"));
        targetPanel.add(targetLanguageDropdown);
        
        translationOptionsPanel.add(sourcePanel);
        translationOptionsPanel.add(targetPanel);
        
        // Assemble the top panel.
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(translationOptionsPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        
        // Create text areas.
        transcriptionTextArea = new JTextArea();
        transcriptionTextArea.setLineWrap(true);
        transcriptionTextArea.setWrapStyleWord(true);
        transcriptionTextArea.setEditable(true);  // Editable for manual text entry.
        transcriptionTextArea.setBorder(BorderFactory.createTitledBorder("Transcribed/Entered Text"));
        
        translationTextArea = new JTextArea();
        translationTextArea.setEditable(false);
        translationTextArea.setLineWrap(true);
        translationTextArea.setWrapStyleWord(true);
        translationTextArea.setBorder(BorderFactory.createTitledBorder("Translated Text"));
        
        // Split pane for the two text areas.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(transcriptionTextArea), new JScrollPane(translationTextArea));
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
        
        // File path for the audio file.
        String audioFilePath = "demo/src/main/resources/recorded_audio.wav";
        recorder = new Recorder(audioFilePath);
        
        // Audio Tools actions.
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transcriptionTextArea.setText("");
                translationTextArea.setText("");
                recorder.Start();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recorder.Stop();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                try {
                    // Use the selected source language for audio transcription.
                    Language sourceLang = (Language) sourceLanguageDropdown.getSelectedItem();
                    String transcript = SpeechToText.convertAudioToText(audioFilePath, sourceLang.getCode());
                    transcriptionTextArea.setText(transcript);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(Main.this,
                            "Error during transcription: " + ex.getMessage(),
                            "Transcription Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Translate Tools action.
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = transcriptionTextArea.getText();
                if (inputText == null || inputText.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(Main.this,
                            "Please enter or transcribe some text to translate.",
                            "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                TranslateInput translator = new TranslateInput();
                translator.setSourceLanguage((Language) sourceLanguageDropdown.getSelectedItem());
                translator.setTargetLanguage((Language) targetLanguageDropdown.getSelectedItem());
                String translatedText = translator.TranslateInput(inputText);
                translationTextArea.setText(translatedText);
            }
        });
        
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Main().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
            }
        });
    }
}
