package com.hexagon.game.util;

/**
 * Created by Sven on 06.03.2018.
 */

public class Usernames {

    private static String[] names = new String[] {
            "HansMeier",
            "MartinSchütz",
            "GeorgGross",
            "RobertRasend",
            "PeterLustig",
            "GustavGans",
            "HansHammer",
            "JosephJosephus",
            "GundulaGause",
            "FriederikeFreak",
            "AngeloMerkel",
            "AlbertEinstein"
    };

    public static String getRandom() {
        return names[(int) (Math.random() * (names.length-1))];
    }

}
