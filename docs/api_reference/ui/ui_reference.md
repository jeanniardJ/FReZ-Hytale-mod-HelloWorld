# UI System Reference

Complete reference for Hytale's UI system - DSL syntax, Common.ui components, Java API, and all properties.

## UI DSL Syntax

Hytale uses a custom Domain Specific Language (DSL) for `.ui` files. This is **NOT** XML, XAML, or JSON.

### File Structure

```
// Comments start with //

// Import external UI files
$C = "../Common.ui";
$Other = "path/to/Other.ui";

// Define custom styles/constants
@MyConstant = 100;
@MyStyle = LabelStyle(FontSize: 16, TextColor: #ffffff);

// Root element (only ONE root element per file)
Group {
  // Properties use colon and end with semicolon
  PropertyName: value;

  // Child elements
  ChildElement {
    // ...
  }
}
```

### Syntax Rules

| Rule                          | Correct                | Incorrect          |
|-------------------------------|------------------------|--------------------|
| String values must be quoted  | `Text: "Hello";`       | `Text: Hello;`     |
| Properties end with semicolon | `Width: 100;`          | `Width: 100`       |
| Colors use hex format         | `#ffffff`, `#fff`      | `white`, `rgb()`   |
| Alpha in colors               | `#141c26(0.95)`        | `#141c26cc`        |
| Element IDs start with #      | `Label #Title { }`     | `Label Title { }`  |
| One root element per file     | Single `Group { }`     | Multiple roots     |
| Import syntax                 | `$C = "../Common.ui";` | `import Common.ui` |

### Import and Reference Syntax

```
// Import a UI file and assign to variable
$C = "../Common.ui";

// Use imported components with @
$C.@TextButton #MyButton {
  @Text = "Click";  // Template parameter
}

// Reference a style from imported file
Style: $C.@DefaultLabelStyle;

// Define local style
@LocalStyle = LabelStyle(FontSize: 20);
```

### Template Instantiation

```
// Basic template usage
$ImportVar.@TemplateName #ElementID {
  @Parameter = value;      // Template parameters use @
  RegularProperty: value;  // Regular properties use :
}

// Example with Common.ui button
$C.@SecondaryTextButton #SaveBtn {
  @Text = "Save";
  Anchor: (Width: 120, Height: 40);
}
```

## Element Types

### Group (Container)

The primary container element for layouts.

```
Group {
  Anchor: (Width: 400, Height: 300);
  Background: #141c26;
  LayoutMode: Top;
  Padding: (Full: 20);
  FlexWeight: 1;
  Visible: true;

  // Children go here
}
```

**Properties:**

| Property     | Type             | Description                                                            |
|--------------|------------------|------------------------------------------------------------------------|
| `Anchor`     | Anchor           | Size and position constraints                                          |
| `Background` | Color/PatchStyle | Background color or 9-slice image                                      |
| `LayoutMode` | Enum             | How children are arranged (e.g. `Top`, `Left`, `Overlay`)              |
| `Padding`    | Padding          | Internal spacing (Note: Use padding on parent, NOT margin on children) |
| `FlexWeight` | Number           | Flexible sizing weight                                                 |
| `Visible`    | Boolean          | Visibility                                                             |
| `Enabled`    | Boolean          | Interaction enabled                                                    |
| `Opacity`    | Number (0-1)     | Transparency                                                           |

### Label (Text Display)

```
Label {
  Text: "Hello World";
  Anchor: (Height: 30);
  Style: (FontSize: 16, TextColor: #ffffff);
}

// With ID for dynamic updates
Label #StatusText {
  Text: "Status: Ready";
  Anchor: (Height: 24);
  Style: @MyLabelStyle;
}
```

**Properties:**

| Property  | Type       | Description      |
|-----------|------------|------------------|
| `Text`    | String     | Display text     |
| `Style`   | LabelStyle | Text styling     |
| `Anchor`  | Anchor     | Size constraints |
| `Visible` | Boolean    | Visibility       |

### TextButton

```
TextButton #MyButton {
  Text: "Click Me";
  Anchor: (Width: 120, Height: 44);
  Style: @MyButtonStyle;
}
```

**Properties:**

