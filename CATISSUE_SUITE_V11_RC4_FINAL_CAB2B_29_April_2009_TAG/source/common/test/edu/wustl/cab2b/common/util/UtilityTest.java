package edu.wustl.cab2b.common.util;

import junit.framework.TestCase;

/**
 * @author deepak_shingan
 */
public class UtilityTest extends TestCase {

    /**
     * Test method for {@link edu.wustl.cab2b.common.util.Utility#replaceAllWords(java.lang.String, java.lang.String, java.lang.String)}.
     */
    public final void testReplaceAllWords() {
        //char replacement test 
        assertEquals("Test", Utility.replaceAllWords("Teasta", "a", ""));

        //String replacement test 
        assertEquals("What is this? this is this and is this Correct?",
                     Utility.replaceAllWords("What is that? that is that and is that Correct?", "that", "this"));
        //Null argument passing 
        assertEquals(null, Utility.replaceAllWords(null, "that", "this"));

        //No replacement found test  
        assertEquals("No replacement test", Utility.replaceAllWords("No replacement test", "Deepak", "Shingan"));
    }

    public void testCapitalizeString() {
        assertEquals("Gene", Utility.capitalizeFirstCharacter("gene"));
    }

    public void testCapitalizeStringAlreadyCapital() {
        assertEquals("Gene", Utility.capitalizeFirstCharacter("Gene"));
    }
}
