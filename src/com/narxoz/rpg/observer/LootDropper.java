package com.narxoz.rpg.observer;

import java.util.Random;

public class LootDropper implements GameObserver {

    private final Random random;

    private final String[] phaseLoot = {
            "Shadow Crystal",
            "Ancient Bone Charm",
            "Cursed Coin",
            "Darksteel Fragment"
    };

    private final String[] finalLoot = {
            "Boss Crown",
            "Legendary Relic",
            "Cursed Dungeon Key",
            "Mythic Soul Gem"
    };

    public LootDropper() {
        this(99L);
    }

    public LootDropper(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED) {
            String loot = phaseLoot[random.nextInt(phaseLoot.length)];
            System.out.println("[LootDropper] Phase reward dropped: " + loot);
        }

        if (event.getType() == GameEventType.BOSS_DEFEATED) {
            String loot = finalLoot[random.nextInt(finalLoot.length)];
            System.out.println("[LootDropper] Final boss loot dropped: " + loot);
        }
    }
}