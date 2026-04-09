package com.narxoz.rpg.strategy;

public class BossPhaseThreeStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return Math.max(1, (int) Math.round(basePower * 1.7));
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return 0;
    }

    @Override
    public String getName() {
        return "Boss Phase 3 - Desperate";
    }
}