package com.gmm.bot.model;

import com.gmm.bot.enumeration.GemModifier;

import java.util.*;

public class Const {
    public static final String ID_TOKEN = "ID_TOKEN";

    //user receive command
    public static final String LEAVE_ROOM = "LEAVE_ROOM";
    public static final String START_GAME = "START_GAME";
    public static final String END_GAME = "END_GAME";
    public static final String START_TURN = "START_TURN";
    public static final String END_TURN = "END_TURN";

    public static final String ON_SWAP_GEM = "ON_SWAP_GEM";
    public static final String ON_PLAYER_USE_SKILL = "ON_PLAYER_USE_SKILL";
    public static final String ON_SURRENDER = "ON_SURRENDER";

    public static final String COMMAND_ID = "Battle";
    public static final String BATTLE_MODE = "BATTLE_MODE";

    //user request command
    public static final String SWAP_GEM = "Battle.SWAP_GEM";
    public static final String USE_SKILL = "Battle.USE_SKILL";
    public static final String SURRENDER = "Battle.SURRENDER";
    public static final String FINISH_TURN = "Battle.FINISH_TURN";
    public static final String I_AM_READY = "Battle.I_AM_READY";
    public static final String LOBBY_FIND_GAME = "LOBBY_FIND_GAME";
    public static final String PLAYER_JOINED_GAME = "PLAYER_JOINED_GAME";
    public static final Set<GemModifier> GEM_MODIFIER = new HashSet<>(Arrays.asList(GemModifier.from(8),GemModifier.from(7),GemModifier.from(6),GemModifier.from(5)));
    public static final List<String> BEST_HERO = new ArrayList<>(Arrays.asList("THUNDER_GOD","CERBERUS","AIR_SPIRIT","SEA_GOD","FIRE_SPIRIT"));
}
