package me.uwunixx.radiationrp.suits;

public enum SuitLevel {
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4),
    LEVEL_5(5);

    private final int level;

    SuitLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static SuitLevel fromInt(int i) {
        for (SuitLevel s : values()) {
            if (s.level == i) return s;
        }
        return null;
    }
}
