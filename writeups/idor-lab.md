# writeup: Insecure Direct Object References (IDOR)

### the goal
The objective was to access unauthorized user communication history, extract sensitive 
authentication credentials belonging to another user (`carlos`), and perform an 
account takeover.

### the input
The vulnerability was located in the file retrieval URL parameter path used by the 
chat transcript download feature (`/download-transcript/[NUMBER].txt`).

### What payload worked
1. Initiated a standard support chat session to generate a valid transaction index.
2. Copied the download link provided by the "View transcript" action.
3. Directly manipulated the static reference ID at the end of the URL from your session's
   number down to `1.txt`.
4. Requested the modified URL to force the server to fetch and return the chat log of a
   completely different user session.

### Why it worked
The application suffered from Insecure Direct Object References (IDOR).
The server-side code used a simple, predictable sequential identifier (`1.txt`, `2.txt`)
to fetch stored chat logs from the file system. So the backend lacked any
access control checks to verify if the user requesting the file was the actual owner
or participant of that specific chat session.

### How to fix
1. **Implement Access Control:** The backend handler must check the user's
   active session token against an Access Control List (ACL) before serving any
   static file to ensure they have explicit permission to view it.
3. **Use Indirect Object References (UUIDs):** Instead of using sequential, predictable
   integers (like `1`, `2`, `3`), generate long, random, non-guessable Universally Unique
   Identifiers (UUIDs) for file names (e.g., `f81d4fae-7dec-11d0-a765-00a0c91e6bf6.txt`).
   This prevents attackers from guessing other files.

   <img width="1122" height="631" alt="image" src="https://github.com/user-attachments/assets/764cb733-0243-43b3-afb0-1b040277647c" />

