package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    private static final int WORD_LENGTH = 5;
    private static final int MAX_GUESSES = 5;
    
    private JLabel topLabel;
    private JTextField textbox;
    private JButton submit;
    private JTextArea outputArea;
    
    private JLabel[][] tiles;
    private int currentRow = 0;
    
    private WordleGame game;

    public App(WordleGame game) {
        this.game = game;

        //////////////////////////////
        // SWING WINDOW
        //////////////////////////////
        this.setTitle("WORDLE GAME");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setContentPane(mainPanel);

        // Top label (instructions)
        this.topLabel = new JLabel("<html>This is Wordle. You have 5 tries to guess the 5-letter word.<br>" +
                " Red = wrong letter, Yellow = right letter wrong place, Green = correct spot.</html>");
        this.topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(this.topLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(MAX_GUESSES, WORD_LENGTH, 5, 5));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tiles = new JLabel[MAX_GUESSES][WORD_LENGTH];

        for (int row = 0; row < MAX_GUESSES; row++) {
            for (int col = 0; col < WORD_LENGTH; col++) {
                JLabel tile = new JLabel(" ", SwingConstants.CENTER);
                tile.setPreferredSize(new Dimension(40, 40));
                tile.setOpaque(true);
                tile.setBackground(Color.DARK_GRAY);
                tile.setForeground(Color.WHITE);
                tile.setFont(tile.getFont().deriveFont(Font.BOLD, 18f));
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                tiles[row][col] = tile;
                gridPanel.add(tile);
            }
        }

        mainPanel.add(gridPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Input panel (textbox + button)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

        this.textbox = new JTextField();
        this.textbox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        inputPanel.add(this.textbox);

        inputPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        this.submit = new JButton("Submit guess!");
        inputPanel.add(this.submit);

        mainPanel.add(inputPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Output area (for guesses + feedback)
        this.outputArea = new JTextArea(15, 30);
        this.outputArea.setEditable(false);
        this.outputArea.setLineWrap(true);
        this.outputArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(this.outputArea);
        mainPanel.add(scrollPane);

        // Button action: handle guess
        this.submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGuess();
            }
        });

        // Pressing Enter in the textbox triggers submit too
        this.textbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGuess();
            }
        });

        this.pack();
        this.setLocationRelativeTo(null); // center on screen
    }

    private void handleGuess() {
        
        if (currentRow >= MAX_GUESSES) {
            return;
        }

        String uword = textbox.getText().trim().toLowerCase();
        if (uword.isEmpty()) {
            return;
        }

        // Clear textbox for next input
        textbox.setText("");

        // Validate guess
        if (!game.isGuessValid(uword)) {
            outputArea.append(uword + " is not valid, try another word.\n");
            return;
        }

        String result = game.checkGuess(uword);
        
        updateGridRow(uword, result);

        // Win condition
        if (uword.equals(result)) {
            outputArea.append("You guessed: " + uword + "\n");
            outputArea.append("Result: " + result + "\n");
            outputArea.append("You win! The word was " + game.getWord() + "\n");
            endGame();
        }
        // Lose condition
        else if (game.getGuessesLeft() <= 0) {
            outputArea.append("You guessed: " + uword + "\n");
            outputArea.append("Result: " + result + "\n");
            outputArea.append("You lose! The word was " + game.getWord() + "\n");
            endGame();
        }
        // Continue playing
        else {
            outputArea.append("You guessed: " + uword + "\n");
            outputArea.append("Result: " + result + "\n");
            outputArea.append("You have " + game.getGuessesLeft() + " guesses left.\n\n");
        }
    }
    private void updateGridRow(String guess, String result) {
        if (currentRow >= MAX_GUESSES) {
            return;
        }

        for (int col = 0; col < WORD_LENGTH; col++) {
            char guessedChar = guess.charAt(col);
            char resChar = result.charAt(col);

            JLabel tile = tiles[currentRow][col];
            tile.setText(String.valueOf(Character.toUpperCase(guessedChar)));

            Color bg;
            if (resChar == '.') {
                bg = Color.DARK_GRAY;      // not in word
            } else if (resChar == '*') {
                bg = Color.YELLOW;         // wrong place, right letter
            } else {
                bg = Color.GREEN;          // correct letter, correct place
            }

            tile.setBackground(bg);
        }

        currentRow++;
    }

    private void endGame() {
        textbox.setEditable(false);
        submit.setEnabled(false);
        topLabel.setText("Game over. Restart the app to play again.");
    }

    public static void main(String[] args) {
        // WORDS SETUP (same as you had, but done before launching Swing)
        List<String> words = new ArrayList<>();

        InputStream in = App.class.getResourceAsStream("/words.txt");
        if (in == null) {
            System.out.println("Could not find words.txt on classpath");
            return;
        }

         try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            List<String> lines = br.lines().collect(Collectors.toList());
            for (String line : lines) {
                String w = line.trim().toLowerCase();
                if (!w.isEmpty()) {
                    words.add(w);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading words.txt: " + e.getMessage());
            return;
        }

        if (words.isEmpty()) {
            System.out.println("No words found in words.txt");
            return;
        }

        WordleGame game = new WordleGame(words);

        SwingUtilities.invokeLater(() -> {
            App app = new App(game);
            app.setVisible(true);
        });
    }
}
