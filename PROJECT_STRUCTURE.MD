# 📂 CẤU TRÚC PROJECT CALCULATOR - GIẢI THÍCH CHI TIẾT

## 🎯 TỔNG QUAN CẤU TRÚC

```
Calculator Project/
├── 📁 androidTest/          → Test trên device/emulator
├── 📁 main/                 → SOURCE CODE CHÍNH ⭐
│   ├── 📁 java/             → Code Java
│   │   └── com.example.calculator/
│   │       ├── 📁 model/        → Data & Logic
│   │       ├── 📁 presenter/    → Controller
│   │       ├── 📁 ui/           → Activities
│   │       ├── 📁 utils/        → Utilities
│   │       └── 📄 CalculatorContract.java
│   └── 📁 res/              → Resources (layouts, colors)
├── 📁 test [unitTest]/      → Unit tests
├── 📄 .gitignore            → Git ignore rules
└── 📄 AndroidManifest.xml   → App configuration
```

---

## 📂 CHI TIẾT TỪNG TẦNG

### 🏗️ **TẦNG 1: androidTest**
```
📁 androidTest/
└── 📁 java/
    └── com.example.calculator/
        └── ExampleInstrumentedTest.java
```

**Mục đích:**
- Test app trên **thiết bị thật** hoặc **emulator**
- Test UI, interactions, database

**Ví dụ:**
```java
// Test click button trên emulator
@Test
public void testButtonClick() {
    onView(withId(R.id.btn7)).perform(click());
    onView(withId(R.id.tvResult)).check(matches(withText("7")));
}
```

**Khi nào dùng:**
- Test UI flows
- Test với Espresso
- Test database, SharedPreferences
- Test navigation

**Chạy test:**
```bash
./gradlew connectedAndroidTest
```

---

### 🏗️ **TẦNG 2: main (QUAN TRỌNG NHẤT) ⭐**

Đây là **SOURCE CODE CHÍNH** của app!

```
📁 main/
├── 📁 java/                        → Code logic
│   └── com.example.calculator/
│       ├── 📁 model/               → Tầng 1: Data & Business Logic
│       │   ├── CalculatorConstants.java
│       │   └── CalculatorModel.java
│       ├── 📁 presenter/           → Tầng 2: Controller
│       │   └── CalculatorPresenter.java
│       ├── 📁 ui/                  → Tầng 3: View (UI)
│       │   └── MainActivity.java
│       ├── 📁 utils/               → Utilities
│       │   └── NumberFormatter.java
│       └── CalculatorContract.java → Interface kết nối
└── 📁 res/                         → Resources
    ├── 📁 layout/                  → XML layouts
    ├── 📁 values/                  → Colors, strings, styles
    ├── 📁 drawable/                → Images, icons
    └── 📁 mipmap/                  → App icons
```

---

### 📁 **TẦNG 2.1: model/ (Data & Logic Layer)**

```
📁 model/
├── 📄 CalculatorConstants.java     → Hằng số, operators
└── 📄 CalculatorModel.java         → Business logic
```

#### **CalculatorConstants.java**

**Vai trò:** Lưu trữ **constants** và **operations**

**Nội dung:**
```java
public class CalculatorConstants {
    // Operators
    public static final String ADD = "+";
    public static final String SUBTRACT = "−";
    public static final String MULTIPLY = "×";
    public static final String DIVIDE = "÷";
    
    // Operations map
    public static final Map<String, BiFunction<Double, Double, Double>> OPERATION_MAP = new HashMap<>() {{
        put(ADD, Double::sum);
        put(SUBTRACT, (a, b) -> a - b);
        put(MULTIPLY, (a, b) -> a * b);
        put(DIVIDE, (a, b) -> b != 0 ? a / b : Double.NaN);
    }};
    
    // Scientific functions
    public static final Map<String, Function<Double, Double>> SCIENTIFIC_MAP = new HashMap<>() {{
        put("sin", v -> Math.sin(Math.toRadians(v)));
        put("cos", v -> Math.cos(Math.toRadians(v)));
        // ...
    }};
}
```

**Tại sao cần:**
- Tập trung constants ở 1 chỗ
- Dễ maintain
- Tránh hardcode

---

#### **CalculatorModel.java**

**Vai trò:** Xử lý **business logic** - não của app!

**Chức năng:**
```java
public class CalculatorModel {
    private double firstNumber = 0;
    private String operator = "";
    private String currentNumber = "";
    
    // Handle input
    public void handleNumber(String number) { ... }
    public void handleOperator(String op) { ... }
    public void handleEquals() { ... }
    
    // Calculations
    public double performCalculation() { ... }
    
    // Scientific
    public void handleScientific(String function) { ... }
    
    // Get results
    public String getResultDisplay() { ... }
    public String getExpression() { ... }
}
```

**Nhiệm vụ:**
- Lưu trữ state (currentNumber, operator)
- Thực hiện tính toán
- Format numbers
- Validate input
- KHÔNG biết gì về UI!

**Ví dụ flow:**
```
User nhấn "7" 
→ handleNumber("7")
→ currentNumber = "7"
→ return "7"
```

