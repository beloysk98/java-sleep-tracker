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
        int startHour = session.getStart().getHour();
        int endHour = session.getEnd().getHour();
        long durationMinutes = session.getDurationMinutes();
        
        boolean isShort = durationMinutes < 240;
        boolean isDayTime = startHour >= 10 && startHour <= 16;
        boolean isNotOvernight = startHour < endHour;

        return isShort && isDayTime && isNotOvernight;
    }

    private boolean isSleeplessNight(SleepingSession session) {
        LocalDateTime nightStart = session.getStart().toLocalDate().atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);
        return session.getEnd().isBefore(nightStart) || session.getStart().isAfter(nightEnd);
    }

    private Chronotype getChronotype(SleepingSession session) {
        int startHour = session.getStart().getHour();
        int endHour = session.getEnd().getHour();

        int adjustedEndHour = endHour;
        if (endHour < startHour) {
            adjustedEndHour = endHour + 24;
        }

        boolean isOwlStart = startHour >= 23 || startHour <= 5;
        boolean isOwlEnd = adjustedEndHour >= 9;

        if (isOwlStart && isOwlEnd) {
            return Chronotype.OWL;
        }

        boolean isLarkStart = startHour <= 22;
        boolean isLarkEnd = endHour <= 7;

        if (isLarkStart && isLarkEnd) {
            return Chronotype.LARK;
        }

        return Chronotype.DOVE;
    }
}