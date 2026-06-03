package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analysis.SleeplessNightsCountFunction;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleeplessNightsCountFunctionTest {

    @Test
    void testEmptyList() {
        SleeplessNightsCountFunction func = new SleeplessNightsCountFunction();
        SleepAnalysisResult result = func.apply(List.of());
        assertEquals(0L, result.getValue());
    }

    @Test
    void testOneNightWithSleep() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, Month.OCTOBER, 1, 23, 0),
                        LocalDateTime.of(2024, Month.OCTOBER, 2, 7, 0),
                        SleepQuality.GOOD
                )
        );
        SleeplessNightsCountFunction func = new SleeplessNightsCountFunction();
        SleepAnalysisResult result = func.apply(sessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    void testOneSleeplessNight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, Month.OCTOBER, 1, 7, 0),
                        LocalDateTime.of(2024, Month.OCTOBER, 1, 15, 0),
                        SleepQuality.NORMAL
                )
        );
        SleeplessNightsCountFunction func = new SleeplessNightsCountFunction();
        SleepAnalysisResult result = func.apply(sessions);
        assertEquals(1L, result.getValue());
    }

    @Test
    void testMultipleNightsWithMixedSleep() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2024, Month.OCTOBER, 1, 23, 0),
                        LocalDateTime.of(2024, Month.OCTOBER, 2, 7, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2024, Month.OCTOBER, 3, 8, 0),
                        LocalDateTime.of(2024, Month.OCTOBER, 3, 16, 0),
                        SleepQuality.NORMAL
                )
        );
        SleeplessNightsCountFunction func = new SleeplessNightsCountFunction();
        SleepAnalysisResult result = func.apply(sessions);
        assertEquals(1L, result.getValue());
    }
}