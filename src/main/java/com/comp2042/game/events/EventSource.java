package com.comp2042.game.events;

/**
 * Enumeration representing the source of a game event.
 * USER indicates player input, THREAD indicates automatic/timed events.
 */
public enum EventSource {
    /** Event triggered by user input (keyboard press) */
    USER, 
    /** Event triggered automatically by the game thread (timed down movement) */
    THREAD
}

