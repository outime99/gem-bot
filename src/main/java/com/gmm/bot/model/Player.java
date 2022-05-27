package com.gmm.bot.model;

import com.gmm.bot.enumeration.GemType;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
public class Player {
    private int id;
    private String displayName;
    private List<Hero> heroes;
    private Set<GemType> heroGemType;

    public Player(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
        heroes = new ArrayList<>();
        heroGemType = new LinkedHashSet<>();
    }

    public Optional<Hero> anyHeroFullMana() {
        return heroes.stream().filter(hero -> hero.isAlive() && hero.isFullMana()).findFirst();
    }

    public Hero firstHeroenEmyPlayerAlive() {
//        return heroes.stream().filter(Hero::isAlive).findFirst().orElse(null);
        List<Hero> herolive = heroes.stream().filter(Hero::isAlive).collect(Collectors.toList());
        herolive.forEach(hero -> System.out.println(hero.getName()));
        return herolive.get(0);
    }

    public Hero firstHeroAlive() {
//        return heroes.stream().filter(Hero::isAlive).findFirst().orElse(null);
        heroes.forEach(hero -> {
            if(hero.isAlive()) System.out.println(hero.getName());
        });
        List<Hero> herolive = heroes.stream().filter(Hero::isAlive).collect(Collectors.toList());
        herolive.forEach(hero -> System.out.println(hero.getName()));
        return herolive.get(herolive.size()-1);
    }

    public String bestHeroEnemy(List<Hero> heros) {
        for (int i=0;i<heros.size();i++){
            if(heros.get(i).isAlive() && Const.BEST_HERO.contains(heros.get(i).getName())) return heros.get(i).getName();
        }
        return heros.get(heros.size()-1).getName();
    }

    public Set<GemType> getRecommendGemType() {
        List<GemType> response = new ArrayList<>();
        heroGemType.clear();
        heroGemType.addAll(Arrays.asList(GemType.PURPLE,GemType.RED,GemType.BLUE,GemType.BROWN,GemType.GREEN,GemType.YELLOW,GemType.SWORD));
        Set<GemType> gemTypeHeroLive = new HashSet<>();
        heroes.forEach(hero -> {
            if(hero.isAlive()) {
                gemTypeHeroLive.addAll(hero.getGemTypes());
            }
        });
//        heroes.stream().filter(Hero::isAlive).forEach(hero -> gemTypeHeroLive.addAll(hero.getGemTypes()));
        List<GemType> listAllGem = new ArrayList<>();
        listAllGem.addAll(heroGemType);
        listAllGem.forEach(gemType -> {
            if(gemTypeHeroLive.contains(gemType)) response.add(gemType);
        });
        response.add(GemType.SWORD);
//        heroGemType.add(GemType.SWORD);
//        List<GemType> gemTypes = new ArrayList<>(heroGemType);
//        heroGemType.clear();
//        heroGemType.addAll(gemTypes);
        System.out.println(response);
        heroGemType.clear();
        heroGemType.addAll(response);
        return heroGemType;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }
}
