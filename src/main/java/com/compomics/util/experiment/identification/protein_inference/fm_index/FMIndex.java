package com.compomics.util.experiment.identification.protein_inference.fm_index;

import com.compomics.util.experiment.biology.Protein;
import com.compomics.util.experiment.identification.protein_sequences.SequenceFactory;
import com.compomics.util.experiment.identification.protein_sequences.SequenceFactory.ProteinIterator;
import com.compomics.util.experiment.biology.AminoAcid;
import com.compomics.util.experiment.biology.AminoAcidPattern;
import com.compomics.util.experiment.biology.AminoAcidSequence;
import com.compomics.util.experiment.biology.MassGap;
import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.identification.amino_acid_tags.Tag;
import com.compomics.util.experiment.identification.amino_acid_tags.TagComponent;
import com.compomics.util.experiment.identification.amino_acid_tags.matchers.TagMatcher;
import com.compomics.util.experiment.identification.protein_inference.PeptideMapper;
import com.compomics.util.preferences.SequenceMatchingPreferences;
import com.compomics.util.waiting.Duration;
import com.compomics.util.waiting.WaitingHandler;
import com.sun.dtdparser.DTDEventListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * The FM index.
 *
 * @author Dominik Kopczynski
 * @author Marc Vaudel
 */
public class FMIndex implements PeptideMapper {

    /**
     * Sampled suffix array.
     */
    private int[] suffixArrayPrimary = null;
    //private int[] suffixArrayReversed = null;

    /**
     * Wavelet tree for storing the burrows wheeler transform.
     */
    private WaveletTree occurrenceTablePrimary = null;
    private WaveletTree occurrenceTableReversed = null;
    /**
     * Less table for doing an update step according to the LF step.
     */

    private int[] lessTablePrimary = null;
    private int[] lessTableReversed = null;

    /**
     * Length of the indexed string (all concatenated protein sequences).
     */
    private int indexStringLength = 0;
    /**
     * Every 2^samplingShift suffix array entry will be sampled.
     */
    private final int samplingShift = 3;
    /**
     * Mask of fast modulo operations.
     */
    private final int samplingMask = (1 << samplingShift) - 1;
    /**
     * Bit shifting for fast multiplying / dividing operations.
     */
    private final int sampling = 1 << samplingShift;
    /**
     * Storing the starting positions of the protein sequences.
     */
    private ArrayList<Integer> boundaries = null;
    /**
     * List of all accession IDs in the FASTA file.
     */
    private ArrayList<String> accessions = null;
    
    /**
     * List of all amino acid masses
     */
    private double[] aaMasses = null;
    
