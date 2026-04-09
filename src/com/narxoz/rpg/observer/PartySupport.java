package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PartySupport implements GameObserver {

    private final List<Hero> heroes;
    private final int healAmount;
    private final Random random;

    public PartySupport(List<Hero> heroes, int healAmount) {
        this(heroes, healAmount, 42L);
    }

    public PartySupport(List<Hero> heroes, int healAmount, long seed) {
        this.heroes = new ArrayList<>(heroes);
        this.healAmount = healAmount;
        this.random = new Random(seed);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP) {
            return;
        }

        List<Hero> livingAllies = new ArrayList<>();
        for (Hero hero : heroes) {
            if (hero.isAlive() && !hero.getName().equals(event.getSourceName())) {
                livingAllies.add(hero);
            }
        }

        if (livingAllies.isEmpty()) {
            return;
        }

        Hero target = livingAllies.get(random.nextInt(livingAllies.size()));
        int beforeHeal = target.getHp();
        target.heal(healAmount);
        int healedAmount = target.getHp() - beforeHeal;

        if (healedAmount > 0) {
            System.out.println("[PartySupport] " + target.getName()
                    + " receives support healing for " + healedAmount + " HP.");
        }
    }
}