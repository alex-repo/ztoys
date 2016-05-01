package com.ap.common.core;

import java.util.EventObject;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Listened {

    static final CopyOnWriteArrayList<AbstractEventListener> listeners = new CopyOnWriteArrayList<>();

    public default void addListener(AbstractEventListener listener) {
        if (listener instanceof AbstractEventListener)
            listeners.add(listener);
    }

    public default void removeListener(AbstractEventListener listener) {
        if (listener instanceof AbstractEventListener)
            listeners.remove(listener);
    }

    default void fireEvent(EventObject evt) {
        for (AbstractEventListener listener : listeners) {
            listener.handleEvent(evt);
        }
    }

}
