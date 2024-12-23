package com.saucedemo.tests;

import com.saucedemo.pages.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTestWithJUnit {
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private CheckoutOverviewPage checkoutOverviewPage;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        // Desativar o Selenium Manager
        System.setProperty("webdriver.remote.driver", "false");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        checkoutOverviewPage = new CheckoutOverviewPage(driver);
    }

    @Test
@DisplayName("CN001 - Login com usuário válido")
    void testValidLogin() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        assertTrue(productsPage.isProductPageDisplayed());
    }

    @Test
@DisplayName("CN002 - Login com usuário inválido")
    void testInvalidLogin() {
        loginPage.enterUsername("locked_out_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        assertTrue(loginPage.isErrorMessageDisplayed());

        // Espera até que o elemento de erro seja clicável (com timeout aumentado)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Aumentando o timeout
        WebElement errorElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='error']")));

        // Verifica se o elemento está visível antes de interagir
        assertTrue(errorElement.isDisplayed());

        assertEquals("Epic sadface: Sorry, this user has been locked out.", errorElement.getText());
    }

    @Test
@DisplayName("CN003 - Login com senha inválida")
    void testInvalidPassword() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("senha_errada");
        loginPage.clickLogin();
        sleep(1000);
        assertTrue(loginPage.isErrorMessageDisplayed());

        // Espera até que o elemento de erro seja clicável (com timeout aumentado)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Aumentando o timeout
        WebElement errorElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='error']")));

        // Verifica se o elemento está visível antes de interagir
        assertTrue(errorElement.isDisplayed());

        assertEquals("Epic sadface: Username and password do not match any user in this service", errorElement.getText());
    }

    @Test
@DisplayName("CN004 - Listagem de produtos")
    void testProductListing() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        assertTrue(productsPage.getProductCount() > 0);

        // Verifica se os produtos estão listados com nome, descrição e preço
        List<WebElement> productNames = productsPage.getProductNames();
        List<WebElement> productDescriptions = driver.findElements(By.className("inventory_item_desc"));
        List<WebElement> productPrices = productsPage.getProductPrices();

        for (WebElement productName : productNames) {
            assertTrue(productName.isDisplayed());
            assertFalse(productName.getText().isEmpty());
        }
        for (WebElement productDescription : productDescriptions) {
            assertTrue(productDescription.isDisplayed());
            assertFalse(productDescription.getText().isEmpty());
        }
        for (WebElement productPrice : productPrices) {
            assertTrue(productPrice.isDisplayed());
            assertFalse(productPrice.getText().isEmpty());
        }
    }

    @Test
@DisplayName("CN005 - Visualizar detalhes do produto")
    void testProductDetails() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        // Clica no primeiro produto da lista
        WebElement firstProduct = productsPage.getProductNames().get(0);
        String productName = firstProduct.getText();
        firstProduct.click();
        sleep(1000);

        // Verifica se está na página de detalhes do produto
        WebElement productDetailsName = driver.findElement(By.className("inventory_details_name"));
        assertTrue(productDetailsName.isDisplayed());
        assertEquals(productDetailsName.getText(), productName);

        //Verifica a descrição
        WebElement productDetailsDesc = driver.findElement(By.className("inventory_details_desc"));
        assertTrue(productDetailsDesc.isDisplayed());
        assertFalse(productDetailsDesc.getText().isEmpty());

        //Verifica o preço
        WebElement productDetailsPrice = driver.findElement(By.className("inventory_details_price"));
        assertTrue(productDetailsPrice.isDisplayed());
        assertFalse(productDetailsPrice.getText().isEmpty());
    }

    @Test
@DisplayName("CN006 - Ordenação de produtos por nome (A-Z)")
    void testProductSortingNameAZ() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.selectSortOption("Name (A to Z)");
        sleep(1000);

        List<WebElement> productNames = productsPage.getProductNames();
        for (int i = 0; i < productNames.size() - 1; i++) {
            String name1 = productNames.get(i).getText();
            String name2 = productNames.get(i + 1).getText();
            assertTrue(name1.compareTo(name2) <= 0);
        }
    }

    @Test
@DisplayName("CN007 - Ordenação de produtos por nome (Z-A)")
    void testProductSortingNameZA() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.selectSortOption("Name (Z to A)");
        sleep(1000);

        List<WebElement> productNames = productsPage.getProductNames();
        for (int i = 0; i < productNames.size() - 1; i++) {
            String name1 = productNames.get(i).getText();
            String name2 = productNames.get(i + 1).getText();
            assertTrue(name1.compareTo(name2) >= 0);
        }
    }

    @Test
