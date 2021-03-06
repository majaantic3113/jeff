/*
 * Copyright 2009 Nemanja Jovanovic
 *
 * This file is part of JEFF (Java Explanation Facility Framework).
 *
 * JEFF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JEFF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with JEFF.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goodoldai.jeff.report.txt;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author Nemanja Jovanovic
 */
public class TXTImageChunkBuilderTest extends AbstractJeffTest {

    TXTImageChunkBuilder instance;
    PrintWriter pw;
    ImageExplanationChunk echunk;
    ImageExplanationChunk echunk1;
    BufferedReader br;

    /**
     * Creates instances that are used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        String[] tags = {"tag1", "tag2"};

        instance = new TXTImageChunkBuilder();
        pw = new PrintWriter(new File("tekst.txt"));
        echunk = new ImageExplanationChunk(new ImageData("test.jpg"));
        echunk1 = new ImageExplanationChunk(-10, "testGroup", "testRule", tags, new ImageData("test.jpg", "testCaption"));

        br = new BufferedReader(new FileReader("tekst.txt"));
    }

    @After
    public void tearDown() throws IOException{

        pw.close();
        br.close();

        new File("tekst.txt").delete();
        instance = null;
    }

    /**
     * Test of buildReportChunk method, of class TXTImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    @Test
    public void testBuildReportChunkAllNullArguments() {
        try {
            instance.buildReportChunk(null, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class TXTImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the first null argument
     */
    @Test
    public void testBuildReportChunkMissingFirstArgumant() {
        try {
            instance.buildReportChunk(null, pw, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'echunk' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class TXTImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    @Test
    public void testBuildReportChunkMissingSecondArgumant() {
        try {
            instance.buildReportChunk(echunk, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class TXTImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the first argument
     */
    @Test
    public void testBuildReportChunkWrongTypeFirsArgumant() {
        try {
            instance.buildReportChunk(new TextExplanationChunk("test"), pw, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The ExplanationChunk must be type of ImageExplanationChunk";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class TXTImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    @Test
    public void testBuildReportChunkWrongTypeSecondArgumant() {
        try {
            instance.buildReportChunk(echunk, "test", false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' must be the type of java.io.PrintWriter";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class TXTImageChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements.
     */
    @Test
    public void testBuildSuccesfull() throws FileNotFoundException, IOException {
        instance.buildReportChunk(echunk1, pw, true);
        pw.close();

        //checks if the file is created
        assertTrue(new File("tekst.txt").exists());

        //skips the lines in document that are tested else were
        br.readLine();
        br.readLine();
        br.readLine();
        br.readLine();
        
        //checks the content
        assertEquals("Caption is: testCaption", br.readLine());
        assertEquals("The path to this image is: test.jpg", br.readLine());
        assertEquals("", br.readLine());

        //checks if anyting eles has been writen to file by mistake
        assertEquals(null, br.readLine());


    }

    /**
     * Test of buildReportChunk method, of class TXTImageChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content.
     */
    @Test
    public void testBuildSuccesfull2() throws FileNotFoundException, IOException {
        instance.buildReportChunk(echunk, pw, true);
        pw.close();

       //checks if the file is created
        assertTrue(new File("tekst.txt").exists());

        //skips the lines in document that are tested else were
        br.readLine();
        
        //checks the content
        assertEquals("The path to this image is: test.jpg", br.readLine());
        assertEquals("", br.readLine());

        //checks if anyting eles has been writen to file by mistake
        assertEquals(null, br.readLine());


    }

    /**
     * Test of buildReportChunk method, of class TXTImageChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content, but with no headers.
     */
    @Test
    public void testBuildSuccesfull3NoHeaders() throws FileNotFoundException, IOException {
        instance.buildReportChunk(echunk, pw, false);
        pw.close();

       //checks if the file is created
        assertTrue(new File("tekst.txt").exists());

        //checks the content
        assertEquals("The path to this image is: test.jpg", br.readLine());
        assertEquals("", br.readLine());

        //checks if anyting else has been writen to file by mistake
        assertEquals(null, br.readLine());


    }
}
