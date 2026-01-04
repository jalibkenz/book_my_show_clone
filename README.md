# BookMyShow (local dev notes)

This README lists the main endpoints and quick example curl commands for local testing.

Prerequisites
- Java 21
- MySQL (or override with H2 in-memory for quick run)
- Optional: Redis, MongoDB when using full features

Build & run (dev)
```bash
export JAVA_HOME="$(/usr/libexec/java_home -v21)"
# optionally set env vars used by the app
export RAZORPAY_KENZSYSTEMS_KEY_ID=dummy
export RAZORPAY_KENZSYSTEMS_KEY_SECRET=dummy
./mvnw -DskipTests package
java -jar target/bookmyshow-0.0.1-SNAPSHOT.jar
```

Config
- `src/main/resources/application.properties` contains app config.
- Booking hold TTL (minutes) can be configured with `booking.hold.ttlMinutes` (default 5).

Key endpoints

Theatre / internal (listings)
- GET /theatres/{theatreId}/shows
- GET /theatres/{theatreId}/shows/{showId}/seats
- GET /shows/{showId}/seats
- POST /shows  (create show)

Public / customer (booking)
- GET /theatres/{theatreId}/shows
- GET /shows/{showId}/seats
- POST /shows/{showId}/bookings  -> create hold, returns bookingId
- POST /bookings/{bookingId}/pay  -> confirm payment and finalize booking
- GET /bookings/{bookingId}       -> get booking status/details
- GET /payments/config            -> returns public payment config

Quick create-show example
```bash
curl -X POST http://localhost:8080/shows \
  -H "Content-Type: application/json" \
  -d '{
    "theatreId": "11111111-1111-1111-1111-111111111111",
    "movieId":  "22222222-2222-2222-2222-222222222222",
    "screenName":"Screen-1",
    "seatRows": 2,
    "seatsPerRow": 3,
    "totalSeats": 6,
    "startTime":"2026-01-05T15:00:00",
    "endTime":"2026-01-05T17:00:00",
    "basePrice": 100.00
  }'
```

Hold (book) and pay flow
```bash
# 1. Hold a seat (creates HELD booking)
curl -X POST http://localhost:8080/shows/<SHOW_ID>/bookings \
 -H "Content-Type: application/json" \
 -d '{"userId":"33333333-3333-3333-3333-333333333333","seatNumber":"A1"}'
# response: bookingId (uuid)

# 2. Confirm payment
curl -X POST http://localhost:8080/bookings/<BOOKING_ID>/pay \
 -H "Content-Type: application/json" \
 -d '{"paymentId":"razorpay_payment_abc123"}'
```

Hold release
- HELD bookings older than `booking.hold.ttlMinutes` are released by a scheduler and set to CANCELLED; seats return to AVAILABLE.

Notes
- The booking/payment flow uses PESSIMISTIC locks on `Seat` and optimistic locking on `Show.availableSeats` to handle concurrency.
- Consider adding idempotency keys and webhook verification for real payment integration.

If you want, I can add tests that run an in-memory flow or wire a test profile that skips external beans — tell me which you'd prefer.

