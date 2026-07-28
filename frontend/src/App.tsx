import { useEffect, useMemo, useRef, useState } from 'react'
import { Client, IMessage } from '@stomp/stompjs'
import { createRoom, getMessages, roomExists } from './api'
import type { ChatMessage } from './types'

function generateUserCode() {
  return `USER-${Math.floor(1000 + Math.random() * 9000)}`
}

function App() {
  const [roomCode, setRoomCode] = useState('')
  const [joinCode, setJoinCode] = useState('')
  const [userCode, setUserCode] = useState('')
  const [messages, setMessages] = useState<ChatMessage[]>([])
  const [text, setText] = useState('')
  const [online, setOnline] = useState(1)
  const [error, setError] = useState('')
  const clientRef = useRef<Client | null>(null)
  const bottomRef = useRef<HTMLDivElement | null>(null)

  const inRoom = Boolean(roomCode)

  const wsUrl = useMemo(() => {
    const configured = import.meta.env.VITE_WS_URL
    if (configured) return configured
    return `${window.location.protocol === 'https:' ? 'wss' : 'ws'}://${window.location.host}/ws`
  }, [])

  useEffect(() => {
    const pathCode = window.location.pathname.match(/^\/room\/([A-Za-z0-9]+)$/)?.[1]
    if (pathCode) {
      joinRoom(pathCode)
    }
    return () => clientRef.current?.deactivate()
  }, [])

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  async function joinRoom(code: string) {
    const normalized = code.trim().toUpperCase()
    if (!normalized) return
    setError('')
    try {
      if (!(await roomExists(normalized))) {
        setError('Room not found.')
        return
      }

      const identity = userCode || generateUserCode()
      setUserCode(identity)
      setRoomCode(normalized)
      window.history.replaceState({}, '', `/room/${normalized}`)

      const history = await getMessages(normalized)
      setMessages(history)

      const client = new Client({
        brokerURL: wsUrl,
        reconnectDelay: 3000,
        onConnect: () => {
          client.subscribe(`/topic/room/${normalized}`, (frame: IMessage) => {
            const incoming: ChatMessage = JSON.parse(frame.body)
            setMessages(prev => prev.some(m => m.id === incoming.id) ? prev : [...prev, incoming])
          })
        },
        onStompError: () => setError('Real-time connection failed. Please retry.')
      })

      client.activate()
      clientRef.current = client
    } catch {
      setError('Could not join room. Please try again.')
    }
  }

  async function handleCreate() {
    try {
      const code = await createRoom()
      await joinRoom(code)
    } catch {
      setError('Could not create room.')
    }
  }

  function sendMessage() {
    const value = text.trim()
    if (!value || !clientRef.current?.connected || value.length > 500) return

    clientRef.current.publish({
      destination: `/app/chat/${roomCode}`,
      body: JSON.stringify({ userCode, message: value })
    })
    setText('')
  }

  function handleKeyDown(e: React.KeyboardEvent<HTMLInputElement>) {
    if (e.key === 'Enter') sendMessage()
  }

  function copyLink() {
    navigator.clipboard.writeText(window.location.href)
  }

  if (!inRoom) {
    return (
      <main className="page center">
        <section className="card home">
          <div className="logo">💬</div>
          <h1>Anonymous Chat</h1>
          <p className="muted">Create a room and share the link. No account required.</p>

          <button className="primary" onClick={handleCreate}>Create Room</button>

          <div className="divider"><span>or</span></div>

          <input
            value={joinCode}
            onChange={e => setJoinCode(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && joinRoom(joinCode)}
            placeholder="Enter room code"
            maxLength={12}
            autoCapitalize="characters"
          />
          <button className="secondary" onClick={() => joinRoom(joinCode)}>Join Room</button>

          {error && <p className="error">{error}</p>}
        </section>
      </main>
    )
  }

  return (
    <main className="page">
      <section className="chat-card">
        <header className="header">
          <div>
            <h1>Anonymous Chat</h1>
            <div className="room-info">
              Room <strong>{roomCode}</strong> · You <strong>{userCode}</strong>
            </div>
          </div>
          <div className="online">● {online} online</div>
        </header>

        <div className="share">
          <span>{window.location.href}</span>
          <button onClick={copyLink}>Copy link</button>
        </div>

        <div className="messages">
          {messages.length === 0 && (
            <div className="empty">No messages yet. Say hello 👋</div>
          )}

          {messages.map(m => (
            <div className={`message ${m.userCode === userCode ? 'mine' : ''}`} key={m.id}>
              <div className="meta">
                <strong>{m.userCode}</strong>
                <time>{new Date(m.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</time>
              </div>
              <div className="bubble">{m.message}</div>
            </div>
          ))}
          <div ref={bottomRef} />
        </div>

        <div className="composer">
          <input
            value={text}
            onChange={e => setText(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="Type a message..."
            maxLength={500}
          />
          <button className="primary send" onClick={sendMessage} disabled={!text.trim()}>Send</button>
        </div>

        <div className="privacy">No account · No name · No email · No phone number</div>
        {error && <div className="error bottom-error">{error}</div>}
      </section>
    </main>
  )
}

export default App
