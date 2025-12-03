const API_URL = "http://localhost:8081/api/todos";
const token = localStorage.getItem("token");

// Om ingen token -> skicka tillbaka till login
if (!token) {
    window.location.href = "index.html";
}

//      HÄMTA TODOS 
async function loadTodos() {
    const res = await fetch(API_URL, {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const todos = await res.json();
    showTodos(todos);
}

//   VISA TODOS
function showTodos(todos) {
    const list = document.getElementById("todoList");
    list.innerHTML = "";

    todos.forEach(todo => {
        const li = document.createElement("li");
        li.className = "todo-item";

        li.innerHTML = `
            <div>
                <strong>${todo.title}</strong><br>
                <small>${todo.description}</small><br>
                <small>Skapad: ${new Date(todo.createdAt).toLocaleString()}</small>
            </div>

            <div class="actions">
                <button onclick="toggleCompleted(${todo.id}, ${!todo.completed})">
                    ${todo.completed ? "☑️ Klar" : "⬜ Markera klar"}
                </button>

                <button class="delete" onclick="deleteTodo(${todo.id})">🗑️ Ta bort</button>
            </div>
        `;

        list.appendChild(li);
    });
}

//    LÄGG TILL TODO
document.getElementById("addTodoBtn").addEventListener("click", async () => {
    const title = document.getElementById("newTitle").value;
    const description = document.getElementById("newDesc").value;

    if (!title.trim()) return alert("Titel behövs!");

    await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ title, description })
    });

    document.getElementById("newTitle").value = "";
    document.getElementById("newDesc").value = "";

    loadTodos();
});

//    MARKERA KLAR / EJ KLAR
async function toggleCompleted(id, value) {
    await fetch(`${API_URL}/${id}/completed?value=${value}`, {
        method: "PATCH",
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    loadTodos();
}

//    TA BORT TODO
async function deleteTodo(id) {
    await fetch(`${API_URL}/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    loadTodos();
}

//    LOGGA UT
document.getElementById("logoutBtn").addEventListener("click", () => {
    localStorage.removeItem("token");
    window.location.href = "index.html";
});

//     LADDA VID START
loadTodos();
