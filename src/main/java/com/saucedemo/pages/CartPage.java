package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CartPage {
    private WebDriver driver;

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isCartPageDisplayed() {
        return pageTitle.getText().equals("Your Cart");
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public void clickCheckout() {
        checkoutButton.click();
        sleep(1000); // Pausa para visualização
    }

    public void clickContinueShopping() {
        continueShoppingButton.click();
        sleep(1000);
    }

    public void removeProductFromCart(String productName) {
        for (WebElement item : cartItems) {
            WebElement title = item.findElement(By.className("inventory_item_name"));
            if (title.getText().equals(productName)) {
                WebElement removeButton = item.findElement(By.cssSelector("button[id^='remove-']"));
                removeButton.click();
                sleep(1000);
                break;
            }
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