package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutOverviewPage {
    private WebDriver driver;

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(className = "complete-header")
    private WebElement completeHeader;

    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isOverviewPageDisplayed() {
        return pageTitle.getText().equals("Checkout: Overview");
    }

    public void clickFinish() {
        finishButton.click();
        sleep(1000);
    }

    public boolean isOrderComplete() {
        return completeHeader.getText().equals("Thank you for your order!");
    }
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}