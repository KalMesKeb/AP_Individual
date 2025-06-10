import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotepadApp extends JFrame {

    JTextArea textArea;
    JScrollPane scrollPane;
    JFileChooser fileChooser;
    File currentFile;
    JLabel statusLabel;
    boolean wordWrap = false;
    int fontSize = 16;
    String fontFamily = "Arial";

    public NotepadApp() {
        setTitle("Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setFont(new Font(fontFamily, Font.PLAIN, fontSize));
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        createMenuBar();
        createStatusBar();

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Documents (*.txt)", "txt"));

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem newWindowItem = new JMenuItem("New Window");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");
        JMenuItem printItem = new JMenuItem("Print");
        JMenuItem exitItem = new JMenuItem("Exit");

        newItem.addActionListener(e -> new NotepadApp());
        newWindowItem.addActionListener(e -> new NotepadApp());
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        saveAsItem.addActionListener(e -> saveAsFile());
        printItem.addActionListener(e -> printFile());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newItem);
        fileMenu.add(newWindowItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(printItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(e -> textArea.selectAll());

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.addSeparator();
        editMenu.add(selectAll);

        JMenu formatMenu = new JMenu("Format");
        JCheckBoxMenuItem wordWrapItem = new JCheckBoxMenuItem("Word Wrap");
        wordWrapItem.addActionListener(e -> toggleWordWrap(wordWrapItem.isSelected()));
        JMenu fontMenu = new JMenu("Font");

        JMenuItem fontSizeItem = new JMenuItem("Font Size");
        fontSizeItem.addActionListener(e -> changeFontSize());

        JMenuItem fontFamilyItem = new JMenuItem("Font Family");
        fontFamilyItem.addActionListener(e -> changeFontFamily());

        fontMenu.add(fontSizeItem);
        fontMenu.add(fontFamilyItem);
        formatMenu.add(wordWrapItem);
        formatMenu.add(fontMenu);

        JMenu viewMenu = new JMenu("View");
        JMenuItem zoomIn = new JMenuItem("Zoom In");
        JMenuItem zoomOut = new JMenuItem("Zoom Out");
        JCheckBoxMenuItem statusBar = new JCheckBoxMenuItem("Status Bar", true);

        zoomIn.addActionListener(e -> zoom(2));
        zoomOut.addActionListener(e -> zoom(-2));
        statusBar.addActionListener(e -> toggleStatusBar(statusBar.isSelected()));

        viewMenu.add(zoomIn);
        viewMenu.add(zoomOut);
        viewMenu.add(statusBar);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About Notepad");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this, "Java Notepad Clone\nBy Kaleb Mesfin", "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(about);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createStatusBar() {
        statusLabel = new JLabel("Ready");
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void toggleWordWrap(boolean enable) {
        textArea.setLineWrap(enable);
        textArea.setWrapStyleWord(enable);
    }

    private void changeFontSize() {
        String sizeStr = JOptionPane.showInputDialog(this, "Enter font size:", fontSize);
        try {
            int newSize = Integer.parseInt(sizeStr);
            fontSize = newSize;
            textArea.setFont(new Font(fontFamily, Font.PLAIN, fontSize));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid size");
        }
    }

    private void changeFontFamily() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String selected = (String) JOptionPane.showInputDialog(this, "Choose Font:", "Font Family",
                JOptionPane.PLAIN_MESSAGE, null, fonts, fontFamily);
        if (selected != null) {
            fontFamily = selected;
            textArea.setFont(new Font(fontFamily, Font.PLAIN, fontSize));
        }
    }

    private void toggleStatusBar(boolean visible) {
        statusLabel.setVisible(visible);
    }

    private void zoom(int delta) {
        fontSize += delta;
        if (fontSize < 8) fontSize = 8;
        textArea.setFont(new Font(fontFamily, Font.PLAIN, fontSize));
    }

    private void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                textArea.read(reader, null);
                reader.close();
                currentFile = file;
                setTitle(file.getName());
                statusLabel.setText("Opened: " + file.getName());
            } catch (IOException e) {
                showError("Error opening file.");
            }
        }
    }

    private void saveFile() {
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                textArea.write(writer);
                statusLabel.setText("Saved: " + currentFile.getName());
            } catch (IOException e) {
                showError("Error saving file.");
            }
        } else {
            saveAsFile();
        }
    }

    private void saveAsFile() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            saveFile();
        }
    }

    private void printFile() {
        try {
            boolean done = textArea.print();
            if (done) statusLabel.setText("Printed successfully.");
        } catch (Exception e) {
            showError("Print failed.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NotepadApp::new);
    }
}
