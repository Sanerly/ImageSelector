package com.imgselector.observer;


import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * @介绍： 被观察者管理类
 * @作者： sunset
 * @日期： 2018/6/14
 */
public class ObserverManager implements IObservable {

    private static ObserverManager instance;
    private Map<String, IObserver> iObservers = new HashMap<>();

    public static ObserverManager getInstance() {
        if (instance == null) {
            synchronized (ObserverManager.class) {
                if (instance == null) {
                    instance = new ObserverManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void addObserver(String key, IObserver observer) {
        if (iObservers == null) return;
        iObservers.put(key, observer);
    }

    @Override
    public void sendObserver(String key, Object obj) {
        if (iObservers == null || iObservers.size() <= 0) return;
        for (Map.Entry<String, IObserver> entry : iObservers.entrySet()) {
            if (entry.getKey().equals(key)) {
                entry.getValue().ObserverUpdate(key, obj);
            }
        }
    }

    @Override
    public void remove(String key) {
        if (iObservers == null || iObservers.size() <= 0) return;
        if (iObservers.containsKey(key)) {
            iObservers.remove(key);
        }
    }

    /**
     * 清空订阅的观察者
     */
    public void clear() {
        if (iObservers == null || iObservers.size() <= 0) return;
        iObservers.clear();
        iObservers = null;
    }
}
