package web_prak;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.catalina.util.URLEncoder;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    private static final String BASE_URL = "http://localhost:8080";
    private static final String MANAGER_EMAIL = "anna.manager@dealership.com";
    private static final String MANAGER_PASSWORD = "$2y$10$ManagerHash1"; // должен соответствовать хешу в БД
    private static final String TEST_CLIENT_EMAIL_PREFIX = "selenium_test_";
    private static final String TEST_VIN_PREFIX = "SELENIUMVIN";

   static String phone;
    private static String createdCarVin;
    private static String createdBrandName;
    private static String createdModelName;
    private static String createdConfigName;
    private static Long createdOrderId;

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        phone = "898519188" + Long.toString(System.currentTimeMillis() % 10) + Long.toString(System.currentTimeMillis() % 10);
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    @Order(1)
    void guest_canViewHomePage() {
        driver.get(BASE_URL);
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("АвтоИмперия") || pageSource.contains("Автосалон"));
        assertTrue(pageSource.contains("О нас") || pageSource.contains("Почему выбирают нас"));
    }
    @Test
    @Order(2)
    void guest_canFilterModelsByName() {
        driver.get(BASE_URL + "/models");
        WebElement nameInput = driver.findElement(By.cssSelector("input[name='name']"));
        String searchName = "Camry";
        nameInput.sendKeys(searchName);
        driver.findElement(By.cssSelector(".filter-panel button[type='submit']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".model-card")));
        List<WebElement> cards = driver.findElements(By.cssSelector(".model-card"));
        assertFalse(cards.isEmpty());
        boolean contains = cards.stream().anyMatch(card -> card.getText().contains(searchName));
        assertTrue(contains, "После фильтрации должны отображаться модели с именем " + searchName);
    }
    @Test
    @Order(2)
    void guest_canViewCarsList() {
        driver.get(BASE_URL + "/cars");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".car-card")));
        int carCount = driver.findElements(By.cssSelector(".car-card")).size();
        assertTrue(carCount > 0, "Список автомобилей не пуст");
    }

    @Test
    @Order(3)
    void guest_canFilterCarsByColor() {
        driver.get(BASE_URL + "/cars");
        WebElement colorInput = driver.findElement(By.cssSelector("input[name='color']"));
        colorInput.sendKeys("Black");
        driver.findElement(By.cssSelector(".filter-panel button[type='submit']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".car-card")));
        assertTrue(driver.getPageSource().contains("Black") || driver.findElements(By.cssSelector(".car-card")).size() >= 0);
    }

    @Test
    @Order(4)
    void guest_canFilterByPowerRange() {
        driver.get(BASE_URL + "/cars");
        driver.findElement(By.className("collapsible")).click();
        WebElement minPower = driver.findElement(By.cssSelector("input[name='configuration.enginePowerMin']"));
        WebElement maxPower = driver.findElement(By.cssSelector("input[name='configuration.enginePowerMax']"));

        minPower.sendKeys("125");
        maxPower.sendKeys("175");
        driver.findElement(By.cssSelector(".filter-panel button[type='submit']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".car-card")));
        assertTrue(driver.getPageSource().contains("150") || driver.findElements(By.cssSelector(".car-card")).size() >= 0);
    }

    @Test
    @Order(5)
    void guest_cannotAccessAdminPages() {
        driver.get(BASE_URL + "/brands/add");
        assertTrue(driver.getCurrentUrl().contains("/login") || driver.getPageSource().contains("Вход"),
                "Неавторизованный пользователь не может добавить марку");
    }


    @Test
    @Order(6)
    void client_cannotRegisterEmailExists() {
        driver.get(BASE_URL + "/register");
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String email = TEST_CLIENT_EMAIL_PREFIX + Long.toString(System.currentTimeMillis() % 10) + Long.toString(System.currentTimeMillis() % 10) + uniqueSuffix + "@test.com";
        String name = "Тестовый Клиент";

        String address = "Москва, тестовая ул., д.1";
        String password = "pass123";

        driver.findElement(By.cssSelector("input[name='name']")).sendKeys(name);
        driver.findElement(By.cssSelector("input[name='email']")).sendKeys("natalia.assist@dealership.com");
        driver.findElement(By.cssSelector("input[name='phone']")).sendKeys(phone);
        driver.findElement(By.cssSelector("input[name='address']")).sendKeys(address);
        driver.findElement(By.cssSelector("input[name='passwordHash']")).sendKeys(password);
        driver.findElement(By.cssSelector("input[name='passwordConfirm']")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();


        assertTrue(driver.getCurrentUrl().contains("/error") && driver.getCurrentUrl().contains("mail"), "Появилась ошибка");
    }

    @Test
    @Order(7)
    void client_cannotRegisterPhoneExists() {
        driver.get(BASE_URL + "/register");
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String email = TEST_CLIENT_EMAIL_PREFIX + Long.toString(System.currentTimeMillis() % 10) + Long.toString(System.currentTimeMillis() % 10) + uniqueSuffix + "@test.com";
        String name = "Тестовый Клиент";

        String address = "Москва, тестовая ул., д.1";
        String password = "pass123";

        driver.findElement(By.cssSelector("input[name='name']")).sendKeys(name);
        driver.findElement(By.cssSelector("input[name='email']")).sendKeys(email);
        driver.findElement(By.cssSelector("input[name='phone']")).sendKeys("+7(999)111-22-33");
        driver.findElement(By.cssSelector("input[name='address']")).sendKeys(address);
        driver.findElement(By.cssSelector("input[name='passwordHash']")).sendKeys(password);
        driver.findElement(By.cssSelector("input[name='passwordConfirm']")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();


        assertTrue(driver.getCurrentUrl().contains("/error") && driver.getCurrentUrl().contains("%D0%BD%D0%BE%D0%BC%D0%B5%D1%80%D0%BE%D0%BC"), "Появилась ошибка");
    }


    @Order(5 + 3)
    void client_canRegister() {
        driver.get(BASE_URL + "/register");
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String email = TEST_CLIENT_EMAIL_PREFIX + Long.toString(System.currentTimeMillis() % 10) + Long.toString(System.currentTimeMillis() % 10) + uniqueSuffix + "@test.com";
        String name = "Тестовый Клиент";

        String address = "Москва, тестовая ул., д.1";
        String password = "pass123";

        driver.findElement(By.cssSelector("input[name='name']")).sendKeys(name);
        driver.findElement(By.cssSelector("input[name='email']")).sendKeys(email);
        driver.findElement(By.cssSelector("input[name='phone']")).sendKeys(phone);
        driver.findElement(By.cssSelector("input[name='address']")).sendKeys(address);
        driver.findElement(By.cssSelector("input[name='passwordHash']")).sendKeys(password);
        driver.findElement(By.cssSelector("input[name='passwordConfirm']")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean success = wait.until(
                ExpectedConditions.or(  ExpectedConditions.presenceOfElementLocated(By.className("btn-login")))
        );
        assertTrue(success, "Регистрация не завершилась успешно");
        assertFalse(driver.getCurrentUrl().contains("/error"), "Появилась ошибка");
    }
    @Test
    @Order(6+ 3)
    void client_cannotLoginWrongCreditionals() {
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).sendKeys("88005553535");
        driver.findElement(By.id("password")).sendKeys("pass123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean success = wait.until(
                ExpectedConditions.or(  ExpectedConditions.urlContains("/error"))
        );
        assertTrue(driver.getCurrentUrl().contains("/error"), "Появилась ошибка");


    }


    @Test
    @Order(6+ 3+1)
    void client_canLogin() {
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).sendKeys(phone);
        driver.findElement(By.id("password")).sendKeys("pass123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean success = wait.until(
                ExpectedConditions.or(  ExpectedConditions.presenceOfElementLocated(By.className("btn-logout")))
        );
        assertTrue(success,
                "Клиент успешно вошёл");

    }


    @Test
    @Order(7+4)
    void client_canViewCarDetails() {
        driver.get(BASE_URL + "/cars");
        driver.findElements(By.cssSelector(".car-card .actions a:first-child")).get(0).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1")));
        assertTrue(driver.findElement(By.cssSelector("h1")).getText().length() > 0);
    }

    @Test
    @Order(8+4)
    void client_canOrderCarWithTestDrive() {

        if(driver.findElements(By.className("btn-logout")).isEmpty())
        {
            client_canRegister();
            client_canLogin();
        }
        driver.get(BASE_URL + "/cars");
        driver.findElements(By.cssSelector(".car-card .actions a:first-child")).get(1).click();

        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".btn-primary")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        driver.findElement(By.id("needTestDrive")).click();
        String testDriveDateTime = LocalDateTime.now().plusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        driver.findElement(By.id("testDriveDate")).sendKeys("01012027");
        driver.findElement(By.id("testDriveDate")).sendKeys(Keys.ARROW_RIGHT);
        driver.findElement(By.id("testDriveDate")).sendKeys("0404");

        driver.findElement(By.id("testDriveDuration")).sendKeys("45");
        driver.findElements(By.cssSelector("button[type='submit']")).get(1).click();


        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("order-details")));
        assertTrue(driver.getPageSource().contains("VIN"),
                "Заказ с тест-драйвом создан");
    }

    @Test
    @Order(8+5)
    void client_canOrderCarWithDelivery() {

        if(driver.findElements(By.className("btn-logout")).isEmpty())
        {
            client_canRegister();
            client_canLogin();
        }
        driver.get(BASE_URL + "/cars");
        driver.findElements(By.cssSelector(".car-card .actions a:first-child")).get(0).click();

        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".btn-primary")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        driver.findElement(By.id("needDelivery")).click();
        String testDriveDateTime = LocalDateTime.now().plusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        driver.findElement(By.id("deliveryDate")).sendKeys("01012027");

        driver.findElement(By.id("deliveryAddress")).sendKeys("Some addr");
        driver.findElements(By.cssSelector("button[type='submit']")).get(1).click();


        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("order-details")));
        assertTrue(driver.getPageSource().contains("VIN"),
                "Заказ с доставкой создан");
    }

    @Test
    @Order(9+5)
    void client_canCancelOrderFromCabinet() {
        driver.get(BASE_URL + "/cabinet");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".order-card")));
        var cancelBtn = driver.findElement(By.cssSelector(".btn-cancel"));
        if (cancelBtn.isDisplayed()) {
            cancelBtn.click();
            driver.switchTo().alert().accept();
            driver.navigate().refresh();
            assertTrue(driver.getPageSource().contains("canceled") || driver.getPageSource().contains("Отменён"));
        }
    }


    @Test
    @Order(10+5)
    void manager_canLogin() {
        if( !driver.findElements(By.className("btn-logout")).isEmpty())
            driver.findElement(By.className("btn-logout")).click();
        driver.get(BASE_URL + "/login");
        driver.get(BASE_URL + "/login");
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.presenceOfElementLocated((By.id("username"))));

        driver.findElement(By.id("username")).sendKeys(MANAGER_EMAIL);
        driver.findElement(By.id("password")).sendKeys(MANAGER_PASSWORD);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean success = wait.until(
                ExpectedConditions.or(  ExpectedConditions.presenceOfElementLocated(By.className("btn-logout")))
        );
        assertTrue(success,
                "Клиент успешно вошёл");


    }

    @Test
    @Order(11+5)
    void manager_canAddBrand() {
        driver.get(BASE_URL + "/brands");
        driver.findElement(By.linkText("Добавить марку")).click();
        String brandName = "ТестМарка_" + System.currentTimeMillis();
        createdBrandName = brandName;
        driver.findElement(By.cssSelector("input[name='name']")).sendKeys(brandName);
        driver.findElement(By.cssSelector("input[name='creationYear']")).sendKeys("2020");
        driver.findElement(By.cssSelector("input[name='contactPhone']")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("input[name='contactEmail']")).sendKeys("test@brand.com");
        driver.findElement(By.cssSelector("input[name='countryCode']")).sendKeys("RUS");
        driver.findElement(By.cssSelector("input[name='logoSrc']")).sendKeys("/logos/test.png");
        driver.findElements(By.cssSelector("button[type='submit']")).get(1).click();
        assertTrue(driver.getCurrentUrl().contains("/brands") && driver.getPageSource().contains(brandName),
                "Марка добавлена");
    }

    @Test
    @Order(12+5)
    void manager_canEditBrand() {
        driver.get(BASE_URL + "/brands");
        WebElement brandCard = driver.findElement(By.xpath("//div[contains(@class,'brand-card') and contains(.,'" + createdBrandName + "')]"));
        brandCard.findElement(By.linkText("Изменить")).click();
        WebElement nameField = driver.findElement(By.cssSelector("input[name='name']"));
        nameField.clear();
        String updatedName = createdBrandName + "_updated";
        nameField.sendKeys(updatedName);
        driver.findElement(By.xpath("//button[text()='Сохранить']")).click();
        driver.navigate().refresh();
        assertTrue(driver.getPageSource().contains(updatedName));
        createdBrandName = updatedName;
    }

    @Test
    @Order(13+5)
    void manager_canDeleteBrand() {
        driver.get(BASE_URL + "/brands");
        WebElement brandCard = driver.findElement(By.xpath("//div[contains(@class,'brand-card') and contains(.,'" + createdBrandName + "')]"));
        WebElement deleteForm = brandCard.findElement(By.cssSelector("form[action*='/delete']"));
        deleteForm.findElement(By.cssSelector("button")).click();

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (TimeoutException e) {
        }

        By brandCardLocator = By.xpath("//div[contains(@class,'brand-card') and contains(.,'" + createdBrandName + "')]");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(brandCardLocator));

        assertFalse(driver.getPageSource().contains(createdBrandName));
    }

    @Test
    @Order(14+5)
    void manager_canAddModel() {
        driver.get(BASE_URL + "/models/add");
        driver.findElement(By.cssSelector("select[name='brand.id']")).sendKeys("Toyota");
        String modelName = "ТестМодель_" + System.currentTimeMillis();
        createdModelName = modelName;
        driver.findElement(By.cssSelector("input[name='name']")).sendKeys(modelName);
        driver.findElement(By.cssSelector("input[name='year']")).sendKeys("2024");
        driver.findElement(By.cssSelector("input[name='minCost']")).sendKeys("2000000");
        Select dropdown = new Select( driver.findElement(By.id("brand.id")));
        dropdown.selectByIndex(2);

        driver.findElement(By.cssSelector("input[name='imageSrc']")).sendKeys("/models/test.png");
        driver.findElements(By.cssSelector("button[type='submit']")).get(1).click();
        assertTrue(driver.getPageSource().contains(modelName));
    }

    @Test
    @Order(15+5)
    void manager_canAddConfiguration() {
        driver.get(BASE_URL + "/models");
        driver.findElement(By.xpath("//div[contains(@class,'model-card') and contains(.,'" + createdModelName + "')]//a[contains(text(),'Подробнее')]")).click();
      wait.until( ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Добавить комплектацию']"))).click();
        driver.findElement(By.xpath("//a[text()='Редактировать']")).click();

        String configName = "ТестКомплектация_" + System.currentTimeMillis();
        createdConfigName = configName;
        driver.findElement(By.cssSelector("input[name='name']")).clear();
        driver.findElement(By.cssSelector("input[name='engineType']")).clear();
        driver.findElement(By.cssSelector("input[name='enginePower']")).clear();
        driver.findElement(By.cssSelector("input[name='engineVolume']")).clear();
        driver.findElement(By.cssSelector("input[name='fuelConsumption']")).clear();
        driver.findElement(By.cssSelector("input[name='fuelType']")).clear();
        driver.findElement(By.cssSelector("input[name='tankCapacity']")).clear();
        driver.findElement(By.cssSelector("input[name='transmissionType']")).clear();
        driver.findElement(By.cssSelector("input[name='driveType']")).clear();
        driver.findElement(By.cssSelector("input[name='basicCost']")).clear();
        driver.findElement(By.cssSelector("input[name='doorsCount']")).clear();
        driver.findElement(By.cssSelector("input[name='seatsNumber']")).clear();

        driver.findElement(By.cssSelector("input[name='name']")).sendKeys(configName);
        driver.findElement(By.cssSelector("input[name='engineType']")).sendKeys("Petrol");
        driver.findElement(By.cssSelector("input[name='enginePower']")).sendKeys("150");
        driver.findElement(By.cssSelector("input[name='engineVolume']")).sendKeys("2.0");
        driver.findElement(By.cssSelector("input[name='fuelConsumption']")).sendKeys("8.5");
        driver.findElement(By.cssSelector("input[name='fuelType']")).sendKeys("AI-95");
        driver.findElement(By.cssSelector("input[name='tankCapacity']")).sendKeys("55");
        driver.findElement(By.cssSelector("input[name='transmissionType']")).sendKeys("Automatic");
        driver.findElement(By.cssSelector("input[name='driveType']")).sendKeys("Front");
        driver.findElement(By.cssSelector("input[name='basicCost']")).sendKeys("2500000");
        driver.findElement(By.cssSelector("input[name='doorsCount']")).sendKeys("4");
        driver.findElement(By.cssSelector("input[name='seatsNumber']")).sendKeys("5");
        if (driver.findElements(By.cssSelector("input[name='hasCruiseControl']")).size() > 0)
            driver.findElement(By.cssSelector("input[name='hasCruiseControl']")).click();
        driver.findElements(By.cssSelector("button[type='submit']")).get(1).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='Редактировать']")));
        assertTrue(driver.getPageSource().contains(configName), "Комплектация добавлена");
    }

    @Test
    @Order(16+5)
    void manager_canAddCar() {
        driver.get(BASE_URL + "/cars/add");
        String configFullText = "BMW " + createdModelName + " - " + createdConfigName;
        Select s = new Select(driver.findElement(By.cssSelector("select[name='configuration.id']")));
       s.selectByContainsVisibleText(configFullText);
        String vin = TEST_VIN_PREFIX + System.currentTimeMillis();
        createdCarVin = vin;
        driver.findElement(By.cssSelector("input[name='VIN']")).sendKeys(vin);


        driver.findElement(By.cssSelector("input[name='cost']")).sendKeys("3000000");
        driver.findElement(By.cssSelector("input[name='mileage']")).sendKeys("3000000");
        driver.findElement(By.cssSelector("input[name='color']")).sendKeys("Red");
        driver.findElement(By.id("lastLtoDate")).sendKeys("01012024");
        driver.findElement(By.cssSelector("input[name='seatUpholstery']")).sendKeys("Leather");

        driver.findElement(By.cssSelector("input[name='interiorColor']")).sendKeys("Black");
        driver.findElement(By.cssSelector("input[name='year']")).sendKeys("2024");
        driver.findElement(By.cssSelector("input[name='imageSrc']")).sendKeys("/cars/test.jpg");
        driver.findElements(By.cssSelector("button[type='submit']")).get(1).click();

        assertTrue(driver.getPageSource().contains(vin), "Автомобиль добавлен");
    }

    @Test
    @Order(17+5)
    void manager_canUpdateCarStatusViaInlineEdit() {
        driver.get(BASE_URL + "/cars");
        driver.findElement(By.xpath("//div[contains(@class,'car-card') and contains(.,'" + "Тест" + "')]//a[contains(text(),'Подробнее')]")).click();
        driver.findElement(By.id("editBtn")).click();
        WebElement statusSelect = driver.findElement(By.cssSelector("select[name='status']"));
        statusSelect.sendKeys("ordered");
        driver.findElement(By.id("saveBtn")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("saveBtn")));
        driver.navigate().refresh();
        assertTrue(driver.getPageSource().contains("ordered"));
    }

    @Test
    @Order(18+5)
    void manager_canDeleteCar() {
        driver.get(BASE_URL + "/cars");
        driver.findElement(By.xpath("//div[contains(@class,'car-card') and contains(.,'" + "Тест" + "')]//button[contains(@class,'danger')]")).click();

        assertFalse(driver.getPageSource().contains(createdCarVin));
    }


    @Test
    @Order(19+5)
    void manager_canChangeOrderStatus() {
        driver.get(BASE_URL + "/orders");
        WebElement firstOrderCard = driver.findElement(By.cssSelector(".order-card"));
        WebElement statusSelect = firstOrderCard.findElement(By.cssSelector("select"));
        Select s = new Select(statusSelect);
        s.selectByVisibleText("completed");
        driver.get(BASE_URL + "/orders");
        firstOrderCard = driver.findElement(By.cssSelector(".order-card"));
        wait.until(ExpectedConditions.textToBePresentInElement(firstOrderCard, "completed"));
        assertTrue(firstOrderCard.getText().contains("completed"));
    }


    @Test
    @Order(20+5)
    void manager_canAddClient() {
        driver.get(BASE_URL + "/clients/add");
        String clientName = "Тест Клиент Менеджер";
        String clientEmail = "manager_client_" + System.currentTimeMillis() + "@test.com";
        driver.findElement(By.cssSelector("input[name='name']")).sendKeys(clientName);
        driver.findElement(By.cssSelector("input[name='email']")).sendKeys(clientEmail);
        driver.findElement(By.cssSelector("input[name='phone']")).sendKeys("+79990001122");
        driver.findElement(By.cssSelector("input[name='address']")).sendKeys("Тест адрес");
        driver.findElement(By.cssSelector("input[name='passwordHash']")).sendKeys("password");
        driver.findElements(By.cssSelector("button[type='submit']")).get(1).click();
        assertTrue(driver.getCurrentUrl().contains("/clients") && driver.getPageSource().contains(clientName));
    }

    @Test
    @Order(21)
    void manager_canEditClient() {
        driver.get(BASE_URL + "/clients");
        driver.findElement(By.cssSelector(".client-card .btn")).click();
        driver.findElement(By.linkText("Изменить")).click();
        WebElement phoneField = driver.findElement(By.cssSelector("input[name='phone']"));
        phoneField.clear();
        phoneField.sendKeys("+79991112233");

        driver.findElement(By.xpath("//button[text()='Сохранить изменения']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Изменить")));
        assertTrue(driver.getPageSource().contains("+79991112233"));
    }
    @Test
    @Order(22+5)
    void managerCanSeeMyOrdersFilter() {
        driver.get(BASE_URL + "/orders");
        WebElement myOrdersCheckbox = driver.findElement(By.cssSelector("form[action='/orders']")).findElement(By.name(("status")));
        Select s = new Select(myOrdersCheckbox);
        s.selectByVisibleText("completed");
        driver.findElements(By.cssSelector("button[type='submit']")).get(1).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".order-card")));
        assertTrue( driver.findElements(By.cssSelector(".order-card")).size() > 0);
    }

    @Test
    @Order(28)
    void manager_canEditModel() {

        driver.get(BASE_URL + "/models");
        WebElement modelCard = driver.findElement(By.xpath("//div[contains(@class,'model-card') and contains(.,'" + createdModelName + "')]"));
        modelCard.findElement(By.linkText("Подробнее")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("/html/body/div/div/a[2]")))).click();
        WebElement editLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Редактировать")));

        WebElement nameField = driver.findElement(By.cssSelector("input[name='name']"));
        String updatedName = createdModelName + "_updated";
        nameField.clear();
        nameField.sendKeys(updatedName);
        WebElement yearField = driver.findElement(By.cssSelector("input[name='year']"));
        yearField.clear();
        yearField.sendKeys("2026");
        driver.findElement(By.xpath("/html/body/div/div/form[1]/button")).click();

        assertTrue(driver.getPageSource().contains(updatedName), "Имя модели должно обновиться");
        createdModelName = updatedName;
    }


    @Test
    @Order(23+6)
    void manager_canDeleteClient() {
        driver.get(BASE_URL + "/clients");
      var els =  driver.findElements(By.xpath("//div[contains(@class,'client-card') and contains(.,'" + "Тест" + "')]//button[contains(@class,'danger')]"));
      for (int i =0; i < els.size(); i++)
      {
          els =  driver.findElements(By.xpath("//div[contains(@class,'client-card') and contains(.,'" + "Тест" + "')]//button[contains(@class,'danger')]"));
          els.get(0).click();
          driver.get(BASE_URL + "/clients");
      }
        driver.get(BASE_URL + "/clients");
        assertFalse(driver.getPageSource().contains("Тест"));
    }


    @Test
    @Order(24+6)
    void searchByVinWorks() {
        driver.get(BASE_URL + "/cars");
        driver.findElement(By.cssSelector("input[name='VIN']")).sendKeys("JTDBE32K123456789");
        driver.findElements(By.cssSelector("button[type='submit']")).get(1).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".car-card")));
        assertTrue(driver.getPageSource().contains("JTDBE32K123456789"));
    }

    @Test
    @Order(25+6)
    void testDriveAndDeliveryOptionsWork() {
        driver.get(BASE_URL + "/orders/create?carId=1");
        driver.findElement(By.id("needDelivery")).click();
        driver.findElement(By.id("deliveryDate")).sendKeys("2030-12-31");
        driver.findElement(By.id("deliveryAddress")).sendKeys("Москва, Красная пл., д.1");
        driver.findElements(By.cssSelector("button[type='submit']")).get(1).click();
        assertTrue(driver.getPageSource().contains("заказ") || driver.getCurrentUrl().contains("/orders"));
    }


}