/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.UI;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.File;
/**
 *
 * @author ispaycy
 */
public class FileTypeManager {
    public static final String FILE_EXTENSION = ".jn";
    public static final String FILE_DESCRIPTION = "Jemat Pseudocode Files (*.jn)";
    
    private static Icon jnIcon = null;
    
    static {
        // Load the icon once
        try {
            jnIcon = new ImageIcon(FileTypeManager.class.getResource("/img/jn_icon.png"));
        } catch (Exception e) {
            System.err.println("JN icon not found: /img/jn_icon.png");
        }
    }
    
    public static JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        
        // Set file filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            FILE_DESCRIPTION, 
            "jn"
        );
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
        
        // Set custom file view
        fileChooser.setFileView(new JnFileView());
        
        return fileChooser;
    }
    
    public static String ensureExtension(String fileName) {
        if (!fileName.toLowerCase().endsWith(FILE_EXTENSION)) {
            return fileName + FILE_EXTENSION;
        }
        return fileName;
    }
    
    public static boolean hasJnExtension(File file) {
        return file.getName().toLowerCase().endsWith(FILE_EXTENSION);
    }
    
    private static class JnFileView extends FileView {
        @Override
        public Icon getIcon(File f) {
            if (f != null && hasJnExtension(f) && jnIcon != null) {
                return jnIcon;
            }
            return super.getIcon(f);
        }
        
        @Override
        public String getTypeDescription(File f) {
            if (hasJnExtension(f)) {
                return "Jemat Pseudocode File";
            }
            return super.getTypeDescription(f);
        }
        
        @Override
        public String getDescription(File f) {
            if (hasJnExtension(f)) {
                return "Jemat pseudocode script";
            }
            return super.getDescription(f);
        }
    }
}
