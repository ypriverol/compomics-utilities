package com.compomics.util.experiment.identification.matches;

import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.identification.IdentificationMatch;
import com.compomics.util.experiment.identification.spectrum_assumptions.PeptideAssumption;
import com.compomics.util.experiment.identification.SpectrumIdentificationAssumption;
import com.compomics.util.experiment.identification.spectrum_assumptions.TagAssumption;
import com.compomics.util.experiment.identification.protein_inference.proteintree.ProteinTree;
import com.compomics.util.experiment.identification.amino_acid_tags.matchers.TagMatcher;
import com.compomics.util.experiment.identification.protein_inference.PeptideProteinMapping;
import com.compomics.util.preferences.SequenceMatchingPreferences;
import java.io.IOException;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class models a spectrum match.
 *
 * @author Marc Vaudel
 */
public class SpectrumMatch extends IdentificationMatch {

    /**
     * The version UID for Serialization/Deserialization compatibility.
     */
    static final long serialVersionUID = 3227760855215444318L;
    /**
     * The index of the matched spectrum.
     */
    private String spectrumKey;
    /**
     * Map of the identification algorithm assumption: advocate number &gt;
     * score &gt; assumptions.
     */
    private HashMap<Integer, HashMap<Double, ArrayList<SpectrumIdentificationAssumption>>> assumptionsMap = null;
    /**
     * The size of the keys used for the tag assumptions map.
     */
    private int tagAssumptionsMapKeySize = -1;
    /**
     * The best peptide assumption.
     */
    private PeptideAssumption bestPeptideAssumption;
    /**
     * The best tag assumption.
     */
    private TagAssumption bestTagAsssumption;
    /**
     * The spectrum number in the mgf file. Will be used in case the spectrum
     * title does not match.
     */
    private Integer spectrumNumber = null;

    /**
     * Constructor for the spectrum match.
     */
    public SpectrumMatch() {
    }

    /**
     * Constructor for the spectrum match.
     *
     * @param spectrumKey the matched spectrumKey
     * @param assumption the matching assumption
     */
    public SpectrumMatch(String spectrumKey, SpectrumIdentificationAssumption assumption) {
        int advocateId = assumption.getAdvocate();
        if (assumptionsMap == null) {
            assumptionsMap = new HashMap<Integer, HashMap<Double, ArrayList<SpectrumIdentificationAssumption>>>(1);
        }
        assumptionsMap.put(advocateId, new HashMap<Double, ArrayList<SpectrumIdentificationAssumption>>());
        assumptionsMap.get(advocateId).put(assumption.getScore(), new ArrayList<SpectrumIdentificationAssumption>());
        assumptionsMap.get(advocateId).get(assumption.getScore()).add(assumption);
        this.spectrumKey = spectrumKey;
    }

    /**
     * Constructor for the spectrum match.
     *
     * @param spectrumKey the matched spectrum key
     */
    public SpectrumMatch(String spectrumKey) {
        this.spectrumKey = spectrumKey;
    }

    /**
     * Getter for the best peptide assumption.
     *
     * @return the best peptide assumption for the spectrum
     */
    public PeptideAssumption getBestPeptideAssumption() {
        return bestPeptideAssumption;
    }

    /**
     * Setter for the best peptide assumption.
     *
     * @param bestAssumption the best peptide assumption for the spectrum
     */
    public void setBestPeptideAssumption(PeptideAssumption bestAssumption) {
        this.bestPeptideAssumption = bestAssumption;
    }

    /**
     * Getter for the best tag assumption.
     *
     * @return the best tag assumption for the spectrum
     */
    public TagAssumption getBestTagAssumption() {
        return bestTagAsssumption;
    }

    /**
     * Setter for the best tag assumption.
     *
     * @param bestTagAsssumption the best tag assumption for the spectrum
     */
    public void setBestTagAssumption(TagAssumption bestTagAsssumption) {
        this.bestTagAsssumption = bestTagAsssumption;
    }

    @Override
    public String getKey() {
        return spectrumKey;
    }

    /**
     * Return all assumptions for the specified search engine indexed by their
     * e-value. Null if none found.
     *
     * @param advocateId the desired advocate ID
     *
     * @return all assumptions
     */
    public HashMap<Double, ArrayList<SpectrumIdentificationAssumption>> getAllAssumptions(int advocateId) {
        if (assumptionsMap == null) {
            return null;
        }
        return assumptionsMap.get(advocateId);
    }

