package agenda;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class Event {

    private final String myTitle;
    private final LocalDateTime myStart;
    private final Duration myDuration;

    private Repetition repetition; //Repetition (0..1)

    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    public void setRepetition(ChronoUnit frequency) {
        this.repetition = new Repetition(frequency);
    }

    public void addException(LocalDate date) {
        if (repetition != null) {
            repetition.addException(date);
        }
    }

    public void setTermination(LocalDate terminationInclusive) {
        if (repetition != null) {
            Termination termination = new Termination(
                    myStart.toLocalDate(),           // date de début
                    repetition.getFrequency(),       // fréquence
                    terminationInclusive             // date de fin
            );
            repetition.setTermination(termination);
        }
    }

    public void setTermination(long numberOfOccurrences) {
        if (repetition != null) {
            Termination termination = new Termination(
                    myStart.toLocalDate(),           // date de début
                    repetition.getFrequency(),       // fréquence
                    numberOfOccurrences              // nombre d'occurrences
            );
            repetition.setTermination(termination);
        }
    }

    public int getNumberOfOccurrences() {
        if (repetition != null && repetition.getTermination() != null) {
            return (int) repetition.getTermination().numberOfOccurrences();
        }
        return 1; // Un événement non repetitif se produit une seule fois
    }

    public LocalDate getTerminationDate() {
        if (repetition != null && repetition.getTermination() != null) {
            return repetition.getTermination().terminationDateInclusive();
        }
        return myStart.toLocalDate(); // Pour un événement non repetitif, la date de fin est la date de début
    }

    /**
     * Tests if an event occurs on a given day
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */
    public boolean isInDay(LocalDate aDay) {
        LocalDateTime end = myStart.plus(myDuration);
        LocalDateTime dayStart = aDay.atStartOfDay();
        LocalDateTime dayEnd = aDay.plusDays(1).atStartOfDay();
        boolean isInDay = myStart.isBefore(dayEnd) && end.isAfter(dayStart);

        if (repetition == null) {
            return isInDay;
        }

        if (myStart.toLocalDate().equals(aDay)) {
            return true;
        }

        if (repetition.doesNotOccurAt(aDay)) {
            return false;
        }

        LocalDate startDate = myStart.toLocalDate();
        ChronoUnit frequency = repetition.getFrequency();

        long periodsBetween = startDate.until(aDay, frequency);

        return periodsBetween > 0;
    }

    public String getTitle() {

        return myTitle;
    }

    public LocalDateTime getStart() {

        return myStart;
    }

    public Duration getDuration() {

        return myDuration;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}
