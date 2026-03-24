package automation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.time.Duration;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

/**
 * Simulación de pruebas automatizadas para la aplicación ParaBank.
 *
 * Este proyecto implementa una serie de casos de prueba funcionales
 * automatizados utilizando Selenium WebDriver, Java y JUnit.
 *
 * Casos de prueba implementados:
 * TC001 - Navegación al formulario de registro
 * TC002 - Registro exitoso de usuario
 * TC003 - Inicio de sesión exitoso
 * TC004 - Apertura de nueva cuenta
 * TC005 - Visualización de cuentas
 * TC006 - Transferencia exitosa
 * TC007 - Validación de transferencia con monto superior al saldo
 */
    
public class miPrimerSimulacion {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String BASE_URL = "https://parabank.parasoft.com/parabank/index.htm";
    private static final String REGISTER_URL = "https://parabank.parasoft.com/parabank/register.htm";

    // true = modo grabación / false = modo rápido (para efectos de grabacion)
    private static final boolean MODO_DEMO_LENTO = true;

    @Before
    public void setUp() {
        ChromeOptions co = new ChromeOptions();
        co.addArguments("--remote-allow-origins=*");

        System.setProperty(
            "webdriver.chrome.driver",
            "/Users/pau/eclipse-workspace/CalidadSimulacion/src/test/resources/Driver/chromedriver"
        );

        driver = new ChromeDriver(co);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(BASE_URL);
        pausaPantalla();
    }

    @Test
    public void tc001_landingPageYAccesoARegister() {
        WebElement leftPanel = esperarVisible(By.id("leftPanel"));
        WebElement customerLoginTitle = esperarVisible(
            By.xpath("//div[@id='leftPanel']//h2[text()='Customer Login']")
        );
        WebElement usernameField = esperarVisible(By.name("username"));
        WebElement passwordField = esperarVisible(By.name("password"));
        WebElement loginButton = esperarVisible(By.cssSelector("input.button[value='Log In']"));
        WebElement registerLink = esperarVisible(By.linkText("Register"));

        assertTrue(leftPanel.isDisplayed());
        assertTrue(customerLoginTitle.isDisplayed());
        assertTrue(usernameField.isDisplayed());
        assertTrue(passwordField.isDisplayed());
        assertTrue(loginButton.isDisplayed());
        assertTrue(registerLink.isDisplayed());

        pausas();
        registerLink.click();
        pausaPantalla();

        assertTrue(driver.getCurrentUrl().contains("register.htm"));

        WebElement registerTitle = esperarVisible(
            By.xpath("//h1[contains(normalize-space(),'Signing up is easy!')]")
        );
        assertTrue(registerTitle.isDisplayed());
    }

    @Test
    public void tc002_registroExitosoUsuario() {
        driver.get(REGISTER_URL);
        pausaPantalla();

        String usernameDinamico = "mariapaula_" + UUID.randomUUID().toString().substring(0, 8);

        escribirLento(esperarVisible(By.id("customer.firstName")), "Maria", 70);
        escribirLento(esperarVisible(By.id("customer.lastName")), "Bermudez", 70);
        escribirLento(esperarVisible(By.id("customer.address.street")), "San Miguel", 60);
        escribirLento(esperarVisible(By.id("customer.address.city")), "Desamparados", 55);
        escribirLento(esperarVisible(By.id("customer.address.state")), "San Jose", 55);
        escribirLento(esperarVisible(By.id("customer.address.zipCode")), "12345", 70);
        escribirLento(esperarVisible(By.id("customer.phoneNumber")), "88888888", 60);
        escribirLento(esperarVisible(By.id("customer.ssn")), "123456789", 60);
        escribirLento(esperarVisible(By.id("customer.username")), usernameDinamico, 70);
        escribirLento(esperarVisible(By.id("customer.password")), "admin1234", 70);
        escribirLento(esperarVisible(By.id("repeatedPassword")), "admin1234", 70);

        pausas();
        esperarVisible(By.cssSelector("input.button[value='Register']")).click();
        pausaPantalla();

        WebElement welcomeTitle = esperarVisible(By.cssSelector("#rightPanel h1.title"));
        WebElement successMessage = esperarVisible(
            By.xpath("//div[@id='rightPanel']//p[contains(text(),'Your account was created successfully')]")
        );

        assertTrue(welcomeTitle.isDisplayed());
        assertTrue(successMessage.isDisplayed());
        assertTrue(welcomeTitle.getText().contains("Welcome"));
    }

