package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

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

        long totalNights = ChronoUnit.DAYS.between(
                globalStart.toLocalDate(), globalEnd.toLocalDate()
        );

        long nightsWithSleep = LongStream.range(0, totalNights)
                .mapToObj(i -> globalStart.toLocalDate().plusDays(i))
                .filter(date -> {
                    LocalDateTime nightStart = date.atStartOfDay();
                    LocalDateTime nightEnd = nightStart.plusHours(6);
                    return sessions.stream().anyMatch(session ->
                            session.getEnd().isAfter(nightStart) && session.getStart().isBefore(nightEnd)
                    );
                })
                .count();

        long sleeplessNights = totalNights - nightsWithSleep;
        return new SleepAnalysisResult("Бессонные ночи", sleeplessNights);
    }
}