# writeup: SQL injection UNION attack, retrieving data from other tables

### the goal
The objective of this lab was to perform a SQL injection UNION attack
to retrieve all usernames and passwords from the database and then use
the extracted credentials to log in as the administrator user.

### the input (Parameter)
The vulnerable input was located in the `category` GET parameter within the
URL path (`/filter?category=Gifts`).

### What payload worked
The payload that successfully exploited the vulnerability was:
(`Gifts' UNION SELECT username, password FROM users--`)

### Why did it work
The input parameter was directly concatenated into a backend SQL query string
without sanitization. By inputting a single quote (`'`), the original query string
was closed early. The `UNION SELECT` statement successfully appended a new query
that requested rows from the `users` table, matching the exact column count and
data types expected by the application. The double dash (`--')
commented out the remainder of the original query to prevent syntax errors.

### How to fix
To prevent this vulnerability, the application must avoid dynamic string concatenation
and implement parameterized queries (prepared statements). Using prepared statements
ensures the database treats user input strictly as data rather than executable code,
completely neutralizing SQL injection attempts regardless of the payload structure.
