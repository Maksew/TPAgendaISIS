package agenda;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste des événements simples, sans répétition
 */
public class SimpleEventTest {
    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1_2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // 89 minutes
    Duration min_89 = Duration.ofMinutes(89);

    // Un événement simple
    // November 1st, 2020, 22:30, 89 minutes
    Event simple = new Event("Simple event", nov_1_2020_22_30, min_89);
    
    // Un événement qui chevauche 2 jours
    // November 1st, 2020, 22:30, 120 minutes
    Event overlapping = new Event("Overlapping event", nov_1_2020_22_30, min_120);

    @Test
    public void eventIsInItsStartDay() {
        assertTrue(simple.isInDay(nov_1_2020), "Un événement a lieu dans son jour de début");
        assertTrue(overlapping.isInDay(nov_1_2020), "Un événement a lieu dans son jour de début");
    }

    @Test
    public void eventIsNotInDayBefore() {
        assertFalse(simple.isInDay(nov_1_2020.minusDays(1)),  "Un événement n'a pas lieu avant son jour de début");
        assertFalse(overlapping.isInDay(nov_1_2020.minusDays(1)),  "Un événement n'a pas lieu avant son jour de début");
    }

    @Test
    public void overlappingEventIsInDayAfter() {
        assertFalse(simple.isInDay(nov_1_2020.plusDays(1)),      "Cet événement ne déborde pas sur le jour suivant");
        assertTrue(overlapping.isInDay(nov_1_2020.plusDays(1)),  "Cet événement déborde sur le jour suivant");
    }
    @Test
    public void toStringShowsEventTitle() {
        assertTrue(simple.toString().contains("Simple event"),
            "toString() doit montrer le titre de l'événement");
    }

    @Test
    public void gettersWork() {
        assertEquals("Simple event", simple.getTitle(), "getTitle doit retourner le titre correct");
        assertEquals(nov_1_2020_22_30, simple.getStart(), "getStart doit retourner le début correct");
        assertEquals(min_89, simple.getDuration(), "getDuration doit retourner la durée correcte");
    }

    @Test
    public void getNumberOfOccurrencesReturnOneForSimpleEvent() {
        assertEquals(1, simple.getNumberOfOccurrences(),
                "Un événement simple doit avoir une seule occurrence");
    }

    @Test
    public void getTerminationDateReturnsStartDateForSimpleEvent() {
        assertEquals(nov_1_2020, simple.getTerminationDate(),
                "La date de fin d'un événement simple doit être sa date de début");
    }

    @Test
    public void toStringShowsAllAttributes() {
        String eventString = simple.toString();
        assertTrue(eventString.contains("Simple event"), "toString doit contenir le titre");
        assertTrue(eventString.contains(nov_1_2020_22_30.toString()), "toString doit contenir la date de début");
        assertTrue(eventString.contains(min_89.toString()), "toString doit contenir la durée");
    }

    @Test
    public void exactlyOneDayEvent() {
        LocalDateTime start = LocalDate.of(2020, 11, 1).atStartOfDay();
        Event dayEvent = new Event("24h Event", start, Duration.ofHours(24));

        assertTrue(dayEvent.isInDay(LocalDate.of(2020, 11, 1)),
                "L'événement doit être dans son jour de début");
        assertTrue(dayEvent.isInDay(LocalDate.of(2020, 11, 2)),
                "L'événement de 24h doit être aussi dans le jour suivant");
    }

    @Test
    public void zeroMinutesEvent() {
        Event instantEvent = new Event("Zero Duration", nov_1_2020_22_30, Duration.ZERO);
        assertTrue(instantEvent.isInDay(nov_1_2020),
                "Un événement de durée zéro doit être détecté dans son jour");
        assertFalse(instantEvent.isInDay(nov_1_2020.plusDays(1)),
                "Un événement de durée zéro ne doit pas déborder sur le jour suivant");
    }

    @Test
    public void addExceptionToSimpleEvent() {
        simple.addException(nov_1_2020);
        assertTrue(simple.isInDay(nov_1_2020),
                "Les exceptions ne doivent pas affecter un événement simple");
    }
    
}
