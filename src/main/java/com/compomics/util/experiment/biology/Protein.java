package com.compomics.util.experiment.biology;

import com.compomics.util.experiment.massspectrometry.utils.StandardMasses;
import com.compomics.util.experiment.personalization.ExperimentObject;
import com.compomics.util.protein.Header.DatabaseType;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class models a protein.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class Protein extends ExperimentObject {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = 1987224639519365761L;
    /**
     * The protein accession.
     */
    private String accession;
    /**
     * Boolean indicating if the protein is not existing (decoy protein for
     * instance).
     */
    private boolean decoy;
    /**
     * The protein sequence.
     */
    private String sequence;
    /**
     * The protein database type.
     */
    private DatabaseType databaseType;

    /**
     * Constructor for a protein.
     */
    public Protein() {
        
    }

    /**
     * Simplistic constructor for a protein (typically used when loading
     * identification files).
     *
     * @param accession The protein accession
     * @param isDecoy boolean indicating whether the protein is a decoy
     */
    public Protein(String accession, boolean isDecoy) {
        
        this.accession = accession;
        this.decoy = isDecoy;
        
    }

    /**
     * Constructor for a protein.
     *
     * @param accession The protein accession
     * @param sequence The protein sequence
     * @param isDecoy boolean indicating whether the protein is a decoy
     */
    public Protein(String accession, String sequence, boolean isDecoy) {
        
        this.accession = accession;
        this.sequence = sequence;
        this.decoy = isDecoy;
        
    }

    /**
     * Constructor for a protein.
     *
     * @param accession The protein accession
     * @param databaseType The protein database the protein comes from
     * @param sequence The protein sequence
     * @param isDecoy boolean indicating whether the protein is a decoy
     */
    public Protein(String accession, DatabaseType databaseType, String sequence, boolean isDecoy) {
        
        this.accession = accession;
        this.databaseType = databaseType;
        this.sequence = sequence;
        this.decoy = isDecoy;
        
    }

    /**
     * Indicates if the protein is factice (from a decoy database for instance).
     *
     * @return a boolean indicating if the protein is factice
     */
    public boolean isDecoy() {
        
        return decoy;
        
    }

    /**
     * Getter for the protein accession.
     *
     * @return the protein accession
     */
    public String getAccession() {
        
        return accession;
        
    }

    /**
     * Getter for the protein database type.
     *
     * @return the protein database type
     */
    public DatabaseType getDatabaseType() {
        
        return databaseType;
        
    }

    /**
     * Getter for the protein sequence.
     *
     * @return the protein sequence
     */
    public String getSequence() {
        
        return sequence;
        
    }

    /**
     * Returns the key for protein indexing. For now the protein accession.
     *
     * @return the key for protein indexing.
     */
    public String getProteinKey() {
        
        return accession;
        
    }

    /**
     * Returns the number of amino acids in the sequence.
     *
     * @return the number of amino acids in the sequence
     */
    public int getLength() {
        
        return sequence.length();
        
    }

    /**
     * Returns the observable amino acids in the sequence when using the given
     * enzymes with the given maximal peptide length.
     *
     * @param enzymes the enzymes to use
     * @param pepMaxLength the max peptide length
     *
     * @return the number of observable amino acids of the sequence
     */
    public int[] getObservableAminoAcids(ArrayList<Enzyme> enzymes, double pepMaxLength) {
        
        int lastCleavage = -1;
        
        int[] observableAas = new int[sequence.length()];
        
        for (int i = 0 ; i < sequence.length() - 1 ; i++) {
            
            char charati = sequence.charAt(i), charatiPlusOne = sequence.charAt(i + 1);
            
            if (enzymes.stream().anyMatch(enzyme -> enzyme.isCleavageSite(charati, charatiPlusOne))) {
                
                if (i - lastCleavage <= pepMaxLength) {
                    
                    for (int k = lastCleavage ; k < i ; k++) {
                        
                        observableAas[k] = 1;
                        
                    }
                }
                
                lastCleavage = i;
                
            }
        }
        
        if (sequence.length() - 1 - lastCleavage <= pepMaxLength) {
            
            for (int k = lastCleavage ; k < sequence.length() ; k++) {
                
                observableAas[k] = 1;
                
            }
        }
        
        return observableAas;
    }

    /**
     * Returns the number of observable amino acids in the sequence.
     *
     * @param enzymes the enzymes to use
     * @param pepMaxLength the max peptide length
     *
     * @return the number of observable amino acids of the sequence
     */
    public int getObservableLength(ArrayList<Enzyme> enzymes, double pepMaxLength) {
        
        int[] observalbeAas = getObservableAminoAcids(enzymes, pepMaxLength);
        
        return Arrays.stream(observalbeAas).sum();
    }

    /**
     * Returns the number of cleavage sites.
     *
     * @param enzymes the enzymes to use
     *
     * @return the number of possible peptides
     */
    public int getNCleavageSites(ArrayList<Enzyme> enzymes) {
        
        int nCleavageSites = 0;
        
        for (int i = 0; i < sequence.length() - 1; i++) {
            
            char charati = sequence.charAt(i), charatiPlusOne = sequence.charAt(i + 1);
            
            if (enzymes.stream().anyMatch(enzyme -> enzyme.isCleavageSite(charati, charatiPlusOne))) {
                
                nCleavageSites++;
            
            }
        }
        
        return nCleavageSites;
    }

    /**
     * Returns the protein's molecular weight. (Note that when using a
     * SequenceFactory it is recommended to use the SequenceFactory's
     * computeMolecularWeight method instead, as that method stored the computed
     * molecular weights instead of recalculating them every time.)
     *
     * @return the protein's molecular weight in Da
     */
    public double computeMolecularWeight() {

        double mass = StandardMasses.h2o.mass;

        mass += sequence.chars().mapToDouble(aa -> AminoAcid.getAminoAcid((char) aa).getMonoisotopicMass()).sum();

        return mass;
    }

    /**
     * Returns the number of enzymatic termini for the given enzyme on this
     * protein at the given location.
     *
     * @param peptideStart the 0 based index of the peptide start on the protein
     * @param peptideEnd the 0 based index of the peptide end on the protein
     * @param enzyme the enzyme to use
     *
     * @return true of the peptide is non-enzymatic
     */
    public int getNEnzymaticTermini(int peptideStart, int peptideEnd, Enzyme enzyme) {

        int nEnzymatic = 0;

        if (peptideStart == 0) {

            nEnzymatic++;

        } else {

            char aaBefore = sequence.charAt(peptideStart - 1);
            char aaAfter = sequence.charAt(peptideStart);

            if (enzyme.isCleavageSite(aaBefore, aaAfter)) {

                nEnzymatic++;

            }

        }

        if (peptideEnd == sequence.length() - 1) {

            nEnzymatic++;

        } else {

            char aaBefore = sequence.charAt(peptideEnd);
            char aaAfter = sequence.charAt(peptideEnd + 1);

            if (enzyme.isCleavageSite(aaBefore, aaAfter)) {

                nEnzymatic++;

            }
        }

        return nEnzymatic;
    }

    /**
     * Returns the maximal number of enzymatic termini for the given enzymes on
     * this protein at the given location.
     *
     * @param peptideStart the 0 based index of the peptide start on the protein
     * @param peptideEnd the 0 based index of the peptide end on the protein
     * @param enzymes the enzymes to use
     *
     * @return true of the peptide is non-enzymatic
     */
    public int getNEnzymaticTermini(int peptideStart, int peptideEnd, ArrayList<Enzyme> enzymes) {

        return enzymes.stream().mapToInt(enzyme -> getNEnzymaticTermini(peptideStart, peptideEnd, enzyme)).max().orElse(0);
    }

    /**
     * Returns the maximal number of enzymatic termini for the given peptide
     * sequence using the given enzymes on this protein at the given locations.
     *
     * @param peptideStartEnd a list of 0 based index pairs of the peptide start
     * on the protein
     * @param enzymes the enzymes to use
     *
     * @return true of the peptide is non-enzymatic
     */
    public int getNEnzymaticTermini(ArrayList<int[]> peptideStartEnd, ArrayList<Enzyme> enzymes) {

        return peptideStartEnd.stream().mapToInt(startEnd -> getNEnzymaticTermini(startEnd[0], startEnd[1], enzymes)).max().orElse(0);
    }
}
