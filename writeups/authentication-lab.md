# writeup: Password Reset broken logic

### the goal
The objective of this lab was to bypass standard authentication controls, find a 
flaw in the password reset mechanism and achieve complete account takeover of 
the target user `carlos`.

### the input
The vulnerability was located inside the hidden HTML form parameters (`username` 
and `temp-forgot-password-token`) submitted during the password selection phase.

### What Payload worked
1. It generated a password reset link using a legitimate account session.
2. opened browser Developer Tools (`F12`) on the password submission page.
3. completely deleted the string value inside the token input field (`value=""`).
4. modified the hidden target user attribute from `wiener` to `carlos` (`value="carlos"`).
5. submitted a new password string directly through the modified form template.

### Why it worked
The backend server architecture contained a logical flaw. Even though it verified
tokens when they were provided, it completely skipped the cryptographic validation
step if the token parameter arrived completely empty or not. It then blindly 
executed the password modification against whichever string was passed by the user
in the username parameter.

<img width="1122" height="631" alt="image" src="https://github.com/user-attachments/assets/21e3fc2c-aef7-477c-a0d9-94ba9e4cf8c7" />


### How to Fix
The backend password reset handler must implement a strict "fail-closed" policy.
If the reset token is missing, blank or malformed, the system must terminate
the request immediately and deny access. The application must never trust
user identity data passed via mutable client-side elements like hidden HTML form fields.