| Property  | Type            | Description      |
|-----------|-----------------|------------------|
| `Text`    | String          | Button label     |
| `Style`   | TextButtonStyle | Button styling   |
| `Anchor`  | Anchor          | Size constraints |
| `Enabled` | Boolean         | Can be clicked   |
| `Visible` | Boolean         | Visibility       |

### Image (Deprecated Pattern)

> **⚠️ Warning:** The direct `<Image>` node is widely deprecated in current Hytale modding due to parser instability.

**Recommended Alternative:**
Use a `Group` with a `Background` property instead.

**Legacy Syntax (Avoid if crashing):**

```
Image {
  TexturePath: "Common/MyImage.png";
  Anchor: (Width: 64, Height: 64);
  Tint: #ffffff;
}
```

**Properties:**

| Property      | Type   | Description        |
|---------------|--------|--------------------|
| `TexturePath` | String | Path to texture    |
| `Anchor`      | Anchor | Size constraints   |
| `Tint`        | Color  | Color tint overlay |
| `Opacity`     | Number | Transparency       |

### TextField (Text Input)

```
TextField #NameInput {
  PlaceholderText: "Enter name...";
  Anchor: (Height: 38);
  Style: @DefaultInputFieldStyle;
  FlexWeight: 1;
}
```

**Properties:**

| Property          | Type            | Description            |
|-------------------|-----------------|------------------------|
| `Value`           | String          | Current text value     |
| `PlaceholderText` | String          | Placeholder when empty |
| `Style`           | InputFieldStyle | Input styling          |
| `Anchor`          | Anchor          | Size constraints       |
| `FlexWeight`      | Number          | Flexible width         |

### NumberField

```
NumberField #AmountInput {
  Value: 100;
  Anchor: (Width: 80, Height: 38);
}
```

### CheckBox

```
CheckBox #MyCheckbox {
  Value: true;
  Anchor: (Width: 22, Height: 22);
  Style: @DefaultCheckBoxStyle;
}
```

### Slider

```
Slider #VolumeSlider {
  Value: 0.75;
  MinValue: 0;
  MaxValue: 1;
  Anchor: (Height: 20);
  Style: @DefaultSliderStyle;
}
```

### DropdownBox

```
DropdownBox #MyDropdown {
  Anchor: (Width: 200, Height: 32);
  Style: @DefaultDropdownBoxStyle;
}
```

### ScrollView

```
ScrollView {
  Anchor: (Width: 300, Height: 200);
  Style: @DefaultScrollbarStyle;

  // Scrollable content
  Group {
    LayoutMode: Top;
    // Items...
  }
}
```

## Property Types

### Anchor

Controls size and position of elements.

```
Anchor: (
  Width: 200,        // Fixed width
  Height: 50,        // Fixed height
  Top: 10,           // Top margin
  Bottom: 10,        // Bottom margin
  Left: 10,          // Left margin
  Right: 10,         // Right margin
  Horizontal: 10,    // Left + Right
  Vertical: 10       // Top + Bottom
);

// Shorthand
Anchor: (Width: 200, Height: 50);
Anchor: (Height: 40);
```

### Padding

Internal spacing within containers.

```
Padding: (
  Full: 20,          // All sides
  Horizontal: 10,    // Left + Right
  Vertical: 10,      // Top + Bottom
  Top: 10,
  Bottom: 10,
  Left: 10,
  Right: 10
);

// Shorthand
Padding: (Full: 20);
Padding: (Horizontal: 10, Vertical: 5);
```

### LayoutMode

How children are arranged in a container.

| Value     | Description                   |
|-----------|-------------------------------|
| `Top`     | Stack vertically from top     |
| `Bottom`  | Stack vertically from bottom  |
| `Left`    | Stack horizontally from left  |
| `Right`   | Stack horizontally from right |
| `Center`  | Center all children           |
| `Overlay` | Stack on top of each other    |

```
Group {
  LayoutMode: Top;
  // Children stack vertically
}
```

### Color

```
// Hex colors
Background: #ffffff;      // White
Background: #fff;         // Short form
Background: #141c26;      // Dark blue

// With alpha
Background: #141c26(0.95); // 95% opacity
Background: #000000(0.5);  // 50% black overlay
```

