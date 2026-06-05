package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analysis.ChronotypeFunction;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChronotypeFunctionTest {

    @Test
    void testOwlChronotype() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, Month.OCTOBER, 1, 23, 30),
                        LocalDateTime.of(2024, Month.OCTOBER, 2, 9, 30),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, Month.OCTOBER, 2, 23, 45),
                        LocalDateTime.of(2024, Month.OCTOBER, 3, 10, 0),
                        SleepQuality.GOOD
                )
        );
        ChronotypeFunction func = new ChronotypeFunction();
        SleepAnalysisResult result = func.apply(sessions);
        String chronotype = (String) result.getValue();
        assertTrue(chronotype.contains("Сова"));
    }

    @Test
    void testLarkChronotype() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, Month.OCTOBER, 1, 21, 0),
                        LocalDateTime.of(2024, Month.OCTOBER, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, Month.OCTOBER, 2, 21, 30),
                        LocalDateTime.of(2024, Month.OCTOBER, 3, 6, 30),
                        SleepQuality.GOOD
                )
        );
        ChronotypeFunction func = new ChronotypeFunction();
        SleepAnalysisResult result = func.apply(sessions);
        String chronotype = (String) result.getValue();
        assertTrue(chronotype.contains("Жаворонок"));
    }

    @Test
    void testDoveChronotype() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, Month.OCTOBER, 1, 22, 30),
                        LocalDateTime.of(2024, Month.OCTOBER, 2, 8, 0),
                        SleepQuality.GOOD
                )
        );
        ChronotypeFunction func = new ChronotypeFunction();
        SleepAnalysisResult result = func.apply(sessions);
        String chronotype = (String) result.getValue();
        assertTrue(chronotype.contains("Голубь"));
    }
}