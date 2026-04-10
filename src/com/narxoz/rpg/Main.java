package com.narxoz.rpg;

import com.narxoz.rpg.combatant.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.engine.DungeonEngine;
import com.narxoz.rpg.engine.EncounterResult;
import com.narxoz.rpg.observer.AchievementTracker;
import com.narxoz.rpg.observer.BattleLogger;
import com.narxoz.rpg.observer.GameEventPublisher;
import com.narxoz.rpg.observer.HeroStatusMonitor;
import com.narxoz.rpg.observer.LootDropper;
import com.narxoz.rpg.observer.PartySupport;
import com.narxoz.rpg.strategy.AggressiveStrategy;
import com.narxoz.rpg.strategy.BalancedStrategy;
import com.narxoz.rpg.strategy.DefensiveStrategy;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Homework 7: The Cursed Dungeon ===\n");

        Hero warrior = new Hero("Warrior", 140, 24, 10, new BalancedStrategy());
        Hero rogue = new Hero("Rogue", 105, 20, 9, new AggressiveStrategy());
        Hero guardian = new Hero("Guardian", 160, 18, 12, new DefensiveStrategy());

        List<Hero> heroes = Arrays.asList(warrior, rogue, guardian);

        GameEventPublisher publisher = new GameEventPublisher();

        DungeonBoss boss = new DungeonBoss("The Cursed Warden", 260, 20, 10, publisher);

        BattleLogger battleLogger = new BattleLogger();
        AchievementTracker achievementTracker = new AchievementTracker();
        PartySupport partySupport = new PartySupport(heroes, 18);
        HeroStatusMonitor heroStatusMonitor = new HeroStatusMonitor(heroes);
        LootDropper lootDropper = new LootDropper();

        publisher.registerObserver(battleLogger);
        publisher.registerObserver(achievementTracker);
        publisher.registerObserver(partySupport);
        publisher.registerObserver(heroStatusMonitor);
        publisher.registerObserver(lootDropper);
        publisher.registerObserver(boss);

        System.out.println("Heroes:");
        for (Hero hero : heroes) {
            System.out.println(" - " + hero.getName()
                    + " | HP: " + hero.getHp()
                    + " | ATK: " + hero.getAttackPower()
                    + " | DEF: " + hero.getDefense()
                    + " | Strategy: " + hero.getStrategy().getName());
        }

        System.out.println("\nBoss:");
        System.out.println(" - " + boss.getName()
                + " | HP: " + boss.getHp()
                + " | ATK: " + boss.getAttackPower()
                + " | DEF: " + boss.getDefense()
                + " | Strategy: " + boss.getStrategy().getName());

        System.out.println("\nA planned mid-battle strategy switch will happen on round 4.");
        System.out.println("Guardian will switch from Defensive to Aggressive.\n");

        DungeonEngine engine = new DungeonEngine(heroes, boss, publisher, 12)
                .scheduleHeroStrategySwitch(guardian, 4, new AggressiveStrategy());

        EncounterResult result = engine.runEncounter();

        System.out.println("\n=== Encounter Result ===");
        System.out.println("Heroes won      : " + result.isHeroesWon());
        System.out.println("Rounds played   : " + result.getRoundsPlayed());
        System.out.println("Surviving heroes: " + result.getSurvivingHeroes());

        System.out.println("\nFinal hero states:");
        for (Hero hero : heroes) {
            System.out.println(" - " + hero.getName()
                    + ": " + hero.getHp() + "/" + hero.getMaxHp()
                    + " HP | Strategy: " + hero.getStrategy().getName()
                    + " | Alive: " + hero.isAlive());
        }

        System.out.println("\nBoss final state:");
        System.out.println(" - " + boss.getName()
                + ": " + boss.getHp() + "/" + boss.getMaxHp()
                + " HP | Phase: " + boss.getCurrentPhase()
                + " | Strategy: " + boss.getStrategy().getName()
                + " | Alive: " + boss.isAlive());

        System.out.println("\n=== Demo Complete ===");
    }
}