### LabelStyle

Text styling properties.

```
@MyLabelStyle = LabelStyle(
  FontSize: 16,
  TextColor: #ffffff,
  RenderBold: true,
  RenderItalic: false,
  RenderUppercase: false,
  HorizontalAlignment: Center,  // Start, Center, End
  VerticalAlignment: Center,    // Top, Center, Bottom
  FontName: "Default",          // or "Secondary"
  LetterSpacing: 0,
  LineSpacing: 1.0,
  Wrap: true,
  Overflow: Ellipsis            // or Clip, Visible
);

// Inline style
Style: (FontSize: 14, TextColor: #96a9be, VerticalAlignment: Center);
```

### TextButtonStyle

Button state styling.

```
@MyButtonStyle = TextButtonStyle(
  Default: (
    Background: #3a7bd5,
    LabelStyle: (FontSize: 14, TextColor: #ffffff, RenderBold: true, HorizontalAlignment: Center, VerticalAlignment: Center)
  ),
  Hovered: (
    Background: #4a8be5,
    LabelStyle: (FontSize: 14, TextColor: #ffffff, RenderBold: true, HorizontalAlignment: Center, VerticalAlignment: Center)
  ),
  Pressed: (
    Background: #2a6bc5,
    LabelStyle: (FontSize: 14, TextColor: #ffffff, RenderBold: true, HorizontalAlignment: Center, VerticalAlignment: Center)
  ),
  Disabled: (
    Background: #555555,
    LabelStyle: (FontSize: 14, TextColor: #888888, RenderBold: true, HorizontalAlignment: Center, VerticalAlignment: Center)
  ),
  Sounds: @ButtonSounds
);
```

### PatchStyle (9-Slice)

For scalable backgrounds using 9-slice images.

```
PatchStyle(
  TexturePath: "Common/Button.png",
  Border: 12                    // All sides (Top, Bottom, Left, Right)
)

// Advanced Syntax (Different borders)
PatchStyle(
  TexturePath: "Common/Panel.png",
  HorizontalBorder: 80,         // Affects Left and Right only
  VerticalBorder: 12            // Affects Top and Bottom only
)
```

> **Best Practice:** Always use `PatchStyle` for UI backgrounds (buttons, panels) to prevent pixel stretching artifacts.
> A border of `10-12` is standard for Hytale's 32x textures.

## Common.ui Components

Import with: `$C = "../Common.ui";`

### Button Components

| Component                        | Parameters                     | Description                         |
|----------------------------------|--------------------------------|-------------------------------------|
| `@TextButton`                    | `@Text`, `@Anchor`, `@Sounds`  | Primary button (blue)               |
| `@SecondaryTextButton`           | `@Text`, `@Anchor`, `@Sounds`  | Secondary button (gray)             |
| `@TertiaryTextButton`            | `@Text`, `@Anchor`, `@Sounds`  | Tertiary button                     |
| `@CancelTextButton`              | `@Text`, `@Anchor`, `@Sounds`  | Destructive/cancel button (red)     |
| `@SmallDefaultTextButtonStyle`   | `@Text`, `@Anchor`, `@Sounds`  | Small primary                       |
| `@SmallSecondaryTextButtonStyle` | `@Text`, `@Anchor`, `@Sounds`  | Small secondary                     |
| `@DefaultButtonStyle`            | `@Anchor`, `@Sounds`           | Icon button                         |
| `@SecondaryButtonStyle`          | `@Anchor`, `@Sounds`, `@Width` | Secondary icon                      |
| `@TertiaryButtonStyle`           | `@Anchor`, `@Sounds`, `@Width` | Tertiary icon                       |
| `@CancelButtonStyle`             | `@Anchor`, `@Sounds`, `@Width` | Cancel icon                         |
| `@CloseButton`                   | -                              | Pre-positioned close button (32x32) |
| `@BackButton`                    | -                              | Pre-positioned back button          |

**Usage:**

```
$C.@TextButton #MyButton {
  @Text = "Click Me";
  Anchor: (Width: 150, Height: 44);
}

$C.@SecondaryTextButton #CancelBtn {
  @Text = "Cancel";
  Anchor: (Width: 100, Height: 44);
}
```

