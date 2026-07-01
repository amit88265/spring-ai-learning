const STORAGE_KEY = "spring-ai-memory-chat-state-v1";

const state = loadState();

const sessionList = document.querySelector("#session-list");
const messagesEl = document.querySelector("#messages");
const form = document.querySelector("#chat-form");
const input = document.querySelector("#prompt-input");
const sendButton = document.querySelector("#send-button");
const newChatButton = document.querySelector("#new-chat-button");
const clearChatButton = document.querySelector("#clear-chat-button");
const sessionTitle = document.querySelector("#session-title");
const memoryCount = document.querySelector("#memory-count");

ensureActiveSession();
render();
refreshMemoryCount();

newChatButton.addEventListener("click", () => {
    createSession();
    render();
    refreshMemoryCount();
    input.focus();
});

clearChatButton.addEventListener("click", async () => {
    const session = getActiveSession();
    if (!session) {
        return;
    }

    session.messages = [];
    session.title = "New chat";
    saveState();
    render();

    try {
        await fetch(`/api/chat/memory/${encodeURIComponent(session.id)}`, {
            method: "DELETE"
        });
        await refreshMemoryCount();
    } catch (error) {
        addMessage("error", "Could not clear server memory for this chat.");
    }
});

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    await sendPrompt();
});

input.addEventListener("keydown", (event) => {
    if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault();
        form.requestSubmit();
    }
});

input.addEventListener("input", () => {
    input.style.height = "auto";
    input.style.height = `${input.scrollHeight}px`;
});

function loadState() {
    try {
        const parsed = JSON.parse(localStorage.getItem(STORAGE_KEY));
        if (parsed && Array.isArray(parsed.sessions)) {
            return parsed;
        }
    } catch (error) {
        console.warn("Could not load chat state", error);
    }

    return {
        activeSessionId: null,
        sessions: []
    };
}

function saveState() {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
}

function ensureActiveSession() {
    if (state.sessions.length === 0) {
        createSession();
        return;
    }

    if (!state.sessions.some((session) => session.id === state.activeSessionId)) {
        state.activeSessionId = state.sessions[0].id;
        saveState();
    }
}

function createSession() {
    const id = crypto.randomUUID();
    const session = {
        id,
        title: "New chat",
        createdAt: Date.now(),
        messages: []
    };

    state.sessions.unshift(session);
    state.activeSessionId = id;
    saveState();
}

function getActiveSession() {
    return state.sessions.find((session) => session.id === state.activeSessionId);
}

function setActiveSession(id) {
    state.activeSessionId = id;
    saveState();
    render();
    refreshMemoryCount();
}

function render() {
    renderSessions();
    renderMessages();
}

function renderSessions() {
    sessionList.innerHTML = "";

    for (const session of state.sessions) {
        const button = document.createElement("button");
        button.type = "button";
        button.className = `session-button${session.id === state.activeSessionId ? " active" : ""}`;
        button.addEventListener("click", () => setActiveSession(session.id));

        const name = document.createElement("span");
        name.className = "session-name";
        name.textContent = session.title || "New chat";

        const meta = document.createElement("span");
        meta.className = "session-meta";
        meta.textContent = `${session.messages.length} messages`;

        button.append(name, meta);
        sessionList.append(button);
    }
}

function renderMessages() {
    const session = getActiveSession();
    messagesEl.innerHTML = "";
    sessionTitle.textContent = session?.title || "New chat";

    if (!session || session.messages.length === 0) {
        const empty = document.createElement("div");
        empty.className = "empty-state";
        empty.innerHTML = "<h3>Start a memory chat</h3><p>Ask a question, follow up naturally, and the same hidden conversation id will keep context for this session.</p>";
        messagesEl.append(empty);
        return;
    }

    for (const message of session.messages) {
        messagesEl.append(renderMessage(message));
    }

    messagesEl.scrollTop = messagesEl.scrollHeight;
}

function renderMessage(message) {
    const wrapper = document.createElement("article");
    wrapper.className = `message ${message.role}`;

    const bubble = document.createElement("div");
    bubble.className = "bubble";
    bubble.textContent = message.content;
    wrapper.append(bubble);

    if (message.meta) {
        const meta = document.createElement("div");
        meta.className = "message-meta";
        meta.textContent = message.meta;
        wrapper.append(meta);
    }

    return wrapper;
}

function addMessage(role, content, meta = "") {
    const session = getActiveSession();
    if (!session) {
        return;
    }

    session.messages.push({
        role,
        content,
        meta,
        createdAt: Date.now()
    });
    saveState();
    render();
}

async function sendPrompt() {
    const prompt = input.value.trim();
    const session = getActiveSession();

    if (!prompt || !session) {
        return;
    }

    addMessage("user", prompt);
    if (session.title === "New chat") {
        session.title = prompt.length > 42 ? `${prompt.slice(0, 42)}...` : prompt;
        saveState();
        renderSessions();
        sessionTitle.textContent = session.title;
    }

    input.value = "";
    input.style.height = "auto";
    setSending(true);

    try {
        const response = await fetch("/api/chat/memory", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                conversationId: session.id,
                message: prompt
            })
        });

        if (!response.ok) {
            throw new Error(`Request failed with status ${response.status}`);
        }

        const data = await response.json();
        const meta = [
            data.provider,
            data.model,
            data.profile,
            data.latencyMs ? `${data.latencyMs} ms` : null
        ].filter(Boolean).join(" | ");

        addMessage("assistant", data.answer || "", meta);
        await refreshMemoryCount();
    } catch (error) {
        addMessage("error", `The chat request failed. ${error.message}`);
    } finally {
        setSending(false);
        input.focus();
    }
}

function setSending(isSending) {
    sendButton.disabled = isSending;
    input.disabled = isSending;
    sendButton.textContent = isSending ? "Sending..." : "Send";
}

async function refreshMemoryCount() {
    const session = getActiveSession();
    if (!session) {
        memoryCount.textContent = "0 memory messages";
        return;
    }

    try {
        const response = await fetch(`/api/chat/memory/${encodeURIComponent(session.id)}`);
        if (!response.ok) {
            throw new Error("Memory summary unavailable");
        }

        const summary = await response.json();
        const count = summary.messageCount ?? 0;
        memoryCount.textContent = `${count} memory ${count === 1 ? "message" : "messages"}`;
    } catch (error) {
        memoryCount.textContent = "Memory unavailable";
    }
}