    /**
     * 
     */
    private double minMass = 0.;
    /**
     * Returns the position of a value in the array or if not found the position
     * of the closest smaller value.
     *
     * @param array the array
     * @param key the key
     * @return he position of a value in the array or if not found the position
     * of the closest smaller value
     */
    private static int binarySearch(ArrayList<Integer> array, int key) {
        int low = 0;
        int mid = 0;
        int high = array.size() - 1;
        while (low <= high) {
            mid = (low + high) >> 1;
            if (array.get(mid) == key) {
                break;
            } else if (array.get(mid) <= key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (mid > 0 && key < array.get(mid)) {
            mid -= 1;
        }
        return mid;
    }

    /**
     * Constructor.
     *
     * @param waitingHandler the waiting handler
     * @param displayProgress if true, the progress is displayed
     */
    public FMIndex(WaitingHandler waitingHandler, boolean displayProgress) {
        SequenceFactory sf = SequenceFactory.getInstance(100000);
        boolean deNovo = true; // TODO: change it for de novo
        int maxProgressBar = 6 + ((deNovo) ? 4 : 0);

        if (waitingHandler != null && displayProgress && !waitingHandler.isRunCanceled()) {
            waitingHandler.setSecondaryProgressCounterIndeterminate(false);
            waitingHandler.setMaxSecondaryProgressCounter(maxProgressBar
            );
            waitingHandler.setSecondaryProgressCounter(0);
        }
        
        
        char[] aminoAcids = AminoAcid.getAminoAcids();
        aaMasses = new double[128];
        for (int i = 0; i < aminoAcids.length; ++i){
            aaMasses[aminoAcids[i]] = AminoAcid.getAminoAcid(aminoAcids[i]).getMonoisotopicMass();
        }
        minMass = aaMasses['G'];
        

        StringBuilder TT = new StringBuilder();
        boundaries = new ArrayList<Integer>();
        accessions = new ArrayList<String>();
        boundaries.add(0);
        indexStringLength = 0;
        int numProteins = 0;

        try {
            ProteinIterator pi = sf.getProteinIterator(false);
            while (pi.hasNext()) {
                if (waitingHandler != null && waitingHandler.isRunCanceled()) {
                    return;
                }
                Protein currentProtein = pi.getNextProtein();
                int proteinLen = currentProtein.getLength();
                indexStringLength += proteinLen;
                ++numProteins;
                boundaries.add(indexStringLength + numProteins);
                accessions.add(currentProtein.getAccession());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        indexStringLength += Math.max(0, numProteins - 1); // delimiters between protein sequences
        indexStringLength += 1; // sentinal

        if (displayProgress && waitingHandler != null && !waitingHandler.isRunCanceled()) {
            waitingHandler.increaseSecondaryProgressCounter();
        }

        byte[] T = new byte[indexStringLength];
        T[indexStringLength - 1] = '$'; // adding the sentinal

        int tmpN = 0;
        try {
            ProteinIterator pi = sf.getProteinIterator(false);

            while (pi.hasNext()) {
                if (waitingHandler != null && waitingHandler.isRunCanceled()) {
                    return;
                }
                Protein currentProtein = pi.getNextProtein();
                int proteinLen = currentProtein.getLength();
                if (tmpN > 0) {
                    T[tmpN++] = '/'; // adding the delimiters
                }
                System.arraycopy(currentProtein.getSequence().getBytes(), 0, T, tmpN, proteinLen);
                tmpN += proteinLen;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (displayProgress && waitingHandler != null && !waitingHandler.isRunCanceled()) {
            waitingHandler.increaseSecondaryProgressCounter();
        }

        
        
        // create the suffix array using at most 128 characters
        suffixArrayPrimary = SuffixArraySorter.buildSuffixArray(T, 128);
        if (displayProgress && waitingHandler != null && !waitingHandler.isRunCanceled()) {
            waitingHandler.increaseSecondaryProgressCounter();
        }
        
        
        
        // Prepare alphabet
        char[] sortedAas = new char[AminoAcid.getAminoAcids().length + 2];
        System.arraycopy(AminoAcid.getAminoAcids(), 0, sortedAas, 0, AminoAcid.getAminoAcids().length);
        sortedAas[AminoAcid.getAminoAcids().length] = '$';
        sortedAas[AminoAcid.getAminoAcids().length + 1] = '/';
        Arrays.sort(sortedAas);

        
        long[] alphabet = new long[]{0, 0};
        for (int i = 0; i < sortedAas.length; ++i) {
            int shift = sortedAas[i] - (sortedAas[i] & 64);
            alphabet[(int) (sortedAas[i] >> 6)] |= 1L << shift;
        }

        
        
        // create Burrows-Wheeler-Transform
        byte[] bwt = new byte[indexStringLength];
        for (int i = 0; i < indexStringLength; ++i) {
            bwt[i] = (suffixArrayPrimary[i] != 0) ? T[suffixArrayPrimary[i] - 1] : T[indexStringLength - 1];
        }
        if (displayProgress && waitingHandler != null && !waitingHandler.isRunCanceled()) {
            waitingHandler.increaseSecondaryProgressCounter();
        }
        
        

        // sampling suffix array
        int[] sampledSuffixArray = new int[((indexStringLength + 1) >> samplingShift) + 1];
        int sampledIndex = 0;
        for (int i = 0; i < indexStringLength; i += sampling) {
            if (waitingHandler != null && waitingHandler.isRunCanceled()) {
                return;
            }
            sampledSuffixArray[sampledIndex++] = suffixArrayPrimary[i];
        }
        suffixArrayPrimary = sampledSuffixArray;
        if (displayProgress && waitingHandler != null && !waitingHandler.isRunCanceled()) {
            waitingHandler.increaseSecondaryProgressCounter();
        }


        occurrenceTablePrimary = new WaveletTree(bwt, alphabet, waitingHandler);
        lessTablePrimary = occurrenceTablePrimary.createLessTable();
        if (displayProgress && waitingHandler != null && !waitingHandler.isRunCanceled()) {
            waitingHandler.increaseSecondaryProgressCounter();
        }

        
        bwt = null;
        if (deNovo) {
            byte[] TReversed = new byte[indexStringLength];
            for (int i = 0; i < indexStringLength - 1; ++i) {
                TReversed[indexStringLength - 2 - i] = T[i];
            }
            TReversed[indexStringLength - 1] = '$';

            // create the suffix array using at most 128 characters
            int[] suffixArrayReversed = SuffixArraySorter.buildSuffixArray(TReversed, 128);
            if (displayProgress && waitingHandler != null && !waitingHandler.isRunCanceled()) {
                waitingHandler.increaseSecondaryProgressCounter();
            }

            // create Burrows-Wheeler-Transform
            bwt = new byte[indexStringLength];
            for (int i = 0; i < indexStringLength; ++i) {
                bwt[i] = (suffixArrayReversed[i] != 0) ? TReversed[suffixArrayReversed[i] - 1] : TReversed[indexStringLength - 1];
            }
            if (displayProgress && waitingHandler != null && !waitingHandler.isRunCanceled()) {
                waitingHandler.increaseSecondaryProgressCounter();
            }
            
            

            occurrenceTableReversed = new WaveletTree(bwt, alphabet, waitingHandler);
            lessTableReversed = occurrenceTableReversed.createLessTable();
            if (displayProgress && waitingHandler != null && !waitingHandler.isRunCanceled()) {
                waitingHandler.increaseSecondaryProgressCounter();
            }
            
            TReversed = null;
            
            /*
            ArrayList<Character> setCharacter = new ArrayList<Character>();
            occurrenceTableReversed.rangeQuery(12, 13, setCharacter);
            for (char c : setCharacter){
                System.out.println(c);
            }*/
        }
        
        T = null;
        bwt = null;
    }

    /**
     * Returns a list of all possible amino acids per position in the peptide
     * according to the sequence matching preferences.
     *
     * @param peptide the peptide
     * @param seqMatchPref the sequence matching preferences
     * @param numPositions the number of positions
     * @return a list of all possible amino acids per position in the peptide
     */
    public ArrayList<String> createPeptideCombinations(String peptide, SequenceMatchingPreferences seqMatchPref, int[] numPositions) {
        ArrayList<String> combinations = new ArrayList<String>();

        SequenceMatchingPreferences.MatchingType sequenceMatchingType = seqMatchPref.getSequenceMatchingType();
        if (sequenceMatchingType == SequenceMatchingPreferences.MatchingType.string) {
            for (int i = 0; i < peptide.length(); ++i) {
                combinations.add(peptide.substring(i, i + 1));
            }
        } else {
            double maxX = seqMatchPref.getLimitX();
            double countX = 0;
            for (int i = 0; i < peptide.length(); ++i) {
                if (peptide.charAt(i) == 'X') {
                    ++countX;
                }
            }
            if (countX / (double) (peptide.length()) < maxX) {
                if (sequenceMatchingType == SequenceMatchingPreferences.MatchingType.aminoAcid || sequenceMatchingType == SequenceMatchingPreferences.MatchingType.indistiguishableAminoAcids) {
                    boolean indistinghuishable = sequenceMatchingType == SequenceMatchingPreferences.MatchingType.indistiguishableAminoAcids;

                    for (int i = 0; i < peptide.length(); ++i) {
                        if (AminoAcid.getAminoAcid(peptide.charAt(i)).iscombination()) {
                            String chars = peptide.substring(i, i + 1);
                            char[] aaCombinations = AminoAcid.getAminoAcid(peptide.charAt(i)).getSubAminoAcids();
                            for (int j = 0; j < aaCombinations.length; ++j) {
                                chars += aaCombinations[j];
                            }
                            combinations.add(chars);
                            numPositions[0] += 1;
                        } else if (indistinghuishable && (peptide.charAt(i) == 'I' || peptide.charAt(i) == 'L')) {
                            String chars = peptide.substring(i, i + 1);
                            char[] aaCombinations = AminoAcid.getAminoAcid(peptide.charAt(i)).getCombinations();
                            for (int j = 0; j < aaCombinations.length; ++j) {
                                chars += aaCombinations[j];
                            }
                            switch (peptide.charAt(i)) {
                                case 'I':
                                    chars += "L";
                                    break;
                                case 'L':
                                    chars += "I";
                                    break;
                            }

                            combinations.add(chars);
                            numPositions[0] += 1;
                        } else {
                            combinations.add(peptide.substring(i, i + 1));
                        }
                    }
                }
            } else {
                numPositions[1] = 0;
            }
        }
        return combinations;
    }

    /**
     * Returns a list of all possible amino acids per position in the peptide
     * according to the sequence matching preferences.
     * @param tagComponents
     * @param seqMatchPref
     * @param numPositions
     * @return 
     */
    public TagElement[] createPeptideCombinations(TagElement[] tagComponents, SequenceMatchingPreferences seqMatchPref, int[] numPositions) {
        
        int numElements = 0;
        for (int i = 0; i < tagComponents.length; ++i){
            if (tagComponents[i].isMass) ++numElements;
            else numElements += tagComponents[i].sequence.length();
        }
        
        TagElement[] combinations = new TagElement[numElements];

        int combinationPosition = 0;
        SequenceMatchingPreferences.MatchingType sequenceMatchingType = seqMatchPref.getSequenceMatchingType();
        if (sequenceMatchingType == SequenceMatchingPreferences.MatchingType.string) {
            for (TagElement tagElement : tagComponents){
                if (tagElement.isMass){
                    combinations[combinationPosition++] = new TagElement(true, "", tagElement.mass);
                }
                else {
                    for (int j = 0; j < tagElement.sequence.length(); ++j) {
                        combinations[combinationPosition++] = new TagElement(false, tagElement.sequence.substring(j, j + 1), tagElement.mass);
                    }
                }
            }
        } else 
        if (sequenceMatchingType == SequenceMatchingPreferences.MatchingType.aminoAcid || sequenceMatchingType == SequenceMatchingPreferences.MatchingType.indistiguishableAminoAcids) {
            boolean indistinghuishable = sequenceMatchingType == SequenceMatchingPreferences.MatchingType.indistiguishableAminoAcids;

            for (TagElement tagElement : tagComponents) {
                if (!tagElement.isMass) {
                    String subSequence = tagElement.sequence;
                    for (char amino : subSequence.toCharArray()) {
                        //countX += (amino == 'X') ? 1 : 0;
                        if (AminoAcid.getAminoAcid(amino).iscombination()) {
                            String chars = String.valueOf(amino);
                            char[] aaCombinations = AminoAcid.getAminoAcid(amino).getSubAminoAcids();
                            for (int j = 0; j < aaCombinations.length; ++j) {
                                chars += aaCombinations[j];
                            }
                            combinations[combinationPosition++] = new TagElement(false, chars, tagElement.mass);
                            numPositions[0] += 1;
                        } else if (indistinghuishable && (amino == 'I' || amino == 'L')) {
                            String chars = String.valueOf(amino);
                            char[] aaCombinations = AminoAcid.getAminoAcid(amino).getCombinations();
                            for (int j = 0; j < aaCombinations.length; ++j) {
                                chars += aaCombinations[j];
                            }
                            switch (amino) {
                                case 'I':
                                    chars += "L";
                                    break;
                                case 'L':
                                    chars += "I";
                                    break;
                            }
                            
                            combinations[combinationPosition++] = new TagElement(false, chars, tagElement.mass);
                            numPositions[0] += 1;
                        } else {
                            combinations[combinationPosition++] = new TagElement(false, String.valueOf(amino), tagElement.mass);
                        }
                    }
                } else 
                    combinations[combinationPosition++] = new TagElement(true, "", tagElement.mass);
            }
        }
        return combinations;
    }
    
       
    
    

    /**
     * Method to get the text position using the sampled suffix array.
     *
     * @param index the position
     * @return the text position
     */
    private int getTextPosition(int index) {
        int numIterations = 0;
        while (((index & samplingMask) != 0) && (index != 0)) {
            int aa = occurrenceTablePrimary.getCharacter(index);
            index = lessTablePrimary[aa] + occurrenceTablePrimary.getRank(index - 1, aa);
            ++numIterations;
        }
        int pos = suffixArrayPrimary[index >> samplingShift] + numIterations;
        return (pos < indexStringLength) ? pos : pos - indexStringLength;
    }

    /**
     * Main method for mapping a peptide with all variants against all
     * registered proteins in the experiment. This method is implementing the
     * backward search.
     *
     * @param peptide the peptide
     * @param seqMatchPref the sequence matching preferences
     * @return the protein mapping
     */
    @Override
    public HashMap<String, HashMap<String, ArrayList<Integer>>> getProteinMapping(String peptide, SequenceMatchingPreferences seqMatchPref) {

        HashMap<String, HashMap<String, ArrayList<Integer>>> allMatches = new HashMap<String, HashMap<String, ArrayList<Integer>>>();

        String pep_rev = new StringBuilder(peptide).reverse().toString();
        int lenPeptide = peptide.length();
        int[] numPositions = new int[]{0, 1};
        ArrayList<String> combinations = createPeptideCombinations(pep_rev, seqMatchPref, numPositions);

        if (numPositions[1] > 0) {

            ArrayList<MatrixContent>[] backwardList = (ArrayList<MatrixContent>[])new ArrayList[lenPeptide + 1];

            for (int i = 0; i <= lenPeptide; ++i) {
                backwardList[i] = new ArrayList<MatrixContent>(10);
            }

            backwardList[0].add(new MatrixContent(0, indexStringLength - 1, '\0', null)); // L, R, char, previous content
            for (int j = 0; j < lenPeptide; ++j) {
                String combinationSequence = combinations.get(j);
                ArrayList<MatrixContent> cell = backwardList[j];
                for (MatrixContent content : cell){
                    int leftIndexOld = content.left;
                    int rightIndexOld = content.right;

                    for (char amino : combinationSequence.toCharArray()) {

                        int intAminoAcid = (int)amino;
                        int leftIndex = lessTablePrimary[intAminoAcid] + occurrenceTablePrimary.getRank(leftIndexOld - 1, intAminoAcid);
                        int rightIndex = lessTablePrimary[intAminoAcid] + occurrenceTablePrimary.getRank(rightIndexOld, intAminoAcid) - 1;

                        if (leftIndex <= rightIndex) {
                            backwardList[j + 1].add(new MatrixContent(leftIndex, rightIndex, (char) intAminoAcid, content));
                        }
                    }
                }
            }

            // Traceback
            for (MatrixContent content : backwardList[lenPeptide]){
                MatrixContent currentContent = content;
                String currentPeptide = "";

                while (currentContent.previousContent != null) {
                    currentPeptide += currentContent.character;
                    currentContent = currentContent.previousContent;
                }

                int leftIndex = content.left;
                int rightIndex = content.right;

                HashMap<String, ArrayList<Integer>> matches = new HashMap<String, ArrayList<Integer>>();

                for (int j = leftIndex; j <= rightIndex; ++j) {
                    int pos = getTextPosition(j);
                    int index = binarySearch(boundaries, pos);
                    String accession = accessions.get(index);

                    if (!matches.containsKey(accession)) {
                        matches.put(accession, new ArrayList<Integer>());
                    }
                    matches.get(accession).add(pos - boundaries.get(index));
                }

                allMatches.put(currentPeptide, matches);
            }
        }
        return allMatches;
    }

    @Override
    public void emptyCache() {
        // No cache here
    }

    @Override
    public void close() throws IOException, SQLException {
        // No open connection here
    }

    @Override
    public HashMap<Peptide, HashMap<String, ArrayList<Integer>>> getProteinMapping(Tag tag, TagMatcher tagMatcher, SequenceMatchingPreferences sequenceMatchingPreferences, Double massTolerance) throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        long startTime = System.nanoTime();
        HashMap<Peptide, HashMap<String, ArrayList<Integer>>> allMatches = new HashMap<Peptide, HashMap<String, ArrayList<Integer>>>();
        
        
        int maxSequencePosition = -1;
        TagElement[] tagElements = new TagElement[tag.getContent().size()];
        for (int i = 0; i < tag.getContent().size(); ++i) {
            if (tag.getContent().get(i) instanceof MassGap){
                tagElements[i] = new TagElement(true, "", tag.getContent().get(i).getMass());
            }
            else if (tag.getContent().get(i) instanceof AminoAcidSequence){
                tagElements[i] = new TagElement(false, tag.getContent().get(i).asSequence(), 0.);
                if (maxSequencePosition == -1 || tagElements[i].sequence.length() < tagElements[i].sequence.length()){
                    maxSequencePosition = i;
                }
            }
            else {
                throw new UnsupportedOperationException("Unsupported tag in tag mapping for FM-Index.");
            }
        }
        
        
        
        
        
        final boolean turned = (tagElements.length == 3 && 
                tagElements[0].isMass &&
                !tagElements[1].isMass &&
                tagElements[2].isMass &&
                tagElements[0].mass < tagElements[2].mass
                );
        
        
        
        
        TagElement[] refTagContent = null;
        int[] lessPrimary = null;
        int[] lessReversed = null;
        WaveletTree occurrencePrimary = null;
        WaveletTree occurrenceReversed = null;
        WaveletTree occurrencePrimaryNext = null;
        WaveletTree occurrenceReversedNext = null;
        
        if (turned){
            
            refTagContent = new TagElement[tagElements.length];
            for (int i = tagElements.length - 1, j = 0; i >= 0; --i, ++j) {
                String sequenceReversed = (new StringBuilder(tagElements[i].sequence).reverse()).toString();
                refTagContent[j] = new TagElement(tagElements[i].isMass, sequenceReversed, tagElements[i].mass);
            }
            
            lessReversed = lessTablePrimary;
            lessPrimary = lessTableReversed;
            occurrenceReversed = occurrenceTablePrimary;
            occurrencePrimary = occurrenceTableReversed;
        }
        else {
            refTagContent = tagElements;
            lessPrimary = lessTablePrimary;
            lessReversed = lessTableReversed;
            occurrencePrimary = occurrenceTablePrimary;
            occurrenceReversed = occurrenceTableReversed;
        }
        
        
        
        
        String orig = "";
        for (TagElement t : refTagContent){
            if (t.isMass) orig += t.mass.toString() + " ";
            else orig += t.sequence + " ";
        }

        
        
        int aminoInsertions = 0;
        TagElement[] tagComponents = new TagElement[maxSequencePosition];
        for (int i = maxSequencePosition - 1, j = 0; i >= 0; --i, ++j) {
            String sequenceReversed = (new StringBuilder(refTagContent[i].sequence).reverse()).toString();
            tagComponents[j] = new TagElement(refTagContent[i].isMass, sequenceReversed, refTagContent[i].mass);
            if (tagComponents[j].isMass) {
                aminoInsertions += Math.ceil(tagComponents[j].mass / minMass);
            }
        }
        
        
        
        int aminoInsertionsReversed = 0;
        TagElement[] tagComponentsReverse = new TagElement[tagElements.length - maxSequencePosition];
        for (int i = maxSequencePosition, j = 0; i < refTagContent.length; ++i, ++j) {
            tagComponentsReverse[j] = refTagContent[i];
            if (tagComponentsReverse[j].isMass) {
                aminoInsertionsReversed += Math.ceil(tagComponentsReverse[j].mass / minMass);
            }
        }
        
        
        
        
        ArrayList<MatrixContent> cached = isCached(tagComponentsReverse);
        if (cached != null && cached.size() == 0){
            
            long endTime = System.nanoTime();
            long microseconds = (endTime - startTime) / 1;
            System.out.println("Duration: " + microseconds + " " + orig + " -1");
            return allMatches;
        }
        
        
        

        int[] numPositions = new int[]{0, 1};
        TagElement[] combinations = createPeptideCombinations(tagComponents, sequenceMatchingPreferences, numPositions);
        TagElement[] combinationsReversed = createPeptideCombinations(tagComponentsReverse, sequenceMatchingPreferences, numPositions);
        int lenCombinations = combinations.length;
        int lenCombinationsReversed = combinationsReversed.length;

        

        int matrixLen = 100;
        
        ArrayList<MatrixContent>[][] matrixReversed = (ArrayList[][])new ArrayList[aminoInsertionsReversed + 1][lenCombinationsReversed + 1];
        ArrayList<MatrixContent>[][] matrix = (ArrayList[][])new ArrayList[aminoInsertions + 1][lenCombinations + 1];
        
        for (int i = 0; i <= aminoInsertionsReversed; ++i) {
            for (int j = 0; j <= lenCombinationsReversed; ++j) {
                matrixReversed[i][j] = new ArrayList<MatrixContent>(matrixLen);
            }
        }
        matrixReversed[0][0].add(new MatrixContent(0, indexStringLength - 1, '\0', null, 0, null)); // L, R, char, tracebackRow, tracebackCol, last_index, mass, peptideSequence
        
        
        for (int i = 0; i <= aminoInsertions; ++i) {
            for (int j = 0; j <= lenCombinations; ++j) {
                matrix[i][j] = new ArrayList<MatrixContent>(matrixLen);
            }
        }
        
        
        int trials = 0;
        boolean nextRow = true;
        
        if (cached == null){
            for (int i = 0; i <= aminoInsertionsReversed && nextRow; ++i) {
                nextRow = false;
                ArrayList<MatrixContent>[] row = matrixReversed[i];
                for (int j = 0; j < lenCombinationsReversed; ++j) {
                    ArrayList<MatrixContent> cell = row[j];   
                    String combinationReversedSequence = combinationsReversed[j].sequence;
                    int combinationReversedSequenceLength = combinationReversedSequence.length();
                    Double combinationReversedMass = combinationsReversed[j].mass;

                    if (!combinationsReversed[j].isMass) {
                        for (MatrixContent content : cell){
                            int leftIndexOld = content.left;
                            int rightIndexOld = content.right;

                            for (int l = 0; l < combinationReversedSequenceLength; ++l) {
                                int intAminoAcid = (int) combinationReversedSequence.charAt(l);
                                int leftIndex = lessReversed[intAminoAcid] + occurrenceReversed.getRank(leftIndexOld - 1, intAminoAcid);
                                int rightIndex = lessReversed[intAminoAcid] + occurrenceReversed.getRank(rightIndexOld, intAminoAcid) - 1;


                                if (leftIndex <= rightIndex) {
                                    row[j + 1].add(new MatrixContent(leftIndex, rightIndex, (char) intAminoAcid, content, 0, null));
                                }
                            }
                        }
                    } else {
                        for (MatrixContent content : cell){
                            int leftIndexOld = content.left;
                            int rightIndexOld = content.right;
                            double oldMass = content.mass;
                            
                            ArrayList<Character> setCharacter = new ArrayList<Character>();
                            if (rightIndexOld - leftIndexOld == 0) setCharacter.add(occurrenceReversed.getCharacter(leftIndexOld));
                            else occurrenceReversed.rangeQuery(leftIndexOld - 1, rightIndexOld, setCharacter);
                            for (char amino : setCharacter){
                                ++trials;

                                int intAminoAcid = (int) amino;
                                int leftIndex = lessReversed[intAminoAcid] + occurrenceReversed.getRank(leftIndexOld - 1, intAminoAcid);
                                int rightIndex = lessReversed[intAminoAcid] + occurrenceReversed.getRank(rightIndexOld, intAminoAcid) - 1;

                                
                                
                                if (amino == '$' || amino == '/') continue;

                                double newMass = oldMass + aaMasses[amino];
                                if (newMass - massTolerance > combinationReversedMass) {
                                    continue;
                                }


                                if (leftIndex <= rightIndex) {
                                    if (Math.abs(combinationReversedMass - newMass) < massTolerance) {
                                        row[j + 1].add(new MatrixContent(leftIndex, rightIndex, (char) intAminoAcid, content, 0, null));
                                    } else if (i < aminoInsertionsReversed) {
                                        matrixReversed[i + 1][j].add(new MatrixContent(leftIndex, rightIndex, (char) intAminoAcid, content, newMass, null));
                                        nextRow = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }




            // Traceback Reverse
            for (int i = 0; i <= aminoInsertionsReversed; ++i) {

                for (MatrixContent content : matrixReversed[i][lenCombinationsReversed]) {
                    MatrixContent currentContent = content;
                    String currentPeptide = "";

                    int leftIndexFront = 0;
                    int rightIndexFront = indexStringLength - 1;
                    

                    while (currentContent.previousContent != null) {
                        currentPeptide += currentContent.character;
                        int currentChar = currentContent.character;
                        leftIndexFront = lessPrimary[currentChar] + occurrencePrimary.getRank(leftIndexFront - 1, currentChar);
                        rightIndexFront = lessPrimary[currentChar] + occurrencePrimary.getRank(rightIndexFront, currentChar) - 1;
                        currentContent = currentContent.previousContent;
                        
                    }
                    matrix[0][0].add(new MatrixContent(leftIndexFront, rightIndexFront, '\0', null, 0, (new StringBuilder(currentPeptide).reverse()).toString()));
                }
            }

            cacheIt(tagComponentsReverse, matrix[0][0]);
        }
        else {
            matrix[0][0] = cached;
        }
        
        // Map Front
        nextRow = true;
        for (int i = 0; i <= aminoInsertions && nextRow; ++i) {
            nextRow = false;
            ArrayList<MatrixContent>[] row = matrix[i];
            for (int j = 0; j < lenCombinations; ++j) {
                ArrayList<MatrixContent> cell = row[j];
                String combinationSequence = combinations[j].sequence;
                int combinationSequenceLength = combinationSequence.length();
                Double combinationMass = combinations[j].mass;
                int cellSize = cell.size();

                if (!combinations[j].isMass) {
                    for (MatrixContent content : cell){
                        int leftIndexOld = content.left;
                        int rightIndexOld = content.right;

                        for (int l = 0; l < combinationSequenceLength; ++l) {

                            int intAminoAcid = (int) combinationSequence.charAt(l);
                            int leftIndex = lessPrimary[intAminoAcid] + occurrencePrimary.getRank(leftIndexOld - 1, intAminoAcid);
                            int rightIndex = lessPrimary[intAminoAcid] + occurrencePrimary.getRank(rightIndexOld, intAminoAcid) - 1;

                            if (leftIndex <= rightIndex) {
                                row[j + 1].add(new MatrixContent(leftIndex, rightIndex, (char)intAminoAcid, content, 0, null));
                            }
                        }
                    }
                } else {
                    for (MatrixContent content : cell){
                        int leftIndexOld = content.left;
                        int rightIndexOld = content.right;
                        double oldMass = content.mass;

                        ArrayList<Character> setCharacter = new ArrayList<Character>();
                        if (rightIndexOld - leftIndexOld > 0) setCharacter.add(occurrencePrimary.getCharacter(leftIndexOld));
                        else occurrencePrimary.rangeQuery(leftIndexOld - 1, rightIndexOld, setCharacter);
                        for (char amino : setCharacter){
                            ++trials;

                            int intAminoAcid = (int) amino;
                            int leftIndex = lessPrimary[intAminoAcid] + occurrencePrimary.getRank(leftIndexOld - 1, intAminoAcid);
                            int rightIndex = lessPrimary[intAminoAcid] + occurrencePrimary.getRank(rightIndexOld, intAminoAcid) - 1;

                            double newMass = oldMass + aaMasses[amino];
                            if (newMass - massTolerance > combinationMass) {
                                continue;
                            }

                            if (leftIndex <= rightIndex) {
                                if (Math.abs(combinationMass - newMass) < massTolerance) {
                                    row[j + 1].add(new MatrixContent(leftIndex, rightIndex, (char) intAminoAcid, content, 0, null));
                                } else if (i < aminoInsertions) {
                                    matrix[i + 1][j].add(new MatrixContent(leftIndex, rightIndex, (char) intAminoAcid, content, newMass, null));
                                    nextRow = true;
                                }
                            }
                        }
                    }
                }
            }
        }


        String peptide = "";
        // Traceback Front
        for (int i = 0; i <= aminoInsertions; ++i) {

            for (MatrixContent content : matrix[i][lenCombinations]) {
                MatrixContent currentContent = content;
                String currentPeptide = "";

                while (currentContent.previousContent != null) {
                    currentPeptide += currentContent.character;
                    currentContent = currentContent.previousContent;
                }
                int leftIndex = content.left;
                int rightIndex = content.right;

                peptide = currentPeptide + currentContent.peptideSequence;

                if (turned){
                    leftIndex = 0;
                    rightIndex = indexStringLength - 1;
                    for (int p = 0; p < peptide.length(); ++p){
                        int intAminoAcid = (int)peptide.charAt(p);
                        leftIndex = lessReversed[intAminoAcid] + occurrenceReversed.getRank(leftIndex - 1, intAminoAcid);
                        rightIndex = lessReversed[intAminoAcid] + occurrenceReversed.getRank(rightIndex, intAminoAcid) - 1;
                    }
                    peptide = (new StringBuilder(peptide).reverse()).toString();
                }

                HashMap<String, ArrayList<Integer>> matches = new HashMap<String, ArrayList<Integer>>();

                for (int j = leftIndex; j <= rightIndex; ++j) {
                    int pos = getTextPosition(j);
                    int index = binarySearch(boundaries, pos);
                    String accession = accessions.get(index);

                    if (!matches.containsKey(accession)) {
                        matches.put(accession, new ArrayList<Integer>());
                    }
                    matches.get(accession).add(pos - boundaries.get(index));
                }

                allMatches.put(new Peptide(peptide, null), matches);
            }
        }

        long endTime = System.nanoTime();
        long microseconds = (endTime - startTime) / 1;
        System.out.println("Duration: " + microseconds + " " + orig + " " + peptide + " " + trials);
        return allMatches;
    }
    
    
    
    
    
    private class TagElement {
        boolean isMass;
        String sequence;
        Double mass;
        
        TagElement(boolean isMass, String sequence, Double mass){
            this.isMass = isMass;
            this.sequence = sequence;
            this.mass = mass;
        }
    }
    
    private class CacheElement {
        String sequence;
        Double mass;
        ArrayList<MatrixContent> precomputed;
        
        public CacheElement(String sequence, Double mass, ArrayList<MatrixContent> precomputed){
            this.sequence = sequence;
            this.mass = mass;
            this.precomputed = precomputed;
        }
    }
    
    /**
     * 
     */
    private boolean cacheLocked = false;
    
    private synchronized boolean lockCache(boolean lock){
        if (cacheLocked && lock){
            return false;
        }
        if (!cacheLocked && lock){
            cacheLocked = lock;
            return true;
        }
        if (cacheLocked && !lock){
            cacheLocked = lock;
            return true;
        }
        return true;            
    }
    
    LinkedList<CacheElement> cache = new LinkedList<CacheElement>();
    
    private ArrayList<MatrixContent> isCached(TagElement[] tagComponents){
        if (tagComponents.length != 2 || tagComponents[0].isMass || !tagComponents[1].isMass) return null;
        
        ArrayList<MatrixContent> cached = null;
        while(!lockCache(true));
        ListIterator<CacheElement> listIterator = cache.listIterator();
        while (listIterator.hasNext()) {
            CacheElement cacheElement = listIterator.next();
            if (cacheElement.sequence.compareTo(tagComponents[0].sequence) == 0 && (Math.abs(cacheElement.mass - tagComponents[1].mass) < 1e-5)){
                cached = new ArrayList<MatrixContent>();
                for (MatrixContent matrixContent : cacheElement.precomputed){
                    cached.add(new MatrixContent(matrixContent));
                }
                break;
            }       
        }
        
        while(!lockCache(false));
        return cached;
    }
    
    
    private void cacheIt(TagElement[] tagComponents, ArrayList<MatrixContent> toCache){   
        if (tagComponents.length != 2 || tagComponents[0].isMass || !tagComponents[1].isMass) return;

        while(!lockCache(true));
        ArrayList<MatrixContent> cacheContent = new ArrayList<MatrixContent>();
        for (MatrixContent matrixContent : toCache){
            cacheContent.add(new MatrixContent(matrixContent));
        }
        CacheElement cacheElement = new CacheElement(tagComponents[0].sequence, tagComponents[1].mass, cacheContent);
        cache.addFirst(cacheElement);
        if (cache.size() > 50){
            cache.removeLast();
        }
        
        while(!lockCache(false));
    }
}
