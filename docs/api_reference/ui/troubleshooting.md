# UI Troubleshooting

Debugging guide for Hytale Custom UI errors (Red X, parser crashes, etc.)

## 1. The "Unknown Node Type" Crash

**Error Log:**
`HytaleClient.Interface.UI.Markup.TextParser+TextParserException: Failed to parse file ... – Unknown node type: Image`

**Why it happens:**
The Hytale client parser in certain versions does not support the `<Image>` tag as a standalone element, or only
supports it within specific parent containers.

**The Fix:**
Standardize your UI to use **Groups with Backgrounds** instead of Image nodes. This is functionally identical but
parser-safe.

* ❌ **Avoid:** `Image { TexturePath: "..."; }`
* ✅ **Use:** `Group { Background: ( TexturePath: "..." ); }`

## 2. The "Failed to Apply CustomUI" Crash Loop

**Symptoms:**

* Game stutters every X seconds.
* Client eventually disconnects with "Failed to load CustomUI documents".
* Console spam of packet errors.

**Why it happens:**
Your Java code is resending the entire UI file (`builder.append(...)`) on every update tick (e.g., inside a scheduled
task). Reloading the DOM repeatedly corrupts the client's UI state.

**The Fix:**
Implement the **Load-Update Separation Pattern**:

1. **Initialize:** Send the structure once.
2. **Update:** Send only variable changes using `update(false, builder)`.

## 3. The "Red X" (Asset Resolution Failure)

**Symptoms:**

* UI loads physically but all textures are replaced by large red crosses.

**Why it happens:**
The client cannot find the file at the specified path. This is usually a pathing context issue.

**The Fix:**

1. **Locality:** Move assets to a subfolder (e.g., `Assets/`) **directly inside** the folder containing your `.ui` file.
2. **Relative Pathing:** Reference them simply as `"Assets/MyTexture.png"`.
3. **Manifest:** Ensure `manifest.json` has `"IncludesAssetPack": true`.

## 5. "Failed to load CustomUI documents" (Parsing Error)

**Symptoms:**
* Client disconnects immediately upon opening the UI.
* Log shows `Crash - Failed to load CustomUI documents`.

**Causes & Fixes:**

1.  **Comments (`//`)**: The parser may fail if comments are present, especially on the same line as properties.
    *   **Fix**: Remove all comments from `.ui` files.

2.  **Broken Imports**: Defining a variable that points to a missing file (e.g., `$C = "../Common.ui";`) causes a crash, even if the variable is unused.
    *   **Fix**: Remove or comment out imports to missing files.

3.  **Missing Style Types**: Defining a complex style without its type wrapper causes a parsing error.
    *   ❌ **Wrong**: `Style: ( Default: (...) );`
    *   ✅ **Correct**: `Style: TextButtonStyle( Default: (...) );`

4.  **Inline Styles**: Using variables (`@Style`) can sometimes be unstable.
    *   **Fix**: Define styles inline directly in the component: `Style: LabelStyle(FontSize: 14, ...);`