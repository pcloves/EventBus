package event.com.pcloves.eventBus.imp;

import com.pcloves.eventBus.interfaces.IEventFilter;
import com.pcloves.eventBus.interfaces.IEventHandler;

class EventHandler
{
	final static IEventHandler<EventHandler, Event> handleEvent = EventHandler::handleEvent;
	final static IEventHandler<EventHandler, Event1> handleEvent1 = EventHandler::handleEvent1;
	final static IEventFilter<EventHandler, Event> filterEvent = EventHandler::filterEvent;

	int eventCallCount = 0;
	int event1CallCount = 0;
	int eventParam = 0;

	private static boolean filterEvent(final EventHandler eventHandler, final Event event)
	{
		return false;
	}
	private static void handleEvent(final EventHandler eventHandler, final Event event)
	{
		eventHandler.eventCallCount++;
		eventHandler.eventParam = event.param++;
	}
	private static void handleEvent1(final EventHandler eventHandler, final Event1 event1)
    {
		eventHandler.event1CallCount++;
    }
}
