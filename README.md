# Library Management System

A full-stack library management system with role-based access control, book circulation (checkout/return/renew), fine tracking, and admin/librarian/member dashboards.

- **Backend:** Java 21, Spring Boot 4, Spring Security, Spring Data JPA (Hibernate), PostgreSQL, JWT auth, Maven
- **Frontend:** Angular 21 (standalone components), TypeScript, Tailwind CSS

## Features

- **Authentication & authorization** — JWT-based login/signup, stateless sessions, BCrypt-hashed passwords.
- **Three roles:**
  - **ADMIN** — full access, including the dashboard/stats view.
  - **LIBRARIAN** — manages books, members, circulation desk operations (checkout/issued list), and fines.
  - **MEMBER** — browses the catalog, borrows books, views/returns/renews their own loans, pays their own fines, views their own profile.
- **Book catalog** — add/edit/delete books and copies, search by title, per-copy status (available/issued/reserved).
- **Circulation** — checkout, return, and renew, with automatic overdue-fine calculation and a reservation queue.
- **Fines** — pending/history views for staff, self-service "my fines" + pay flow for members.
- **Admin dashboard** — totals for books, members, issued loans, and fines.

## Demo login credentials

Seeded automatically on first run against an empty database (see `AdminSeeder`/`DataSeeder` below):

| Role | Email | Password |
|---|---|---|
| Admin | `admin@library.com` | set via `ADMIN_PASSWORD` (see Configuration below), or check the startup log for the generated one-time password |
| Librarian | `librarian@library.com` | `librarian123` |
| Member | `arjun.mehta@example.com` | `member123` |
| Member | `priya.nair@example.com` | `member123` |
| Member | `sofia.rossi@example.com` | `member123` |
| Member | `daniel.kim@example.com` | `member123` |
| Member | `omar.farouk@example.com` | `member123` |

## Project structure

```
backend/    Spring Boot REST API (Maven)
frontend/   Angular SPA
```

## Backend

### Configuration

The backend reads the following (all have local-dev defaults, but **must** be set explicitly outside local development):

| Property | Env var | Purpose |
|---|---|---|
| `app.jwt.secret` | `JWT_SECRET` | HMAC-SHA256 signing key for JWTs. If unset, a random key is generated at startup (logged as a warning) — tokens won't survive a restart. |
| `app.jwt.expiration-ms` | `JWT_EXPIRATION_MS` | Token lifetime in ms (default 24h). |
| `app.admin.email` | `ADMIN_EMAIL` | Email of the seeded admin account (default `admin@library.com`). |
| `app.admin.password` | `ADMIN_PASSWORD` | Password for the seeded admin account. If unset, a random one-time password is generated and logged at startup. |
| `app.cors.allowed-origins` | `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed frontend origins (default `http://localhost:4200`). |

Database connection is configured in `backend/src/main/resources/application.properties` (defaults to a local PostgreSQL instance at `localhost:5432/library_db`).

### Running

```bash
cd backend
./mvnw spring-boot:run
```

On first run against an empty database, two seeders run automatically:

- **`AdminSeeder`** creates the admin account (`ADMIN_EMAIL`/`ADMIN_PASSWORD`, or the generated defaults above).
- **`DataSeeder`** populates sample data (only when the `books` table is empty): a dozen books with copies across several genres, a sample `LIBRARIAN` account, five sample `MEMBER` accounts, and a few sample loans (including one overdue loan with a pending fine), so the app isn't empty on first login.

See [Demo login credentials](#demo-login-credentials) above for the accounts this creates.

### Security model

- Route access is enforced centrally in `SecurityConfig` via `authorizeHttpRequests` (role-based route matchers), backed by a stateless JWT filter.
- Object-level ownership (a member acting only on their own loans/fines/profile, vs. staff acting on anyone's) is enforced in the service layer via `MemberAccessGuard`.
- Passwords are BCrypt-hashed; the JWT signing secret and admin credentials are externalized via configuration, never hardcoded.

## Frontend

### Running

```bash
cd frontend
npm install
npm start
```

Serves on `http://localhost:4200` by default, talking to the backend at `http://localhost:8080/api` (configurable via `frontend/src/environments/environment.ts`).

### Structure

- `core/services` — HTTP clients for auth, books, members, circulation, fines, admin.
- `core/guards` — route guards (`adminGuard`, `staffGuard` for ADMIN+LIBRARIAN, `memberGuard`).
- `core/interceptors` — attaches the JWT to outgoing requests and redirects to login on a 401.
- `admin/*` — staff-facing pages (dashboard, books, members, issued loans, fines).
- `member/*` — member-facing pages (dashboard, browse/borrow books, pay fine, profile).
- `auth/*` — login and signup.

## Notes

This project uses Angular's zone-based change detection (`provideZoneChangeDetection`) — components rely on automatic view updates after async operations, so avoid removing the `zone.js` polyfill without also migrating state updates to signals or manual `ChangeDetectorRef` calls.
