package com.pcloves.eventBus.interfaces;

import java.io.Serializable;

@FunctionalInterface
public interface IEventHandler<E extends IEvent> extends Serializable
{
	/**
	 * 事件处理
	 * @param event 事件
	 */
    void handler(E event);
}
