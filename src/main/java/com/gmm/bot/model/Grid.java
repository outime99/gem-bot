package com.gmm.bot.model;

import com.gmm.bot.enumeration.GemModifier;
import com.gmm.bot.enumeration.GemType;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Getter
@Setter
public class Grid {
    private List<Gem> gems = new ArrayList<>();
    private Set<GemType> gemTypes = new HashSet<>();
    private Set<GemType> myHeroGemType;
    private ISFSArray gemModifiers;
    private int[][] gemss ;

    public Grid(ISFSArray gemsCode,ISFSArray gemModifiers, Set<GemType> heroGemType) {
        updateGems(gemsCode,gemModifiers);
        this.myHeroGemType = heroGemType;
    }

    //gemModifiers gem đặc biệt
    public void updateGems(ISFSArray gemsCode,ISFSArray gemModifiers ) {
        gems.clear();
        gemTypes.clear();
        this.gemModifiers = gemModifiers;
        if(gemModifiers != null){
            for (int i = 0; i < gemsCode.size(); i++) {
                Gem gem = new Gem(i, GemType.from(gemsCode.getByte(i)), GemModifier.from(gemModifiers.getByte(i)));
//                if(Integer.parseInt(gemModifiers.getByte(i).toString())!=0) System.out.println(gemModifiers.getByte(i).toString()+" "+i);
                gems.add(gem);
                gemTypes.add(gem.getType());
            }
        } else {
            for (int i = 0; i < gemsCode.size(); i++) {
                Gem gem = new Gem(i, GemType.from(gemsCode.getByte(i)));
                gems.add(gem);
                gemTypes.add(gem.getType());
            }
        }

    }


    public Pair<Integer> recommendSwapGem(Player botPlayer) {
        myHeroGemType.clear();
//        myHeroGemType.addAll(botPlayer.getRecommendGemType());
//        myHeroGemType.addAll(Arrays.asList(GemType.GREEN,GemType.YELLOW,GemType.BLUE,GemType.BROWN,GemType.RED,GemType.PURPLE,GemType.SWORD));
        myHeroGemType.addAll(Arrays.asList(GemType.BLUE,GemType.BROWN,GemType.GREEN,GemType.YELLOW,GemType.RED,GemType.PURPLE,GemType.SWORD));
//        System.out.println(myHeroGemType.toString());
        List<GemSwapInfo> listMatchGem = suggestMatch();
        if (listMatchGem.isEmpty()) {
            return new Pair<>(-1, -1);
        }
        Optional<GemSwapInfo> matchGemSizeThanFour =
                listMatchGem.stream().filter(gemMatch -> gemMatch.getSizeMatch() > 4).findFirst();
        if (matchGemSizeThanFour.isPresent()) {
            return matchGemSizeThanFour.get().getIndexSwapGem();
        }
        for (GemSwapInfo gemSwapInfo : listMatchGem) {
            List<GemSwapInfo> gemSwapInfos = new ArrayList<>();
            if(checkGemModifier(gemSwapInfo)) {
                gemSwapInfos.add(gemSwapInfo);
            }
            gemSwapInfos.sort(Comparator.comparing(o -> String.valueOf(o.getGemModifier().getCode())));
            if(!gemSwapInfos.isEmpty()){
                return gemSwapInfos.get(gemSwapInfos.size()-1).getIndexSwapGem();
            }
        }
        Optional<GemSwapInfo> matchGemSizeThanThree =
                listMatchGem.stream().filter(gemMatch -> gemMatch.getSizeMatch() > 3 && myHeroGemType.contains(gemMatch.getType())).findFirst();
        if (matchGemSizeThanThree.isPresent()) {
//            System.out.println(matchGemSizeThanThree.get().getType());
//            System.out.println(myHeroGemType);
            return matchGemSizeThanThree.get().getIndexSwapGem();
        }
//        Optional<GemSwapInfo> matchGemSword =
//                listMatchGem.stream().filter(gemMatch -> gemMatch.getType() == GemType.SWORD).findFirst();
//        if (matchGemSword.isPresent()) {
//            return matchGemSword.get().getIndexSwapGem();
//        }

//        for (GemSwapInfo gemSwap: listMatchGem) {
//            for (GemType heroGemType : myHeroGemType) {
//                if ((gemSwap.getSizeMatch() > 3 && heroGemType.equals(gemSwap.getType())) ||
//                        (gemSwap.getSizeMatch() > 3 && Objects.equals(gemSwap.getType(), GemType.SWORD))) {
//                    return gemSwap.getIndexSwapGem();
//                }
//            }
//        }

        for (GemType type : myHeroGemType) {
            Optional<GemSwapInfo> matchGem =
                    listMatchGem.stream().filter(gemMatch -> gemMatch.getType() == type).findFirst();
            if (matchGem.isPresent()) {
                return matchGem.get().getIndexSwapGem();
            }
        }
        return listMatchGem.get(0).getIndexSwapGem();
    }