---

### 📁 **TẦNG 2.2: presenter/ (Controller Layer)**

```
📁 presenter/
└── 📄 CalculatorPresenter.java
```

#### **CalculatorPresenter.java**

**Vai trò:** **Cầu nối** giữa View (UI) và Model (Logic)

**Chức năng:**
```java
public class CalculatorPresenter implements CalculatorContract.Presenter {
    private final CalculatorContract.ViewInterface view;
    private final CalculatorModel model;
    
    public CalculatorPresenter(CalculatorContract.ViewInterface view) {
        this.view = view;
        this.model = new CalculatorModel();
    }
    
    @Override
    public void onButtonClick(View v, String buttonText) {
        int id = v.getId();
        
        // Phân loại button
        if (id == R.id.btn7) {
            model.handleNumber("7");
        } else if (id == R.id.btnAdd) {
            model.handleOperator("+");
        }
        
        // Update UI
        view.updateResult(model.getResultDisplay());
        view.updateExpression(model.getExpression());
    }
}
```

**Nhiệm vụ:**
- Nhận sự kiện từ View (button clicks)
- Gọi Model xử lý logic
- Update View với kết quả
- KHÔNG chứa business logic!
- KHÔNG chứa UI code!

**Flow hoàn chỉnh:**
```
User click button "7"
    ↓
MainActivity.onClick()
    ↓
Presenter.onButtonClick(view, "7")
    ↓
Model.handleNumber("7")
    ↓
Model returns "7"
    ↓
Presenter gọi view.updateResult("7")
    ↓
MainActivity.updateResult("7")
    ↓
TextView shows "7"
```

---

### 📁 **TẦNG 2.3: ui/ (View Layer)**

```
📁 ui/
└── 📄 MainActivity.java
```

#### **MainActivity.java**

**Vai trò:** **Hiển thị UI** và **nhận input**

**Chức năng:**
```java
public class MainActivity extends AppCompatActivity 
        implements CalculatorContract.ViewInterface {
    
    private TextView tvResult, tvExpression;
    private CalculatorPresenter presenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize views
        tvResult = findViewById(R.id.tvResult);
        tvExpression = findViewById(R.id.tvExpression);
        
        // Create presenter
        presenter = new CalculatorPresenter(this);
        
        // Setup buttons
        setupButtonListeners();
    }
    
    // Implement interface methods
    @Override
    public void updateResult(String result) {
        tvResult.setText(result);
    }
    
    @Override
    public void updateExpression(String expression) {
        tvExpression.setText(expression);
    }
    
    @Override
    public void showError(String message) {
        tvExpression.setText(message);
    }
}
```

**Nhiệm vụ:**
- Hiển thị UI (layout, TextView, Button)
- Nhận user input (button clicks)
- Gọi Presenter khi có sự kiện
- Update UI theo lệnh của Presenter
- KHÔNG chứa logic tính toán!

---

### 📁 **TẦNG 2.4: utils/ (Utilities)**

```
📁 utils/
└── 📄 NumberFormatter.java
```

#### **NumberFormatter.java**

**Vai trò:** **Helper functions** dùng chung

**Chức năng:**
```java
public class NumberFormatter {
    public static String format(double number) {
        if (Double.isNaN(number)) return "Error";
        if (Double.isInfinite(number)) return "∞";
        
        // Format với DecimalFormat
        String result = DECIMAL_FORMAT.format(number);
        
        // Remove trailing zeros
        result = result.replaceAll("0+$", "").replaceAll("\\.$", "");
        
        return result;
    }
}
```

**Nhiệm vụ:**
- Format numbers
- Validation helpers
- String utilities
- Date/time helpers
- Constants

**Tại sao cần folder utils:**
- Code reuse
- DRY principle
- Dễ test
- Dễ maintain

---

### 📄 **CalculatorContract.java (Interface)**

```
📄 CalculatorContract.java
```

**Vai trò:** **Định nghĩa giao tiếp** giữa View và Presenter

**Nội dung:**
```java
public interface CalculatorContract {
    
    // View phải implement các methods này
    interface ViewInterface {
        void updateResult(String result);
        void updateExpression(String expression);
        void showError(String message);
    }
    
    // Presenter phải implement method này
    interface Presenter {
        void onButtonClick(View v, String buttonText);
    }
}
```

**Tại sao cần:**
- **Contract** = Hợp đồng giữa View và Presenter
- Đảm bảo View có đủ methods cần thiết
- Đảm bảo Presenter implement đúng interface
- Dễ test (mock)
- Dễ thay đổi implementation

**Ví dụ:**
```java
// MainActivity PHẢI implement ViewInterface
public class MainActivity implements CalculatorContract.ViewInterface {
    // Nếu thiếu updateResult() → COMPILE ERROR
    @Override
    public void updateResult(String result) { ... }
}

// Presenter PHẢI implement Presenter
public class CalculatorPresenter implements CalculatorContract.Presenter {
    // Nếu thiếu onButtonClick() → COMPILE ERROR
    @Override
    public void onButtonClick(View v, String text) { ... }
}
```

