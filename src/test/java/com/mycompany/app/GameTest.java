package com.mycompany.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void newGameHasEmptyBoardAndPlayers() {
        assertEquals(State.PLAYING, game.state);
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        for (char c : game.board) {
            assertEquals(' ', c);
        }
    }

    @Test
    void checkState_detectsXWinFirstRow() {
        char[] b = board(
                'X', 'X', 'X',
                ' ', 'O', ' ',
                'O', ' ', ' ');
        assertEquals(State.XWIN, game.checkState(b));
    }

    @Test
    void checkState_detectsOWinColumn() {
        char[] b = board(
                'O', 'X', ' ',
                'O', 'X', ' ',
                'O', ' ', ' ');
        assertEquals(State.OWIN, game.checkState(b));
    }

    @Test
    void checkState_detectsXWinDiagonal() {
        char[] b = board(
                'X', 'O', 'O',
                ' ', 'X', ' ',
                ' ', ' ', 'X');
        assertEquals(State.XWIN, game.checkState(b));
    }

    @Test
    void checkState_playingWhenMovesRemain() {
        char[] b = board(
                'X', 'O', ' ',
                ' ', ' ', ' ',
                ' ', ' ', ' ');
        assertEquals(State.PLAYING, game.checkState(b));
    }

    @Test
    void checkState_drawWhenBoardFullNoWinner() {
        char[] b = board(
                'X', 'O', 'X',
                'O', 'O', 'X',
                'O', 'X', 'O');
        assertEquals(State.DRAW, game.checkState(b));
    }

    @Test
    void evaluatePosition_xWinForXPlayer() {
        char[] b = board(
                'X', 'X', 'X',
                ' ', 'O', ' ',
                'O', ' ', ' ');
        assertEquals(Game.INF, game.evaluatePosition(b, game.player1));
    }

    @Test
    void evaluatePosition_xWinAgainstOPlayer() {
        char[] b = board(
                'X', 'X', 'X',
                ' ', 'O', ' ',
                'O', ' ', ' ');
        assertEquals(-Game.INF, game.evaluatePosition(b, game.player2));
    }

    @Test
    void evaluatePosition_oWinForOPlayer() {
        char[] b = board(
                'O', 'X', ' ',
                'O', 'X', ' ',
                'O', ' ', ' ');
        assertEquals(Game.INF, game.evaluatePosition(b, game.player2));
    }

    @Test
    void evaluatePosition_drawScoresZero() {
        char[] b = board(
                'X', 'O', 'X',
                'O', 'O', 'X',
                'O', 'X', 'O');
        assertEquals(0, game.evaluatePosition(b, game.player1));
        assertEquals(0, game.evaluatePosition(b, game.player2));
    }

    @Test
    void evaluatePosition_inProgressReturnsMinusOne() {
        char[] b = board(
                'X', ' ', ' ',
                ' ', 'O', ' ',
                ' ', ' ', ' ');
        assertEquals(-1, game.evaluatePosition(b, game.player2));
    }

    @Test
    void generateMoves_listsAllEmptyCells() {
        char[] b = board(
                'X', ' ', 'O',
                ' ', ' ', ' ',
                ' ', ' ', ' ');
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(b, moves);
        assertEquals(7, moves.size());
        assertTrue(moves.contains(1));
        assertTrue(moves.contains(3));
        assertFalse(moves.contains(0));
        assertFalse(moves.contains(2));
    }

    @Test
    void miniMax_returnsLegalMoveOnEmptyBoardForO() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        try {
            int move = game.MiniMax(Arrays.copyOf(game.board, 9), game.player2);
            assertTrue(move >= 1 && move <= 9);
        } finally {
            System.setOut(old);
        }
    }

    @Test
    void checkState_detectsXWinEvenWhenGameSymbolIsO() {
        char[] b = board(
                'X', 'X', 'X',
                'O', 'O', ' ',
                ' ', ' ', ' ');
        game.symbol = 'O';
        assertEquals(State.XWIN, game.checkState(b));
    }

    @Test
    void miniMax_blocksImmediateXWin() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        try {
            char[] b = board(
                    'X', 'X', ' ',
                    'O', ' ', ' ',
                    ' ', ' ', ' ');
            int move = game.MiniMax(b, game.player2);
            assertEquals(3, move);
        } finally {
            System.setOut(old);
        }
    }

    @Test
    void miniMax_takesWinningMoveForO() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        try {
            char[] b = board(
                    'O', 'X', 'X',
                    'O', 'X', ' ',
                    ' ', ' ', ' ');
            int move = game.MiniMax(b, game.player2);
            assertEquals(7, move);
        } finally {
            System.setOut(old);
        }
    }

    private static char[] board(char... cells) {
        assertEquals(9, cells.length);
        return Arrays.copyOf(cells, 9);
    }
}
