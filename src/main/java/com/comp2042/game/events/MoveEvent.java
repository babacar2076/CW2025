package com.comp2042.game.events;

/**
 * Immutable event class representing a game movement action.
 * Contains information about the type of movement and its source (user input or automatic).
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a new MoveEvent with the specified type and source.
     * @param eventType The type of movement (DOWN, LEFT, RIGHT, ROTATE)
     * @param eventSource The source of the event (USER or THREAD)
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the type of movement event.
     * @return EventType enum value (DOWN, LEFT, RIGHT, or ROTATE)
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source of the movement event.
     * @return EventSource enum value (USER for player input, THREAD for automatic movement)
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}

