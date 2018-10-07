package event.com.pcloves.eventBus.imp;

import com.pcloves.eventBus.imp.EventBus;
import com.pcloves.eventBus.interfaces.EEventPriority;
import com.pcloves.eventBus.interfaces.IEventBus;
import com.pcloves.eventBus.interfaces.IEventHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

class EventBusTest
{
	private static final int LoopCount = 100000;
	private IEventBus eventBus;
	private EventHandler eventHandler;

	@BeforeEach void setUp()
	{
		eventBus = new EventBus();
		eventHandler = new EventHandler();
		EventHandler.event1CallCount = 0;
		EventHandler.eventCallCount = 0;
	}

	@Test void  testCallCount()
	{
		eventBus.registerEvent(eventHandler, Event.class, EventHandler.handleEvent, EEventPriority.Normal);

		for (int i = 0; i < LoopCount; i++)
		{
			eventBus.sendEvent(new Event(1));
		}

		Assertions.assertEquals(LoopCount, EventHandler.eventCallCount);
		Assertions.assertEquals(0, EventHandler.event1CallCount);
	}

	@Test void  testUnregisterEvent()
	{
		final IEventHandler<EventHandler, Event> handleEvent = EventHandler.handleEvent;

		Assertions.assertEquals(handleEvent, EventHandler.handleEvent);

		eventBus.registerEvent(eventHandler, Event.class, EventHandler.handleEvent, EEventPriority.Normal);
		eventBus.registerEvent(eventHandler, Event.class, EventHandler.handleEvent, EEventPriority.Normal);

		eventBus.unRegisterEvent(Event.class, handleEvent);

		for (int i = 0; i < LoopCount; i++)
		{
			eventBus.sendEvent(new Event(1));
		}

		Assertions.assertEquals(0, EventHandler.eventCallCount);
		Assertions.assertEquals(0, EventHandler.event1CallCount);
	}

	@Test void  testEventFilter()
	{
		eventBus.registerEvent(eventHandler, Event.class, EventHandler.handleEvent, EEventPriority.Normal);
		eventBus.registerEvent(eventHandler, Event1.class, EventHandler.handleEvent1, EEventPriority.Normal);
		eventBus.addEventFilter(Event.class, EventHandler.filterEvent);

		for (int i = 0; i < LoopCount; i++)
		{
			eventBus.sendEvent(new Event(i));
			eventBus.sendEvent(new Event1(i));
		}

		Assertions.assertEquals(0, EventHandler.eventCallCount);
		Assertions.assertEquals(LoopCount, EventHandler.event1CallCount);
	}

	@Test void  testPostEvent()
	{
		eventBus.registerEvent(eventHandler, Event.class, EventHandler.handleEvent, EEventPriority.Normal);

		int loopCount = new Random(System.currentTimeMillis()).nextInt(2 * LoopCount);
		for (int i = 0; i < loopCount; i++)
		{
			eventBus.postEvent(new Event(1));
		}

		Assertions.assertEquals(0, EventHandler.eventCallCount);

		eventBus.dispatchCachedEvent(50000);
		Assertions.assertEquals(Math.min(loopCount, 50000), EventHandler.eventCallCount);
	}
}
