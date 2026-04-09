package com.narxoz.rpg.observer;

public class BattleLogger implements GameObserver {

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case ATTACK_LANDED:
                System.out.println("[BattleLogger] " + event.getSourceName()
                        + " landed an attack for " + event.getValue() + " damage.");
                break;

            case HERO_LOW_HP:
                System.out.println("[BattleLogger] " + event.getSourceName()
                        + " is in critical condition! HP: " + event.getValue());
                break;

            case HERO_DIED:
                System.out.println("[BattleLogger] " + event.getSourceName()
                        + " has fallen.");
                break;

            case BOSS_PHASE_CHANGED:
                System.out.println("[BattleLogger] " + event.getSourceName()
                        + " changed to phase " + event.getValue() + ".");
                break;

            case BOSS_DEFEATED:
                System.out.println("[BattleLogger] " + event.getSourceName()
                        + " was defeated after " + event.getValue() + " rounds.");
                break;

            default:
                break;
        }
    }
}