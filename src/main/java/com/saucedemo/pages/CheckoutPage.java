// src/main/java/com/saucedemo/pages/CheckoutPage.java
package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutPage {
    private WebDriver driver;

    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        firstNameInput.sendKeys(firstName);
        sleep(1000);
        lastNameInput.sendKeys(lastName);
        sleep(1000);
        postalCodeInput.sendKeys(postalCode);
        sleep(1000);
        continueButton.click();
        sleep(1000);
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}