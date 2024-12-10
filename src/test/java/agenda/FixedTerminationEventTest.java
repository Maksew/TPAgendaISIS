package agenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste des événements répétitifs avec une date de terminaison, et des exceptions
 */
public class FixedTerminationEventTest {

    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1__2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);

    // January 5, 2021
    LocalDate jan_5_2021 = LocalDate.of(2021, 1, 5);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // Un événement qui se répète toutes les semaines et se termine à une date donnée
    Event fixedTermination;

    // Un événement qui se répète toutes les semaines et se termine après un nombre donné d'occurrences
    Event fixedRepetitions;

    @BeforeEach
    void setUp() {
        fixedTermination = new Event("Fixed termination weekly", nov_1__2020_22_30, min_120);
        fixedTermination.setRepetition(ChronoUnit.WEEKS);
        fixedTermination.setTermination(jan_5_2021);

        fixedRepetitions = new Event("Fixed termination weekly", nov_1__2020_22_30, min_120);
        fixedRepetitions.setRepetition(ChronoUnit.WEEKS);
        fixedRepetitions.setTermination(10);
    }

    @Test
    public void canCalculateNumberOfOccurrencesFromTerminationDate() {
        assertEquals(10, fixedTermination.getNumberOfOccurrences(),
            "Cet événement doit se répéter 10 fois");
    }

    @Test
    public void canCalculateTerminationDateFromNumberOfOccurrences() {
        LocalDate termination = LocalDate.of(2021,1, 3);
        assertEquals(termination, fixedRepetitions.getTerminationDate(),
                "Cet événement doit se terminer le 3 janvier");
    }

    @Test
    public void occursInTerminationDay() {
        LocalDate lastDay = nov_1_2020.plusWeeks(9).plusDays(1);
        assertTrue(fixedRepetitions.isInDay(lastDay),
            "Cet événement a lieu le jour de sa terminaison");
    }


    @Test
    public void eventIsInItsStartDay() {
        assertTrue(fixedTermination.isInDay(nov_1_2020),
            "Un événement a lieu dans son jour de début");
        assertTrue(fixedRepetitions.isInDay(nov_1_2020),
            "Un événement a lieu dans son jour de début");
    }

    @Test
    public void eventIsNotInDayBefore() {
        assertFalse(fixedTermination.isInDay(nov_1_2020.minusDays(1)),
            "Un événement n'a pas lieu avant son jour de début");
        assertFalse(fixedRepetitions.isInDay(nov_1_2020.minusDays(1)), "Un événement n'a pas lieu avant son jour de début");
    }

    @Test
    public void eventOccurs10WeeksAfter() {
        assertTrue(fixedTermination.isInDay(nov_1_2020.plusWeeks(9)),
            "Cet événement se produit toutes les semaines");
        assertTrue(fixedRepetitions.isInDay(nov_1_2020.plusWeeks(9)),
            "Cet événement se produit toutes les semaines");
    }

    @Test
    public void eventIsNotInExceptionDays() {
        fixedTermination.addException(nov_1_2020.plusWeeks(2)); // ne se produit pas à W+2
        fixedTermination.addException(nov_1_2020.plusWeeks(4)); // ne se produit pas à W+4
        assertTrue(fixedTermination.isInDay(nov_1_2020.plusWeeks(1)),
            "Cet événement se produit toutes les semaines");
        assertFalse(fixedTermination.isInDay(nov_1_2020.plusWeeks(2)),
            "Cet événement ne se produit pas à W+2");
        assertTrue(fixedTermination.isInDay(nov_1_2020.plusWeeks(3)),
            "Cet événement se produit toutes les semaines");
        assertFalse(fixedTermination.isInDay(nov_1_2020.plusWeeks(4)),
            "Cet événement ne se produit pas à W+4");
    }

    @Test
    public void setMultipleTerminations() {
        Event multipleTerminations = new Event("Multiple terminations", nov_1__2020_22_30, min_120);
        multipleTerminations.setRepetition(ChronoUnit.WEEKS);

        // Première terminaison avec date
        multipleTerminations.setTermination(jan_5_2021);
        assertEquals(10, multipleTerminations.getNumberOfOccurrences(),
                "Le nombre d'occurrences doit être correct après la première terminaison");

        // Changement pour terminaison avec nombre d'occurrences
        multipleTerminations.setTermination(5);
        assertEquals(5, multipleTerminations.getNumberOfOccurrences(),
                "Le nombre d'occurrences doit être mis à jour");

        // Retour à une terminaison avec date
        LocalDate newEndDate = nov_1_2020.plusWeeks(3);
        multipleTerminations.setTermination(newEndDate);
        assertEquals(newEndDate, multipleTerminations.getTerminationDate(),
                "La date de terminaison doit être mise à jour");
    }

    @Test
    public void differentFrequencies() {
        // Test avec répétition journalière
        Event dailyEvent = new Event("Daily Event", nov_1__2020_22_30, min_120);
        dailyEvent.setRepetition(ChronoUnit.DAYS);
        dailyEvent.setTermination(5);
        assertTrue(dailyEvent.isInDay(nov_1_2020.plusDays(3)),
                "L'événement doit se produire 3 jours après");

        // Test avec répétition mensuelle
        Event monthlyEvent = new Event("Monthly Event", nov_1__2020_22_30, min_120);
        monthlyEvent.setRepetition(ChronoUnit.MONTHS);
        monthlyEvent.setTermination(3);
        assertTrue(monthlyEvent.isInDay(nov_1_2020.plusMonths(2)),
                "L'événement doit se produire 2 mois après");
    }

    @Test
    public void multipleExceptions() {
        Event eventWithExceptions = new Event("Event with exceptions", nov_1__2020_22_30, min_120);
        eventWithExceptions.setRepetition(ChronoUnit.WEEKS);
        eventWithExceptions.setTermination(5);

        // Ajouter plusieurs exceptions
        LocalDate exception1 = nov_1_2020.plusWeeks(1);
        LocalDate exception2 = nov_1_2020.plusWeeks(2);
        eventWithExceptions.addException(exception1);
        eventWithExceptions.addException(exception2);

        // Vérifier les exceptions
        assertFalse(eventWithExceptions.isInDay(exception1),
                "L'événement ne doit pas se produire le jour de la première exception");
        assertFalse(eventWithExceptions.isInDay(exception2),
                "L'événement ne doit pas se produire le jour de la deuxième exception");
        assertTrue(eventWithExceptions.isInDay(nov_1_2020.plusWeeks(3)),
                "L'événement doit se produire un jour sans exception");
    }
}
