package com.narxoz.rpg.observer;

import java.util.ArrayList;
import java.util.List;

public class GameEventPublisher {

    private final List<GameObserver> observers = new ArrayList<>();

    public void registerObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(GameEvent event) {
        if (event == null) {
            return;
        }

        List<GameObserver> snapshot = new ArrayList<>(observers);
        for (GameObserver observer : snapshot) {
            observer.onEvent(event);
        }
    }
}