    /**
     * Return all assumptions for all identification algorithms as a list. Null
     * if none found.
     *
     * @return all assumptions
     */
    public ArrayList<SpectrumIdentificationAssumption> getAllAssumptions() {
        if (assumptionsMap == null) {
            return null;
        }
        ArrayList<SpectrumIdentificationAssumption> result = new ArrayList<SpectrumIdentificationAssumption>();
        for (HashMap<Double, ArrayList<SpectrumIdentificationAssumption>> seMap : assumptionsMap.values()) {
            for (double eValue : seMap.keySet()) {
                result.addAll(seMap.get(eValue));
            }
        }
        return result;
    }

    /**
     * Returns the assumptions map: advocate id &gt; score &gt; list of
     * assumptions.
     *
     * @return the assumptions map
     */
    public HashMap<Integer, HashMap<Double, ArrayList<SpectrumIdentificationAssumption>>> getAssumptionsMap() {
        return assumptionsMap;
    }

    /**
     * Removes all assumptions but the best ones from the spectrum map.
     */
    public void removeAssumptions() {
        assumptionsMap = null;
    }

    /**
     * Add a first hit.
     *
     * @param otherAdvocateId the index of the new advocate
     * @param otherAssumption the new identification assumption
     * @param ascendingScore indicates whether the score is ascending when hits
     * get better
     */
    public void addHit(int otherAdvocateId, SpectrumIdentificationAssumption otherAssumption, boolean ascendingScore) {
        if (assumptionsMap == null) {
            assumptionsMap = new HashMap<Integer, HashMap<Double, ArrayList<SpectrumIdentificationAssumption>>>(1);
        }
        HashMap<Double, ArrayList<SpectrumIdentificationAssumption>> advocateMap = assumptionsMap.get(otherAdvocateId);
        if (advocateMap == null) {
            advocateMap = new HashMap<Double, ArrayList<SpectrumIdentificationAssumption>>(1);
            assumptionsMap.put(otherAdvocateId, advocateMap);
        }
        double score = otherAssumption.getScore();
        ArrayList<SpectrumIdentificationAssumption> assumptionList = advocateMap.get(score);
        if (assumptionList == null) {
            assumptionList = new ArrayList<SpectrumIdentificationAssumption>(1);
            advocateMap.put(score, assumptionList);
        }
        assumptionList.add(otherAssumption);
    }

    @Override
    public MatchType getType() {
        return MatchType.Spectrum;
    }

    /**
     * Replaces the new key. The key of the PSM should always be the same as the
     * spectrum key it links to.
     *
     * @param newKey the new key
     */
    public void setKey(String newKey) {
        this.spectrumKey = newKey;
    }

    /**
     * Returns the spectrum number in the spectrum file. Returns null if not
     * implemented (versions older than 3.4.17). 1 is the first spectrum.
     *
     * @return the spectrum number in the spectrum file
     */
    public Integer getSpectrumNumber() {
        return spectrumNumber;
    }

    /**
     * Sets the spectrum number in the spectrum file. 1 is the first spectrum.
     *
     * @param spectrumNumber the spectrum number in the spectrum file
     */
    public void setSpectrumNumber(Integer spectrumNumber) {
        this.spectrumNumber = spectrumNumber;
    }

    /**
     * Removes an assumption from the mapping.
     *
     * @param assumption the peptide assumption to remove
     */
    public void removeAssumption(SpectrumIdentificationAssumption assumption) {
        if (assumptionsMap != null) {
            ArrayList<Integer> seToRemove = new ArrayList<Integer>();
            for (int se : assumptionsMap.keySet()) {
                ArrayList<Double> eValueToRemove = new ArrayList<Double>();
                for (double eValue : assumptionsMap.get(se).keySet()) {
                    assumptionsMap.get(se).get(eValue).remove(assumption);
                    if (assumptionsMap.get(se).get(eValue).isEmpty()) {
                        eValueToRemove.add(eValue);
                    }
                }
                for (double eValue : eValueToRemove) {
                    assumptionsMap.get(se).remove(eValue);
                }
                if (assumptionsMap.get(se).isEmpty()) {
                    seToRemove.add(se);
                }
            }
            for (int se : seToRemove) {
                assumptionsMap.remove(se);
            }
        }
    }