---

### 📁 **res/ (Resources)**

```
📁 res/
├── 📁 layout/              → XML UI layouts
│   ├── activity_main.xml       (portrait)
│   └── activity_main.xml       (landscape - trong layout-land/)
├── 📁 values/              → Values
│   ├── colors.xml              → Màu sắc
│   ├── strings.xml             → Text strings
│   ├── styles.xml              → Styles
│   └── themes.xml              → App theme
├── 📁 drawable/            → Vector graphics, gradients
│   └── gradient_background.xml
└── 📁 mipmap/              → App icons
    ├── ic_launcher.png
    └── ic_launcher_round.png
```

**Tại sao tách resources:**
- Dễ translate (multi-language)
- Dễ theme (dark mode)
- Dễ responsive (different screens)
- Clean code (tách UI khỏi logic)

---

### 🏗️ **TẦNG 3: test [unitTest]**

```
📁 test [unitTest]/
└── 📁 java/
    └── com.example.calculator/
        └── ExampleUnitTest.java
```

**Mục đích:**
- Test logic KHÔNG CẦN device/emulator
- Test nhanh (chạy trên máy)
- Test pure Java code

**Ví dụ:**
```java
@Test
public void testAddition() {
    CalculatorModel model = new CalculatorModel();
    model.handleNumber("5");
    model.handleOperator("+");
    model.handleNumber("3");
    model.handleEquals();
    
    assertEquals("8", model.getResultDisplay());
}
```

**Chạy test:**
```bash
./gradlew test
```

---

### 📄 **.gitignore**

```
📄 .gitignore
```

**Mục đích:**
- Ignore files không cần commit
- Build outputs, cache, IDE settings

**Nội dung:**
```gitignore
*.apk
build/
.gradle/
.idea/
local.properties
*.keystore
```

---

### 📄 **AndroidManifest.xml**

```
📄 AndroidManifest.xml
```

**Mục đích:**
- Khai báo app configuration
- Permissions
- Activities
- Services

**Nội dung quan trọng:**
```xml
<manifest>
    <application
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.Calculator">
        
        <!-- Main Activity -->
        <activity
            android:name=".ui.MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
    </application>
</manifest>
```

---

## 🔗 LUỒNG DỮ LIỆU (DATA FLOW)

### **MVP Pattern Flow:**

```
┌──────────────┐
│     USER     │
└──────┬───────┘
       │ Click button "7"
       ↓
┌──────────────────────────┐
│   VIEW (MainActivity)    │
│  • Nhận click event      │
│  • Gọi presenter         │
└──────────┬───────────────┘
           │ presenter.onButtonClick(view, "7")
           ↓
┌──────────────────────────┐
│  PRESENTER               │
│  • Phân tích button      │
│  • Gọi model.handleNumber│
└──────────┬───────────────┘
           │ model.handleNumber("7")
           ↓
┌──────────────────────────┐
│  MODEL                   │
│  • currentNumber = "7"   │
│  • return "7"            │
└──────────┬───────────────┘
           │ return "7"
           ↓
┌──────────────────────────┐
│  PRESENTER               │
│  • Nhận kết quả "7"      │
│  • Gọi view.updateResult │
└──────────┬───────────────┘
           │ view.updateResult("7")
           ↓
┌──────────────────────────┐
│  VIEW (MainActivity)     │
│  • tvResult.setText("7") │
│  • Hiển thị "7" trên màn │
└──────────────────────────┘
```

---

## 📊 SUMMARY - TÓM TẮT

### **Phân tầng:**

| Tầng | Folder | Vai trò | Nhiệm vụ |
|------|--------|---------|----------|
| **View** | ui/ | Hiển thị UI | Show UI, nhận input, gọi Presenter |
| **Presenter** | presenter/ | Controller | Nhận events, gọi Model, update View |
| **Model** | model/ | Logic | Business logic, calculations, state |
| **Utils** | utils/ | Helpers | Format, validation, utilities |
| **Contract** | root | Interface | Định nghĩa giao tiếp View-Presenter |

### **Nguyên tắc:**

✅ **View** KHÔNG chứa logic  
✅ **Presenter** KHÔNG chứa UI code  
✅ **Model** KHÔNG biết về View  
✅ Giao tiếp qua **Interface**

### **Lợi ích:**

✅ **Testable** - Dễ test từng layer  
✅ **Maintainable** - Dễ maintain  
✅ **Scalable** - Dễ mở rộng  
✅ **Clean** - Code sạch, rõ ràng

---

## 🎯 KẾT LUẬN

**Cấu trúc này theo MVP Pattern:**
- **M**odel: Business logic
- **V**iew: UI
- **P**resenter: Controller

**Tại sao dùng MVP:**
- Tách biệt concerns
- Dễ test
- Dễ maintain
- Professional architecture

**So với code 1 file:**
- Old: 1 file 450 dòng (khó đọc, khó test)
- New: 6 files, mỗi file < 200 dòng (dễ đọc, dễ test)

**→ Đây là cách code app Android professional! 🚀**