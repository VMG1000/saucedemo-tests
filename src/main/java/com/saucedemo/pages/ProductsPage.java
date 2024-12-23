// src/main/java/com/saucedemo/pages/ProductsPage.java
package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

public class ProductsPage {
    private WebDriver driver;

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> products;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(className = "inventory_item_desc")
    private List<WebElement> productDescriptions;

    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = "[data-test='add-to-cart-sauce-labs-backpack']")
    private WebElement addBackpackButton;

    @FindBy(css = "[data-test='add-to-cart-sauce-labs-bike-light']")
    private WebElement addBikeLightButton;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isProductPageDisplayed() {
        return pageTitle.getText().equals("Products");
    }

    public int getProductCount() {
        return products.size();
    }

    public void selectSortOption(String option) {
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(option);
        sleep(1000); // Pausa para visualização
    }

    public List<WebElement> getProductNames() {
        return productNames;
    }

    public List<WebElement> getProductPrices() {
        return productPrices;
    }

    public void addProductToCart(String productName) {
        addBackpackButton.click();
        sleep(1000); // Pausa para visualização
    }

    public void addMultipleProductsToCart() {
        addBackpackButton.click();
        sleep(1000); // Pausa para visualização
        addBikeLightButton.click();
        sleep(1000); // Pausa para visualização
    }

    public String getCartItemCount() {
        return cartBadge.getText();
    }

    public void logout() {
        menuButton.click();
        sleep(500); // Pequena pausa para o menu abrir
        logoutLink.click();
        sleep(1000); // Pausa para visualização
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}