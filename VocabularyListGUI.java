import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VocabularyListGUI extends JFrame {

    private JTextArea documentArea, userEnteredWords;
    private JTable unigramTable, bigramTable, trigramTable;
    private JButton loadDocumentButton, processDocumentButton, sortButton, bigramButton, trigramButton, predictNextButton, showStatsButton;
    private JLabel totalWordsLabel, totalDifferentWordsLabel, averageLabel, stdDevLabel;
    private JButton UnicodeButton, FirstLetterButton;
    
    private MyHashTable myHashTable;
    private MyHashTable bigramHashTable;
    private MyHashTable trigramHashTable;

    
    private List<String>predictedWords = new ArrayList<String>(20);
    private String[] words;

    public VocabularyListGUI(MyHashTable myHashTable, MyHashTable bigramHashTable, MyHashTable trigramHashTable) {
        this.myHashTable = myHashTable;
        this.bigramHashTable = bigramHashTable;
        this.trigramHashTable = trigramHashTable;

        setTitle("Vocabulary List GUI");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createComponents();
        setLayout(new BorderLayout());
        addComponents();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createComponents() {
        documentArea = new JTextArea();
        documentArea.setEditable(false);
        userEnteredWords = new JTextArea();
        userEnteredWords.setEditable(true);
        userEnteredWords.setColumns(10);

        unigramTable = createTable();
        bigramTable = createTable();
        trigramTable = createTable();

        loadDocumentButton = new JButton("Load Doc");
        processDocumentButton = new JButton("Process Doc");

        sortButton = new JButton("Sort Unigrams");
        bigramButton = new JButton("load Bigram");
        trigramButton = new JButton("load Trigram");
        showStatsButton = new JButton("Show Statistics");

        UnicodeButton = new JButton("Hash: Unicode Sum");
        FirstLetterButton = new JButton("Hash: First Letter");
        
        totalWordsLabel = new JLabel("Total Words: ");
        totalDifferentWordsLabel = new JLabel("Total Different Words: ");
        averageLabel = new JLabel("Average: 0");
        stdDevLabel = new JLabel("Standard Deviation: 0");
        
        loadDocumentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDocument();
            }
        });

        processDocumentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processDocument();
                
            }
        });

        bigramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processBigram();
            }
        });

        trigramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processTrigram();
            }
        });
        
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortUnigramTable();
            }
        });

        showStatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStatistics();
            }
        });

        UnicodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                myHashTable = new MyHashTable(10, new UnicodeSumHash(10));
                DefaultTableModel unigramTableModel = (DefaultTableModel) unigramTable.getModel();
                unigramTableModel.setRowCount(0);

                bigramHashTable = new MyHashTable(10, new UnicodeSumHash(10));
                DefaultTableModel bigramTableModel = (DefaultTableModel) bigramTable.getModel();
                bigramTableModel.setRowCount(0);

                trigramHashTable = new MyHashTable(10, new UnicodeSumHash(10));
                DefaultTableModel trigramTableModel = (DefaultTableModel) trigramTable.getModel();
                trigramTableModel.setRowCount(0);

                JOptionPane.showMessageDialog(VocabularyListGUI.this, "Hash Algorithm is set to Unicode Sum Hashing. Please click on process document.", "Hash Algorithm switched to Unicode Sum", JOptionPane.INFORMATION_MESSAGE);
                
            }
        });

        FirstLetterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                myHashTable = new MyHashTable(10, new FirstLetterHash(10));
                DefaultTableModel unigramTableModel = (DefaultTableModel) unigramTable.getModel();
                unigramTableModel.setRowCount(0);

                bigramHashTable = new MyHashTable(10, new FirstLetterHash(10));
                DefaultTableModel bigramTableModel = (DefaultTableModel) bigramTable.getModel();
                bigramTableModel.setRowCount(0);

                trigramHashTable = new MyHashTable(10, new FirstLetterHash(10));
                DefaultTableModel trigramTableModel = (DefaultTableModel) trigramTable.getModel();
                trigramTableModel.setRowCount(0);

                JOptionPane.showMessageDialog(VocabularyListGUI.this, "Hash Algorithm is set to First Letter Hashing. Please click on process document.", "Hash Algorithm switched to First letter", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        predictNextButton = new JButton("Predict Next");
        predictNextButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String predictedTextArea = userEnteredWords.getText();
            if(!predictedTextArea.isEmpty()){
                predictNextWords(userEnteredWords.getText());
                StringBuilder DialogueWord = new StringBuilder();
                for(String ele: predictedWords){
                DialogueWord.append(ele+" ");
               }
               JOptionPane.showMessageDialog(VocabularyListGUI.this, "Most likely words: "+DialogueWord, "Prediction Prompt", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(VocabularyListGUI.this, "Please enter some text to find most likely words: No words found", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
            
        }
    });
    }

    private JTable createTable() {
        JTable table = new JTable(new DefaultTableModel(new Object[]{"Words", "Counts"}, 0));
        table.setFillsViewportHeight(true);
        return table;
    }

    private void addComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel documentPanel = new JPanel();
        documentPanel.setLayout(new BorderLayout());
        documentPanel.setBorder(BorderFactory.createTitledBorder("Document"));
        documentPanel.add(new JScrollPane(documentArea), BorderLayout.CENTER);
        documentPanel.setPreferredSize(new Dimension(documentPanel.getPreferredSize().width, 300));

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new GridLayout(1, 3));
        tablesPanel.add(createTablePanel(unigramTable, "Unigram"));
        tablesPanel.add(createTablePanel(bigramTable, "Bigram"));
        tablesPanel.add(createTablePanel(trigramTable, "Trigram"));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(loadDocumentButton);
        buttonsPanel.add(processDocumentButton);
        buttonsPanel.add(sortButton);
        buttonsPanel.add(bigramButton);
        buttonsPanel.add(trigramButton);
        buttonsPanel.add(showStatsButton);
        buttonsPanel.add(userEnteredWords);
        buttonsPanel.add(predictNextButton);
        buttonsPanel.add(UnicodeButton);
        buttonsPanel.add(FirstLetterButton);
        
        mainPanel.add(documentPanel, BorderLayout.NORTH);
        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    private void predictNextWords(String inputText) {
        String[] mywords = inputText.split("\\s+");
        predictedWords.clear();
        predictedWords = generateMostLikelyWordSequence(mywords, 10);

        List<String>bigramList = generateMostLikelyBigramSequence(mywords,10);
        for(String ele: bigramList){
            System.out.print(ele+ " ");
        }
    }

    private JPanel createTablePanel(JTable table, String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void processTrigram(){
        String documentText = documentArea.getText();
        if(documentText.isEmpty()){
            JOptionPane.showMessageDialog(VocabularyListGUI.this, "No document loaded. Please click on load document.", "No document loaded Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        trigramHashTable.clear();
        String[] newWord = new String[words.length/4];
        for(int i=0;i<words.length/4;i++){
            newWord[i]=words[i];
        }
        String[] wordTrigrams = mergeWordsIntoTriWords(newWord);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        for (String word : wordTrigrams) {
            trigramHashTable.insert(word);
        }
        DefaultTableModel trigramTableModel = (DefaultTableModel) trigramTable.getModel();
        trigramTableModel.setRowCount(0);

        List<String> trigramWords = trigramHashTable.displayAllWords();

        for (int z = 0; z < trigramWords.size(); z++) {
            String trigramWord = trigramWords.get(z);
            int TrigramCount = trigramHashTable.getCount(trigramWord);
            trigramTableModel.addRow(new Object[]{trigramWord, TrigramCount});
        }
        }
        });
    }

    private void processBigram(){
        String documentText = documentArea.getText();
        if(documentText.isEmpty()){
            JOptionPane.showMessageDialog(VocabularyListGUI.this, "No document loaded. Please click on load document.", "No document loaded Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        bigramHashTable.clear();
        String[] newWord = new String[words.length/4];
        for(int i=0;i<words.length/4;i++){
            newWord[i]=words[i];
        }
        String[] wordPairs = mergeWordsIntoPairs(newWord);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
        for (String word : wordPairs) {
            bigramHashTable.insert(word);
        }

        
        DefaultTableModel bigramTableModel = (DefaultTableModel) bigramTable.getModel();
        bigramTableModel.setRowCount(0);

        List<String> bigramWords = bigramHashTable.displayBigrams();
        for (int z = 0; z < bigramWords.size(); z++) {
            String bigramWord = bigramWords.get(z);
            int Bigramcount = bigramHashTable.getCount(bigramWord);
            bigramTableModel.addRow(new Object[]{bigramWord, Bigramcount});
        }
            }

        });
    }

    private void loadDocument() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Text Document");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String content = new String(Files.readAllBytes(selectedFile.toPath()));
                documentArea.setText(content);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading the document.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String[] mergeWordsIntoPairs(String[] words) {
        int length = words.length;
        String[] pairs = new String[length - 1];
    
        for (int i = 0; i < length - 1; i++) {
            pairs[i] = words[i] + " " + words[i + 1];
        }
    
        return pairs;
    }

    private String[] mergeWordsIntoTriWords(String[] words) {
        int length = words.length;
        String[] pairs = new String[length - 2];
    
        for (int i = 0; i < length - 2; i++) {
            pairs[i] = words[i] + " " + words[i + 1] + " " +words[i+2];
        }
    
        return pairs;
    }
    
    private void processDocument() {

        String documentText = documentArea.getText();
        if(documentText.isEmpty()){
            JOptionPane.showMessageDialog(VocabularyListGUI.this, "No document loaded. Please click on load document.", "No document loaded Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        myHashTable.clear();
        bigramHashTable.clear();
        words = documentText.split("\\s+");

        for (String word : words) {
            myHashTable.insert(word);
        }

        updateTables();

    }

    private void updateTables() {
        DefaultTableModel unigramTableModel = (DefaultTableModel) unigramTable.getModel();

        unigramTableModel.setRowCount(0);

        List<String> words = myHashTable.displayAllWords();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            int count = myHashTable.getCount(word);
            unigramTableModel.addRow(new Object[]{word, count});

        }
    }

    private void updateStatistics() {
        int totalWords = myHashTable.getTotalWords();
        int totalDifferentWords = myHashTable.getTotalDifferentWords();
        double averageListLength = myHashTable.getAverageListLength();
        double stdDevListLength = myHashTable.getStandardDeviationListLength();

        if(totalWords==0){
            JOptionPane.showMessageDialog(VocabularyListGUI.this, "No Statistics to show. Please process the document first.", "No Stats Found Error", JOptionPane.ERROR_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(VocabularyListGUI.this, "Total Words: "+totalWords+ "\nTotal Different Words: "+totalDifferentWords +
            "\nAvg List Length: "+averageListLength +"\nStd Dev List Length: "+String.format("%.2f", stdDevListLength) +"\nList Lengths: "+Arrays.toString(myHashTable.getListLengths()), "Statistics prompt", JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void sortUnigramTable() {
        if(documentArea.getText().isEmpty()){
            JOptionPane.showMessageDialog(VocabularyListGUI.this, "No document loaded. Please click on load document", "No document loaded Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DefaultTableModel unigramTableModel = (DefaultTableModel) unigramTable.getModel();
    
        int rowCount = unigramTableModel.getRowCount();
    
        if(rowCount==0){
            JOptionPane.showMessageDialog(VocabularyListGUI.this, "No data found in unigram table. Please load and then process the document", "No data in unigram table Error", JOptionPane.ERROR_MESSAGE);
        }

        List<Object[]> rows = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Object[] row = new Object[2];
            row[0] = unigramTableModel.getValueAt(i, 0);
            row[1] = unigramTableModel.getValueAt(i, 1);
            rows.add(row);
        }

        rows.sort((row1, row2) -> Integer.compare((int) row2[1], (int) row1[1]));
    
        unigramTableModel.setRowCount(0);
    
        for (Object[] row : rows) {
            unigramTableModel.addRow(row);
        }
    }

    public List<String> generateMostLikelyWordSequence(String[] inputWords, int maxWords) {
        List<String> result = new ArrayList<>();
        String[] currentWords = inputWords.clone();
        for (int i = 0; i < maxWords; i++) {
            // System.out.println(i);
            String nextWord = findNextWord(currentWords);
            
            if (nextWord == null) {
                break;
            }
            result.add(nextWord);
            currentWords = shiftArray(currentWords, nextWord);
            myHashTable.setCountZero(nextWord);
        }

        return result;
    }

    private String findNextWord(String[] currentWords) {
        double maxProbability = 0.0;
        String nextWord = null;
        
        for (String word : myHashTable.displayAllWords()) {
            double probability = calculateUnigramProbability(word);
            if (probability > maxProbability) {
                maxProbability = probability;
                nextWord = word;
            }
            
        }

        return nextWord;
    }

    private double calculateUnigramProbability(String word) {
        int countUnigram = myHashTable.getCount(word);
        int totalUnigrams = myHashTable.getTotalWords();
        return countUnigram > 0 ? (double) countUnigram / totalUnigrams : 0.0;
    }

    private String[] shiftArray(String[] array, String newItem) {
        System.arraycopy(array, 1, array, 0, array.length - 1);
        array[array.length - 1] = newItem;
        return array;
    }

    public List<String> generateMostLikelyBigramSequence(String[] inputWords, int maxWords) {
        List<String> result = new ArrayList<>();
        String[] currentWords = inputWords.clone();
    
        for (int i = 0; i < maxWords; i++) {
            System.out.println("currentWords: "+currentWords);
            String nextBigram = findNextBigram(currentWords);
            System.out.println("nextBigram: "+nextBigram);
            if (nextBigram == null) {
                break;
            }
            result.add(nextBigram);
        }
    
        return result;
    }
    
    private String findNextBigram(String[] currentWords) {
        double maxProbability = 0.0;
        String nextBigram = null;
    
        for (String bigram : bigramHashTable.displayBigrams()) {
            double probability = calculateBigramProbability(bigram);
            System.out.println("probability: "+probability);
            if (probability > maxProbability) {
                maxProbability = probability;
                nextBigram = bigram;
            }
            System.out.println("nextBigram: "+nextBigram);
            if (nextBigram == null) {
                continue;
            }else{
                bigramHashTable.setCountZero(nextBigram);   
            }
            
        }
    
        return nextBigram;
    }
    
    private double calculateBigramProbability(String bigram) {
        int countBigram = bigramHashTable.getCount(bigram);
        int totalBigrams = bigramHashTable.getTotalWords();
        return countBigram > 0 ? (double) countBigram / totalBigrams : 0.0;
    }
}