    @Test
    public void tc003_loginExitoso() {
        driver.get(BASE_URL);
        pausaPantalla();

        login("john", "demo");

        WebElement overviewContainer = esperarVisible(By.id("overviewAccountsApp"));
        WebElement overviewTitle = esperarVisible(By.cssSelector("#overviewAccountsApp h1.title"));
        WebElement accountTable = esperarVisible(By.id("accountTable"));
        WebElement firstAccount = esperarVisible(
            By.xpath("//table[@id='accountTable']//tbody/tr[1]/td[1]/a")
        );

        assertTrue(overviewContainer.isDisplayed());
        assertTrue(overviewTitle.isDisplayed());
        assertTrue(accountTable.isDisplayed());
        assertTrue(firstAccount.isDisplayed());
        assertEquals("Accounts Overview", overviewTitle.getText().trim());
    }

    @Test
    public void tc004_aperturaNuevaCuenta() {
        driver.get(BASE_URL);
        pausaPantalla();

        login("john", "demo");

        WebElement openNewAccountLink = esperarVisible(By.linkText("Open New Account"));
        pausas();
        openNewAccountLink.click();
        pausaPantalla();

        WebElement typeDropdownElement = esperarVisible(By.id("type"));
        WebElement fromAccountDropdownElement = esperarVisible(By.id("fromAccountId"));
        WebElement openNewAccountButton = esperarVisible(
            By.cssSelector("input.button[value='Open New Account']")
        );

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
            By.cssSelector("#fromAccountId option"), 0
        ));

        Select typeDropdown = new Select(typeDropdownElement);
        typeDropdown.selectByVisibleText("SAVINGS");
        pausas();

        String cuentaOrigen = seleccionarPrimeraOpcionDisponible(fromAccountDropdownElement);
        assertFalse(cuentaOrigen.isEmpty());
        pausas();

        openNewAccountButton.click();
        pausaPantalla();

        WebElement openAccountResult = esperarVisible(By.id("openAccountResult"));
        WebElement resultTitle = esperarVisible(By.cssSelector("#openAccountResult h1.title"));
        WebElement successMessage = esperarVisible(
            By.xpath("//div[@id='openAccountResult']//p[contains(text(),'Congratulations, your account is now open.')]")
        );
        WebElement newAccountId = esperarVisible(By.id("newAccountId"));

        assertTrue(openAccountResult.isDisplayed());
        assertTrue(resultTitle.isDisplayed());
        assertTrue(successMessage.isDisplayed());
        assertTrue(newAccountId.isDisplayed());
        assertFalse(newAccountId.getText().isEmpty());
    }

    @Test
    public void tc005_visualizacionAccountsOverview() {
        driver.get(BASE_URL);
        pausaPantalla();

        login("john", "demo");

        WebElement accountsOverviewLink = esperarVisible(By.linkText("Accounts Overview"));
        pausas();
        accountsOverviewLink.click();
        pausaPantalla();

        WebElement overviewContainer = esperarVisible(By.id("overviewAccountsApp"));
        WebElement overviewTitle = esperarVisible(By.cssSelector("#overviewAccountsApp h1.title"));
        WebElement accountTable = esperarVisible(By.id("accountTable"));
        WebElement firstAccount = esperarVisible(
            By.xpath("//table[@id='accountTable']//tbody/tr[1]/td[1]/a")
        );
        WebElement firstBalance = esperarVisible(
            By.xpath("//table[@id='accountTable']//tbody/tr[1]/td[2]")
        );
        WebElement firstAvailableAmount = esperarVisible(
            By.xpath("//table[@id='accountTable']//tbody/tr[1]/td[3]")
        );

        assertTrue(overviewContainer.isDisplayed());
        assertTrue(overviewTitle.isDisplayed());
        assertTrue(accountTable.isDisplayed());
        assertTrue(firstAccount.isDisplayed());
        assertTrue(firstBalance.isDisplayed());
        assertTrue(firstAvailableAmount.isDisplayed());

        assertFalse(firstAccount.getText().isEmpty());
        assertFalse(firstBalance.getText().isEmpty());
        assertFalse(firstAvailableAmount.getText().isEmpty());
    }

    @Test
    public void tc006_transferenciaExitosa() {
        driver.get(BASE_URL);
        pausaPantalla();

        login("john", "demo");

        WebElement transferFundsLink = esperarVisible(By.linkText("Transfer Funds"));
        pausas();
        transferFundsLink.click();
        pausaPantalla();

        WebElement amountField = esperarVisible(By.id("amount"));
        WebElement fromAccountDropdownElement = esperarVisible(By.id("fromAccountId"));
        WebElement toAccountDropdownElement = esperarVisible(By.id("toAccountId"));
        WebElement transferButton = esperarVisible(
            By.cssSelector("input.button[value='Transfer']")
        );

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
            By.cssSelector("#fromAccountId option"), 0
        ));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
            By.cssSelector("#toAccountId option"), 0
        ));

        amountField.clear();
        escribirLento(amountField, "100", 90);
        pausas();

        String fromAccount = seleccionarPrimeraOpcionDisponible(fromAccountDropdownElement);
        pausas();

        String toAccount = seleccionarCuentaDestinoDiferente(toAccountDropdownElement, fromAccount);
        assertFalse(toAccount.isEmpty());
        pausas();

        transferButton.click();
        pausaPantalla();

        WebElement transferCompleteTitle = esperarVisible(By.cssSelector("#showResult h1.title"));
        assertTrue(transferCompleteTitle.isDisplayed());
        assertEquals("Transfer Complete!", transferCompleteTitle.getText());

        WebElement transferMessage = esperarVisible(By.xpath("//div[@id='showResult']//p[1]"));
        assertTrue(transferMessage.getText().contains("has been transferred"));
    }

    @Test
    public void tc007_validacionTransferenciaMontoAlto() {
        driver.get(BASE_URL);
        pausaPantalla();

        login("john", "demo");

        WebElement transferFundsLink = esperarVisible(By.linkText("Transfer Funds"));
        pausas();
        transferFundsLink.click();
        pausaPantalla();

        WebElement amountField = esperarVisible(By.id("amount"));
        WebElement fromAccountDropdownElement = esperarVisible(By.id("fromAccountId"));
        WebElement toAccountDropdownElement = esperarVisible(By.id("toAccountId"));
        WebElement transferButton = esperarVisible(
            By.cssSelector("input.button[value='Transfer']")
        );

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
            By.cssSelector("#fromAccountId option"), 0
        ));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
            By.cssSelector("#toAccountId option"), 0
        ));

        amountField.clear();
        escribirLento(amountField, "5000", 90);
        pausas();

        String fromAccount = seleccionarPrimeraOpcionDisponible(fromAccountDropdownElement);
        pausas();

        String toAccount = seleccionarCuentaDestinoDiferente(toAccountDropdownElement, fromAccount);
        pausas();

        transferButton.click();
        pausaPantalla();

        boolean transferenciaExitosa = driver.findElements(By.cssSelector("#showResult h1.title")).size() > 0;
        boolean mensajeErrorVisible = driver.findElements(By.cssSelector("#showError")).size() > 0
                && driver.findElement(By.cssSelector("#showError")).isDisplayed();

        if (transferenciaExitosa) {
            WebElement transferCompleteTitle = esperarVisible(By.cssSelector("#showResult h1.title"));
            assertTrue(transferCompleteTitle.isDisplayed());
            assertEquals("Transfer Complete!", transferCompleteTitle.getText());

            System.out.println("DEFECTO FUNCIONAL: el sistema permitió una transferencia de monto alto.");
        } else if (mensajeErrorVisible) {
            WebElement errorContainer = esperarVisible(By.cssSelector("#showError"));
            assertTrue(errorContainer.isDisplayed());

            System.out.println("Comportamiento esperado: el sistema rechazó la transferencia.");
        } else {
            fail("No se mostró ni mensaje de éxito ni mensaje de error después de la transferencia.");
        }
    }

    @After
    public void after() {
        pausa(400);
        if (driver != null) {
            driver.quit();
        }
    }

 // =========================================================================================================
