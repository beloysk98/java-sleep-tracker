package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction {

    private enum Chronotype { OWL, LARK, DOVE }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> chronotypeCounts = sessions.stream()
                .filter(session -> !isDaytimeSession(session))
                .filter(session -> !isSleeplessNight(session))
                .map(this::getChronotype)
                .collect(Collectors.groupingBy(
                        chronotype -> chronotype,
                        Collectors.counting()
                ));

        long owl = chronotypeCounts.getOrDefault(Chronotype.OWL, 0L);
        long lark = chronotypeCounts.getOrDefault(Chronotype.LARK, 0L);
        long dove = chronotypeCounts.getOrDefault(Chronotype.DOVE, 0L);

        String resultType;
        if (owl > lark && owl > dove) {
            resultType = "Сова (ночной человек)";
        } else if (lark > owl && lark > dove) {
            resultType = "Жаворонок (утренний человек)";
        } else {
            resultType = "Голубь (средний уровень)";
        }

        return new SleepAnalysisResult("Хронотип", resultType);
    }

    private boolean isDaytimeSession(SleepingSession session) {
        if (session.getDurationMinutes() > 480) {
            return false;
        }
        int startHour = session.getStart().getHour();
        return startHour >= 12 && session.getDurationMinutes() <= 360;
    }

    private boolean isSleeplessNight(SleepingSession session) {
        LocalDateTime nightStart = session.getStart().toLocalDate().atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);
        return session.getEnd().isBefore(nightStart) || session.getStart().isAfter(nightEnd);
    }

    private Chronotype getChronotype(SleepingSession session) {
        int startHour = session.getStart().getHour();
        int endHour = session.getEnd().getHour();

        if (startHour >= 23 && endHour >= 9) {
            return Chronotype.OWL;
        }
        if (startHour <= 22 && endHour <= 7) {
            return Chronotype.LARK;
        }
        return Chronotype.DOVE;
    }

}