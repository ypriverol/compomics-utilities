package com.compomics.util.protein_sequences_manager.gui.sequences_import.taxonomy;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * Taxonomy tree panel.
 *
 * @author Kenneth Verheggen
 */
public class TaxonomyTreePanel extends javax.swing.JPanel {

    /**
     * The query.
     */
    private final UniprotTaxonomyProvider query;

    /**
     * Constructor.
     */
    public TaxonomyTreePanel() {
        initComponents();
        query = new UniprotTaxonomyProvider((DefaultTreeModel) taxonomyTree.getModel());
        taxonomyTree.setExpandsSelectedPaths(true);
    }

    /**
     * Build the tree from a tab file.
     *
     * @param tabFile a taxonomy TabFile (as downloaded from UniProt)
     * @throws IOException if an IOException is thrown
     * @throws InterruptedException if an InterruptedException is thrown
     */
    public void buildTreeFromTabFile(File tabFile) throws IOException, InterruptedException {
        //  collapseAll();
        taxonomyTree.setModel(query.getModelFromFile(tabFile));
        expandAll();
    }

    /**
     * Build the ree after search.
     *
     * @param taxonomyName the taxonomy term (ID or name)
     * @throws IOException if an IOException is thrown
     * @throws InterruptedException if an InterruptedException is thrown
     * @throws MalformedURLException if a MalformedURLException is thrown
     * @throws URISyntaxException if a URISyntaxException is thrown
     */
    public void buildTreeAfterSearch(String taxonomyName) throws IOException, InterruptedException, MalformedURLException, URISyntaxException {
        //  collapseAll();
        String taxonomyID = query.getCachedTaxonomyID(taxonomyName);
        if (taxonomyID == null) {
            taxonomyID = taxonomyName;
        }
        try {//make this a swingworker?
            DefaultTreeModel modelAfterSearch = query.getModelAfterSearch(taxonomyID);
            taxonomyTree.setModel(modelAfterSearch);
            expandAll();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Query overflow",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Build tree after click.
     * 
     * @param taxonomyName the taxonomy term (ID or name)
     * @throws IOException if an IOException is thrown
     */
    public void buildTreeAfterClick(String taxonomyName) throws IOException {
        String taxonomyID = (taxonomyName);
        if (taxonomyID == null) {
            taxonomyID = taxonomyName;
        }
        TreePath[] selectionPaths = taxonomyTree.getSelectionPaths();
        taxonomyTree.setModel(query.getModelAfterClick(taxonomyID));
        taxonomyTree.setSelectionPaths(selectionPaths);
    }

    /**
     * Returns the selected taxonomy map.
     * 
     * @return the selected taxonomy map
     */
    public HashMap<String, String> getSelectedTaxonomyMap() {
        HashMap<String, String> taxIDMap = new HashMap<String, String>();
        if (taxonomyTree.getSelectionPaths().length > 0) {
            for (TreePath path : taxonomyTree.getSelectionPaths()) {
                String taxonomyName = ((DefaultMutableTreeNode) path.getLastPathComponent()).toString();
                taxIDMap.put(query.getCachedTaxonomyID(taxonomyName), taxonomyName);
            }
        }
        return taxIDMap;
    }

    /**
     * Returns the taxonomy tree.
     * 
     * @return the taxonomy tree
     */
    public JTree getTaxonomyTree() {
        return taxonomyTree;
    }

    /**
     * Expands all found nodes in the taxonomy tree.
     */
    public void expandAll() {
        for (int i = 0; i < taxonomyTree.getRowCount(); i++) {
            taxonomyTree.expandRow(i);
        }
    }

    /**
     * Collapses all found nodes in the taxonomy tree.
     */
    public void collapseAll() {
        for (int i = 0; i < taxonomyTree.getRowCount(); i++) {
            taxonomyTree.collapseRow(i);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        taxonomyTreeScrollPane = new javax.swing.JScrollPane();
        taxonomyTree = new javax.swing.JTree();
        tfSearchField = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        taxonomyTreeScrollPane.setBorder(null);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Eukaryota");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Alveolata");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Amoebozoa");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Apusozoa");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Breviatea");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Centroheliozoa");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Cryptophyta");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("environmental samples");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Euglenozoa");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Fornicata");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Glaucocystophyceae");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Haptophyceae");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Heterolobosea");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Jakobida");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Katablepharidophyta");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Malawimonadidae");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Opisthokonta");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Oxymonadida");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Parabasalia");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Rhizaria");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Rhodophyta");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Stramenopiles");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("unclassified eukaryotes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Viridiplantae");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Bacteria");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Actinobacteria");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Aquificae");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Armatimonadetes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Bacteroidetes/Chlorobi group");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Caldiserica");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Chlamydiae/Verrucomicrobia group");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Chloroflexi");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Chrysiogenetes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Cyanobacteria");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Deferribacteres");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Deinococcus-Thermus");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Dictyoglomi");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Elusimicrobia");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("environmental samples");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Fibrobacteres/Acidobacteria group");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Firmicutes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Fusobacteria");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Gemmatimonadetes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Nitrospinae");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Nitrospirae");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Planctomycetes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Proteobacteria");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Spirochaetes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Synergistetes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Tenericutes");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Thermodesulfobacteria");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Thermotogae");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("unclassified Bacteria");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Archaea");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Aenigmarchaeota");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Crenarchaeota");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Diapherotrites");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("environmental samples");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Euryarchaeota");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Geoarchaeota");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Korarchaeota");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Nanoarchaeota");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Nanohaloarchaeota");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Parvarchaeota");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Thaumarchaeota");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("unclassified Archaea");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Viruses");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Deltavirus");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("dsDNA viruses, no RNA stage");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("dsRNA viruses");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("environmental samples");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Retro-transcribing viruses");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Satellites");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ssDNA viruses");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ssRNA viruses");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("unassigned viruses");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("unclassified archaeal viruses");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("unclassified phages");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("unclassified virophages");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("unclassified viruses");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        taxonomyTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        taxonomyTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                taxonomyTreeMouseClicked(evt);
            }
        });
        taxonomyTreeScrollPane.setViewportView(taxonomyTree);

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(taxonomyTreeScrollPane)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tfSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addContainerGap(191, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(taxonomyTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Search UniProt.
     * 
     * @param evt 
     */
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        try {
            buildTreeAfterSearch(tfSearchField.getText().replace(" ", "%20"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Connection to UniProt failed " + System.lineSeparator() + ex,
                    "Connection error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException ex) {
            ex.printStackTrace(); // @TODO: better error handling
        } catch (URISyntaxException ex) {
            ex.printStackTrace(); // @TODO: better error handling
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    /**
     * Taxonomy selection.
     * 
     * @param evt 
     */
    private void taxonomyTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taxonomyTreeMouseClicked
        TreePath tp = taxonomyTree.getPathForLocation(evt.getX(), evt.getY());
        //     if (!evt.isControlDown() && !evt.isShiftDown()) {
        try {
            if (tp != null) {
                DefaultMutableTreeNode clickedNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
                buildTreeAfterClick(clickedNode.toString());
                DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) clickedNode.getChildAt(0);
                taxonomyTree.addSelectionPath(new TreePath(childAt.getPath()));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //just the end of a node reached
        } catch (IOException ex) {
            ex.printStackTrace(); // @TODO: better error handling
        }
        // }
    }//GEN-LAST:event_taxonomyTreeMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JTree taxonomyTree;
    private javax.swing.JScrollPane taxonomyTreeScrollPane;
    private javax.swing.JTextField tfSearchField;
    // End of variables declaration//GEN-END:variables
}
