package com.narxoz.rpg.observer;

import java.util.HashSet;
import java.util.Set;

public class AchievementTracker implements GameObserver {

    private final Set<String> unlockedAchievements = new HashSet<>();
    private int attacksLanded = 0;
    private int heroDeaths = 0;

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case ATTACK_LANDED:
                attacksLanded++;
                if (attacksLanded >= 1) {
                    unlock("First Blood");
                }
                if (attacksLanded >= 10) {
                    unlock("Relentless");
                }
                break;

            case HERO_DIED:
                heroDeaths++;
                unlock("Fallen Comrade");
                break;

            case BOSS_DEFEATED:
                unlock("Boss Slayer");
                if (heroDeaths == 0) {
                    unlock("No Man Left Behind");
                }
                break;

            default:
                break;
        }
    }

    private void unlock(String achievementName) {
        if (unlockedAchievements.add(achievementName)) {
            System.out.println("[AchievementTracker] Unlocked: " + achievementName);
        }
    }
}