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
	private EventHandler1 eventHandler1;

	@BeforeEach void setUp()
	{
		eventBus = new EventBus();
		eventHandler = new EventHandler();
		eventHandler1 = new EventHandler1();
	}

	@Test void  testCallCount()
	{
		eventBus.registerEvent(eventHandler, Event.class, EventHandler.handleEvent, EEventPriority.Normal);

		for (int i = 0; i < LoopCount; i++)
		{
			eventBus.sendEvent(new Event(1));
		}

		Assertions.assertEquals(LoopCount, eventHandler.eventCallCount);
		Assertions.assertEquals(0, eventHandler.event1CallCount);
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

		Assertions.assertEquals(0, eventHandler.eventCallCount);
		Assertions.assertEquals(0, eventHandler.event1CallCount);
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

		Assertions.assertEquals(0, eventHandler.eventCallCount);
		Assertions.assertEquals(LoopCount, eventHandler.event1CallCount);
	}

	@Test void  testPostEvent()
	{
		eventBus.registerEvent(eventHandler, Event.class, EventHandler.handleEvent, EEventPriority.Normal);

		int loopCount = new Random(System.currentTimeMillis()).nextInt(2 * LoopCount);
		for (int i = 0; i < loopCount; i++)
		{
			eventBus.postEvent(new Event(1));
		}

		Assertions.assertEquals(0, eventHandler.eventCallCount);

		eventBus.dispatchCachedEvent(50000);
		Assertions.assertEquals(Math.min(loopCount, 50000), eventHandler.eventCallCount);
	}

	@Test void  testEventPriority()
	{
		eventBus.registerEvent(eventHandler, Event.class, EventHandler.handleEvent, EEventPriority.Normal);
		eventBus.registerEvent(eventHandler1, Event.class, EventHandler1.handleEvent, EEventPriority.High);

		final Event event = new Event(100);
		eventBus.sendEvent(event);

		Assertions.assertEquals(eventHandler1.eventParam, 100);
		Assertions.assertEquals(eventHandler.eventParam, 101);
		Assertions.assertEquals(event.param, 102);
	}
}
