package com.narxoz.rpg.strategy;

public class DefensiveStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return Math.max(1, (int) Math.round(basePower * 0.75));
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return Math.max(0, (int) Math.round(baseDefense * 1.5));
    }

    @Override
    public String getName() {
        return "Defensive";
    }
}