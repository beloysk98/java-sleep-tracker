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

        // Получаем первую дату, которую нужно проверить
        LocalDate firstNightDate = globalStart.toLocalDate();

        // Если сессия началась после 12:00, ночь относится к следующему дню
        if (globalStart.getHour() >= 12) {
            firstNightDate = firstNightDate.plusDays(1);
        }

        long totalNights = ChronoUnit.DAYS.between(firstNightDate, globalEnd.toLocalDate());

        if (totalNights <= 0) {
            return new SleepAnalysisResult("Бессонные ночи", 0L);
        }

        // Создаём final переменную для использования в лямбде
        final List<SleepingSession> finalSessions = sessions;
        final LocalDate finalFirstNightDate = firstNightDate;

        long nightsWithSleep = LongStream.range(0, totalNights)
                .mapToObj(i -> finalFirstNightDate.plusDays(i))
                .filter(nightDate -> {
                    LocalDateTime nightStart = nightDate.atStartOfDay();
                    LocalDateTime nightEnd = nightStart.plusHours(6);
                    return finalSessions.stream().anyMatch(session ->
                            session.getEnd().isAfter(nightStart) && session.getStart().isBefore(nightEnd)
                    );
                })
                .count();

        long sleeplessNights = totalNights - nightsWithSleep;
        return new SleepAnalysisResult("Бессонные ночи", sleeplessNights);
    }
}