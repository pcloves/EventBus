package com.pcloves.eventBus.interfaces;

import java.io.Serializable;

@FunctionalInterface
public interface IEventHandler<E extends IEvent> extends Serializable
{
    void handler(Object subscriber, E event);
}
