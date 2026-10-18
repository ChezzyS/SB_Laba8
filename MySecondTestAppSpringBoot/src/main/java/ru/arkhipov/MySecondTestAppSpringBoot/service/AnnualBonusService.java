package ru.arkhipov.MySecondTestAppSpringBoot.service;

import ru.arkhipov.MySecondTestAppSpringBoot.model.Positions;

import java.time.Year;

public interface AnnualBonusService {

    /**
     * Годовая премия. Количество дней в году зависит от того, високосный год или нет.
     *
     * @param year год, за который считается премия
     */
    double calculate(Positions positions, double salary, double bonus, int workDays, int year);

    /**
     * Годовая премия за текущий год.
     */
    default double calculate(Positions positions, double salary, double bonus, int workDays) {
        return calculate(positions, salary, bonus, workDays, Year.now().getValue());
    }

    /**
     * Квартальная премия. Считается только для управленцев (isManager == true),
     * для остальных должностей выбрасывает IllegalArgumentException.
     */
    double calculateQuarterlyBonus(Positions positions, double salary, double bonus, int workDays, int year);

    /**
     * Квартальная премия за текущий год.
     */
    default double calculateQuarterlyBonus(Positions positions, double salary, double bonus, int workDays) {
        return calculateQuarterlyBonus(positions, salary, bonus, workDays, Year.now().getValue());
    }
}
