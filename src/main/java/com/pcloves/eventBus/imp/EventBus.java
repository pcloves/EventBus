package com.pcloves.eventBus.imp;

import com.pcloves.eventBus.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventBus implements IEventBus
{
    private static long index = 0;
    private static Logger log = LoggerFactory.getLogger(EventBus.class);

    private final Map<Class, List<EventHandlerData>> eventType2HandlerDataListMap = new HashMap<>(512);
    private final Map<IEventHandler, EventHandlerData> eventHandler2HandlerDataMap = new HashMap<>(2048);
    private final Queue<IEvent> cachedEventQueue = new ConcurrentLinkedQueue<>();
	private final Map<Class, Set<IEventFilter>> eventType2EventFilterSetMap = new HashMap<>(512);

    private static class EventHandlerData<E extends IEvent>
    {
        private final long priority;
        private final IEventHandler<E> handler;

        private EventHandlerData(final EEventPriority priority, final IEventHandler<E> handler) {
            this.priority = ((long) priority.ordinal() << 60) + (index++);
            this.handler = handler;
        }
    }

    @Override public <E extends IEvent> void registerEvent(final Class<E> eventType,
														   final IEventHandler<E> eventHandler,
														   final EEventPriority priority)
    {
		final int modifiers = eventHandler.getClass().getModifiers();
		boolean isFinal = Modifier.isFinal(modifiers);
		if(!isFinal) return;

		if(eventHandler2HandlerDataMap.containsKey(eventHandler)) return;

        final List<EventHandlerData> handlerDataList = eventType2HandlerDataListMap.computeIfAbsent(eventType, k -> new LinkedList<>());
        final EventHandlerData<E> handlerData = new EventHandlerData<>(priority, eventHandler);

        handlerDataList.add(handlerData);
        handlerDataList.sort(Comparator.comparing(o -> o.priority));
        eventHandler2HandlerDataMap.put(eventHandler, handlerData);
    }

    @Override public <E extends IEvent> void unRegisterEvent(final Class<E> eventType, final IEventHandler<E> eventHandler)
    {
		final Class<? extends IEventHandler> handlerClass = eventHandler.getClass();
		final int modifiers = handlerClass.getModifiers();
		boolean isFinal = Modifier.isFinal(modifiers);
		if(!isFinal) return;

        final EventHandlerData handlerData = eventHandler2HandlerDataMap.get(eventHandler);
        if(handlerData == null) return;
        eventHandler2HandlerDataMap.remove(eventHandler);

        final List<EventHandlerData> handlerDataList = eventType2HandlerDataListMap.get(eventType);
        if(handlerDataList == null) return;
        handlerDataList.remove(handlerData);
    }

	@Override public <E extends IEvent> void addEventFilter(final Class<E> eventType, final IEventFilter<E> eventFilter)
	{
		Set<IEventFilter> eventFilterSet = eventType2EventFilterSetMap.computeIfAbsent(eventType, k -> new HashSet<>(10));
		eventFilterSet.add(eventFilter);
	}

	@Override public <E extends IEvent> void removeEventFilter(final Class<E> eventType, final IEventFilter<E> eventFilter)
	{
		Set<IEventFilter> eventFilterSet = eventType2EventFilterSetMap.get(eventType);
		if(eventFilterSet == null) return;

		eventFilterSet.remove(eventFilter);
	}

	@Override public <E extends IEvent> void sendEvent(final E event)
    {
        final Class<? extends IEvent> eventType = event.getClass();
		final List<EventHandlerData> handlerDataList = eventType2HandlerDataListMap.get(eventType);
        if(handlerDataList == null) return;

        for(EventHandlerData handlerData : handlerDataList)
        {
            try
            {
                final IEventHandler<E> handler = handlerData.handler;

				boolean isFilter = false;
				final Set<IEventFilter> eventFilterSet = eventType2EventFilterSetMap.get(eventType);
				if(eventFilterSet != null)
				{
					for(IEventFilter eventFilter : eventFilterSet)
					{
						if(!eventFilter.filter(event)){
							isFilter = true;
							log.info("event is filtered, event:{}", event.getClass().getSimpleName());
							break;
						}
					}
				}


                if(!isFilter)
					handler.handler(event);
            }
            catch (Exception e){
				log.error("exception caught.", e);
            }
        }
    }

    @Override public <E extends IEvent> void postEvent(final E event)
    {
		cachedEventQueue.add(event);
    }

	@Override public void dispatchCachedEvent(final int maxDispatchCount)
	{
		int dispatchCount = Math.min(cachedEventQueue.size(), maxDispatchCount);
		while (dispatchCount > 0)
		{
			dispatchCount--;

			final IEvent event = cachedEventQueue.poll();
			sendEvent(event);
		}
	}
}
