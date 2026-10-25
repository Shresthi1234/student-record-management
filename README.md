# Student Record Management System

Full-stack React + Java 21 Spring Boot + MySQL project. Separate `frontend/` and `backend/` folders can be pushed to separate GitHub repositories. The frontend uses Vite, the backend uses Maven, and JPA creates the table.

## 1. Prerequisites
Install Node.js 20+ (npm included), Java JDK 21, Maven 3.9+, MySQL 8+, Git and VS Code. Use separate VS Code terminals for frontend and backend. Check `node -v`, `npm -v`, `java -version`, `mvn -version`.

## 2. Run frontend FIRST
Open terminal inside `frontend`:

```bash
npm install
npm run dev
```

Open http://localhost:5173 . **The interface will load, but show a connection warning until the backend starts.** This is expected. To customize the backend URL, copy `.env.example` to `.env` and edit `VITE_API_URL`; restart Vite after editing.

## 3. Prepare local database
Start MySQL Server. In MySQL Workbench execute `database/create_database.sql`, or run:

```sql
CREATE DATABASE IF NOT EXISTS student_records;
```

JPA creates the `students` table automatically after Spring Boot connects. A local database must be running before the backend can start successfully.

## 4. Run backend SECOND
Open another terminal inside `backend`. Set credentials in your terminal (do not commit them). In Windows PowerShell:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/student_records"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
$env:FRONTEND_URL="http://localhost:5173"
mvn spring-boot:run
```

Check http://localhost:8080/api/students (initially `[]`). Reload the frontend. Add a student, refresh, edit and delete. Verify `SELECT * FROM students;` in MySQL Workbench.

**Note:** You requested frontend, backend, database in that order. You can launch the frontend first, but **the database must be ready before the backend can connect**, so practical setup order is frontend → database → backend → integrated testing.

## 5. Push TWO GitHub repositories
Create empty repositories on GitHub, e.g. `student-records-frontend` and `student-records-backend`. In `frontend` terminal:

```bash
git init
git add .
git commit -m "Initial React frontend"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/student-records-frontend.git
git push -u origin main
```

Repeat inside `backend`, using `student-records-backend` and a different commit message. Do not upload `.env` or passwords. `database/create_database.sql` can be copied into backend repo as documentation if desired.

## 6. Create cloud MySQL database on Aiven
Create an Aiven for MySQL service, create database `student_records`, and copy its hostname, port, username and password. Use the JDBC URL format:

```
jdbc:mysql://YOUR_HOST:YOUR_PORT/student_records?sslMode=REQUIRED
```

Check Aiven's service connection details and any required TLS settings. Test connectivity before deployment. Do not use the Aiven console URL as the JDBC URL.

## 7. Deploy backend on Render
Render → New → Web Service → connect the backend GitHub repository. Select **Docker** runtime; Dockerfile path `./Dockerfile` (repo root is backend). Add environment variables:

```
DB_URL=jdbc:mysql://YOUR_HOST:YOUR_PORT/student_records?sslMode=REQUIRED
DB_USERNAME=YOUR_AIVEN_USERNAME
DB_PASSWORD=YOUR_AIVEN_PASSWORD
FRONTEND_URL=https://YOUR_SITE.netlify.app
```

Deploy. Verify `https://YOUR_RENDER_SERVICE.onrender.com/api/students` returns JSON. If using a Render free instance, first request may take time while it wakes up. Render injects `PORT` automatically.

## 8. Deploy frontend on Netlify
Netlify → Add new project → Import existing project → connect frontend GitHub repo. Build command `npm run build`, publish directory `dist`. Set environment variable:

```
VITE_API_URL=https://YOUR_RENDER_SERVICE.onrender.com
```

Deploy, then copy the exact Netlify HTTPS site URL into Render's `FRONTEND_URL` environment variable and redeploy/restart backend. The URLs must match exactly (no trailing slash). Add, edit, and delete a student from Netlify; confirm changes persist in Aiven.

Alternative: Run `npm run build` locally in frontend and drag the `dist` folder into Netlify for manual deployment, as in the screenshot. Set `VITE_API_URL` in `frontend/.env` **before** building. Git-based deployment is recommended for automatic updates.

## API
GET `/api/students`, GET `/api/students/{id}`, POST `/api/students`, PUT `/api/students/{id}`, DELETE `/api/students/{id}`.

Sample POST JSON:

```json
{"rollNumber":"24CSE001","name":"Asha Rao","email":"asha@example.com","department":"CSE","year":3,"phone":"9876543210"}
```

## Notes
This is a classroom/demo CRUD application without authentication. Do not expose real student personal data or use it as a production system without authentication, authorization, and additional security. For public deployment, protect write endpoints and use test data only.
