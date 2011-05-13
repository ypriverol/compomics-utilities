package com.compomics.util.experiment.quantification.reporterion.quantification;

import com.compomics.util.experiment.identification.matches.PeptideMatch;
import com.compomics.util.experiment.quantification.Ratio;
import com.compomics.util.experiment.quantification.reporterion.quantification.PsmQuantification;
import com.compomics.util.experiment.personalization.ExperimentObject;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * This class models quantification at the peptide level.
 * User: Marc
 * Date: Sep 1, 2010
 * Time: 3:49:22 PM
 */
public class PeptideQuantification extends ExperimentObject {

    /**
     * the identification peptide match
     */
    private PeptideMatch peptideMatch;

    /**
     * The corresponding spectrum quantification
     */
    private HashMap<String, PsmQuantification> psmQuantification = new HashMap<String, PsmQuantification>();

    /**
     * The estimated ratios
     */
    private HashMap<Integer, Ratio> ratios = new HashMap<Integer, Ratio>();

    /**
     * Constructor for the peptide quantification
     * @param peptideMatch              the identification peptide match
     */
    public PeptideQuantification(PeptideMatch peptideMatch) {
        this.peptideMatch = peptideMatch;
    }
    /**
     * Constructor for the peptide quantification
     * @param peptideMatch              the identification peptide match
     * @param psmQuantification         the corresponding spectrum quantification 
     */
    public PeptideQuantification(PeptideMatch peptideMatch, HashMap<String, PsmQuantification> psmQuantification) {
        this.peptideMatch = peptideMatch;
        this.psmQuantification = psmQuantification;
    }

    /**
     * Constructor for the peptide quantification
     * @param peptideMatch              the identification peptide match
     * @param psmQuantification         the corresponding spectrum quantification
     * @param ratios                    the estimated ratios
     */
    public PeptideQuantification(PeptideMatch peptideMatch, HashMap<String, PsmQuantification> psmQuantification, HashMap<Integer, Ratio> ratios) {
        this.peptideMatch = peptideMatch;
        this.psmQuantification = psmQuantification;
        this.ratios = ratios;
    }

    /**
     * sets new peptide ratios
     * @param ratios    the new peptide ratios
     */
    public void setPeptideRatios(HashMap<Integer, Ratio> ratios) {
        this.ratios = ratios;
    }

    /**
     * Getter for the peptide match
     * @return the peptide match
     */
    public PeptideMatch getPeptideMatch() {
        return peptideMatch;
    }

    /**
     * Getter for the peptide ratios
     * @return the peptide ratios
     */
    public HashMap<Integer, Ratio> getRatios() {
        return ratios;
    }

    /**
     * Getter for the corresponding spectrum quantification
     * @return List of spectrum quantification
     */
    public HashMap<String, PsmQuantification> getPsmQuantification() {
        return psmQuantification;
    }
    /**
     * returns a specific psm quantification
     * @param index the index of the quantification 
     * @return the desired psm
     */
    public PsmQuantification getPsm(String index) {
        return psmQuantification.get(index);
    }

    /**
     * Adds a new psm quantification in the psm quantification map
     * @param newPsmQuantification the new psm quantification
     */
    public void addPsmQuantification(PsmQuantification newPsmQuantification) {
        psmQuantification.put(newPsmQuantification.getKey(), newPsmQuantification);
    }

    /**
     * Returns the key of the peptide quantification
     * @return the key of the peptide quantification
     */
    public String getKey() {
        return peptideMatch.getKey();
    }
}
