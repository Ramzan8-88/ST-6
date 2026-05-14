package com.mycompany.app;

import org.junit.jupiter.api.Test;

import java.awt.GridLayout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UtilityAndUiTest {

    @Test
    void utilityPrintCharBoard() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(buf);
        PrintStream old = System.out;
        System.setOut(ps);
        try {
            Utility.print(new char[]{'X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '});
        } finally {
            System.setOut(old);
        }
        String s = buf.toString();
        assertTrue(s.contains("X"));
        assertTrue(s.contains("O"));
    }

    @Test
    void utilityPrintIntBoard() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(buf);
        PrintStream old = System.out;
        System.setOut(ps);
        try {
            Utility.print(new int[]{1, 2, 3, 0, 0, 0, 0, 0, 0});
        } finally {
            System.setOut(old);
        }
        assertTrue(buf.toString().contains("1"));
    }

    @Test
    void utilityPrintMoveList() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(buf);
        PrintStream old = System.out;
        System.setOut(ps);
        try {
            ArrayList<Integer> m = new ArrayList<>();
            m.add(0);
            m.add(4);
            Utility.print(m);
        } finally {
            System.setOut(old);
        }
        assertTrue(buf.toString().contains("0"));
    }

    @Test
    void ticTacToeCell_gettersAndMarker() {
        TicTacToeCell c = new TicTacToeCell(4, 1, 1);
        assertEquals(4, c.getNum());
        assertEquals(1, c.getCol());
        assertEquals(1, c.getRow());
        assertEquals(' ', c.getMarker());
        c.setMarker("X");
        assertEquals('X', c.getMarker());
        assertFalse(c.isEnabled());
    }

    @Test
    void ticTacToePanel_constructsInHeadlessMode() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        assertNotNull(panel);
        assertEquals(9, panel.getComponentCount());
    }
}
