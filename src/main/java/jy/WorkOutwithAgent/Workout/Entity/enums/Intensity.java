package jy.WorkOutwithAgent.Workout.Entity.enums;

import jy.WorkOutwithAgent.GlobalErrorHandler.GlobalException;

public enum Intensity {
    LEVEL_0(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4),
    LEVEL_5(5),
    LEVEL_6(6),
    LEVEL_7(7),
    LEVEL_8(8),
    LEVEL_9(9),
    LEVEL_10(10);

    private final int value;

    Intensity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // 숫자로부터 Enum 찾기
    public static Intensity fromValue(int value) {
        for (Intensity i : values()) {
            if (i.value == value) return i;
        }
        throw new GlobalException("Invalid intensity value","INVALID_INTENSITY_VALUE_ERROR");
    }
}
