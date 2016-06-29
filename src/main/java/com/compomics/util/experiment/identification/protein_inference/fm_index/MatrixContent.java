package com.compomics.util.experiment.identification.protein_inference.fm_index;

import com.compomics.util.experiment.identification.matches.ModificationMatch;
import java.util.ArrayList;

/**
 * Element for the matrix necessary in pattern search of the FMIndex.
 *
 * @author Dominik Kopczynski
 */
public class MatrixContent {

    /**
     * Left index.
     */
    public int left;
    
    /**
     * Right index.
     */
    public int right;
    
    /**
     * Character which was chosen.
     */
    public int character;
    
    /**
     * Index of the originating entry of a particular cell with the pattern
     * searching matrix.
     */
    public MatrixContent previousContent;
    
    /**
     * Current mass.
     */
    public double mass;
    
    /**
     * Current peptide sequence.
     */
    public String peptideSequence;
    
    /**
     * Current peptide sequence length.
     */
    public int length;
    
    /**
     * Current number of contained X's.
     */
    public int numX;
    
    /**
     * Index to the modifications list.
     */
    public ModificationMatch modification;
    
    /**
     * List of all modifications.
     */
    public ArrayList<ModificationMatch> modifications;
    
    /**
     * List of all modifications.
     */
    public int modificationPos;
    
    /**
     * List of all modifications.
     */
    public int numVariants;
    
    /**
     * Type of edit operation, either deletion 'd', substitution 's' or insertion 'i'
     */
    public char variant;
    
    /**
     * Current storing of all variant operations
     */
    public String allVariants;
    
    
    
    /**
     * Constructor almost empty.
     *
     * @param right right index boundary
     */
    public MatrixContent(int right) {
        this.left = 0;
        this.right = right;
        this.character = 0;
        this.previousContent = null;
        this.mass = 0;
        this.peptideSequence = null;
        this.length = 1;
        this.numX = 0;
        this.modification = null;
        this.modifications = null;
        this.modificationPos = -1;
        this.numVariants = 0;
        this.variant = '\0';
        this.allVariants = null;
    }
    
    
    
    /**
     * Constructor for simple sequance mapping.
     *
     * @param left left index boundary
     * @param right right index boundary
     * @param character current character stored
     * @param previousContent previous matrix content
     * @param numX number of current X amino acids
     */
    public MatrixContent(int left, int right, int character, MatrixContent previousContent, int numX) {
        this.left = left;
        this.right = right;
        this.character = character;
        this.previousContent = previousContent;
        this.mass = 0;
        this.peptideSequence = null;
        this.length = 1;
        this.numX = numX;
        this.modification = null;
        this.modifications = null;
        this.modificationPos = -1;
        this.numVariants = 0;
        this.variant = '\0';
        this.allVariants = null;
    }
    
    
    
    /**
     * Constructor for simple tag mapping.
     *
     * @param left left index boundary
     * @param right right index boundary
     * @param character current character stored
     * @param previousContent previous matrix content
     * @param numX number of current X amino acids
     * @param mass current mass
     * @param length current peptide length
     */
    public MatrixContent(int left, int right, int character, MatrixContent previousContent, int numX, double mass, int length) {
        this.left = left;
        this.right = right;
        this.character = character;
        this.previousContent = previousContent;
        this.mass = mass;
        this.peptideSequence = null;
        this.length = length;
        this.numX = numX;
        this.modification = null;
        this.modifications = null;
        this.modificationPos = -1;
        this.numVariants = 0;
        this.variant = '\0';
        this.allVariants = null;
    }
    
    
    
    /**
     * Constructor for simple tag mapping with peptide Sequence.
     *
     * @param left left index boundary
     * @param right right index boundary
     * @param character current character stored
     * @param previousContent previous matrix content
     * @param numX number of current X amino acids
     * @param mass current mass
     * @param length current peptide length
     * @param peptideSequence peptide sequence
     */
    public MatrixContent(int left, int right, int character, MatrixContent previousContent, int numX, double mass, int length, String peptideSequence) {
        this.left = left;
        this.right = right;
        this.character = character;
        this.previousContent = previousContent;
        this.mass = mass;
        this.peptideSequence = peptideSequence;
        this.length = length;
        this.numX = numX;
        this.modification = null;
        this.modifications = null;
        this.modificationPos = -1;
        this.numVariants = 0;
        this.variant = '\0';
        this.allVariants = null;
    }
    
    
    
    
    

    /**
     * Constructor for sequence with variants.
     *
     * @param left left index boundary
     * @param right right index boundary
     * @param character current character stored
     * @param previousContent previous matrix content
     * @param numX number of current X amino acids
     * @param length length of the current peptide
     * @param numEdits number of edit operations
     * @param variant type of edit operation 
     */
    public MatrixContent(int left, int right, int character, MatrixContent previousContent, int numX, int length, int numEdits, char variant) {
        this.left = left;
        this.right = right;
        this.character = character;
        this.previousContent = previousContent;
        this.mass = 0;
        this.peptideSequence = null;
        this.length = length;
        this.numX = numX;
        this.modification = null;
        this.modifications = null;
        this.modificationPos = -1;
        this.numVariants = numEdits;
        this.variant = variant;
        this.allVariants = null;
    }
    
    
    
    
    
    
    
    