### Input Components

| Component            | Parameters          | Description                |
|----------------------|---------------------|----------------------------|
| `@TextField`         | `@Anchor`           | Text input (height: 38)    |
| `@NumberField`       | `@Anchor`           | Numeric input (height: 38) |
| `@DropdownBox`       | `@Anchor`           | Dropdown selector          |
| `@CheckBox`          | -                   | Checkbox only (22x22)      |
| `@CheckBoxWithLabel` | `@Text`, `@Checked` | Checkbox with label        |

**TextField Properties:**

* `PlaceholderText` - Placeholder text
* `FlexWeight` - Flexible width
* `Value` - Current text value

**NumberField Properties:**

* `Value` - Numeric value
* `PlaceholderText` - Placeholder text

**CheckBoxWithLabel Properties:**

* `@Text` - Label text
* `@Checked` - Initial state (true/false)

**Usage:**

```
$C.@TextField #NameInput {
  FlexWeight: 1;
  PlaceholderText: "Enter name...";
}

$C.@NumberField #AmountInput {
  Anchor: (Width: 100);
  Value: 50;
}

$C.@CheckBoxWithLabel #EnableOption {
  @Text = "Enable feature";
  @Checked = true;
  Anchor: (Height: 28);
}

$C.@DropdownBox #MyDropdown {
  Anchor: (Width: 200, Height: 32);
}
```

### Container Components

| Component             | Parameters                        | Description                         |
|-----------------------|-----------------------------------|-------------------------------------|
| `@Container`          | `@ContentPadding`, `@CloseButton` | Styled container with title area    |
| `@DecoratedContainer` | `@ContentPadding`, `@CloseButton` | Container with decorative borders   |
| `@Panel`              | -                                 | Simple panel with border            |
| `@PageOverlay`        | -                                 | Semi-transparent background overlay |

**Container Structure:**

* `#Title` - Title area (height: 38)
* `#Content` - Content area with padding
* `#CloseButton` - Optional close button

**Usage:**

```
$C.@Container {
  @CloseButton = true;
  Anchor: (Width: 400, Height: 300);

  // Title goes in #Title
  // Content goes in #Content
}
```

### Layout Components

| Component                | Parameters | Description                           |
|--------------------------|------------|---------------------------------------|
| `@ContentSeparator`      | `@Anchor`  | Horizontal line separator (height: 1) |
| `@VerticalSeparator`     | -          | Vertical separator (width: 6)         |
| `@HeaderSeparator`       | -          | Header section separator              |
| `@PanelSeparatorFancy`   | `@Anchor`  | Decorative panel separator            |
| `@ActionButtonContainer` | -          | Container for action buttons          |
| `@ActionButtonSeparator` | -          | Space between action buttons          |

**Usage:**

```
$C.@ContentSeparator { Anchor: (Height: 1); }

Group { Anchor: (Height: 16); }  // Vertical spacer

$C.@VerticalSeparator {}
```

### Text Components

| Component     | Parameters            | Description                         |
|---------------|-----------------------|-------------------------------------|
| `@Title`      | `@Text`, `@Alignment` | Styled title label                  |
| `@Subtitle`   | `@Text`               | Styled subtitle label               |
| `@TitleLabel` | -                     | Large centered title (FontSize: 40) |
| `@PanelTitle` | `@Text`, `@Alignment` | Panel section title                 |

**Usage:**

```
$C.@Title {
  @Text = "My Title";
  @Alignment = Center;
  Anchor: (Height: 38);
}

$C.@Subtitle {
  @Text = "Subtitle text";
}
```

### Utility Components

| Component         | Parameters     | Description                       |
|-------------------|----------------|-----------------------------------|
| `@DefaultSpinner` | `@Anchor`      | Loading spinner animation (32x32) |
| `@HeaderSearch`   | `@MarginRight` | Search input with icon            |
| `@BackButton`     | -              | Pre-positioned back button        |

**Usage:**

```
$C.@DefaultSpinner {
  Anchor: (Width: 32, Height: 32);
}
```

### Common.ui Style Constants

