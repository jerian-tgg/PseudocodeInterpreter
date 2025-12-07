/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package app.UI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.text.StyledDocument;
import javax.swing.SwingUtilities;
import pseudocode.errors.LexerException;
import pseudocode.errors.ParserException;
import pseudocode.errors.RuntimeError;
import pseudocode.interpreter.Interpreter;
import pseudocode.lexer.Lexer;
import pseudocode.lexer.Token;
import pseudocode.parser.Parser;
import pseudocode.parser.ProgramNode;

/**
 *
 * @author ispaycy
 */
public class GUI extends javax.swing.JFrame {
    private FileOperations fileOps;
    private boolean isModified = false;
    private SyntaxHighlighter syntaxHighlighter;
    private javax.swing.Timer highlightTimer;
    private javax.swing.JTextPane textPane; // The actual text pane for syntax highlighting
    /**
     * Creates new form GUI
     * @throws java.io.IOException
     */
    public GUI() throws IOException {
        initComponents();
        setTitle("Jernat Pseudocode Interpreter");
        setIconImage(ImageIO.read(new File("icon.png")));
        
        // Replace JEditorPane with JTextPane (for syntax highlighting support)
        // The form file creates JEditorPane, but we need JTextPane for styled text
        textPane = new javax.swing.JTextPane();
        textPane.setBackground(new java.awt.Color(34, 34, 34));
        textPane.setForeground(new java.awt.Color(204, 204, 204));
        if (jEditorPane1 != null) {
            textPane.setFont(jEditorPane1.getFont());
            textPane.setText(jEditorPane1.getText());
        } else {
            // Set default font if jEditorPane1 wasn't initialized yet
            textPane.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 12));
        }
        jScrollPane1.setViewportView(textPane);
        
        fileOps = new FileOperations();
    
        // Initialize syntax highlighter
        StyledDocument styledDoc = textPane.getStyledDocument();
        syntaxHighlighter = new SyntaxHighlighter(styledDoc);
        
        // Initial highlighting
        SwingUtilities.invokeLater(() -> {
            syntaxHighlighter.highlightDocument();
        });
    
        // Create a timer to debounce highlighting (wait 150ms after user stops typing)
        highlightTimer = new javax.swing.Timer(150, (e) -> {
            try {
                String text = textPane.getText();
                syntaxHighlighter.highlightText(text, 0);
            } catch (Exception ex) {
                // Ignore highlighting errors
            }
        });
        highlightTimer.setRepeats(false); // Only fire once
    
    // Track editor changes for modified status and syntax highlighting
    textPane.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            markAsModified();
            // Debounce syntax highlighting - restart timer on each keystroke
            highlightTimer.restart();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            markAsModified();
            // Debounce syntax highlighting - restart timer on each deletion
            highlightTimer.restart();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            markAsModified();
        }
        });
    }
    
    private void markAsModified() {
        if (!isModified) {
            isModified = true;
            updateWindowTitle();
        }
    }

    private void markAsSaved() {
        isModified = false;
        updateWindowTitle();
    }

    private void updateWindowTitle() {
        String title = "Jemat Pseudocode Interpreter";
        File currentFile = fileOps.getCurrentFile();
        if (currentFile != null) {
            title += " - " + currentFile.getName();
        } else {
            title += " - Untitled";
        }
        if (isModified) {
            title += " *";
        }
        setTitle(title);
    }

    private boolean checkUnsavedChanges() {
        if (isModified) {
            int result = JOptionPane.showConfirmDialog(
                this,
                "You have unsaved changes. Do you want to save before continuing?",
                "Unsaved Changes",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                return saveCurrentFile();
            } else if (result == JOptionPane.NO_OPTION) {
                return true;
            } else {
                return false; // Cancel
            }
        }
        return true;
    }

    private boolean saveCurrentFile() {
        String content = textPane.getText();
        if (fileOps.saveFile(this, content)) {
            markAsSaved();
            JOptionPane.showMessageDialog(this, "File saved successfully!");
            return true;
        }
        return false;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        BG = new javax.swing.JPanel();
        Tool = new javax.swing.JToolBar();
        New = new javax.swing.JButton();
        Open = new javax.swing.JButton();
        Save = new javax.swing.JButton();
        SaveAs = new javax.swing.JButton();
        Run = new javax.swing.JButton();
        CleanTerm = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        Output = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        Editor = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        Menu = new javax.swing.JMenuBar();
        File = new javax.swing.JMenu();
        NewFileCont = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        OpenFileCont = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        SaveFileCont = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        SaveAsFileCont = new javax.swing.JMenuItem();
        Help = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        About = new javax.swing.JMenu();

        jButton2.setText("newf");
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setPreferredSize(new java.awt.Dimension(35, 35));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 51));
        setPreferredSize(new java.awt.Dimension(1280, 768));

        BG.setLayout(new java.awt.BorderLayout());

        Tool.setBackground(new java.awt.Color(51, 51, 51));
        Tool.setBorder(null);
        Tool.setOrientation(javax.swing.SwingConstants.VERTICAL);
        Tool.setRollover(true);
        Tool.setName(""); // NOI18N
        Tool.setPreferredSize(new java.awt.Dimension(75, 75));
        Tool.setRequestFocusEnabled(false);

        New.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/newF.png"))); // NOI18N
        New.setToolTipText("New File");
        New.setFocusable(false);
        New.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        New.setPreferredSize(new java.awt.Dimension(75, 75));
        New.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewActionPerformed(evt);
            }
        });
        Tool.add(New);

        Open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/open.png"))); // NOI18N
        Open.setToolTipText("Open File");
        Open.setFocusable(false);
        Open.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Open.setPreferredSize(new java.awt.Dimension(75, 75));
        Open.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed(evt);
            }
        });
        Tool.add(Open);

        Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        Save.setToolTipText("Save File");
        Save.setFocusable(false);
        Save.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Save.setPreferredSize(new java.awt.Dimension(75, 75));
        Save.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveActionPerformed(evt);
            }
        });
        Tool.add(Save);

        SaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/saveAs.png"))); // NOI18N
        SaveAs.setToolTipText("Save File As");
        SaveAs.setFocusable(false);
        SaveAs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        SaveAs.setPreferredSize(new java.awt.Dimension(75, 75));
        SaveAs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        SaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveAsActionPerformed(evt);
            }
        });
        Tool.add(SaveAs);

        Run.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/run.png"))); // NOI18N
        Run.setToolTipText("Run File");
        Run.setFocusable(false);
        Run.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Run.setPreferredSize(new java.awt.Dimension(75, 75));
        Run.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunActionPerformed(evt);
            }
        });
        Tool.add(Run);

        CleanTerm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/clean.png"))); // NOI18N
        CleanTerm.setToolTipText("Clean Output");
        CleanTerm.setFocusable(false);
        CleanTerm.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CleanTerm.setPreferredSize(new java.awt.Dimension(75, 75));
        CleanTerm.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        CleanTerm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CleanTermActionPerformed(evt);
            }
        });
        Tool.add(CleanTerm);

        BG.add(Tool, java.awt.BorderLayout.WEST);

        jPanel4.setBackground(new java.awt.Color(204, 255, 204));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setPreferredSize(new java.awt.Dimension(100, 678));
        jPanel5.setLayout(new java.awt.BorderLayout());

        Output.setBackground(new java.awt.Color(34, 34, 34));
        Output.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        Output.setPreferredSize(new java.awt.Dimension(1366, 200));
        Output.setRequestFocusEnabled(false);
        Output.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jTextArea1.setBackground(new java.awt.Color(34, 34, 34));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(204, 204, 204));
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        Output.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel5.add(Output, java.awt.BorderLayout.SOUTH);

        Editor.setBackground(new java.awt.Color(34, 34, 34));
        Editor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        Editor.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(34, 34, 34));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jEditorPane1.setBackground(new java.awt.Color(34, 34, 34));
        jEditorPane1.setForeground(new java.awt.Color(204, 204, 204));
        jScrollPane1.setViewportView(jEditorPane1);

        Editor.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.add(Editor, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        BG.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(BG, java.awt.BorderLayout.CENTER);

        Menu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        Menu.setForeground(new java.awt.Color(51, 51, 51));
        Menu.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        Menu.setMinimumSize(new java.awt.Dimension(160, 30));
        Menu.setPreferredSize(new java.awt.Dimension(160, 40));

        File.setForeground(new java.awt.Color(51, 51, 51));
        File.setText("File");

        NewFileCont.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        NewFileCont.setText("New File");
        File.add(NewFileCont);
        File.add(jSeparator8);

        OpenFileCont.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        OpenFileCont.setText("Open File");
        File.add(OpenFileCont);
        File.add(jSeparator7);

        SaveFileCont.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        SaveFileCont.setText("Save File");
        File.add(SaveFileCont);
        File.add(jSeparator6);

        SaveAsFileCont.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        SaveAsFileCont.setText("Safe File As");
        File.add(SaveAsFileCont);

        Menu.add(File);

        Help.setForeground(new java.awt.Color(51, 51, 51));
        Help.setText("Help");
        Help.add(jSeparator1);

        Menu.add(Help);

        About.setForeground(new java.awt.Color(51, 51, 51));
        About.setText("About");
        Menu.add(About);

        setJMenuBar(Menu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveAsActionPerformed
        // TODO add your handling code here:
        String content = textPane.getText();
    if (fileOps.saveFileAs(this, content)) {
        markAsSaved();
        JOptionPane.showMessageDialog(this, "File saved successfully!");
    }
        
    }//GEN-LAST:event_SaveAsActionPerformed

    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenActionPerformed
        // TODO add your handling code here:
        if (!checkUnsavedChanges()) {
        return;
    }
    
    File file = fileOps.openFile(this);
    if (file != null) {
        try {
            String content = fileOps.readFile(file);
            textPane.setText(content);
            markAsSaved();
            // Re-highlight after loading file
            SwingUtilities.invokeLater(() -> {
                syntaxHighlighter.highlightDocument();
            });
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error reading file: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_OpenActionPerformed

    private void NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewActionPerformed
        // TODO add your handling code here:
        if (!checkUnsavedChanges()) {
        return;
    }
    
    File newFile = fileOps.createNewFile(this);
    if (newFile != null) {
        textPane.setText("");
        markAsSaved();
        // Re-highlight after clearing
        SwingUtilities.invokeLater(() -> {
            syntaxHighlighter.highlightDocument();
        });
        JOptionPane.showMessageDialog(this, "New file created: " + newFile.getName());
    }
    }//GEN-LAST:event_NewActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void RunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunActionPerformed
        // Execute pseudocode from the editor and show output in the console area
        String program = textPane.getText();
        jTextArea1.setText("");

        if (program == null || program.trim().isEmpty()) {
            jTextArea1.setText("No pseudocode to run.");
            return;
        }

        // Capture output written via System.out (used by BuiltInFunctions.builtinPrint)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true);
        PrintStream oldOut = System.out;
        System.setOut(ps);

        try {
            Lexer lexer = new Lexer(program);
            List<Token> tokens = lexer.tokenize();

            Parser parser = new Parser(tokens);
            ProgramNode p = parser.parseProgram();

            Interpreter interpreter = new Interpreter();
            interpreter.runProgram(p);

            String output = baos.toString();
            if (output.isEmpty()) {
                jTextArea1.setText("(Program finished with no output.)");
            } else {
                jTextArea1.setText(output);
            }
        } catch (LexerException | ParserException | RuntimeError e) {
            jTextArea1.setText("Error: " + e.getMessage());
        } catch (Exception e) {
            jTextArea1.setText("Unexpected error: " + e.getMessage());
        } finally {
            System.setOut(oldOut);
            try {
                ps.close();
                baos.close();
            } catch (Exception ex) {
                // ignore
            }
        }
    }//GEN-LAST:event_RunActionPerformed

    private void CleanTermActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CleanTermActionPerformed
        // Clear the output console
        jTextArea1.setText("");
    }//GEN-LAST:event_CleanTermActionPerformed

    private void SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveActionPerformed
        // TODO add your handling code here:
        saveCurrentFile();
    }//GEN-LAST:event_SaveActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new GUI().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu About;
    private javax.swing.JPanel BG;
    private javax.swing.JButton CleanTerm;
    private javax.swing.JPanel Editor;
    private javax.swing.JMenu File;
    private javax.swing.JMenu Help;
    private javax.swing.JMenuBar Menu;
    private javax.swing.JButton New;
    private javax.swing.JMenuItem NewFileCont;
    private javax.swing.JButton Open;
    private javax.swing.JMenuItem OpenFileCont;
    private javax.swing.JPanel Output;
    private javax.swing.JButton Run;
    private javax.swing.JButton Save;
    private javax.swing.JButton SaveAs;
    private javax.swing.JMenuItem SaveAsFileCont;
    private javax.swing.JMenuItem SaveFileCont;
    private javax.swing.JToolBar Tool;
    private javax.swing.JButton jButton2;
    private javax.swing.text.JTextComponent jEditorPane1; // JTextPane for syntax highlighting (replaced in constructor)
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
