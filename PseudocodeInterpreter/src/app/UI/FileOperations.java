/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.UI;
import javax.swing.*;
import java.io.*;
import java.nio.file.*;

/**
 *
 * @author ispaycy
 */
public class FileOperations {
    private File currentFile;
    private JFileChooser fileChooser;
    
    public FileOperations() {
        fileChooser = FileTypeManager.createFileChooser();
    }
    
    public boolean isValidFileName(String name) {
        // Allow only: a-z, A-Z, 0-9, "-", "_"
        return name.matches("^[a-zA-Z0-9\\-_]+$");
    }
    
    public String sanitizeFileName(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\-_]", "_");
    }
    
    public File createNewFile(JFrame parent) {
        // Ask for file name
        String fileName = JOptionPane.showInputDialog(
            parent,
            "Enter file name (only a-z, A-Z, 0-9, -, _ allowed):",
            "New File",
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }
        
        // Validate and sanitize
        if (!isValidFileName(fileName)) {
            String sanitized = sanitizeFileName(fileName);
            int result = JOptionPane.showConfirmDialog(
                parent,
                "Invalid file name. Use '" + sanitized + "' instead?",
                "Invalid File Name",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                fileName = sanitized;
            } else {
                return null;
            }
        }
        
        // Ask for location
        fileChooser.setDialogTitle("Select folder for new file");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            File newFile = new File(selectedFolder, 
                FileTypeManager.ensureExtension(fileName));
            
            // Check if file exists
            if (newFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(
                    parent,
                    "File already exists. Overwrite?",
                    "File Exists",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (overwrite != JOptionPane.YES_OPTION) {
                    return null;
                }
            }
            
            try {
                Files.write(newFile.toPath(), "".getBytes());
                currentFile = newFile;
                return newFile;
            } catch (IOException e) {
                showError(parent, "Error creating file: " + e.getMessage());
                return null;
            }
        }
        
        // Reset to default
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return null;
    }
    
    public File openFile(JFrame parent) {
        fileChooser.setDialogTitle("Open Jemat File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            return currentFile;
        }
        return null;
    }
    
    public boolean saveFile(JFrame parent, String content) {
        if (currentFile == null) {
            return saveFileAs(parent, content);
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            showError(parent, "Error saving file: " + e.getMessage());
            return false;
        }
    }
    
    public boolean saveFileAs(JFrame parent, String content) {
        fileChooser.setDialogTitle("Save As");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        if (currentFile != null) {
            fileChooser.setSelectedFile(currentFile);
        }
        
        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            file = new File(FileTypeManager.ensureExtension(file.getAbsolutePath()));
            
            // Check for overwrite
            if (file.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(
                    parent,
                    "File already exists. Overwrite?",
                    "File Exists",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (overwrite != JOptionPane.YES_OPTION) {
                    return false;
                }
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
                currentFile = file;
                return true;
            } catch (IOException e) {
                showError(parent, "Error saving file: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
    
    public String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    public File getCurrentFile() {
        return currentFile;
    }
    
    public void setCurrentFile(File file) {
        this.currentFile = file;
    }
    
    private void showError(JFrame parent, String message) {
        JOptionPane.showMessageDialog(
            parent, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }
}
