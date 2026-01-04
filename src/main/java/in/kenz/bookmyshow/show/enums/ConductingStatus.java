package in.kenz.bookmyshow.show.enums;

public enum ConductingStatus {

    SCHEDULED,          // Created, not yet open for booking
    OPEN_FOR_BOOKING,   // Booking allowed
    HOUSEFUL,          // Seats exhausted
    CANCELLED,          // Canceled by theatre/admin
    COMPLETED           // Show ended successfully
}
