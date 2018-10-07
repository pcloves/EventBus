package event.com.pcloves.eventBus.imp;

import com.pcloves.eventBus.interfaces.IEvent;

class Event1 implements IEvent
{
    private final int param;

    Event1(final int param) {this.param = param;}
}
