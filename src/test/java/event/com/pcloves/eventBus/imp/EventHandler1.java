package event.com.pcloves.eventBus.imp;

import com.pcloves.eventBus.interfaces.IEventFilter;
import com.pcloves.eventBus.interfaces.IEventHandler;

class EventHandler1
{
	final static IEventHandler<EventHandler1, Event> handleEvent = EventHandler1::handleEvent;
	final static IEventHandler<EventHandler1, Event1> handleEvent1 = EventHandler1::handleEvent1;
	final static IEventFilter<EventHandler1, Event> filterEvent = EventHandler1::filterEvent;

	int eventCallCount = 0;
	int event1CallCount = 0;
	int eventParam = 0;

	private static boolean filterEvent(final EventHandler1 eventHandler, final Event event)
	{
		return false;
	}
	private static void handleEvent(final EventHandler1 eventHandler, final Event event)
	{
		eventHandler.eventCallCount++;
		eventHandler.eventParam = event.param++;
	}
	private static void handleEvent1(final EventHandler1 eventHandler, final Event1 event1)
    {
		eventHandler.event1CallCount++;
    }
}
