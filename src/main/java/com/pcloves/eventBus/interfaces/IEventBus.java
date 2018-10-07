package com.pcloves.eventBus.interfaces;

/**
 * 事件总线接口
 *
 */
public interface IEventBus
{
    /**
     * 注册一个事件
     * @param subscriber 事件订阅者
     * @param eventType 事件类型
     * @param eventHandler 事件处理器
     * @param priority 事件处理器的优先级，事件被分发时，优先级遵循两个原则：1、EEventPriority高的优先
     * 分发；2、先注册的优先分发
     * @param <E> 事件类型
     */
    <E extends IEvent> void registerEvent(final Object subscriber, final Class<E> eventType, final IEventHandler<E> eventHandler, final EEventPriority priority);

    /**
     * 反注册某个事件处理器
     * @param eventType 事件类型
     * @param eventHandler 事件处理器
     * @param <E> 事件类型
     */
    <E extends IEvent> void unRegisterEvent(final Class<E> eventType, IEventHandler<E> eventHandler);

	/**
	 * 为某个事件添加一个事件过滤器，{@link IEventBus}会在sendEvent时，首先调用所有的事件过滤器，任何一个过滤器截
	 * 获事件，都会导致事件不会被分发到{@link IEventHandler}上去
	 * @param <E> 事件类型
	 * @param eventType 事件类型
	 * @param eventFilter 事件过滤器
	 */
	<E extends IEvent> void addEventFilter(final Class<E> eventType, final IEventFilter<E> eventFilter);

	/**
	 * 删除一个事件过滤器
	 * @param <E> 事件类型
	 * @param eventType 事件类型
	 * @param eventFilter 事件处理器
	 */
	<E extends IEvent> void removeEventFilter(final Class<E> eventType, IEventFilter<E> eventFilter);

    /**
     * 发送一个事件，该事件会立刻分发到所有已经注册的{@link IEventHandler}
     * @param event 要发送的事件
     * @param <E> 事件类型
     */
    <E extends IEvent> void sendEvent(final E event);

    /**
     * （线程安全）投递一个事件，该事件不会被离开消费，而是被{@link IEventBus}缓存，直到用户手动调用<tt>dispatchEvent<tt/>才
     * 主动分发
     * @param event 要投递的事件
     * @param <E> 事件类型
     */
    <E extends IEvent> void postEvent(final E event);

    /**
     * 分发事件，对所有缓存的时间进行分发
     * @param maxDispatchCount 表示最多从缓存中
     */
    void dispatchCachedEvent(final int maxDispatchCount);
}