@DisplayName("CN008 - Ordenação de produtos por preço (menor para maior)")
    void testProductSortingPriceLowToHigh() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.selectSortOption("Price (low to high)");
        sleep(1000);

        List<WebElement> productPrices = productsPage.getProductPrices();
        for (int i = 0; i < productPrices.size() - 1; i++) {
            double price1 = Double.parseDouble(productPrices.get(i).getText().replace("$", ""));
            double price2 = Double.parseDouble(productPrices.get(i + 1).getText().replace("$", ""));
            assertTrue(price1 <= price2);
        }
    }

    @Test
@DisplayName("CN009 - Ordenação de produtos por preço (maior para menor)")
    void testProductSortingPriceHighToLow() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.selectSortOption("Price (high to low)");
        sleep(1000);

        List<WebElement> productPrices = productsPage.getProductPrices();
        for (int i = 0; i < productPrices.size() - 1; i++) {
            double price1 = Double.parseDouble(productPrices.get(i).getText().replace("$", ""));
            double price2 = Double.parseDouble(productPrices.get(i + 1).getText().replace("$", ""));
            assertTrue(price1 >= price2);
        }
    }

    @Test
@DisplayName("CN010 - Adição de um produto ao carrinho")
    void testAddProductToCart() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.addProductToCart("Sauce Labs Backpack");
        sleep(1000);
        assertEquals(productsPage.getCartItemCount(), "1");

        // Clica no carrinho
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(1000);

        //Verifica se está na página do carrinho
        assertTrue(cartPage.isCartPageDisplayed());

        //Verifica se tem 1 produto no carrinho
        assertEquals(cartPage.getCartItemCount(), 1);
    }

    @Test
@DisplayName("CN011 - Adição de múltiplos produtos ao carrinho")
    void testAddMultipleProductsToCart() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.addMultipleProductsToCart();
        sleep(1000);
        assertEquals(productsPage.getCartItemCount(), "2");

        // Clica no carrinho
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(1000);

        //Verifica se está na página do carrinho
        assertTrue(cartPage.isCartPageDisplayed());

        //Verifica se tem 2 produtos no carrinho
        assertEquals(cartPage.getCartItemCount(), 2);
    }

    @Test
@DisplayName("CN012 - Remover um produto do carrinho")
    void testRemoveProductFromCart() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.addMultipleProductsToCart();
        sleep(1000);
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(1000);
        cartPage.removeProductFromCart("Sauce Labs Backpack");
        sleep(1000);
        assertEquals(cartPage.getCartItemCount(), 1);
    }

    @Test
@DisplayName("CN013 - Visualizar o conteúdo do carrinho")
    void testViewCartContent() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.addProductToCart("Sauce Labs Backpack");
        sleep(1000);
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(1000);
        assertTrue(cartPage.isCartPageDisplayed());
        assertEquals(cartPage.getCartItemCount(), 1);

        // Verifica se o nome do produto no carrinho está correto
        WebElement productNameInCart = driver.findElement(By.className("inventory_item_name"));
        assertTrue(productNameInCart.isDisplayed());
        assertEquals(productNameInCart.getText(), "Sauce Labs Backpack");

        //Verifica se a descrição do produto no carrinho está correto
        WebElement productDescInCart = driver.findElement(By.className("inventory_item_desc"));
        assertTrue(productDescInCart.isDisplayed());
        assertFalse(productDescInCart.getText().isEmpty());

        //Verifica se o preço do produto no carrinho está correto
        WebElement productPriceInCart = driver.findElement(By.className("inventory_item_price"));
        assertTrue(productPriceInCart.isDisplayed());
        assertFalse(productPriceInCart.getText().isEmpty());
    }

    @Test
@DisplayName("CN014 - Concluir o checkout com dados válidos")
    void testValidCheckout() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.addProductToCart("Sauce Labs Backpack");
        sleep(1000);
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(1000);
        cartPage.clickCheckout();
        sleep(1000);
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
        sleep(1000);
        assertTrue(checkoutOverviewPage.isOverviewPageDisplayed());
    }

    @Test
@DisplayName("CN015 - Checkout com nome inválido")
    void testInvalidFirstNameCheckout() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.addProductToCart("Sauce Labs Backpack");
        sleep(1000);
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(1000);
        cartPage.clickCheckout();
        sleep(1000);
        checkoutPage.fillCheckoutInfo("", "Doe", "12345"); // Deixa o campo "Nome" vazio
        sleep(1000);

        // Espera explícita para o elemento de erro ficar visível
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));

        assertTrue(errorElement.getText().contains("Error: First Name is required"));

        // Verifica se o campo Nome está destacado
        WebElement firstNameInput = driver.findElement(By.id("first-name"));
        String errorClass = firstNameInput.getAttribute("class");
        assertTrue(errorClass.contains("error"));
    }

    @Test
