package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analysis.BadQualitySessionsCountFunction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadQualitySessionsCountFunctionTest {

    @Test
    void testEmptyList() {
        BadQualitySessionsCountFunction func = new BadQualitySessionsCountFunction();
        SleepAnalysisResult result = func.apply(List.of());
        assertEquals(0L, result.getValue());
    }

    @Test
    void testCountBadQuality() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), SleepQuality.BAD),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(6), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(5), SleepQuality.BAD)
        );
        BadQualitySessionsCountFunction func = new BadQualitySessionsCountFunction();
        SleepAnalysisResult result = func.apply(sessions);
        assertEquals(2L, result.getValue());
    }
}