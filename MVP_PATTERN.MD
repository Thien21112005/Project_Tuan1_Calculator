# 🏗️ MVP PATTERN - MODEL VIEW PRESENTER

## 🤔 MVP LÀ GÌ?

**MVP** = **M**odel - **V**iew - **P**resenter

Là một **design pattern** (mẫu thiết kế) để tổ chức code trong app.

### **3 thành phần chính:**

```
┌──────────┐     ┌────────────┐     ┌───────┐
│  MODEL   │ ←──→│ PRESENTER  │ ←──→│ VIEW  │
│ (Logic)  │     │(Controller)│     │ (UI)  │
└──────────┘     └────────────┘     └───────┘
```

---

## 📦 CHI TIẾT 3 THÀNH PHẦN

### 1️⃣ **MODEL (Business Logic)**

**Vai trò:** Não của app - xử lý logic và data

**Chứa:**
- Business logic (tính toán)
- Data structures (state)
- Data validation
- Database operations

**KHÔNG chứa:**
- ❌ UI code (TextView, Button)
- ❌ Android framework code (Activity, Fragment)

**Ví dụ trong Calculator:**
```java
public class CalculatorModel {
    private double firstNumber = 0;
    private String operator = "";
    private String currentNumber = "";
    
    // Business logic
    public void handleNumber(String number) {
        currentNumber += number;
    }
    
    public double performCalculation() {
        if (operator.equals("+")) {
            return firstNumber + secondNumber;
        }
        // ...
    }
    
    public String getResultDisplay() {
        return formatNumber(currentNumber);
    }
}
```

**Đặc điểm:**
- ✅ Pure Java (không phụ thuộc Android)
- ✅ Dễ test (không cần emulator)
- ✅ Reusable (có thể dùng cho iOS, web)

---

### 2️⃣ **VIEW (User Interface)**

**Vai trò:** Hiển thị UI và nhận input từ user

**Chứa:**
- UI components (TextView, Button, EditText)
- Layout inflation
- Event listeners
- Show/hide UI elements

**KHÔNG chứa:**
- ❌ Business logic (tính toán)
- ❌ Data processing

**Ví dụ trong Calculator:**
```java
public class MainActivity extends AppCompatActivity 
        implements CalculatorContract.ViewInterface {
    
    private TextView tvResult;
    private CalculatorPresenter presenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvResult = findViewById(R.id.tvResult);
        presenter = new CalculatorPresenter(this);
        
        // Setup listeners
        findViewById(R.id.btn7).setOnClickListener(v -> 
            presenter.onButtonClick(v, "7")
        );
    }
    
    // Interface methods
    @Override
    public void updateResult(String result) {
        tvResult.setText(result);  // CHỈ update UI
    }
}
```

**Đặc điểm:**
- ✅ "Dumb" view - chỉ hiển thị, không suy nghĩ
- ✅ Gọi Presenter khi có event
- ✅ Update UI theo lệnh Presenter

---

### 3️⃣ **PRESENTER (Controller/Mediator)**

**Vai trò:** Cầu nối giữa View và Model

**Chứa:**
- Presentation logic (logic hiển thị)
- Event handling
- Gọi Model để xử lý
- Update View với kết quả

**KHÔNG chứa:**
- ❌ Business logic (để Model làm)
- ❌ UI code (để View làm)

**Ví dụ trong Calculator:**
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
        
        // Phân tích button
        if (id == R.id.btn7) {
            model.handleNumber("7");       // Gọi Model
        } else if (id == R.id.btnAdd) {
            model.handleOperator("+");     // Gọi Model
        }
        
        // Update View
        String result = model.getResultDisplay();
        view.updateResult(result);         // Gọi View
    }
}
```

**Đặc điểm:**
- ✅ Không phụ thuộc Android framework
- ✅ Dễ test với mock View
- ✅ Pure coordinator (chỉ điều phối)

---

## 🔗 CONTRACT (Interface)

**Vai trò:** Định nghĩa "hợp đồng" giữa View và Presenter

```java
public interface CalculatorContract {
    
    // View PHẢI implement
    interface ViewInterface {
        void updateResult(String result);
        void updateExpression(String expression);
        void showError(String message);
    }
    
