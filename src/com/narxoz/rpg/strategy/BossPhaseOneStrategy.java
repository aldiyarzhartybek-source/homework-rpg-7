package com.narxoz.rpg.strategy;

public class BossPhaseOneStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return Math.max(1, (int) Math.round(basePower * 1.0));
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return Math.max(0, (int) Math.round(baseDefense * 1.2));
    }

    @Override
    public String getName() {
        return "Boss Phase 1 - Measured";
    }
}