package event.com.pcloves.eventBus.imp;

import com.pcloves.eventBus.interfaces.IEvent;

class Event implements IEvent
{
    int param;

    Event(final int param) {this.param = param;}
}
