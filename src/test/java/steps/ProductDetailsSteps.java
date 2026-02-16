package steps;

import com.aventstack.extentreports.Status;
import com.microsoft.playwright.Page;
import hooks.Hooks;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ProductDetailsPage;

import static org.junit.Assert.assertTrue;

public class ProductDetailsSteps {
    private final ProductDetailsPage productDetailsPage;

    public ProductDetailsSteps() {
        Page page = Hooks.getPage();
        this.productDetailsPage = new ProductDetailsPage(page);
    }


    @When("user sets quantity to {string}")
    public void userSetsQuantityTo(String quantity) {
        Hooks.getExtentTest().log(Status.INFO, "Setting quantity to: " + quantity);
        productDetailsPage.setQuantity(Integer.parseInt(quantity));
        Hooks.getExtentTest().log(Status.PASS, "Quantity set to: " + quantity);
    }

    @When("user clicks add to cart button")
    public void userClicksAddToCartButton() {
        Hooks.getExtentTest().log(Status.INFO, "Clicking add to cart button");
        productDetailsPage.clickAddToCartButton();
        Hooks.getExtentTest().log(Status.PASS, "Add to cart button clicked");
    }

    @Then("product details page should be displayed")
    public void productDetailsPageShouldBeDisplayed() {
        Hooks.getExtentTest().log(Status.INFO, "Verifying product details page is displayed");
        assertTrue("Product details page should be visible",
                productDetailsPage.isProductDetailsPageDisplayed());
        Hooks.getExtentTest().log(Status.PASS, "Product details page is displayed");
    }

    @Then("product name should be visible")
    public void productNameShouldBeVisible() {
        Hooks.getExtentTest().log(Status.INFO, "Verifying product name is visible");
        assertTrue("Product name should be visible", productDetailsPage.isProductNameVisible());
        String productName = productDetailsPage.getProductName();
        Hooks.getExtentTest().log(Status.PASS, "Product name is visible: " + productName);
    }

    @Then("product price should be visible")
    public void productPriceShouldBeVisible() {
        Hooks.getExtentTest().log(Status.INFO, "Verifying product price is visible");
        assertTrue("Product price should be visible", productDetailsPage.isProductPriceVisible());
        String price = productDetailsPage.getProductPrice();
        Hooks.getExtentTest().log(Status.PASS, "Product price is visible: " + price);
    }

    @Then("add to cart button should be enabled")
    public void addToCartButtonShouldBeEnabled() {
        Hooks.getExtentTest().log(Status.INFO, "Verifying add to cart button is enabled");
        assertTrue("Add to cart button should be enabled",
                productDetailsPage.isAddToCartButtonEnabled());
        Hooks.getExtentTest().log(Status.PASS, "Add to cart button is enabled");
    }
}

