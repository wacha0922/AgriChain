# 🌱 AgriChain — Project Explanation

> **Farm to Fork. Transparent. Trusted.**

AgriChain is a **Kotlin / Jetpack Compose** Android application that brings **blockchain-backed traceability** to the agricultural supply chain. It allows farmers to register products with rich metadata, generates **QR codes** for each product, and lets consumers scan those QR codes to verify the product's origin and journey — all designed to eventually be anchored on the **Polygon blockchain**.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Tech Stack & Dependencies](#2-tech-stack--dependencies)
3. [Architecture](#3-architecture)
4. [Project Structure (File-by-File)](#4-project-structure-file-by-file)
5. [User Flows](#5-user-flows)
6. [Data Models](#6-data-models)
7. [Authentication System](#7-authentication-system)
8. [Database (Room)](#8-database-room)
9. [QR Code System](#9-qr-code-system)
10. [Navigation System](#10-navigation-system)
11. [UI / Design System](#11-ui--design-system)
12. [Supported Roles](#12-supported-roles)
13. [Current Limitations & Future Roadmap](#13-current-limitations--future-roadmap)
14. [How to Build & Run](#14-how-to-build--run)

---

## 1. Project Overview

| Attribute           | Value                                   |
|---------------------|-----------------------------------------|
| **App Name**        | AgriChain                               |
| **Package**         | `com.example.agrichain`                 |
| **Min SDK**         | 26 (Android 8.0 Oreo)                   |
| **Target SDK**      | 37                                      |
| **Language**        | Kotlin                                  |
| **UI Framework**    | Jetpack Compose + Material 3            |
| **Database**        | Room (SQLite)                           |
| **Auth**            | Firebase Auth (Email/Password + Google) |
| **QR Generation**   | ZXing (core)                            |
| **QR Scanning**     | Google Code Scanner (ML Kit)            |
| **Blockchain**      | Polygon (planned — not yet wired)       |

### What Problem Does It Solve?

In India and many developing economies, consumers have no reliable way to trace where their food comes from. AgriChain solves this by:

- Letting **farmers** digitally register every product batch with details like crop type, farm location, harvest date, quantity, quality grade, and cultivation method.
- Generating a **unique Product ID** (`AGRI-YYYYMMDD-XXXXXXXX`) and a **QR code** for each registered product.
- Allowing **consumers** to scan the QR code and instantly see the full traceability details.
- Planning to **anchor all records on the Polygon blockchain** for tamper-proof, decentralized verification.

---

## 2. Tech Stack & Dependencies

### Core Android
| Library | Purpose |
|---------|---------|
| `androidx.core.ktx` | Kotlin extensions for Android framework |
| `androidx.lifecycle.runtime.ktx` | Lifecycle-aware coroutine support |
| `androidx.activity.compose` | Compose integration with Activity |

### Jetpack Compose
| Library | Purpose |
|---------|---------|
| `Compose BOM` | Bill of Materials for consistent Compose versions |
| `compose.ui` | Core Compose UI toolkit |
| `compose.ui.graphics` | Graphics primitives |
| `compose.ui.tooling.preview` | Preview support in Android Studio |
| `compose.material3` | Material Design 3 components |
| `compose.material.icons.extended` | Extended Material icons |

### Navigation
| Library | Purpose |
|---------|---------|
| `androidx.navigation.compose` | Compose-based navigation with NavHost |

### Database
| Library | Purpose |
|---------|---------|
| `androidx.room.runtime` | Room persistence library |
| `androidx.room.compiler` (KSP) | Room annotation processor |

### Authentication
| Library | Purpose |
|---------|---------|
| `firebase-bom` (v34.17.0) | Firebase Bill of Materials |
| `firebase-auth` | Firebase Authentication |
| `kotlinx-coroutines-play-services` | `await()` for Firebase Tasks |
| `credentials` | Android Credential Manager |
| `credentials-play-services-auth` | Play Services backend for Credential Manager |
| `googleid` | Google Identity library for ID tokens |

### QR Code
| Library | Purpose |
|---------|---------|
| `com.google.zxing:core` (3.5.3) | QR code generation (bitmap encoding) |
| `play-services-code-scanner` (16.1.0) | Google Code Scanner (ML Kit barcode scanning) |

### Build Plugins
| Plugin | Purpose |
|--------|---------|
| `android.application` | Android app build plugin |
| `kotlin.compose` | Kotlin Compose compiler plugin |
| `ksp` | Kotlin Symbol Processing (for Room) |
| `google-services` | Google/Firebase services configuration |

---

## 3. Architecture

AgriChain follows a **layered architecture** with clear separation between data, UI, and navigation:

```
┌─────────────────────────────────────────────────┐
│                   UI LAYER                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────────────┐│
│  │  Screens │ │Components│ │    Theme/Tokens   ││
│  └────┬─────┘ └──────────┘ └──────────────────┘│
│       │                                         │
│  ┌────▼──────────────────────────────┐          │
│  │        Navigation (NavGraph)      │          │
│  └────┬──────────────────────────────┘          │
├───────┼─────────────────────────────────────────┤
│       │          DATA LAYER                     │
│  ┌────▼─────┐  ┌─────────────┐  ┌───────────┐  │
│  │Repository│  │ Auth Repos  │  │  QR Gen   │  │
│  └────┬─────┘  └──────┬──────┘  └───────────┘  │
│       │               │                         │
│  ┌────▼─────┐  ┌──────▼──────┐                  │
│  │Room (DAO)│  │Firebase Auth│                  │
│  │  SQLite  │  │ + Google ID │                  │
│  └──────────┘  └─────────────┘                  │
├─────────────────────────────────────────────────┤
│              DOMAIN / MODELS                    │
│  ┌──────────┐ ┌──────────────┐ ┌─────────────┐ │
│  │ Product  │ │ProductStatus │ │Blockchain   │ │
│  │          │ │              │ │   Status    │ │
│  └──────────┘ └──────────────┘ └─────────────┘ │
└─────────────────────────────────────────────────┘
```

**Key Design Decisions:**
- **No ViewModels yet** — State is managed directly in composables with `remember` and `mutableStateOf`. ViewModels will be introduced as the app grows.
- **Singleton Repository** — `ProductRepository` is an `object` (singleton) initialized once in `MainActivity`.
- **No Dependency Injection** — Manual instantiation is used throughout. Hilt/Koin can be added later.
- **Two data models** — `Product` (domain model) and `ProductEntity` (Room entity) with mapper functions in the repository.

---

## 4. Project Structure (File-by-File)

```
app/src/main/java/com/example/agrichain/
│
├── MainActivity.kt              ← App entry point
│
├── data/
│   ├── auth/
│   │   ├── FirebaseAuthRepository.kt   ← Email/password auth
│   │   └── GoogleAuthRepository.kt     ← Google Sign-In via Credential Manager
│   │
│   ├── blockchain/                     ← (Empty) Reserved for Polygon integration
│   │
│   ├── local/
│   │   ├── AgriChainDatabase.kt        ← Room database definition
│   │   ├── ProductDao.kt              ← Data Access Object (CRUD queries)
│   │   └── ProductEntity.kt           ← Room entity (database table)
│   │
│   ├── model/
│   │   └── Product.kt                 ← Domain model + enums (ProductStatus, BlockchainStatus)
│   │
│   ├── qr/
│   │   └── QrCodeGenerator.kt         ← ZXing-based QR bitmap generation
│   │
│   ├── repository/
│   │   └── ProductRepository.kt       ← Central data access (Room-backed)
│   │
│   └── util/
│       └── ProductIdGenerator.kt      ← Generates AGRI-YYYYMMDD-XXXXXXXX IDs
│
└── ui/
    ├── components/
    │   ├── AgriClayButton.kt          ← Reusable claymorphic button
    │   ├── AgriClayCard.kt            ← Reusable claymorphic card
    │   ├── AgriClayChip.kt            ← Reusable claymorphic chip
    │   └── AgriSectionHeader.kt       ← Section header component
    │
    ├── navigation/
    │   ├── AgriChainNavGraph.kt       ← Full navigation graph + auth wiring
    │   └── AgriChainRoutes.kt         ← Route constants & builders
    │
    ├── screens/
    │   ├── auth/
    │   │   ├── SignInScreen.kt        ← Email/password + Google sign-in UI
    │   │   ├── SignUpScreen.kt        ← Account creation form
    │   │   └── WelcomeScreen.kt       ← Role selection (6 roles)
    │   │
    │   ├── consumer/
    │   │   ├── QrScannerScreen.kt     ← Google Code Scanner integration
    │   │   └── ProductVerificationScreen.kt ← Product traceability display
    │   │
    │   ├── farmer/
    │   │   ├── AddProductScreen.kt    ← Product registration form (7 fields)
    │   │   ├── FarmerDashboardScreen.kt ← Farmer home with summary + product list
    │   │   └── ProductCreatedScreen.kt  ← Success screen + QR generation
    │   │
    │   └── splash/
    │       └── SplashScreen.kt        ← Animated splash with logo
    │
    └── theme/
        ├── Color.kt                   ← Full color palette (light + dark + status)
        ├── DesignTokens.kt            ← Spacing, corners, component heights
        ├── Theme.kt                   ← Material 3 theme (light/dark schemes)
        └── Type.kt                    ← Typography definitions
```

### Detailed File Explanations

#### `MainActivity.kt`
The single Activity of the app. It:
- Enables edge-to-edge display
- Initializes the `ProductRepository` with the application context (so Room can build the database)
- Sets the Compose content with `AgriChainTheme` → `Surface` → `AgriChainNavGraph`

#### `data/model/Product.kt`
The core domain model containing:
- **`Product`** data class — 11 fields: productId, productName, cropType, farmLocation, harvestDate, quantity, quality, cultivationMethod, createdAt (timestamp), status, blockchainStatus
- **`ProductStatus`** enum — CREATED → HARVESTED → IN_TRANSIT → PROCESSED → AT_RETAILER → SOLD
- **`BlockchainStatus`** enum — PENDING → SUBMITTED → CONFIRMED → FAILED

#### `data/auth/FirebaseAuthRepository.kt`
Wraps Firebase Auth operations with Kotlin coroutines:
- `createAccount(email, password)` — Creates a new user
- `sendEmailVerification()` — Sends verification email to current user
- `reloadCurrentUser()` — Refreshes user state from Firebase
- `signIn(email, password)` — Signs in with email verification check
- `sendPasswordResetEmail(email)` — Password reset flow
- `signOut()` — Signs out the current user
- `getCurrentUser()` — Returns the current `FirebaseUser`

#### `data/auth/GoogleAuthRepository.kt`
Handles Google Sign-In using the modern **Credential Manager API**:
1. Creates a `GetGoogleIdOption` with the server client ID
2. Launches the Google account picker via `CredentialManager`
3. Extracts the Google ID token from the credential
4. Exchanges the ID token for a Firebase credential
5. Signs in to Firebase with `signInWithCredential()`

#### `data/local/AgriChainDatabase.kt`
Room database definition:
- Single entity: `ProductEntity`
- Database version: 1
- Database name: `agrichain_database`
- Exposes `productDao()` abstract function

#### `data/local/ProductDao.kt`
Room Data Access Object with 5 queries:
- `insertProduct()` — Insert/replace a product
- `getAllProducts()` — All products ordered by creation date (newest first)
- `getProductById()` — Find a single product by its ID
- `deleteProduct()` — Delete a specific product entity
- `deleteAllProducts()` — Clear all products (dev/testing utility)

#### `data/local/ProductEntity.kt`
Room entity class mapped to the `products` table. Mirrors `Product` but uses `String` for enum fields (status, blockchainStatus) since Room doesn't natively support enums.

#### `data/repository/ProductRepository.kt`
Singleton repository that:
- Initializes the Room database on first call
- Provides CRUD operations: `addProduct()`, `getAllProducts()`, `getProductById()`, `removeProduct()`, `clear()`
- Contains private mapper extension functions: `Product.toEntity()` and `ProductEntity.toProduct()`
- Safely parses enum strings with fallback defaults

#### `data/qr/QrCodeGenerator.kt`
Generates QR code bitmaps using ZXing:
- Input: content string + size (default 800px)
- Validates non-empty content and positive size
- Uses UTF-8 encoding with minimal margin
- Returns an `ARGB_8888` `Bitmap`

#### `data/util/ProductIdGenerator.kt`
Generates unique product IDs in the format `AGRI-YYYYMMDD-XXXXXXXX`:
- Date portion: Current date formatted as `yyyyMMdd`
- Unique portion: First 8 characters of a UUID (uppercase hex)
- Example: `AGRI-20260813-A7F31C92`

---

## 5. User Flows

### Flow 1: Farmer Product Registration

```
Splash Screen (2s animation)
    │
    ▼
Welcome Screen (Role Selection)
    │ Select "Farmer"
    ▼
Sign In Screen
    │ Email/Password or Google Sign-In
    ▼
Farmer Dashboard
    │ Tap "+ Add Agricultural Product"
    ▼
Add Product Screen (Fill 7 fields)
    │ Tap "Create Product"
    ▼
Product Created Screen
    │ View product details
    │ Tap "Generate Product QR"
    │ View QR code
    │ Tap "Done"
    ▼
Farmer Dashboard
```

### Flow 2: Consumer Product Verification

```
Splash Screen (2s animation)
    │
    ▼
Welcome Screen (Role Selection)
    │ Select "Consumer"
    ▼
Sign In Screen
    │ Email/Password or Google Sign-In
    ▼
QR Scanner Screen
    │ Tap "Open QR Scanner"
    │ Scan a product QR code
    ▼
Product Verification Screen
    │ View full traceability details
    │ View blockchain status
    │ Tap "Scan Another Product" or "Done"
```

### Flow 3: Account Creation

```
Sign In Screen
    │ Tap "Sign Up"
    ▼
Sign Up Screen
    │ Enter: Full Name, Email, Phone, Password
    │ Tap "Create Account"
    │ → Firebase creates account
    │ → Verification email sent
    │ → Auto sign-out (email must be verified first)
    ▼
Sign In Screen (with success message)
    │ User verifies email, then signs in
```

---

## 6. Data Models

### Product (Domain Model)

| Field              | Type              | Description                            |
|--------------------|-------------------|----------------------------------------|
| `productId`        | `String`          | Unique ID (AGRI-YYYYMMDD-XXXXXXXX)    |
| `productName`      | `String`          | Name (e.g., "Organic Tomatoes")        |
| `cropType`         | `String`          | Type of crop (e.g., Rice, Wheat)       |
| `farmLocation`     | `String`          | Farm location (e.g., "Nashik, MH")     |
| `harvestDate`      | `String`          | Harvest date (DD/MM/YYYY)              |
| `quantity`         | `String`          | Amount (e.g., "250 kg")                |
| `quality`          | `String`          | Grade (Premium/A/B/Standard)           |
| `cultivationMethod`| `String`          | Method (Organic/Conventional/Natural)  |
| `createdAt`        | `Long`            | Creation timestamp (milliseconds)      |
| `status`           | `ProductStatus`   | Lifecycle stage                        |
| `blockchainStatus` | `BlockchainStatus`| Polygon verification state             |

### ProductStatus Lifecycle

```
CREATED → HARVESTED → IN_TRANSIT → PROCESSED → AT_RETAILER → SOLD
```

### BlockchainStatus States

```
PENDING → SUBMITTED → CONFIRMED
                   └→ FAILED
```

---

## 7. Authentication System

The app supports **two authentication methods**, both backed by **Firebase Authentication**:

### Email/Password Authentication
- User creates an account with email + password
- A **verification email** is sent automatically
- User **cannot sign in** until the email is verified
- Password reset is supported via `sendPasswordResetEmail()`
- Human-readable error messages are generated for common Firebase errors (wrong password, user not found, weak password, network issues, etc.)

### Google Sign-In
- Uses the modern **Android Credential Manager API** (not the deprecated `GoogleSignInClient`)
- Retrieves a Google ID token via `GetGoogleIdOption`
- Exchanges the token for a Firebase credential via `GoogleAuthProvider`
- No email verification required (Google accounts are pre-verified)

### Error Handling
The `getAuthErrorMessage()` function translates raw Firebase error messages into user-friendly strings for cases like:
- Invalid credentials
- User not found
- Email already in use
- Badly formatted email
- Weak password
- Network errors
- Cancelled Google sign-in
- Developer configuration issues

---

## 8. Database (Room)

### Table: `products`

| Column             | Type      | Notes             |
|--------------------|-----------|-------------------|
| `productId`        | TEXT (PK) | Primary key       |
| `productName`      | TEXT      |                   |
| `cropType`         | TEXT      |                   |
| `farmLocation`     | TEXT      |                   |
| `harvestDate`      | TEXT      |                   |
| `quantity`         | TEXT      |                   |
| `quality`          | TEXT      |                   |
| `cultivationMethod`| TEXT      |                   |
| `createdAt`        | INTEGER   | Epoch millis      |
| `status`           | TEXT      | Enum name string  |
| `blockchainStatus` | TEXT      | Enum name string  |

- Migration strategy: `fallbackToDestructiveMigration()` (data is wiped on schema change — suitable for prototype stage)
- The repository handles `Product ↔ ProductEntity` conversion with safe enum parsing (falls back to defaults for unknown values)

---

## 9. QR Code System

### Generation (ZXing)
- The `QrCodeGenerator` object encodes the **Product ID** (e.g., `AGRI-20260813-A7F31C92`) into a QR code bitmap
- Default size: 800×800 pixels
- Uses UTF-8 encoding with 1px margin
- The bitmap is displayed in-app using Compose's `Image` composable with `asImageBitmap()`

### Scanning (Google Code Scanner / ML Kit)
- Uses `GmsBarcodeScanning.getClient()` to launch the scanner
- No camera permission management needed — Google handles it
- On successful scan, the raw value (Product ID) is extracted
- The app looks up the product in Room and navigates to the verification screen

---

## 10. Navigation System

### Route Map

| Route Constant                 | Screen                      | Parameters      |
|-------------------------------|-----------------------------|-----------------|
| `splash`                      | SplashScreen                | —               |
| `welcome`                     | WelcomeScreen               | —               |
| `sign_in`                     | SignInScreen                 | role (via state) |
| `sign_up`                     | SignUpScreen                 | —               |
| `farmer_dashboard`            | FarmerDashboardScreen        | —               |
| `add_product`                 | AddProductScreen             | —               |
| `product_created/{productId}` | ProductCreatedScreen         | productId       |
| `consumer_dashboard`          | QrScannerScreen              | —               |
| `qr_scanner`                  | QrScannerScreen              | —               |
| `product_verification/{id}`   | ProductVerificationScreen    | productId       |
| `transporter_dashboard`       | *(empty — placeholder)*      | —               |
| `processor_dashboard`         | *(empty — placeholder)*      | —               |
| `retailer_dashboard`          | *(empty — placeholder)*      | —               |
| `government_dashboard`        | *(empty — placeholder)*      | —               |

### Navigation Architecture
- Single `NavHost` in `AgriChainNavGraph`
- Start destination: `splash`
- Role is passed between Welcome → SignIn via `savedStateHandle`
- Product IDs are passed as URL path parameters
- Auth state (success/error messages) is passed via `savedStateHandle`
- `popUpTo` with `inclusive = true` is used to prevent back-stack clutter after sign-in

---

## 11. UI / Design System

### Design Language: Claymorphism

The app uses a **claymorphism / soft earthy** design language:
- Warm cream (`#F6F1E5`) and clay (`#FFFBF2`) backgrounds
- Agricultural greens (`#2E7D32` primary, `#1B5E20` forest)
- Soft sage accents (`#A8C8A0`)
- Large rounded corners (20–28dp)
- Tonal + shadow elevation on cards
- Dark theme with deep forest greens (`#101A14` background)

### Color Palette

| Color Name       | Light Mode  | Dark Mode   | Usage                |
|------------------|-------------|-------------|----------------------|
| Primary          | `#2E7D32`   | `#81C784`   | Buttons, headings    |
| Background       | `#F6F1E5`   | `#101A14`   | Screen background    |
| Surface          | `#FFFBF2`   | `#18251C`   | Cards                |
| On Background    | `#26342A`   | `#F4F1E8`   | Body text            |
| Error            | `#C94A4A`   | `#C94A4A`   | Error states         |
| Polygon Purple   | `#8247E5`   | `#8247E5`   | Blockchain accents   |

### Design Tokens (`DesignTokens.kt`)
Centralized constants for:
- **Spacing**: 4dp → 40dp (ExtraSmall → Section)
- **Corner Radii**: 10dp → 100dp (Small → Pill)
- **Component Heights**: Button (52dp), TextField (56dp), BottomNav (72dp)
- **Card Padding**: Compact (16dp), Standard (20dp), Large (24dp)

### Reusable Components
- **`AgriClayButton`** — Elevated button with claymorphism styling
- **`AgriClayCard`** — Card with tonal + shadow elevation
- **`AgriClayChip`** — Status/filter chip
- **`AgriSectionHeader`** — Section title with consistent styling

---

## 12. Supported Roles

The app defines **6 supply chain roles** on the Welcome screen:

| Role          | Icon | Description       | Dashboard Status     |
|---------------|------|-------------------|----------------------|
| **Farmer**    | 🌾   | Manage your crops | ✅ Fully implemented |
| **Transporter** | 🚚  | Transport produce | ⬜ Placeholder       |
| **Processor** | 🏭   | Process produce   | ⬜ Placeholder       |
| **Retailer**  | 🏪   | Sell produce      | ⬜ Placeholder       |
| **Consumer**  | 👤   | Trace products    | ✅ Functional (QR scan + verify) |
| **Government**| 🏛️   | Monitor supply    | ⬜ Placeholder       |

---

## 13. Current Limitations & Future Roadmap

### Currently Implemented ✅
- [x] Splash screen with animation
- [x] Role selection (6 roles)
- [x] Firebase email/password authentication with verification
- [x] Google Sign-In via Credential Manager
- [x] Farmer Dashboard with summary cards and product list
- [x] Add Product form with 7 fields + dropdowns
- [x] Unique Product ID generation (AGRI-YYYYMMDD-XXXXXXXX)
- [x] Room database persistence
- [x] QR code generation (ZXing)
- [x] QR code scanning (Google Code Scanner)
- [x] Product verification screen with traceability details
- [x] Light + Dark theme support
- [x] Claymorphism design system
- [x] Error message translation for Firebase auth

### Not Yet Implemented ⬜
- [ ] **Polygon blockchain integration** — Records are prepared but not yet submitted on-chain
- [ ] **Transporter/Processor/Retailer/Government dashboards** — Empty placeholders exist
- [ ] **User profile storage** — Name and phone from sign-up are logged but not persisted to Firestore
- [ ] **Password reset flow** — Backend function exists but the UI dialog is not wired
- [ ] **Product details screen** — `onProductSelected` callback is empty on Farmer Dashboard
- [ ] **Firestore cloud sync** — Data is local-only (Room). Cloud sync planned for multi-device
- [ ] **Supply chain tracking** — Status transitions (CREATED → HARVESTED → IN_TRANSIT → etc.)
- [ ] **Image upload** — Product photos / farm images
- [ ] **Offline-first sync** — Room ↔ Firestore reconciliation
- [ ] **ViewModels** — Proper lifecycle-aware state management
- [ ] **Dependency Injection** — Hilt or Koin
- [ ] **Unit & UI tests** — Test infrastructure exists but no tests written
- [ ] **Product search / filter** — Search by name, crop type, date range
- [ ] **Dashboard data from DB** — Farmer Dashboard currently shows hardcoded sample data

---

## 14. How to Build & Run

### Prerequisites
- **Android Studio** (latest stable)
- **JDK 11+**
- **Android SDK 37** installed
- A `google-services.json` file in `app/` (for Firebase)

### Steps

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd AgriChain
   ```

2. **Open in Android Studio:**
   - File → Open → Select the `AgriChain` folder

3. **Sync Gradle:**
   - Android Studio will automatically sync. If not, click "Sync Now".

4. **Configure Firebase:**
   - Ensure `app/google-services.json` is present and matches your Firebase project
   - Enable **Email/Password** and **Google** sign-in providers in Firebase Console

5. **Run the app:**
   - Select a device/emulator (API 26+)
   - Click ▶ Run

### Build Command (CLI)
```bash
./gradlew assembleDebug
```

---

*This document was generated from a full analysis of the AgriChain codebase as of August 2026.*
