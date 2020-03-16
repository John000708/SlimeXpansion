package me.john000708.slimexpansion.utils;

import me.john000708.slimexpansion.SlimeXpansion;

import java.util.logging.Level;
import java.util.logging.Logger;

public class XpansionLogger {
    private static Logger logger = Logger.getLogger(SlimeXpansion.class.getCanonicalName());

    //Private constructor to hide implicit public constructor
    private XpansionLogger() {}

    public static void log(Level level, String message, Object... args) {
        logger.log(level, String.format("SlimeXpansion %s", message), args);
    }

    public static void log(String message, Throwable error) {
        logger.log(Level.SEVERE, String.format("SlimeXpansion %s", message), error);
    }
}
