package com.compomics.util.gui.file_handling;

import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * A simple dialog for selecting between different files.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class FileSelectionDialog extends javax.swing.JDialog {

    /**
     * Empty default constructor
     */
    public FileSelectionDialog() {
    }

    // @TODO: make the list with strings and not files.
    // @TODO: allow editing of the list
    /**
     * A map of the parameter files indexed by their name.
     */
    private HashMap<String, File> fileMap = new HashMap<>();
    /**
     * Boolean indicating whether the user canceled the dialog.
     */
    private boolean canceled = true;

    /**
     * Creates a new FileSelection dialog.
     *
     * @param parent the parent frame
     * @param files the list of files
     * @param text the help text to display
     */
    public FileSelectionDialog(JFrame parent, ArrayList<File> files, String text) {
        super(parent, true);
        initComponents();
        setUpGui(files, text);
        setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
     * Creates a new FileSelection dialog.
     *
     * @param parent the parent dialog
     * @param files the list of files
     * @param text the help text to display
     */
    public FileSelectionDialog(JDialog parent, ArrayList<File> files, String text) {
        super(parent, true);
        initComponents();
        setUpGui(files, text);
        setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
     * Set up the GUI.
     *
     * @param files the list of files
     * @param text the help text to display
     */
    private void setUpGui(ArrayList<File> files, String text) {
        String[] fileNames = new String[files.size()];
        for (int i = 0; i < files.size(); i++) {
            fileNames[i] = files.get(i).getName();
            fileMap.put(files.get(i).getName(), files.get(i));
        }
        fileList.setListData(fileNames);
        fileList.setSelectedIndex(0);
        helpLabel.setText(text);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        helpLabel = new javax.swing.JLabel();
        fileListScrollPane = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SearchGUI Parameters");

        helpLabel.setFont(helpLabel.getFont().deriveFont((helpLabel.getFont().getStyle() | java.awt.Font.ITALIC)));
        helpLabel.setText("Please select the desired file.");

        fileList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        fileList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        fileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fileListMouseClicked(evt);
            }
        });
        fileListScrollPane.setViewportView(fileList);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(helpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(fileListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileListScrollPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton)
                    .addComponent(helpLabel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Updates the parameter selection and closes the dialog.
     *
     * @param evt
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        canceled = false;
        this.dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    /**
     * Closes the dialog without saving.
     *
     * @param evt
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        canceled = true;
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Select the file on double click and close the dialog.
     *
     * @param evt
     */
    private void fileListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileListMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2 && fileList.getSelectedValue() != null) {
            okButtonActionPerformed(null);
        }
    }//GEN-LAST:event_fileListMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JList fileList;
    private javax.swing.JScrollPane fileListScrollPane;
    private javax.swing.JLabel helpLabel;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Returns the file selected by the user.
     *
     * @return the file selected by the user
     */
    public File getSelectedFile() {
        String selectedName = (String) fileList.getSelectedValue();
        return fileMap.get(selectedName);
    }

    /**
     * Indicates whether the user canceled the dialog.
     *
     * @return a boolean indicating whether the canceled the dialog
     */
    public boolean isCanceled() {
        return canceled;
    }
}