    private boolean checkGemModifier(GemSwapInfo gemSwapInfo){
        if(gemModifiers !=null){
            System.out.println(Const.GEM_MODIFIER);
            if(Const.GEM_MODIFIER.contains(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex1())))) {
                gemSwapInfo.setGemModifier(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex1())));
                return true;
            }
            else if(gemSwapInfo.getIndex2() <63) {
                if(Const.GEM_MODIFIER.contains(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()+1)))) {
                    gemSwapInfo.setGemModifier(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()+1)));
                    return true;
            }

            }
            else  if(gemSwapInfo.getIndex2() <62 ) {
                if(Const.GEM_MODIFIER.contains(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()+2)))) {
                    gemSwapInfo.setGemModifier(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()+2)));
                    return true;
                }
            }

                else if(gemSwapInfo.getIndex2()>1) {
                if(Const.GEM_MODIFIER.contains(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()-1)))) {
                    gemSwapInfo.setGemModifier(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()-1)));
                    return true;
            }

                }
                else if(gemSwapInfo.getIndex2()>8) {
                if(Const.GEM_MODIFIER.contains(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()-2)))) {
                    gemSwapInfo.setGemModifier(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()-2)));
                    return true;
            }

                }
                else if(gemSwapInfo.getIndex2()>8) {
                if(Const.GEM_MODIFIER.contains(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()-8)))) {
                    gemSwapInfo.setGemModifier(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()-8)));
                    return true;
            }

                }
                else if(gemSwapInfo.getIndex2()>56) {
                if(Const.GEM_MODIFIER.contains(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()+8)))) {
                    gemSwapInfo.setGemModifier(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()+8)));
                    return true;
            }

                }
                else if(gemSwapInfo.getIndex2()>16) {
                if(Const.GEM_MODIFIER.contains(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()-16)))) {
                    gemSwapInfo.setGemModifier(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()-16)));
                    return true;
            }

                }
                else if(gemSwapInfo.getIndex2()<47) {
                if(Const.GEM_MODIFIER.contains(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()+16)))) {
                    gemSwapInfo.setGemModifier(GemModifier.from(gemModifiers.getByte(gemSwapInfo.getIndex2()+16)));
                    return true;
            }

                }


            }
        return false;
    }
