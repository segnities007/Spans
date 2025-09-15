# StreetPass SNS

A proximity-based social network where you can only see posts from people you've physically encountered.

## Features

- **Proximity Detection**: Bluetooth Low Energy scanning to detect nearby users (~10m range)
- **Time-Limited Access**: Only see posts created before you encountered someone
- **Real-world Connections**: No friend requests, just physical proximity
- **Google OAuth**: Simple authentication and profile setup

## Tech Stack

- **Frontend**: Kotlin, Jetpack Compose
- **Backend**: Supabase

## How It Works

1. App scans for nearby BLE signals in background
2. When users are close, UUIDs are exchanged and stored locally
3. Timeline fetches posts from encountered users with time filtering
4. Re-encountering someone gives access to their latest posts

## Development

**Requirements**: Android 9.0+, BLE device

## Acknowledgments

Nintendo 3DS StreetPass system for inspiration