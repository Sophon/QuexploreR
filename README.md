**QuexploreR** - QR collecting adventure app

Get to a location. Scan a QR. Move on.

**STAGE**: MVP

### Features

- fully KMP + CMP
- screens:
  - QR scanner
    - scan QR via native camera
      - native: Camera Preview composable via `expect` Composable
      - native: camera permissions via `expect` Composable
    - preview QR content to dismiss or save
    - supports these QR types:
      - `URL`
      - `text`
      - `Wifi`
      - `Contact` - MeCard or vCard formats
      - `Geo` - location data
      - `Email` and `Phone`
  - QR catalog
    - displays a list of saved QR items

### Architecture
- MVVM + usecases + Koin DI
- `core`
  - generic `ui` components
  - `arch` - architecture components like errors, `Result`
- `feat`
  - feature package containing all the features of the app
  - each package can have these parts:
    - `ui` - UI and UI logic
    - `native` - all components that require native implementations, be it via interface + `platformModule` or `actual`/`expect`
    - `usecase` - pieces of logic flow
    - `util`
    - `data` and `domain`
- `navigation`
  - setup via `navigation3`
- testing - `QrParser` is covered via unit tests

### Future backlog
- store QR entries via SQL (`SqlDelight`?)
- cache last scanned QR to not re-trigger preview of the captured QR item