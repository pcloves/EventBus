package com.pcloves.eventBus.interfaces;

@FunctionalInterface
public interface IEventFilter<E extends IEvent>
{
    /**
     * 事件过滤
     * @param event 事件
     * @return 返回true表示事件被过滤，否则返回false
     */
	boolean filter(E event);
}