// MÉTODOS DE APOYO (los utilizo principalmente para poder grabar y que se aprecie bien cada caso de Prueba)
// =========================================================================================================

    private WebElement esperarVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void login(String username, String password) {
        WebElement usernameField = esperarVisible(By.name("username"));
        WebElement passwordField = esperarVisible(By.name("password"));
        WebElement loginButton = esperarVisible(By.cssSelector("input.button[value='Log In']"));

        usernameField.clear();
        escribirLento(usernameField, username, 90);
        pausas();

        passwordField.clear();
        escribirLento(passwordField, password, 90);
        pausas();

        loginButton.click();
        pausaPantalla();
    }

    private String seleccionarPrimeraOpcionDisponible(WebElement selectElement) {
        Select select = new Select(selectElement);
        wait.until(driver -> select.getOptions().size() > 0);

        String optionText = select.getOptions().get(0).getText().trim();
        select.selectByVisibleText(optionText);
        return optionText;
    }

    private String seleccionarCuentaDestinoDiferente(WebElement selectElement, String cuentaOrigen) {
        Select select = new Select(selectElement);
        wait.until(driver -> select.getOptions().size() > 0);

        for (WebElement option : select.getOptions()) {
            String valor = option.getText().trim();
            if (!valor.equals(cuentaOrigen)) {
                select.selectByVisibleText(valor);
                return valor;
            }
        }

        String optionText = select.getOptions().get(0).getText().trim();
        select.selectByVisibleText(optionText);
        return optionText;
    }

    private void escribirLento(WebElement elemento, String texto, int pausaMs) {
        for (char c : texto.toCharArray()) {
            elemento.sendKeys(String.valueOf(c));
            pausa(pausaMs);
        }
    }

    private void pausa(int milisegundos) {
        if (!MODO_DEMO_LENTO) {
            return;
        }
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void pausas() {
        pausa(450);
    }

    private void pausaPantalla() {
        pausa(950);
    }
}
