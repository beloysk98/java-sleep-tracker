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

        // Начинаем проверку с даты первой сессии
        LocalDate startDate = globalStart.toLocalDate();
        LocalDate endDate = globalEnd.toLocalDate();

        long totalNights = ChronoUnit.DAYS.between(startDate, endDate);

        long nightsWithSleep = LongStream.range(0, totalNights)
                .mapToObj(i -> startDate.plusDays(i))
                .filter(nightDate -> hasSleepOnNight(nightDate, sessions))
                .count();

        long sleeplessNights = totalNights - nightsWithSleep;
        return new SleepAnalysisResult("Бессонные ночи", sleeplessNights);
    }

    private boolean hasSleepOnNight(LocalDate nightDate, List<SleepingSession> sessions) {
        LocalDateTime nightStart = nightDate.atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);

        return sessions.stream().anyMatch(session ->
                isSessionOverlapsNight(session, nightStart, nightEnd)
        );
    }

    private boolean isSessionOverlapsNight(SleepingSession session, LocalDateTime nightStart, LocalDateTime nightEnd) {
        LocalDateTime sessionStart = session.getStart();
        LocalDateTime sessionEnd = session.getEnd();

        boolean startsBeforeNightEnd = sessionStart.isBefore(nightEnd);
        boolean endsAfterNightStart = sessionEnd.isAfter(nightStart);

        return startsBeforeNightEnd && endsAfterNightStart;
    }
}