package ru.arkhipov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Positions;

import java.time.Year;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays, int year) {
        // 366 дней в високосном году, 365 в остальных
        int daysInYear = Year.of(year).length();
        return salary * bonus * daysInYear * positions.getPositionCoefficient() / workDays;
    }

    @Override
    public double calculateQuarterlyBonus(Positions positions, double salary, double bonus, int workDays, int year) {
        if (!positions.isManager()) {
            throw new IllegalArgumentException(
                    "Квартальная премия рассчитывается только для управленцев, должность " + positions + " не является управленческой");
        }
        // Квартальная премия - четверть годовой
        return calculate(positions, salary, bonus, workDays, year) / 4;
    }
}
