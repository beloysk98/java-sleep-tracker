package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction {

    private enum Chronotype { OWL, LARK, DOVE }

    private static final int OWL_START_LIMIT = 23;
    private static final int OWL_END_LIMIT = 9;
    private static final int LARK_START_LIMIT = 22;
    private static final int LARK_END_LIMIT = 7;
    private static final int DAY_START_MIN = 10;
    private static final int DAY_START_MAX = 16;
    private static final int DAY_DURATION_MAX = 360;

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
        boolean isShort = durationMinutes < DAY_DURATION_MAX;
        boolean isDayTime = startHour >= DAY_START_MIN && startHour <= DAY_START_MAX;
        boolean isNotOvernight = startHour < endHour;

        return isShort && isDayTime && isNotOvernight;
    }

    private Chronotype getChronotype(SleepingSession session) {
        int startHour = session.getStart().getHour();
        int endHour = session.getEnd().getHour();

        // Сова: засыпание после 23:00 И пробуждение после 9:00
        if (startHour >= OWL_START_LIMIT && endHour >= OWL_END_LIMIT) {
            return Chronotype.OWL;
        }

        // Жаворонок: засыпание до 22:00 И пробуждение до 7:00
        if (startHour <= LARK_START_LIMIT && endHour <= LARK_END_LIMIT) {
            return Chronotype.LARK;
        }

        return Chronotype.DOVE;
    }
}