    // Presenter PHẢI implement
    interface Presenter {
        void onButtonClick(View v, String buttonText);
    }
}
```

**Lợi ích:**
- ✅ Đảm bảo View có đủ methods
- ✅ Đảm bảo Presenter implement đúng
- ✅ Dễ mock để test
- ✅ Decoupling (tách rời)

---

## 🔄 DATA FLOW - LUỒNG DỮ LIỆU

### **Flow hoàn chỉnh:**

```
1. User Action
   User click button "7"
        ↓

2. View nhận event
   MainActivity.onClick() được gọi
        ↓

3. View gọi Presenter
   presenter.onButtonClick(view, "7")
        ↓

4. Presenter phân tích
   if (id == R.id.btn7) { ... }
        ↓

5. Presenter gọi Model
   model.handleNumber("7")
        ↓

6. Model xử lý logic
   currentNumber = "7"
   return "7"
        ↓

7. Model trả kết quả
   return "7" to Presenter
        ↓

8. Presenter gọi View
   view.updateResult("7")
        ↓

9. View update UI
   tvResult.setText("7")
        ↓

10. User thấy kết quả
    Screen shows "7"
```

### **Diagram:**

```
    USER
     │
     │ (1) Click "7"
     ↓
┌─────────────────┐
│      VIEW       │
│  MainActivity   │
│                 │
│ • onCreate()    │
│ • onClick()     │◄─── (9) updateResult("7")
│ • updateResult()│
└────────┬────────┘
         │
         │ (3) onButtonClick("7")
         ↓
┌─────────────────┐
│   PRESENTER     │
│                 │
│ • onButtonClick │
│ • Phân tích ID  │
└────────┬────────┘
         │
         │ (5) handleNumber("7")
         ↓
┌─────────────────┐
│     MODEL       │
│                 │
│ • handleNumber  │
│ • currentNumber │
│ • getResult     │◄─── (7) return "7"
└─────────────────┘
```

---

## 💡 SO SÁNH: MVP vs NO PATTERN

### **❌ Không dùng Pattern (Old way):**

```java
public class MainActivity extends AppCompatActivity {
    
    private TextView tvResult;
    private double firstNumber = 0;
    private String operator = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup UI
        // Setup logic
        // Setup everything ← MỘT FILE LÀM HẾT!
    }
    
    public void onClick(View v) {
        if (v.getId() == R.id.btn7) {
            // Update UI
            tvResult.setText("7");
            
            // Update logic
            firstNumber = 7;
            
            // Validate
            if (firstNumber > 0) { ... }
            
            // Calculate
            double result = firstNumber + secondNumber;
            
            // Format
            String formatted = format(result);
            
            // Update UI again
            tvResult.setText(formatted);
        }
        // 400+ lines more...
    }
}
```

**Vấn đề:**
- ❌ UI + Logic + Data lẫn lộn
- ❌ Khó test (phải dùng emulator)
- ❌ Khó maintain (1 file 450+ dòng)
- ❌ Khó reuse (code dính chặt Android)
- ❌ Khó mở rộng

---

### **✅ Dùng MVP (New way):**

**Model.java (Logic):**
```java
public class CalculatorModel {
    private double firstNumber = 0;
    
    public void handleNumber(String num) {
        // Pure logic
    }
    
