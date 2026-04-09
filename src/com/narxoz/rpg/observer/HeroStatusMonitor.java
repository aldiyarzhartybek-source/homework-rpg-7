package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;

import java.util.ArrayList;
import java.util.List;

public class HeroStatusMonitor implements GameObserver {

    private final List<Hero> heroes;

    public HeroStatusMonitor(List<Hero> heroes) {
        this.heroes = new ArrayList<>(heroes);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP
                && event.getType() != GameEventType.HERO_DIED) {
            return;
        }

        System.out.println("[HeroStatusMonitor] Current party status:");
        for (Hero hero : heroes) {
            String status = hero.isAlive() ? "ALIVE" : "DEAD";
            System.out.println("  - " + hero.getName() + ": "
                    + hero.getHp() + "/" + hero.getMaxHp()
                    + " HP [" + status + "]");
        }
    }
}