```
@PrimaryButtonHeight = 44;
@SmallButtonHeight = 32;
@BigButtonHeight = 48;
@ButtonPadding = 24;
@DefaultButtonMinWidth = 172;
@ButtonBorder = 12;
@DropdownBoxHeight = 32;
@TitleHeight = 38;
@InnerPaddingValue = 8;
@FullPaddingValue = 17;
@DisabledColor = #797b7c;
```

## Common.ui Styles

### Button Styles

| Style                            | Description                     |
|----------------------------------|---------------------------------|
| `@DefaultTextButtonStyle`        | Primary button style            |
| `@SecondaryTextButtonStyle`      | Secondary button style          |
| `@TertiaryTextButtonStyle`       | Tertiary button style           |
| `@CancelTextButtonStyle`         | Destructive/cancel button style |
| `@SmallDefaultTextButtonStyle`   | Small primary button style      |
| `@SmallSecondaryTextButtonStyle` | Small secondary button style    |
| `@DefaultButtonStyle`            | Button without text             |
| `@SecondaryButtonStyle`          | Secondary button without text   |
| `@TertiaryButtonStyle`           | Tertiary button without text    |
| `@CancelButtonStyle`             | Cancel button without text      |

### Label Styles

| Style                      | Properties                                                        |
|----------------------------|-------------------------------------------------------------------|
| `@DefaultLabelStyle`       | FontSize: 16, TextColor: #96a9be                                  |
| `@DefaultButtonLabelStyle` | FontSize: 17, TextColor: #bfcdd5, Bold, Uppercase, Center         |
| `@TitleStyle`              | FontSize: 15, Bold, Uppercase, TextColor: #b4c8c9, Secondary font |
| `@SubtitleStyle`           | FontSize: 15, Uppercase, TextColor: #96a9be                       |
| `@PopupTitleStyle`         | FontSize: 38, Bold, Uppercase, Center, LetterSpacing: 2           |

### Input Styles