    public double calculate() {
        // Pure logic
        return result;
    }
}
```

**Presenter.java (Controller):**
```java
public class CalculatorPresenter {
    public void onButtonClick(View v, String text) {
        model.handleNumber(text);
        view.updateResult(model.getResult());
    }
}
```

**MainActivity.java (UI):**
```java
public class MainActivity implements ViewInterface {
    @Override
    public void updateResult(String result) {
        tvResult.setText(result);  // CHỈ update UI
    }
}
```

**Lợi ích:**
- ✅ Tách biệt concerns
- ✅ Dễ test (test Model không cần UI)
- ✅ Dễ maintain (mỗi file < 200 dòng)
- ✅ Dễ reuse (Model dùng được cho iOS/web)
- ✅ Dễ mở rộng

---

## 🧪 TESTING ADVANTAGES

### **Test Model (KHÔNG cần emulator):**

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

**Chạy trong:** 0.1 giây ⚡

---

### **Test Presenter (Mock View):**

```java
@Test
public void testButtonClick() {
    ViewInterface mockView = mock(ViewInterface.class);
    CalculatorPresenter presenter = new CalculatorPresenter(mockView);
    
    View mockButton = mock(View.class);
    when(mockButton.getId()).thenReturn(R.id.btn7);
    
    presenter.onButtonClick(mockButton, "7");
    
    verify(mockView).updateResult("7");
}
```

**Chạy trong:** 0.2 giây ⚡

---

### **Test View (Cần emulator):**

```java
@Test
public void testUIUpdate() {
    onView(withId(R.id.btn7)).perform(click());
    onView(withId(R.id.tvResult)).check(matches(withText("7")));
}
```

**Chạy trong:** 5-10 giây 🐢 (nhưng cần thiết!)

---

## 📊 RULES & BEST PRACTICES

### **✅ DO (Nên làm):**

**Model:**
- ✅ Pure Java/Kotlin
- ✅ Testable without Android
- ✅ Single Responsibility
- ✅ Immutable state (nếu có thể)

**Presenter:**
- ✅ No Android imports (trừ View class)
- ✅ Test với mock View
- ✅ Lightweight (chỉ điều phối)

**View:**
- ✅ Dumb view (chỉ hiển thị)
- ✅ No business logic
- ✅ Call Presenter for everything

---

### **❌ DON'T (Không nên):**

**Model:**
- ❌ KHÔNG import android.widget.*
- ❌ KHÔNG import Activity/Fragment
- ❌ KHÔNG có UI code

**Presenter:**
- ❌ KHÔNG findViewById()
- ❌ KHÔNG setText() trực tiếp
- ❌ KHÔNG chứa business logic phức tạp

**View:**
- ❌ KHÔNG tính toán
- ❌ KHÔNG validate data
- ❌ KHÔNG format numbers

---

## 🎯 KHI NÀO DÙNG MVP?

### **✅ Nên dùng khi:**
- App có logic phức tạp
- Cần test nhiều
- Team lớn (nhiều người)
- Long-term project
- Professional app

### **❌ Có thể không cần khi:**
- App đơn giản (Hello World)
- Prototype nhanh
- One-time use app
- Learning project (beginner)

---

## 🔄 MVP vs OTHER PATTERNS

### **MVP vs MVC:**

**MVC (Model-View-Controller):**
```
View ←→ Controller ←→ Model
     ↘              ↗
       ← direct  ←
```
- View có thể truy cập Model trực tiếp
- Tight coupling

**MVP:**
```
View ←→ Presenter ←→ Model
```
- View KHÔNG biết Model
- Loose coupling

---

### **MVP vs MVVM:**

**MVVM (Model-View-ViewModel):**
```
View ←[Data Binding]→ ViewModel ←→ Model
```
- Data binding tự động
- LiveData / Observable
- Android Architecture Components

**MVP:**
```
View ←[Interface]→ Presenter ←→ Model
```
- Manual updates
- Interface-based
- Simpler

**Khi nào dùng:**
- MVP: Smaller apps, simple logic
- MVVM: Complex apps, reactive programming

---

## 🎓 SUMMARY - TÓM TẮT

**MVP Pattern = 3 layers:**

| Layer | Vai trò | Chứa gì | Không chứa |
|-------|---------|---------|------------|
| **Model** | Logic | Tính toán, data | UI code |
| **View** | UI | TextView, Button | Logic |
| **Presenter** | Controller | Điều phối | UI & Logic phức tạp |

**Nguyên tắc vàng:**
1. View gọi Presenter khi có event
2. Presenter gọi Model để xử lý
3. Model trả kết quả cho Presenter
4. Presenter update View

**Lợi ích:**
- ✅ Testable
- ✅ Maintainable
- ✅ Scalable
- ✅ Clean code
- ✅ Team-friendly

**Trade-off:**
- ➕ Code quality tốt
- ➖ Nhiều files hơn
- ➖ Boilerplate code
- ➖ Learning curve

---

## 💡 FINAL THOUGHTS

**MVP không phải silver bullet!**

Nhưng nó giúp:
- Code sạch hơn
- Dễ maintain hơn
- Dễ test hơn
- Professional hơn

**Với Calculator app:**
- Old: 1 file 450 dòng ❌
- New: 6 files, mỗi file < 200 dòng ✅

**→ Worth it! 🚀**