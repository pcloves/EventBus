package event.com.pcloves.eventBus.imp;

import com.pcloves.eventBus.interfaces.IEventFilter;
import com.pcloves.eventBus.interfaces.IEventHandler;

class EventHandler
{
	final IEventHandler<Event> handleEvent = this::handleEvent;
	final IEventHandler<Event1> handleEvent1 = this::handleEvent1;
	final IEventFilter<Event> filterEvent = this::filterEvent;

	int eventCallCount = 0;
	int event1CallCount = 0;
	int eventParam = 0;

	private boolean filterEvent(final Event event)
	{
		return false;
	}
	private void handleEvent(final Event event)
	{
		eventCallCount++;
		eventParam = event.param++;
	}
	private void handleEvent1(final Event1 event1)
    {
		event1CallCount++;
    }
}