@DisplayName("CN016 - Checkout com sobrenome inválido")
    void testInvalidLastNameCheckout() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.addProductToCart("Sauce Labs Backpack");
        sleep(1000);
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(1000);
        cartPage.clickCheckout();
        sleep(1000);
        checkoutPage.fillCheckoutInfo("John", "", "12345"); // Deixa o campo "Sobrenome" vazio
        sleep(1000);

        // Espera explícita para o elemento de erro ficar visível
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));

        assertTrue(errorElement.getText().contains("Error: Last Name is required"));

        // Verifica se o campo Sobrenome está destacado
        WebElement lastNameInput = driver.findElement(By.id("last-name"));
        String errorClass = lastNameInput.getAttribute("class");
        assertTrue(errorClass.contains("error"));
    }

    @Test
@DisplayName("CN017 - Checkout com CEP inválido")
    void testInvalidPostalCodeCheckout() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.addProductToCart("Sauce Labs Backpack");
        sleep(1000);
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(1000);
        cartPage.clickCheckout();
        sleep(1000);
        checkoutPage.fillCheckoutInfo("John", "Doe", ""); // Deixa o campo "CEP" vazio
        sleep(1000);

        // Espera explícita para o elemento de erro ficar visível
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));

        assertTrue(errorElement.getText().contains("Error: Postal Code is required"));

        // Verifica se o campo CEP está destacado
        WebElement postalCodeInput = driver.findElement(By.id("postal-code"));
        String errorClass = postalCodeInput.getAttribute("class");
        assertTrue(errorClass.contains("error"));
    }

    @Test
@DisplayName("CN018 - Finalizar a compra")
    void testCompleteOrder() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.addProductToCart("Sauce Labs Backpack");
        sleep(1000);
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        sleep(1000);
        cartPage.clickCheckout();
        sleep(1000);
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
        sleep(1000);
        checkoutOverviewPage.clickFinish();
        sleep(1000);
        assertTrue(checkoutOverviewPage.isOrderComplete());
    }

    @Test
@DisplayName("CN019 - Logout do sistema")
    void testLogout() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);
        productsPage.logout();
        sleep(1000);
        assertTrue(driver.getCurrentUrl().equals("https://www.saucedemo.com/"));
    }

    @Test
@DisplayName("CN020 - Verificar menu de navegação")
    public void testNavigationMenu() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);

        // Verifica se o botão do menu está presente
        WebElement menuButton = driver.findElement(By.id("react-burger-menu-btn"));
        assertTrue(menuButton.isDisplayed());

        // Clica no botão do menu
        menuButton.click();
        sleep(1000);

        // Verifica se os itens do menu estão visíveis
        WebElement menuItems = driver.findElement(By.className("bm-menu"));
        assertTrue(menuItems.isDisplayed());

        // Verifica se os itens do menu "Produtos", "Carrinho" e "Checkout" estão presentes e são clicáveis
        WebElement allItemsLink = driver.findElement(By.id("inventory_sidebar_link"));
        assertTrue(allItemsLink.isDisplayed() && allItemsLink.isEnabled());

        WebElement aboutLink = driver.findElement(By.id("about_sidebar_link"));
        assertTrue(aboutLink.isDisplayed() && aboutLink.isEnabled());

        WebElement logoutLink = driver.findElement(By.id("logout_sidebar_link"));
        assertTrue(logoutLink.isDisplayed() && logoutLink.isEnabled());

        WebElement resetAppStateLink = driver.findElement(By.id("reset_sidebar_link"));
        assertTrue(resetAppStateLink.isDisplayed() && resetAppStateLink.isEnabled());
    }

    @Test
@DisplayName("CN021 - Verificar mensagem de confirmação de adição ao carrinho")
    public void testAddToCartConfirmation() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        sleep(1000);

        // Adiciona um produto ao carrinho
        productsPage.addProductToCart("Sauce Labs Backpack");
        sleep(1000);

        // Verifica se a mensagem de confirmação é exibida (não há uma mensagem explícita na interface)

        // Verifica se a quantidade de itens no carrinho é atualizada
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        assertTrue(cartBadge.isDisplayed());
        assertEquals(cartBadge.getText(), "1");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}