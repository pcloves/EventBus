package com.pcloves.eventBus.interfaces;

import java.io.Serializable;

@FunctionalInterface
public interface IEventHandler<T, E extends IEvent> extends Serializable
{
	/**
	 * 事件处理
	 * @param subscriber 事件订阅者
	 * @param event 事件
	 */
    void handler(T subscriber, E event);
}
