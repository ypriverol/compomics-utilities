package com.compomics.util.experiment.biology.aminoacids;

import com.compomics.util.experiment.biology.AminoAcid;
import com.compomics.util.experiment.biology.Atom;
import com.compomics.util.experiment.biology.AtomChain;
import com.compomics.util.experiment.biology.AtomImpl;

/**
 * Leucine.
 *
 * @author Marc Vaudel
 */
public class Leucine extends AminoAcid {

    /**
     * Serial number for backward compatibility.
     */
    static final long serialVersionUID = -656824121858821632L;

    /**
     * Constructor.
     */
    public Leucine() {
        singleLetterCode = "L";
        threeLetterCode = "Leu";
        name = "Leucine";
        averageMass = 113.1576;
        monoisotopicMass = 113.084064;
        monoisotopicAtomChain = new AtomChain();
        monoisotopicAtomChain.append(new AtomImpl(Atom.C, 0), 6);
        monoisotopicAtomChain.append(new AtomImpl(Atom.H, 0), 11);
        monoisotopicAtomChain.append(new AtomImpl(Atom.N, 0), 1);
        monoisotopicAtomChain.append(new AtomImpl(Atom.O, 0), 1);
        subAminoAcidsWithoutCombination = new char[]{'L'};
        subAminoAcidsWithCombination = subAminoAcidsWithoutCombination;
        aminoAcidCombinations = new char[]{'J', 'X'};
        standardGeneticCode = new String[] {"TTA", "TTG", "CTT", "CTC", "CTA", "CTG"};
    }

    @Override
    public boolean iscombination() {
        return false;
    }

    @Override
    public double getHydrophobicity() {
        return 4.76;
    }

    @Override
    public double getHelicity() {
        return 1.28;
    }

    @Override
    public double getBasicity() {
        return 209.6;
    }

    @Override
    public double getPI() {
        return 6.01;
    }

    @Override
    public double getPK1() {
        return 2.33;
    }

    @Override
    public double getPK2() {
        return 9.74;
    }

    @Override
    public double getPKa() {
        return 0.0;
    }

    @Override
    public int getVanDerWaalsVolume() {
        return 124;
    }
}
