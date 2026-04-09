package com.narxoz.rpg.strategy;

public class BossPhaseTwoStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return Math.max(1, (int) Math.round(basePower * 1.35));
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return Math.max(0, (int) Math.round(baseDefense * 0.9));
    }

    @Override
    public String getName() {
        return "Boss Phase 2 - Aggressive";
    }
}