    /**
     * Indicates whether the spectrum match contains a peptide assumption from a
     * search engine.
     *
     * @return a boolean indicating whether the spectrum match contains an
     * assumption
     */
    public boolean hasAssumption() {
        if (assumptionsMap == null) {
            return false;
        }
        for (int se : assumptionsMap.keySet()) {
            for (ArrayList<SpectrumIdentificationAssumption> assumptionsAtScore : assumptionsMap.get(se).values()) {
                if (!assumptionsAtScore.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Indicates whether the spectrum match contains a peptide assumption for
     * the given advocate (for example a search engine, see the Advocate class)
     *
     * @param advocateId The index of the advocate
     * @return a boolean indicating whether the spectrum match contains a
     * peptide assumption for the given advocate
     */
    public boolean hasAssumption(int advocateId) {
        if (assumptionsMap == null) {
            return false;
        }
        if (assumptionsMap.containsKey(advocateId)) {
            for (ArrayList<SpectrumIdentificationAssumption> assumptionsAtEvalue : assumptionsMap.get(advocateId).values()) {
                if (!assumptionsAtEvalue.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a peptide based spectrum match where peptide assumptions are
     * deduced from tag assumptions. The original tag assumption is added to the
     * peptide match as refinement parameter
     *
     * @param proteinTree the protein tree to use to map tags to peptides
     * @param sequenceMatchingPreferences the sequence matching preferences
     * @param massTolerance the MS2 mass tolerance to use
     * @param scoreInAscendingOrder boolean indicating whether the tag score is
     * in the ascending order; ie the higher the score, the better the match.
     * @param tagMatcher the tag matcher to use
     * @param ascendingScore indicates whether the score is ascending when hits
     * get better
     *
     * @return a new spectrum match containing the peptide assumptions made from
     * the tag assumptions.
     *
     * @throws IOException if an IOException occurs
     * @throws SQLException if an SQLException occurs
     * @throws ClassNotFoundException if a ClassNotFoundException occurs
     * @throws InterruptedException if an InterruptedException occurs
     */
    public SpectrumMatch getPeptidesFromTags(ProteinTree proteinTree, TagMatcher tagMatcher, SequenceMatchingPreferences sequenceMatchingPreferences, Double massTolerance,
            boolean scoreInAscendingOrder, boolean ascendingScore)
            throws IOException, InterruptedException, ClassNotFoundException, SQLException {

        SpectrumMatch spectrumMatch = new SpectrumMatch(spectrumKey);

        if (assumptionsMap == null) {
            return spectrumMatch;
        }

        for (int advocateId : assumptionsMap.keySet()) {

            int rank = 1;
            ArrayList<Double> scores = new ArrayList<Double>(assumptionsMap.get(advocateId).keySet());

            if (scoreInAscendingOrder) {
                Collections.sort(scores);
            } else {
                Collections.sort(scores, Collections.reverseOrder());
            }

            for (double score : scores) {
                ArrayList<SpectrumIdentificationAssumption> originalAssumptions = assumptionsMap.get(advocateId).get(score);
                for (SpectrumIdentificationAssumption assumption : originalAssumptions) {
                    if (assumption instanceof TagAssumption) {
                        TagAssumption tagAssumption = (TagAssumption) assumption;
                        ArrayList<PeptideProteinMapping> proteinMapping
                                = proteinTree.getProteinMapping(tagAssumption.getTag(), tagMatcher, sequenceMatchingPreferences, massTolerance);
                        for (Peptide peptide : PeptideProteinMapping.getPeptides(proteinMapping, sequenceMatchingPreferences)) {
                            PeptideAssumption peptideAssumption = new PeptideAssumption(peptide, rank, advocateId,
                                    assumption.getIdentificationCharge(), score, assumption.getIdentificationFile());
                            peptideAssumption.setRawScore(score);
                            peptideAssumption.addUrParam(tagAssumption);
                            spectrumMatch.addHit(advocateId, peptideAssumption, ascendingScore);
                        }
                    }
                }
            }
        }

        return spectrumMatch;
    }
}
