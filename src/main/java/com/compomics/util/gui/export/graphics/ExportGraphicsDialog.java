package com.compomics.util.gui.export.graphics;

import com.compomics.util.Export;
import com.compomics.util.enumeration.ImageType;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import com.compomics.util.io.export.ExportWriter;
import com.compomics.util.preferences.LastSelectedFolder;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.apache.batik.transcoder.TranscoderException;
import org.jfree.chart.ChartPanel;

/**
 * Simple dialog for selecting the image type to export a graphics element to.
 *
 * @author Harald Barsnes
 */
public class ExportGraphicsDialog extends javax.swing.JDialog {

    /**
     * The chart panel to export.
     */
    private ChartPanel chartPanel;
    /**
     * The panel to export.
     */
    private Component graphicsPanel;
    /**
     * The progress dialog.
     */
    private ProgressDialogX progressDialog;
    /**
     * The parent frame.
     */
    private JFrame frame;
    /**
     * The normal icon.
     */
    private Image normalIcon;
    /**
     * The waiting icon.
     */
    private Image waitingIcon;
    /**
     * The last selected folder.
     */
    private LastSelectedFolder lastSelectedFolder = new LastSelectedFolder();

    /**
     * Create and open a new ExportGraphicsDialog.
     *
     * @param frame the parent frame
     * @param normalIcon the normal icon
     * @param waitingIcon the waiting icon
     * @param modal if the dialog is to be modal
     * @param graphicsPanel the graphics panel to export
     * @param lastSelectedFolder the last selected folder
     */
    public ExportGraphicsDialog(JFrame frame, Image normalIcon, Image waitingIcon, boolean modal, Component graphicsPanel, LastSelectedFolder lastSelectedFolder) {
        super(frame, modal);
        this.frame = frame;
        this.graphicsPanel = graphicsPanel;
        this.normalIcon = normalIcon;
        this.waitingIcon = waitingIcon;
        initComponents();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    /**
     * Create and open a new ExportPlot dialog.
     *
     * @param frame the parent frame
     * @param normalIcon the normal icon
     * @param waitingIcon the waiting icon
     * @param modal if the dialog is to be modal
     * @param chartPanel the chart panel to export
     * @param lastSelectedFolder the last selected folder
     */
    public ExportGraphicsDialog(JFrame frame, Image normalIcon, Image waitingIcon, boolean modal, ChartPanel chartPanel, LastSelectedFolder lastSelectedFolder) {
        super(frame, modal);
        this.frame = frame;
        this.chartPanel = chartPanel;
        this.normalIcon = normalIcon;
        this.waitingIcon = waitingIcon;
        initComponents();
        setLocationRelativeTo(frame);
        setVisible(true);
    }
    
    /**
     * Create and open a new ExportGraphicsDialog.
     *
     * @param dialog the parent dialog (has to have a parent JFrame)
     * @param normalIcon the normal icon
     * @param waitingIcon the waiting icon
     * @param modal if the dialog is to be modal
     * @param graphicsPanel the graphics panel to export
     * @param lastSelectedFolder the last selected folder
     */
    public ExportGraphicsDialog(JDialog dialog, Image normalIcon, Image waitingIcon, boolean modal, Component graphicsPanel, LastSelectedFolder lastSelectedFolder) {
        super(dialog, modal);
        this.frame = (JFrame) dialog.getParent();
        this.graphicsPanel = graphicsPanel;
        this.normalIcon = normalIcon;
        this.waitingIcon = waitingIcon;
        initComponents();
        setLocationRelativeTo(dialog);
        setVisible(true);
    }

    /**
     * Create and open a new ExportPlot dialog.
     *
     * @param dialog the parent dialog (has to have a parent JFrame)
     * @param normalIcon the normal icon
     * @param waitingIcon the waiting icon
     * @param modal if the dialog is to be modal
     * @param chartPanel the chart panel to export
     * @param lastSelectedFolder the last selected folder
     */
    public ExportGraphicsDialog(JDialog dialog, Image normalIcon, Image waitingIcon, boolean modal, ChartPanel chartPanel, LastSelectedFolder lastSelectedFolder) {
        super(dialog, modal);
        this.frame = (JFrame) dialog.getParent();
        this.chartPanel = chartPanel;
        this.normalIcon = normalIcon;
        this.waitingIcon = waitingIcon;
        initComponents();
        setLocationRelativeTo(dialog);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        backgroundPanel = new javax.swing.JPanel();
        plottingTypeJPanel = new javax.swing.JPanel();
        pngJRadioButton = new javax.swing.JRadioButton();
        tiffJRadioButton = new javax.swing.JRadioButton();
        pdfJRadioButton = new javax.swing.JRadioButton();
        svgJRadioButton = new javax.swing.JRadioButton();
        exportJButton = new javax.swing.JButton();
        cancelJButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Export Plot");
        setResizable(false);

        backgroundPanel.setBackground(new java.awt.Color(230, 230, 230));

        plottingTypeJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Plot Type"));
        plottingTypeJPanel.setOpaque(false);

        buttonGroup.add(pngJRadioButton);
        pngJRadioButton.setSelected(true);
        pngJRadioButton.setText("PNG (Portable Network Graphics)");
        pngJRadioButton.setIconTextGap(15);
        pngJRadioButton.setOpaque(false);

        buttonGroup.add(tiffJRadioButton);
        tiffJRadioButton.setText("TIFF (Tagged Image File Format)");
        tiffJRadioButton.setIconTextGap(15);
        tiffJRadioButton.setOpaque(false);

        buttonGroup.add(pdfJRadioButton);
        pdfJRadioButton.setText("PDF (Portable Document Format)");
        pdfJRadioButton.setIconTextGap(15);
        pdfJRadioButton.setOpaque(false);

        buttonGroup.add(svgJRadioButton);
        svgJRadioButton.setText("SVG (Scalable Vector Graphics)");
        svgJRadioButton.setIconTextGap(15);
        svgJRadioButton.setOpaque(false);

        javax.swing.GroupLayout plottingTypeJPanelLayout = new javax.swing.GroupLayout(plottingTypeJPanel);
        plottingTypeJPanel.setLayout(plottingTypeJPanelLayout);
        plottingTypeJPanelLayout.setHorizontalGroup(
            plottingTypeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plottingTypeJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(plottingTypeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(svgJRadioButton)
                    .addComponent(pdfJRadioButton)
                    .addComponent(tiffJRadioButton)
                    .addComponent(pngJRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        plottingTypeJPanelLayout.setVerticalGroup(
            plottingTypeJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plottingTypeJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pngJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tiffJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pdfJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(svgJRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        exportJButton.setText("Export");
        exportJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportJButtonActionPerformed(evt);
            }
        });

        cancelJButton.setText("Cancel");
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(backgroundPanelLayout.createSequentialGroup()
                        .addComponent(plottingTypeJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(exportJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton)))
                .addContainerGap())
        );

        backgroundPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelJButton, exportJButton});

        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(plottingTypeJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelJButton)
                    .addComponent(exportJButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Opens a file chooser where the user can select the file type to export
     * to. Then tries to export the plot to the selected format.
     *
     * @param evt
     */
    private void exportJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportJButtonActionPerformed

        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        this.setVisible(false);

        final JFileChooser chooser = new JFileChooser(getLastSelectedFolder());

        // add the correct file filter based on the format selected
        addFileFilter(chooser);

        int selection = chooser.showSaveDialog(this);

        if (selection == JFileChooser.APPROVE_OPTION) {
            String selectedFile = chooser.getSelectedFile().getAbsolutePath();
            lastSelectedFolder.setLastSelectedFolder(ExportWriter.lastFolderKey, selectedFile);
            savePanel(selectedFile, true);
        }

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_exportJButtonActionPerformed

    /**
     * Close the dialog.
     *
     * @param evt
     */
    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_cancelJButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton cancelJButton;
    private javax.swing.JButton exportJButton;
    private javax.swing.JRadioButton pdfJRadioButton;
    private javax.swing.JPanel plottingTypeJPanel;
    private javax.swing.JRadioButton pngJRadioButton;
    private javax.swing.JRadioButton svgJRadioButton;
    private javax.swing.JRadioButton tiffJRadioButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Exports the panel to the selected figure format.
     *
     * @param chartPanel the panel to export
     * @param selectedFile the file to export the chart to
     * @param showSavedMessage if true, a message will be when the export is
     * complete
     */
    private void savePanel(String selectedFile, boolean aShowSavedMessage) {

        final boolean showSavedMessage = aShowSavedMessage;

        if (pngJRadioButton.isSelected()) {
            if (!selectedFile.endsWith(ImageType.PNG.getExtension())) {
                selectedFile += ImageType.PNG.getExtension();
            }
        } else if (tiffJRadioButton.isSelected()) {
            if (!selectedFile.endsWith(ImageType.TIFF.getExtension())) {
                selectedFile += ImageType.TIFF.getExtension();
            }
        } else if (pdfJRadioButton.isSelected()) {
            if (!selectedFile.endsWith(ImageType.PDF.getExtension())) {
                selectedFile += ImageType.PDF.getExtension();
            }
        } else if (svgJRadioButton.isSelected()) {
            if (!selectedFile.endsWith(ImageType.SVG.getExtension())) {
                selectedFile += ImageType.SVG.getExtension();
            }
        }

        boolean saveFile = true;

        if (new File(selectedFile).exists()) {
            int option = JOptionPane.showConfirmDialog(this,
                    "The file " + selectedFile + " already exists. Overwrite?",
                    "Overwrite?", JOptionPane.YES_NO_CANCEL_OPTION);

            if (option != JOptionPane.YES_OPTION) {
                saveFile = false;
            }
        }

        if (saveFile) {

            frame.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            lastSelectedFolder.setLastSelectedFolder(ExportWriter.lastFolderKey, selectedFile);

            final String finalSelectedFile = selectedFile;
            final ExportGraphicsDialog tempRef = this;

            progressDialog = new ProgressDialogX(frame,
                    normalIcon,
                    waitingIcon,
                    true);
            progressDialog.setPrimaryProgressCounterIndeterminate(true);
            progressDialog.setTitle("Saving Figure. Please Wait...");

            new Thread(new Runnable() {

                public void run() {
                    progressDialog.setVisible(true);
                }
            }, "ProgressDialog").start();

            new Thread("SaveFigureThread") {

                @Override
                public void run() {
                    try {

                        ImageType currentImageType;

                        if (pngJRadioButton.isSelected()) {
                            currentImageType = ImageType.PNG;
                        } else if (tiffJRadioButton.isSelected()) {
                            currentImageType = ImageType.TIFF;
                        } else if (pdfJRadioButton.isSelected()) {
                            currentImageType = ImageType.PDF;
                        } else { // svg selected
                            currentImageType = ImageType.SVG;
                        }

                        if (chartPanel != null) {
                            Export.exportChart(chartPanel.getChart(), chartPanel.getBounds(), new File(finalSelectedFile), currentImageType);
                        } else {
                            Export.exportComponent(graphicsPanel, graphicsPanel.getBounds(), new File(finalSelectedFile), currentImageType);
                        }

                        progressDialog.setRunFinished();
                        frame.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                        if (showSavedMessage) {
                            JOptionPane.showMessageDialog(frame, "Plot saved to " + finalSelectedFile, "Plot Saved", JOptionPane.INFORMATION_MESSAGE);
                            tempRef.dispose();
                        }
                    } catch (IOException e) {
                        progressDialog.setRunFinished();
                        tempRef.setVisible(false);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(tempRef, "Unable to export plot: " + e.getMessage(), "Error Exporting Plot", JOptionPane.INFORMATION_MESSAGE);
                        tempRef.dispose();
                    } catch (TranscoderException e) {
                        progressDialog.setRunFinished();
                        tempRef.setVisible(false);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(tempRef, "Unable to export plot: " + e.getMessage(), "Error Exporting Plot", JOptionPane.INFORMATION_MESSAGE);
                        tempRef.dispose();
                    }
                }
            }.start();
        }
    }

    /**
     * Set the correct file filter based on the format selected.
     *
     * @param chooser the file chooser to set the filter for
     */
    private void addFileFilter(JFileChooser chooser) {

        FileFilter filter = null;

        if (pngJRadioButton.isSelected()) {
            filter = new FileFilter() {

                @Override
                public boolean accept(File myFile) {
                    return myFile.getName().toLowerCase().endsWith("png")
                            || myFile.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "PNG (Portable Network Graphics) (.png)";
                }
            };
        } else if (tiffJRadioButton.isSelected()) {
            filter = new FileFilter() {

                @Override
                public boolean accept(File myFile) {
                    return myFile.getName().toLowerCase().endsWith("tif")
                            || myFile.getName().toLowerCase().endsWith("tiff")
                            || myFile.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "TIFF (Tagged Image File Format) (.tif)";
                }
            };
        } else if (pdfJRadioButton.isSelected()) {
            filter = new FileFilter() {

                @Override
                public boolean accept(File myFile) {
                    return myFile.getName().toLowerCase().endsWith("pdf")
                            || myFile.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "PDF (Portable Document Format) (.pdf)";
                }
            };
        } else if (svgJRadioButton.isSelected()) {
            filter = new FileFilter() {

                @Override
                public boolean accept(File myFile) {
                    return myFile.getName().toLowerCase().endsWith("svg")
                            || myFile.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "SVG (Scalable Vector Graphics) (.svg)";
                }
            };
        }

        chooser.setFileFilter(filter);
    }
    
    /**
     * Returns the last selected folder to use.
     * 
     * @return the last selected folder
     */
    private String getLastSelectedFolder() {
        String result = lastSelectedFolder.getLastSelectedFolder(ExportWriter.lastFolderKey);
        if (result == null) {
            result = lastSelectedFolder.getLastSelectedFolder();
        }
        return result;
    }
}
