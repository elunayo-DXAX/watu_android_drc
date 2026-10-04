# WatuDrc — Changelog

<!-- gchangelog:last-logged-commit=0fa67480cf6b631213266a7e88ffd4514aa83c83 -->

## v1.1.79 (39) — 2026-09-21

### Fix LanguageActivity's kill-app-vs-continue check
Replaced the `retrieveLanguageFromPrefs().isEmpty()` check (dead code, since that getter always
self-populates a default) with a check based on `IsRegistered`, `UserPassword`, and `UserKey` — routes to
`MainActivity` only when real user/market data is stored, otherwise closes the app.

### Default market to DRC instead of Nigeria
`retrieveUserCountryCodeFromPrefs()` now falls back to `"cd"` instead of `"ng"` when no country code has been
stored yet.

### Add missing French translation
Added the French translation for `enterpindigits` (PIN-entry validation toast), previously falling back to
English.

### Clean up duplicate release build output directory
Removed the stray nested `release/release/` baseline-profile and metadata files, and refreshed the top-level
release build outputs to match the current build.

---

## v1.1.78 (38) — 2026-09-21

### Modernize LanguageActivity back navigation
Replaced the deprecated `onBackPressed()` override with an `OnBackPressedCallback` registered on the
`OnBackPressedDispatcher`, still routing back navigation through the existing `goToPage()` helper.

---

## v1.1.78 (38) — 2026-09-18

### Fix notification deep links reverting to Home
Tapping "Pay now" or "View Transactions" inside an in-app notification dialog switched tabs and then
immediately reverted to Home; removed the erroneous `goBack()` calls causing the revert.

### Fix payment device-icon crash
Fixed a `NullPointerException` in `FragmentPayment.setDeviceIcon()` by moving the device-icon feature to
`FragmentPaymentProcess`, where the matching layout views actually exist.

### Add back button to confirm-payment dialog
Made the previously hidden back icon visible on the confirm-payment dialog and wired it to dismiss the dialog.

### Route LanguageActivity back navigation through MainActivity
`onBackPressed()`/`goBack()` now call `goToPage()` instead of `finish()`, so backing out of the language
screen lands on Home instead of an arbitrary prior activity.

### Fix OTP screen copy
Fixed a typo ("passode" → "passcode") and clarified "Choose how to receive" to "Choose how to receive OTP" in
both English and French.

---
