package com.jjeanniard.plugins;

import com.hypixel.hytale.logger.HytaleLogger;

import java.util.logging.Level;

import static java.util.logging.Level.*;

public final class Log {
    private static HytaleLogger LOGGER;
    private static boolean IS_AVAILABLE = false;

    static {
        try {
            LOGGER = HytaleLogger.forEnclosingClass();
            IS_AVAILABLE = true;
        } catch (Throwable t) {
            // En cas d'erreur (ex: environnement de test), on désactive le logger Hytale
            System.out.println("[Log] HytaleLogger non disponible. Utilisation de la sortie standard.");
            IS_AVAILABLE = false;
        }
    }

    @Deprecated
    public static void setLog(Level type, String message, Object... args) {
        if (type == INFO) {
            info(message, args);
        } else if (type == WARNING) {
            warning(message, args);
        } else if (type == SEVERE) {
            severe(message, args);
        }
    }

    public static void info(String message) {
        if (IS_AVAILABLE) {
            LOGGER.atInfo().log(message);
        } else {
            System.out.println("[INFO] " + message);
        }
    }

    public static void info(String message, Object... args) {
        if (IS_AVAILABLE) {
            LOGGER.atInfo().log(message, args);
        } else {
            System.out.printf("[INFO] " + message + "%n", args);
        }
    }

    public static void warning(String message) {
        if (IS_AVAILABLE) {
            LOGGER.atWarning().log(message);
        } else {
            System.err.println("[WARNING] " + message);
        }
    }

    public static void warning(String message, Object... args) {
        if (IS_AVAILABLE) {
            LOGGER.atWarning().log(message, args);
        } else {
            System.err.printf("[WARNING] " + message + "%n", args);
        }
    }

    public static void severe(String message) {
        if (IS_AVAILABLE) {
            LOGGER.atSevere().log(message);
        } else {
            System.err.println("[SEVERE] " + message);
        }
    }

    public static void severe(String message, Object... args) {
        if (IS_AVAILABLE) {
            LOGGER.atSevere().log(message, args);
        } else {
            System.err.printf("[SEVERE] " + message + "%n", args);
        }
    }

    public static void debug(String message) {
        if (IS_AVAILABLE) {
            LOGGER.atFine().log(message);
        } else {
            System.out.println("[DEBUG] " + message);
        }
    }

    public static void debug(String message, Object... args) {
        if (IS_AVAILABLE) {
            LOGGER.atFine().log(message, args);
        } else {
            System.out.printf("[DEBUG] " + message + "%n", args);
        }
    }
}
