package com.narxoz.rpg.combatant;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventPublisher;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;
import com.narxoz.rpg.strategy.BossPhaseOneStrategy;
import com.narxoz.rpg.strategy.BossPhaseThreeStrategy;
import com.narxoz.rpg.strategy.BossPhaseTwoStrategy;
import com.narxoz.rpg.strategy.CombatStrategy;

public class DungeonBoss implements GameObserver {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;

    private int currentPhase;
    private CombatStrategy strategy;

    private final CombatStrategy phaseOneStrategy;
    private final CombatStrategy phaseTwoStrategy;
    private final CombatStrategy phaseThreeStrategy;

    private final GameEventPublisher publisher;

    public DungeonBoss(String name, int hp, int attackPower, int defense, GameEventPublisher publisher) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.publisher = publisher;

        this.phaseOneStrategy = new BossPhaseOneStrategy();
        this.phaseTwoStrategy = new BossPhaseTwoStrategy();
        this.phaseThreeStrategy = new BossPhaseThreeStrategy();

        this.currentPhase = 1;
        this.strategy = phaseOneStrategy;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public CombatStrategy getStrategy() {
        return strategy;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int amount) {
        if (amount <= 0 || !isAlive()) {
            return;
        }

        int oldHp = hp;
        hp = Math.max(0, hp - amount);

        checkPhaseThresholds(oldHp, hp);
    }

    private void checkPhaseThresholds(int oldHp, int newHp) {
        double sixtyPercent = maxHp * 0.60;
        double thirtyPercent = maxHp * 0.30;

        if (oldHp > sixtyPercent && newHp <= sixtyPercent) {
            publisher.notifyObservers(new GameEvent(
                    GameEventType.BOSS_PHASE_CHANGED,
                    name,
                    2
            ));
        }

        if (oldHp > thirtyPercent && newHp <= thirtyPercent) {
            publisher.notifyObservers(new GameEvent(
                    GameEventType.BOSS_PHASE_CHANGED,
                    name,
                    3
            ));
        }
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.BOSS_PHASE_CHANGED) {
            return;
        }

        if (!name.equals(event.getSourceName())) {
            return;
        }

        int newPhase = event.getValue();

        if (newPhase == 2 && currentPhase < 2) {
            currentPhase = 2;
            strategy = phaseTwoStrategy;
            System.out.println("[DungeonBoss] " + name
                    + " switches to strategy: " + strategy.getName());
        } else if (newPhase == 3 && currentPhase < 3) {
            currentPhase = 3;
            strategy = phaseThreeStrategy;
            System.out.println("[DungeonBoss] " + name
                    + " switches to strategy: " + strategy.getName());
        }
    }
}