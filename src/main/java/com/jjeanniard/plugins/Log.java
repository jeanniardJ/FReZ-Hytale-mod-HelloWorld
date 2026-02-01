package com.jjeanniard.plugins;

import com.hypixel.hytale.logger.HytaleLogger;

import java.util.logging.Level;

import static java.util.logging.Level.*;

public final class Log {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Deprecated
    public static void setLog(Level type, String message, Object... args) {
        if (type == INFO) {
            if (args.length == 0) {
                LOGGER.atInfo().log(message);
                return;
            }
            LOGGER.atInfo().log(message, args);
        } else if (type == WARNING) {
            if (args.length == 0) {
                LOGGER.atWarning().log(message);
                return;
            }
            LOGGER.atWarning().log(message, args);
        } else if (type == SEVERE) {
            if (args.length == 0) {
                LOGGER.atSevere().log(message);
                return;
            }
            LOGGER.atSevere().log(message, args);
        }
    }

    public static void info(String message) {
        LOGGER.atInfo().log(message);
    }

    public static void info(String message, Object... args) {
        LOGGER.atInfo().log(message, args);
    }

    public static void warning(String message) {
        LOGGER.atWarning().log(message);
    }

    public static void warning(String message, Object... args) {
        LOGGER.atWarning().log(message, args);
    }

    public static void severe(String message) {
        LOGGER.atSevere().log(message);
    }

    public static void severe(String message, Object... args) {
        LOGGER.atSevere().log(message, args);
    }

    public static void debug(String message) {
        LOGGER.atFine().log(message);
    }

    public static void debug(String message, Object... args) {
        LOGGER.atFine().log(message, args);
    }

}
