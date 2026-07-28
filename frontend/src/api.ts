const base = import.meta.env.VITE_API_BASE_URL || ''

export async function createRoom(): Promise<string> {
  const response = await fetch(`${base}/api/rooms`, { method: 'POST' })
  if (!response.ok) throw new Error('Could not create room')
  const data = await response.json()
  return data.roomCode
}

export async function roomExists(roomCode: string): Promise<boolean> {
  const response = await fetch(`${base}/api/rooms/${encodeURIComponent(roomCode)}`)
  return response.ok
}

export async function getMessages(roomCode: string) {
  const response = await fetch(`${base}/api/rooms/${encodeURIComponent(roomCode)}/messages`)
  if (!response.ok) throw new Error('Could not load messages')
  return response.json()
}
