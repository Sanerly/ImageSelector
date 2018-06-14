package com.imgselector.observer;


/**
 *@介绍： 被观察者类
 *@作者： sunset
 *@日期： 2018/6/14
 */
public  interface IObservable{

    /**
     * 订阅观察者
     * @param key
     * @param observer
     */
     void addObserver(String key,IObserver observer);


    /**
     * 发送
     * @param key
     * @param obj
     */
    void sendObserver(String key,Object obj);

    /**
     * 删除
     * @param key 传递的key
     */
    void remove(String key);


}
