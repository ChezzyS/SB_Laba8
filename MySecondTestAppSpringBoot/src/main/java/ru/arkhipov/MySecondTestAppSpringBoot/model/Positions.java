package ru.arkhipov.MySecondTestAppSpringBoot.model;

/**
 * Должности сотрудников.
 * positionCoefficient - коэффициент должности для расчёта премии,
 * isManager - признак управленца (true для менеджеров и иных управленцев).
 */
public enum Positions {


    DEV(2.2, false),

    HR(1.2, false),
    // Manager
    TL(2.6, true),

    PO(2.4, true),

    TPM(2.8, true),

    CTO(3.2, true);

    private final double positionCoefficient;
    private final boolean isManager;

    Positions(double positionCoefficient, boolean isManager) {
        this.positionCoefficient = positionCoefficient;
        this.isManager = isManager;
    }

    public double getPositionCoefficient() {
        return positionCoefficient;
    }

    public boolean isManager() {
        return isManager;
    }
}
