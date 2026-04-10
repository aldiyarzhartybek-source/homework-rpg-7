package com.narxoz.rpg.engine;

import com.narxoz.rpg.combatant.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventPublisher;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.strategy.CombatStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DungeonEngine {

    private final List<Hero> heroes;
    private final DungeonBoss boss;
    private final GameEventPublisher publisher;
    private final int maxRounds;

    private final Set<String> lowHpTriggeredHeroes = new HashSet<>();

    private Hero scheduledSwitchHero;
    private int scheduledSwitchRound = -1;
    private CombatStrategy scheduledSwitchStrategy;

    public DungeonEngine(List<Hero> heroes, DungeonBoss boss, GameEventPublisher publisher) {
        this(heroes, boss, publisher, 50);
    }

    public DungeonEngine(List<Hero> heroes, DungeonBoss boss, GameEventPublisher publisher, int maxRounds) {
        this.heroes = new ArrayList<>(heroes);
        this.boss = boss;
        this.publisher = publisher;
        this.maxRounds = maxRounds;
    }

    public DungeonEngine scheduleHeroStrategySwitch(Hero hero, int round, CombatStrategy strategy) {
        this.scheduledSwitchHero = hero;
        this.scheduledSwitchRound = round;
        this.scheduledSwitchStrategy = strategy;
        return this;
    }

    public EncounterResult runEncounter() {
        int round = 0;

        while (boss.isAlive() && hasLivingHeroes() && round < maxRounds) {
            round++;
            System.out.println("\n=== Round " + round + " ===");

            applyScheduledStrategySwitch(round);

            for (Hero hero : heroes) {
                if (!hero.isAlive() || !boss.isAlive()) {
                    continue;
                }

                heroAttackBoss(hero);

                if (!boss.isAlive()) {
                    publisher.notifyObservers(new GameEvent(
                            GameEventType.BOSS_DEFEATED,
                            boss.getName(),
                            round
                    ));
                    break;
                }
            }

            if (!boss.isAlive()) {
                break;
            }

            for (Hero hero : heroes) {
                if (!hero.isAlive()) {
                    continue;
                }

                bossAttackHero(hero);
            }
        }

        boolean heroesWon = !boss.isAlive() && hasLivingHeroes();
        int survivingHeroes = countLivingHeroes();

        return new EncounterResult(heroesWon, round, survivingHeroes);
    }

    private void applyScheduledStrategySwitch(int currentRound) {
        if (scheduledSwitchHero == null || scheduledSwitchStrategy == null) {
            return;
        }

        if (scheduledSwitchRound == currentRound && scheduledSwitchHero.isAlive()) {
            scheduledSwitchHero.setStrategy(scheduledSwitchStrategy);
            System.out.println("[DungeonEngine] " + scheduledSwitchHero.getName()
                    + " switches strategy to " + scheduledSwitchStrategy.getName() + ".");
        }
    }

    private void heroAttackBoss(Hero hero) {
        int attackValue = hero.getStrategy().calculateDamage(hero.getAttackPower());
        int bossDefense = boss.getStrategy().calculateDefense(boss.getDefense());
        int damage = Math.max(1, attackValue - bossDefense);

        System.out.println(hero.getName() + " attacks " + boss.getName()
                + " using " + hero.getStrategy().getName()
                + " for " + damage + " damage.");

        boss.takeDamage(damage);

        publisher.notifyObservers(new GameEvent(
                GameEventType.ATTACK_LANDED,
                hero.getName(),
                damage
        ));
    }

    private void bossAttackHero(Hero hero) {
        int attackValue = boss.getStrategy().calculateDamage(boss.getAttackPower());
        int heroDefense = hero.getStrategy().calculateDefense(hero.getDefense());
        int damage = Math.max(1, attackValue - heroDefense);

        System.out.println(boss.getName() + " attacks " + hero.getName()
                + " using " + boss.getStrategy().getName()
                + " for " + damage + " damage.");

        hero.takeDamage(damage);

        publisher.notifyObservers(new GameEvent(
                GameEventType.ATTACK_LANDED,
                boss.getName(),
                damage
        ));

        if (hero.isAlive() && isLowHp(hero) && !lowHpTriggeredHeroes.contains(hero.getName())) {
            lowHpTriggeredHeroes.add(hero.getName());
            publisher.notifyObservers(new GameEvent(
                    GameEventType.HERO_LOW_HP,
                    hero.getName(),
                    hero.getHp()
            ));
        }

        if (!hero.isAlive()) {
            publisher.notifyObservers(new GameEvent(
                    GameEventType.HERO_DIED,
                    hero.getName(),
                    0
            ));
        }
    }

    private boolean isLowHp(Hero hero) {
        return hero.getHp() < hero.getMaxHp() * 0.30;
    }

    private boolean hasLivingHeroes() {
        return countLivingHeroes() > 0;
    }

    private int countLivingHeroes() {
        int count = 0;
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                count++;
            }
        }
        return count;
    }
}