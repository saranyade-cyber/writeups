# writeup: reflected XSS into HTML context

### the goal
The objective of this lab was to identify a reflected Cross Site Scripting (XSS) 
vulnerability within a blog's search functionality and execute a payload that 
would successfully trigger a standard browser alert popup window.

### Where the input was
The vulnerable input context was the search blog text input field which maps to 
the `search` GET parameter in the query string of the URL (`/?search=test`).

### The Payload
The payload that was used to trigger the alert box was:
`<script>alert(1)</script>`

### What the browser did
Because the application showed the user's input directly into the page's HTML 
body without any validation, filtering or we can say encoding, the browser's HTML parser 
interpreted the input as code. It parsed the `<script>` tag, switched context to 
the JavaScript engine and executed the `alert(1)` instruction immediately upon 
loading the page.

### How to fix it
To fix this vulnerability, the application must apply contextual output encoding like
(converting `<` to `&lt;` and `>` to `&gt;`) before reflecting any user supplied
strings back into the HTML body. Also, implementing a robust Content Security 
Policy (CSP) restricts the execution of inline scripts which then provides a strong
layer of defense.
strings back into the HTML body. Additionally, implementing a robust Content Security 
Policy (CSP) restricts the execution of inline scripts, providing a strong layer 
of defense.

<img width="1122" height="631" alt="image" src="https://github.com/user-attachments/assets/09f84d52-ba91-46ff-9848-e79e43bc0d13" />