| Style                                | Description                                 |
|--------------------------------------|---------------------------------------------|
| `@DefaultInputFieldStyle`            | Default text input style                    |
| `@DefaultInputFieldPlaceholderStyle` | Placeholder text style (TextColor: #6e7da1) |
| `@InputBoxBackground`                | Input field background                      |
| `@InputBoxHoveredBackground`         | Input field hover state                     |
| `@InputBoxSelectedBackground`        | Input field selected state                  |

### Other Styles

| Style                      | Description          |
|----------------------------|----------------------|
| `@DefaultScrollbarStyle`   | Scrollbar styling    |
| `@DefaultCheckBoxStyle`    | Checkbox styling     |
| `@DefaultDropdownBoxStyle` | Dropdown styling     |
| `@DefaultSliderStyle`      | Slider styling       |
| `@DefaultTextTooltipStyle` | Tooltip styling      |
| `@DefaultColorPickerStyle` | Color picker styling |

## Java API Reference

### UICommandBuilder

Builds commands to manipulate UI elements.

```java
UICommandBuilder cmd = new UICommandBuilder();
```

**Methods:**

| Method                                                 | Description                  |
|--------------------------------------------------------|------------------------------|
| `append(String documentPath)`                          | Load a UI document           |
| `append(String selector, String documentPath)`         | Append template to container |
| `appendInline(String selector, String document)`       | Append inline UI definition  |
| `insertBefore(String selector, String documentPath)`   | Insert before element        |
| `insertBeforeInline(String selector, String document)` | Insert inline before         |
| `set(String selector, String value)`                   | Set string property          |
| `set(String selector, boolean value)`                  | Set boolean property         |
| `set(String selector, int value)`                      | Set integer property         |
| `set(String selector, float value)`                    | Set float property           |
| `set(String selector, double value)`                   | Set double property          |
| `set(String selector, Message message)`                | Set localized message        |
| `set(String selector, Value<T> ref)`                   | Set document reference       |
| `setNull(String selector)`                             | Set to null                  |
| `setObject(String selector, Object data)`              | Set encodable object         |
| `set(String selector, T[] data)`                       | Set array                    |
| `set(String selector, List<T> data)`                   | Set list                     |
| `clear(String selector)`                               | Remove all children          |
| `remove(String selector)`                              | Remove element               |
| `getCommands()`                                        | Get compiled commands        |

**Examples:**

```java
// Load layout
cmd.append("YourPlugin/MyPage.ui");

// Set text
cmd.set("#Title.Text", "Hello World");

// Set visibility
cmd.set("#Panel.Visible", false);

// Set numeric value
cmd.set("#Slider.Value", 0.5f);

// Clear container
cmd.clear("#ItemList");

// Append to container
cmd.append("#ItemList", "YourPlugin/ListItem.ui");
```

### UIEventBuilder

Builds event bindings for UI interactions.

```java
UIEventBuilder evt = new UIEventBuilder();
```

**Methods:**

| Method                                                  | Description           |
|---------------------------------------------------------|-----------------------|
| `addEventBinding(type, selector)`                       | Basic event binding   |
| `addEventBinding(type, selector, locksInterface)`       | With interface lock   |
| `addEventBinding(type, selector, data)`                 | With event data       |
| `addEventBinding(type, selector, data, locksInterface)` | Full signature        |
| `getEvents()`                                           | Get compiled bindings |

**Examples:**

```java
// Simple button event
evt.addEventBinding(
    CustomUIEventBindingType.Activating,
    "#MyButton",
    new EventData().append("Action", "click"),
    false  // locksInterface
);

// Capture input value
evt.addEventBinding(
    CustomUIEventBindingType.Activating,
    "#SubmitButton",
    new EventData()
        .append("Action", "submit")
        .append("@Username", "#UsernameInput.Value"),  // @ prefix captures value
    false
);
```

### CustomUIEventBindingType

| Type                            | ID | Description                   |
|---------------------------------|----|-------------------------------|
| `Activating`                    | 0  | Click or Enter key            |
| `RightClicking`                 | 1  | Right mouse click             |
| `DoubleClicking`                | 2  | Double click                  |
| `MouseEntered`                  | 3  | Mouse enters element          |
| `MouseExited`                   | 4  | Mouse leaves element          |
| `ValueChanged`                  | 5  | Input/slider value changed    |
| `ElementReordered`              | 6  | Element was reordered         |
| `Validating`                    | 7  | Form validation               |
| `Dismissing`                    | 8  | Page being dismissed          |
| `FocusGained`                   | 9  | Element gained focus          |
| `FocusLost`                     | 10 | Element lost focus            |
| `KeyDown`                       | 11 | Key pressed                   |
| `MouseButtonReleased`           | 12 | Mouse button released         |
| `SlotClicking`                  | 13 | Inventory slot clicked        |
| `SlotDoubleClicking`            | 14 | Inventory slot double-clicked |
| `SlotMouseEntered`              | 15 | Mouse entered slot            |
| `SlotMouseExited`               | 16 | Mouse exited slot             |
| `DragCancelled`                 | 17 | Drag operation cancelled      |
| `Dropped`                       | 18 | Item dropped                  |
| `SlotMouseDragCompleted`        | 19 | Slot drag completed           |
| `SlotMouseDragExited`           | 20 | Slot drag exited              |
| `SlotClickReleaseWhileDragging` | 21 | Click released while dragging |
| `SlotClickPressWhileDragging`   | 22 | Click pressed while dragging  |
| `SelectedTabChanged`            | 23 | Tab selection changed         |

### EventData

Container for event data key-value pairs.

```java
// Create with data
EventData data = new EventData()
    .append("Action", "click")
    .append("ItemId", "sword_01")
    .append("@Value", "#Input.Value");  // @ prefix captures UI value

// Factory method
EventData data = EventData.of("Action", "click");

// Access data
Map<String, String> events = data.events();
```

### Value<T>

Reference values from UI documents.

```java
// Create document reference
Value<String> styleRef = Value.ref("Pages/Styles.ui", "SelectedStyle");

// Create direct value
Value<String> directValue = Value.of("Hello");

// Use in command
cmd.set("#Element.Style", styleRef);
```

### InteractiveCustomUIPage<T>

Base class for interactive UI pages.

```java
public class MyPage extends InteractiveCustomUIPage<MyPage.MyEventData> {

    public MyPage(PlayerRef playerRef) {
        super(
            playerRef,                    // Player reference
            CustomPageLifetime.CanDismiss, // How page can be closed
            MyEventData.CODEC              // Event data codec
        );
    }

    @Override
    public void build(
        Ref<EntityStore> ref,
        UICommandBuilder cmd,
        UIEventBuilder evt,
        Store<EntityStore> store
    ) {
        // Called when page opens
    }

    @Override
    public void handleDataEvent(
        Ref<EntityStore> ref,
        Store<EntityStore> store,
        MyEventData data
    ) {
        // Called when player interacts
    }
}
```

### CustomPageLifetime

| Value                                 | Description              |
|---------------------------------------|--------------------------|
| `CantClose`                           | Cannot be closed by user |
| `CanDismiss`                          | Can press ESC to close   |
| `CanDismissOrCloseThroughInteraction` | ESC or button click      |

### BasicCustomUIPage

Simplified base class for non-interactive pages.

```java
public class StaticPage extends BasicCustomUIPage {

    @Override
    public void build(UICommandBuilder cmd) {
        cmd.append("YourPlugin/StaticPage.ui");
    }
}
```

### BuilderCodec

For serializing/deserializing event data.

```java
public static class MyEventData {
    public static final BuilderCodec<MyEventData> CODEC = BuilderCodec.builder(
            MyEventData.class, MyEventData::new
    )
    // String field
    .append(new KeyedCodec<>("Action", Codec.STRING),
        (e, v) -> e.action = v,
        e -> e.action)
    .add()
    // Another field
    .append(new KeyedCodec<>("ItemId", Codec.STRING),
        (e, v) -> e.itemId = v,
        e -> e.itemId)
    .add()
    .build();

    private String action;
    private String itemId;

    public MyEventData() {}  // Required no-arg constructor
}
```

The field names in the codec **must match** the keys in `EventData.append()`.

## Troubleshooting

### "Failed to load CustomUI documents"

**Cause**: Syntax error in your `.ui` file.

**Solutions**:

1. Make sure all text values are quoted: `Text: "Hello";` not `Text: Hello;`
2. Check all properties end with semicolon
3. Verify color format: `#ffffff` or `#fff`
4. Ensure Common.ui import is correct: `$C = "../Common.ui";`

### "Failed to apply CustomUI event bindings"

**Cause**: Element ID in Java doesn't match the `.ui` file.

**Solutions**:

1. Verify the element ID exists in your `.ui` file
2. Check spelling: `#MyButton` in Java must match `#MyButton` in `.ui`
3. For Common.ui components, the ID goes after the component: `$C.@TextButton #MyButton`

### "Selected element in CustomUI command was not found"

**Cause**: Incorrect selector pattern for dynamically appended templates.

**Understanding**: When you append a template to a container, the template itself **becomes** the element at that index.
You don't navigate to a child inside it.

**Wrong:**

```java
// This looks for #Button INSIDE the element at index 0
cmd.set("#Container[0] #Button.Text", "Hello");  // WRONG!
```

**Correct:**

```java
// The element at index 0 IS the button
cmd.set("#Container[0].Text", "Hello");  // CORRECT!
```

**Full pattern:**

```java
// Append template
cmd.append("#WorldButtonsContainer", "YourPlugin/WorldButton.ui");

// The appended template IS #WorldButtonsContainer[0]
String selector = "#WorldButtonsContainer[0]";
cmd.set(selector + ".Text", "World Name");

// Event binding targets the element directly
evt.addEventBinding(
    CustomUIEventBindingType.Activating,
    selector,  // NOT selector + " #Button"
    new EventData().append("Action", "click"),
    false
);
```

### Template parameters vs Properties

**Template parameters** (`@Text`) are set when instantiating a component and define initial values.

**Properties** (`.Text`) are the actual runtime properties you modify from Java.

| Context             | Syntax       | Example                           |
|---------------------|--------------|-----------------------------------|
| In `.ui` file       | `@Parameter` | `@Text = "Default";`              |
| In Java (set value) | `.Property`  | `cmd.set("#Button.Text", "New");` |

**Important**: You cannot use `@Text` in Java selectors. Always use `.Text`:

```java
// WRONG
cmd.set("#Button.@Text", "Hello");

// CORRECT
cmd.set("#Button.Text", "Hello");
```

### Player disconnects when opening page

**Cause**: The `.ui` file has a parse error or doesn't exist.

**Solutions**:

1. Check the file path matches: `"YourPlugin/MyPage.ui"` corresponds to `Common/UI/Custom/YourPlugin/MyPage.ui`
2. Review UI file syntax carefully
3. Start with a minimal working example and add complexity gradually

### UI opens but buttons don't work

**Cause**: Event bindings not set up correctly.

**Solutions**:

1. Ensure `evt.addEventBinding()` is called in `build()`
2. Verify the selector matches the element ID: `"#MyButton"`
3. Check that `handleDataEvent()` handles the action value

## Dynamic UI Patterns

This section covers how to create dynamic lists, update UI at runtime, and work with templates.

### Creating a Dynamic List

**Template file** (`YourPlugin/ListItem.ui`):

```
$C = "../Common.ui";

$C.@SecondaryTextButton {
  @Text = "Item";
  Anchor: (Height: 40);
}
```

**Layout file** (`YourPlugin/MyPage.ui`):

```
$C = "../Common.ui";

Group {
  Anchor: (Width: 400, Height: 300);
  Background: #141c26(0.98);
  LayoutMode: Top;
  Padding: (Full: 20);

  Label {
    Text: "Select an item:";
    Anchor: (Height: 30);
    Style: (FontSize: 16, TextColor: #ffffff);
  }

  Group #ItemList {
    FlexWeight: 1;
    LayoutMode: Top;
    Background: #0d1218(0.5);
    Padding: (Full: 8);
  }
}
```

**Java code**:

```java
@Override
public void build(...) {
    cmd.append(LAYOUT);

    String[] items = {"Apple", "Banana", "Cherry"};

    for (int i = 0; i < items.length; i++) {
        // Append template - this creates element at index i
        cmd.append("#ItemList", "YourPlugin/ListItem.ui");

        // The template IS the element at index i
        String selector = "#ItemList[" + i + "]";

        // Set text directly on the element
        cmd.set(selector + ".Text", items[i]);

        // Bind event directly to the element
        evt.addEventBinding(
            CustomUIEventBindingType.Activating,
            selector,
            new EventData().append("Action", "select").append("Index", String.valueOf(i)),
            false
        );
    }
}
```

### Key Rules for Dynamic Templates

1. **Appended template = element at index**
   ```java
   cmd.append("#Container", "template.ui");
   // Creates #Container[0], then #Container[1], etc.
   ```

2. **Access properties directly**
   ```java
   cmd.set("#Container[0].Text", "value");    // CORRECT
   cmd.set("#Container[0] #ID.Text", "value"); // WRONG (unless template has nested IDs)
   ```

3. **Event bindings target the element**
   ```java
   evt.addEventBinding(..., "#Container[0]", ...);  // CORRECT
   ```

4. **Clear before rebuilding**
   ```java
   cmd.clear("#Container");  // Remove all children
   // Then re-append
   ```

### Updating UI Without Rebuilding

You can send incremental updates:

```java
@Override
public void handleDataEvent(...) {
    UICommandBuilder cmd = new UICommandBuilder();

    // Update specific elements
    cmd.set("#StatusLabel.Text", "Updated!");
    cmd.set("#Counter.Text", String.valueOf(counter++));


    // Send update ONLY (incremental patch)
    this.sendUpdate(cmd, false);
}
```

> **Performance Tip:** Avoid calling `builder.append()` repeatedly in update loops. This forces a full document parse
> and can cause the client to disconnect with "Failed to load CustomUI documents". Always use
`this.sendUpdate(cmd, false)` for dynamic values.

### Clearing and Refreshing

```java
// Clear a container
cmd.clear("#ItemList");

// Or rebuild the entire page
this.rebuild();
```

## Supported Object Types

Objects that can be passed to `setObject()`:

| Type                | Description               |
|---------------------|---------------------------|
| `Area`              | Rectangle area            |
| `ItemGridSlot`      | Inventory slot data       |
| `ItemStack`         | Item with amount/quality  |
| `LocalizableString` | Translated string         |
| `PatchStyle`        | 9-slice background        |
| `DropdownEntryInfo` | Dropdown option           |
| `Anchor`            | Size/position constraints |