    /**
     * Constructor.
     *
     * @param left left index boundary
     * @param right right index boundary
     * @param character current character stored
     * @param previousContent previous matrix content
     * @param mass current mass
     * @param length current peptide length
     * @param numX number of current X amino acids
     * @param modifictationPos index to modification list for ptm
     */
    public MatrixContent(int left, int right, int character, MatrixContent previousContent, double mass, int length, int numX, int modifictationPos) {

        this.left = left;
        this.right = right;
        this.character = character;
        this.previousContent = previousContent;
        this.mass = mass;
        this.peptideSequence = null;
        this.length = length;
        this.numX = numX;
        this.modification = null;
        this.modifications = null;
        this.modificationPos = modifictationPos;
        this.numVariants = 0;
        this.variant = '\0';
        this.allVariants = null;
    }
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     * Constructor.
     *
     * @param left left index boundary
     * @param right right index boundary
     * @param character current character stored
     * @param previousContent previous matrix content
     * @param mass current mass
     * @param peptideSequence intermediate peptide sequence
     * @param length current peptide length
     * @param numX number of current X amino acids
     * @param modification index to modification list
     * @param modifications intermediate list of modifications
     * @param modifictationPos index to modification list for ptm
     */
    public MatrixContent(int left, int right, int character, MatrixContent previousContent, double mass, String peptideSequence, int length, int numX, ModificationMatch modification, ArrayList<ModificationMatch> modifications, int modifictationPos) {

        this.left = left;
        this.right = right;
        this.character = character;
        this.previousContent = previousContent;
        this.mass = mass;
        this.peptideSequence = peptideSequence;
        this.length = length;
        this.numX = numX;
        this.modification = modification;
        this.modifications = modifications;
        this.modificationPos = modifictationPos;
        this.numVariants = 0;
        this.variant = '\0';
        this.allVariants = null;
    }

    /**
     * Constructor.
     *
     * @param left left index boundary
     * @param right right index boundary
     * @param character current character stored
     * @param previousContent previous matrix content
     * @param mass current mass
     * @param length current peptide length
     * @param numX number of current X amino acids
     * @param modifictationPos index to modification list for ptm
     * @param numVariants number of edit operations
     * @param variant type of varient
     * @param allVariants all variants
     */
    public MatrixContent(int left, int right, int character, MatrixContent previousContent, double mass, int length, int numX,
            int modifictationPos, int numVariants, char variant, String allVariants) {


        this.left = left;
        this.right = right;
        this.character = character;
        this.previousContent = previousContent;
        this.mass = mass;
        this.peptideSequence = null;
        this.length = length;
        this.numX = numX;
        this.modification = null;
        this.modifications = null;
        this.modificationPos = modifictationPos;
        this.numVariants = numVariants;
        this.variant = variant;
        this.allVariants = allVariants;
    }

    /**
     * Constructor.
     *
     * @param left left index boundary
     * @param right right index boundary
     * @param character current character stored
     * @param previousContent previous matrix content
     * @param mass current mass
     * @param peptideSequence intermediate peptide sequence
     * @param length current peptide length
     * @param numX number of current X amino acids
     * @param modification index to modification list
     * @param modifications intermediate list of modifications
     * @param modifictationPos index to modification list for ptm
     * @param numVariants number of edit operations
     * @param variant type of varient
     * @param allVariants all variants
     */
    public MatrixContent(int left, int right, int character, MatrixContent previousContent, double mass, String peptideSequence, int length, int numX, ModificationMatch modification, ArrayList<ModificationMatch> modifications,
            int modifictationPos, int numVariants, char variant, String allVariants) {


        this.left = left;
        this.right = right;
        this.character = character;
        this.previousContent = previousContent;
        this.mass = mass;
        this.peptideSequence = peptideSequence;
        this.length = length;
        this.numX = numX;
        this.modification = modification;
        this.modifications = modifications;
        this.modificationPos = modifictationPos;
        this.numVariants = numVariants;
        this.variant = variant;
        this.allVariants = allVariants;
    }
    
    
    
    

    /**
     * Copy constructor.
     *
     * @param foreign foreign matrix content instance
     */
    public MatrixContent(MatrixContent foreign) {
        this.left = foreign.left;
        this.right = foreign.right;
        this.character = foreign.character;
        this.previousContent = foreign.previousContent;
        this.mass = foreign.mass;
        this.peptideSequence = foreign.peptideSequence;
        this.length = foreign.length;
        this.numX = foreign.numX;
        this.modification = foreign.modification;
        this.modifications = foreign.modifications;
        this.modificationPos = foreign.modificationPos;
        this.numVariants = foreign.numVariants;
        this.variant = foreign.variant;
        this.allVariants = foreign.allVariants;
    }

    
}