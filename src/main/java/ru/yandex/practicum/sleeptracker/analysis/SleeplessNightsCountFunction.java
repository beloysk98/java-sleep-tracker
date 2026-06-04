package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Бессонные ночи", 0L);
        }

        LocalDateTime globalStart = sessions.get(0).getStart();
        LocalDateTime globalEnd = sessions.get(sessions.size() - 1).getEnd();

        LocalDate startDate = globalStart.toLocalDate();
        LocalDate endDate = globalEnd.toLocalDate();

        LocalDate firstNightDate = startDate;
        if (globalStart.getHour() >= 12) {
            firstNightDate = startDate.plusDays(1);
        }

        LocalDate lastNightDate = endDate;
        if (globalEnd.getHour() < 6) {
            lastNightDate = endDate.minusDays(1);
        }

        long totalNights = ChronoUnit.DAYS.between(firstNightDate, lastNightDate) + 1;

        if (totalNights <= 0) {
            return new SleepAnalysisResult("Бессонные ночи", 0L);
        }

        // Создаем final переменную для использования в лямбде
        final List<SleepingSession> finalSessions = sessions;
        final LocalDate finalFirstNightDate = firstNightDate;

        long nightsWithSleep = LongStream.range(0, totalNights)
                .mapToObj(i -> finalFirstNightDate.plusDays(i))
                .map(nightDate -> checkNightSleep(nightDate, finalSessions))
                .filter(hasSleep -> hasSleep)
                .count();

        long sleeplessNights = totalNights - nightsWithSleep;
        return new SleepAnalysisResult("Бессонные ночи", sleeplessNights);
    }

    private boolean checkNightSleep(LocalDate nightDate, List<SleepingSession> sessions) {
        LocalDateTime nightStart = nightDate.atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);

        return sessions.stream().anyMatch(session ->
                session.getEnd().isAfter(nightStart) && session.getStart().isBefore(nightEnd)
        );
    }
}