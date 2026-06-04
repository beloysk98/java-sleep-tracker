package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction {

    private enum Chronotype { OWL, LARK, DOVE }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> chronotypeCounts = sessions.stream()
                .filter(session -> !isDaytimeSession(session))
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

        // Дневной сон: короткий (менее 6 часов) и происходит днём
        boolean isShort = durationMinutes < 360;
        boolean isDayTime = startHour >= 10 && startHour <= 16;
        boolean isNotOvernight = startHour < endHour;

        return isShort && isDayTime && isNotOvernight;
    }

    private Chronotype getChronotype(SleepingSession session) {
        int startHour = session.getStart().getHour();
        int endHour = session.getEnd().getHour();

        // Сова: засыпание после 23:00 И пробуждение после 9:00
        if (startHour >= 23 && endHour >= 9) {
            return Chronotype.OWL;
        }

        // Жаворонок: засыпание до 22:00 И пробуждение до 7:00
        if (startHour <= 22 && endHour <= 7) {
            return Chronotype.LARK;
        }

        return Chronotype.DOVE;
    }
}