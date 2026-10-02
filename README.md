# Auto Find — local car services

A Java 21 and Spring Boot website for finding local car services and sending booking enquiries. Visitors search by service and area, view profiles, and send a dated request. Providers can register a listing, sign in, view enquiries sent to **their own** listing, and accept or decline them. Data persists in a local H2 database. The five initial listings are fictional; this app does **not** contact real businesses or send email.

## Run on Windows

1. Extract this ZIP and open the **`autofind` folder containing `pom.xml`** in VS Code.
2. Open **Terminal → New Terminal**. From that folder, run:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

3. When the terminal says the application has started, open <http://localhost:8081/>.
4. Try searching for **Detailing** in **Lucan** and opening the fictional listing.
5. Click **List your service** in the top menu. Create your own demo listing and provider account. Sign in through **Provider portal**.
6. Open your listing's public page in another browser tab and send a booking request with a future date. Return to the provider dashboard to accept or decline it.
7. Stop the server with **Ctrl+C**, start it again, and sign in to check that the listing and requests survived the restart.

The first run may download Spring dependencies. The H2 database file (`autofind-db.mv.db`) is created beside `pom.xml` and is ignored by Git. There is no PostgreSQL setup for this local version. If you are replacing an earlier extracted Auto Find folder, stop its running server before replacing files; the ignored database file can stay where it is.

## Pages and code

| Page | URL | Java class |
| --- | --- | --- |
| Landing and search | `/`, `/services` | `AutoFindController`, `ProviderService` |
| Provider profile | `/services/{id}` | `ProviderService` |
| Booking enquiry | `/services/{id}/request` | `BookingService`, `BookingRequestForm` |
| Saved confirmation | `/requests/{reference}` | `BookingRepository` |
| Provider registration and sign-in | `/providers/register`, `/login` | `ProviderPortalController`, `SecurityConfig` |
| Provider dashboard | `/provider/dashboard` | `ProviderAccountService`, `BookingService` |

The `Provider`, `ProviderAccount` and `Booking` classes are database records. The provider list starts with five fictional examples in `SampleData.java`; it seeds only when the database is empty. Search matches the chosen category and part of a town or county. The booking form validates name, email, preferred date and message length before saving a request. Spring Security hashes passwords with BCrypt and guards the provider dashboard. The status update looks up a booking **by both its ID and the signed-in provider's ID**, preventing one provider from changing another provider's request.

## Tests

```powershell
.\mvnw.cmd test
```

`BookingFlowTests` checks filtering, past-date rejection, a valid request, and its confirmation page. `ProviderPortalTests` checks registration, password hashing, sign-in, and provider ownership of booking updates. Each test class uses an in-memory H2 database. GitHub Actions runs the tests when this project is pushed to `main`.

## Current boundary

This is a **local two-sided demo**, not a live marketplace. Provider status changes are visible on the dashboard, but the customer is not emailed; there is no customer sign-in, provider verification, payment or production deployment. Keep the local demo database out of GitHub. A later milestone can add customer updates, provider editing, and deployment configuration.

## Screenshots

![Home page](screenshots/home.png)
![Service search](screenshots/search.png)
![Provider dashboard](screenshots/dashboard.png)