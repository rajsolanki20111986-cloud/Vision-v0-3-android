# Vision v0.3: changes and status

## Fixed bugs
- Contacts, location and photos were never requested at runtime, so those tools always failed. All needed permissions are now asked at launch.
- Chat history lived only in a ViewModel and was lost on restart. It is now saved in `conversation.jsonl`.
- Voice and text were separate. Both now write to one history, and the voice model sees recent text messages (and the reverse).
- Notification messages were never sent to the voice model, and the sender name was lost (the app name was stored in its place). Both fixed.
- Typing replaced the whole field. It now appends, and falls back to paste when an app refuses.
- Screen reading skipped buttons without text (send, mic, camera icons). It now lists them by resource id or class, and includes scrollable areas and inputs.
- Memory reading skips broken lines, so one bad line cannot break the chat.
- Storage permission on Android 13+ now uses READ_MEDIA_IMAGES.
- Screenshot support needs `canTakeScreenshot` in the accessibility config, which is now set.

## New
- Memory: files for profile, facts, routines, projects and skills, with an editor on the brain screen.
- Tools: memory_list/read/write/append, post_to_chat (puts code or long text in the chat), gmail_search, capture_screen, torch, battery, set_alarm, universal open_app (matches any launcher app by name or package).
- Voice screen: blue arc, mic on/off and speaker on/off switches.
- Chat: his messages are blue, Vision's are yellow. Voice messages are marked with 🎙. The speaker button reads Hindi or English depending on the text.
- Settings: switches for phone control, memory, message alerts and Gmail, plus shortcuts for the permissions.
- Text chat now supports tool calls, so the same tools work by typing.

## Not verified
- This environment has no Kotlin compiler or Android SDK. I checked bracket balance and read every changed line, but the first GitHub build may still show compile errors. If it does, send me the log.
- The Live model name `gemini-3.1-flash-live-preview` and the screenshot field `realtimeInput.video` come from memory of the API. Check them against the current Gemini docs if voice or screenshots fail.
- Gmail uses a pasted access token, which expires (about an hour for playground tokens). A full OAuth login is still to do.

## Not done yet (honest list)
- 100% accurate tapping cannot be guaranteed. Tools fail on some apps, and Vision should re-check with read_screen or capture_screen after each step.
- Continuous screen watching every N seconds is not built. The model can call read_screen repeatedly, but there is no timer loop yet.
- Sending WhatsApp or other messages is not built. Vision can only read notifications and draft replies for you to copy.
- Wallpaper change and Spotify-specific control are not built. Music control uses media keys, which work with the screen off.
- Lock-screen commands work only for tools that don't need accessibility (torch, volume, media, alarm, battery, Gmail).
- Audio thread shutdown (AudioStreamer) can still race with release. It has not caused a crash in the code I read, but it is worth watching.
- The "500–600 lines" bug you mentioned is not in this zip. This version has no separate chat-memory file; the new history file is the one to check.