//find gem ăn dc
    public List<GemSwapInfo> suggestMatch() {
        List<GemSwapInfo> listMatchGem = new ArrayList<>();
        for (Gem currentGem : gems) {
            Gem swapGem = null;
            // If x > 0 => swap left & check
            if (currentGem.getX() > 0) {
                swapGem = gems.get(getGemIndexAt(currentGem.getX() - 1, currentGem.getY()));
                checkMatchSwapGem(listMatchGem, currentGem, swapGem);
            }
            // If x < 7 => swap right & check
            if (currentGem.getX() < 7) {
                swapGem = gems.get(getGemIndexAt(currentGem.getX() + 1, currentGem.getY()));
                checkMatchSwapGem(listMatchGem, currentGem, swapGem);
            }
            // If y < 7 => swap up & check
            if (currentGem.getY() < 7) {
                swapGem = gems.get(getGemIndexAt(currentGem.getX(), currentGem.getY() + 1));
                checkMatchSwapGem(listMatchGem, currentGem, swapGem);
            }
            // If y > 0 => swap down & check
            if (currentGem.getY() > 0) {
                swapGem = gems.get(getGemIndexAt(currentGem.getX(), currentGem.getY() - 1));
                checkMatchSwapGem(listMatchGem, currentGem, swapGem);
            }
        }
        return listMatchGem;
    }

    private void checkMatchSwapGem(List<GemSwapInfo> listMatchGem, Gem currentGem, Gem swapGem) {
        swap(currentGem, swapGem, gems);
        Set<Gem> matchGems = matchesAt(currentGem.getX(), currentGem.getY());
        swap(currentGem, swapGem, gems);
        if (!matchGems.isEmpty()) {
            listMatchGem.add(new GemSwapInfo(currentGem.getIndex(), swapGem.getIndex(), matchGems.size(), currentGem.getType()));
        }
    }

    private int getGemIndexAt(int x, int y) {
        return x + y * 8;
    }

    private void swap(Gem a, Gem b, List<Gem> gems) {
        int tempIndex = a.getIndex();
        int tempX = a.getX();
        int tempY = a.getY();

        // update reference
        gems.set(a.getIndex(), b);
        gems.set(b.getIndex(), a);

        // update data of element
        a.setIndex(b.getIndex());
        a.setX(b.getX());
        a.setY(b.getY());

        b.setIndex(tempIndex);
        b.setX(tempX);
        b.setY(tempY);
    }

    private Set<Gem> matchesAt(int x, int y) {
        Set<Gem> res = new HashSet<>();
        Gem center = gemAt(x, y);
        if (center == null) {
            return res;
        }

        // check horizontally
        List<Gem> hor = new ArrayList<>();
        hor.add(center);
        int xLeft = x - 1, xRight = x + 1;
        while (xLeft >= 0) {
            Gem gemLeft = gemAt(xLeft, y);
            if (gemLeft != null) {
                if (!gemLeft.sameType(center)) {
                    break;
                }
                hor.add(gemLeft);
            }
            xLeft--;
        }
        while (xRight < 8) {
            Gem gemRight = gemAt(xRight, y);
            if (gemRight != null) {
                if (!gemRight.sameType(center)) {
                    break;
                }
                hor.add(gemRight);
            }
            xRight++;
        }
        if (hor.size() >= 3) res.addAll(hor);

        // check vertically
        List<Gem> ver = new ArrayList<>();
        ver.add(center);
        int yBelow = y - 1, yAbove = y + 1;
        while (yBelow >= 0) {
            Gem gemBelow = gemAt(x, yBelow);
            if (gemBelow != null) {
                if (!gemBelow.sameType(center)) {
                    break;
                }
                ver.add(gemBelow);
            }
            yBelow--;
        }
        while (yAbove < 8) {
            Gem gemAbove = gemAt(x, yAbove);
            if (gemAbove != null) {
                if (!gemAbove.sameType(center)) {
                    break;
                }
                ver.add(gemAbove);
            }
            yAbove++;
        }
        if (ver.size() >= 3) res.addAll(ver);

        return res;
    }

    // Find Gem at Position (x, y)
    private Gem gemAt(int x, int y) {
        for (Gem g : gems) {
            if (g != null && g.getX() == x && g.getY() == y) {
                return g;
            }
        }
        return null;
    }

    private void printArrayGems() {
        int width = 8;
        int height = (gems.size() - 1) / width;
        for (int i = height; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                System.out.print((gems.get(j + i * width).getType().getCode() + "\t"));
            }
            System.out.println();
        }
        System.out.println();
    }
    public int bestGem(){
        List<AirSpirit> airSpirits = new ArrayList<>();
        int z=gems.size()-1;
        gemss = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 7; j >=0; j--) {
                gemss[i][j] = gems.get(z--).getType().getCode();
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(gemss[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("=================");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(i!=0 && i!=7&& j!=0&&j!=7) {
                    int temp=0;
                    AirSpirit airSpirit = new AirSpirit();
                    airSpirit.setX(i);
                    airSpirit.setY(j);
                    airSpirit.setGemType(GemType.from((byte) gemss[i][j]));
                    for (int k = i-1; k <= i+1; k++) {
                        for (int l = j-1; l <= j+1; l++) {
                            if(gemss[k][l]==gemss[i][j]) temp++;
//                            System.out.print(gemss[k][l]+" ");
                        }
//                        System.out.println();
                    }
//                    System.out.println("========");
                    airSpirit.setGem(temp);
//                    System.out.println(airSpirit);
                    airSpirits.add(airSpirit);
                }
            }
//            System.out.println();
        }
        airSpirits.sort((o1, o2) -> {
//            if (o1.getGem().equals(o2.getGem()))
//                return o1.getGemType().compareTo(myHeroGemType.stream().findFirst().get());
            return o2.getGem().compareTo(o1.getGem());
        });
        System.out.println(airSpirits.get(0));
        AirSpirit response = airSpirits.get(0);
        return Math.abs(63-(response.getX()*10+response.getY()));
    }
}
