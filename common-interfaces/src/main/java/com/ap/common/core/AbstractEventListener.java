package com.ap.common.core;

import java.util.EventListener;
import java.util.EventObject;

public interface AbstractEventListener extends EventListener {
    void handleEvent(EventObject event);
}
