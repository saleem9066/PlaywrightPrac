package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProductDetailsPage {
    private final Page page;

    private Locator productName() {
        return page.locator("[data-test='product-name']");
    }

    private Locator productPrice() {
        return page.locator("[data-test='unit-price']");
    }

    private Locator quantityInput() {
        return page.locator("[data-test='quantity']");
    }

    private Locator addToCartButton() {
        return page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Add to cart"));
    }


    public ProductDetailsPage(Page page) {
        this.page = page;
    }

    public void setQuantity(int quantity) {
        quantityInput().fill(String.valueOf(quantity));
    }


    public void clickAddToCartButton() {
        page.waitForResponse(
            response -> (response.url().contains("/cart") || response.url().contains("add-to-cart")) && response.status() == 200,
            () -> {
                addToCartButton().click();
            }
        );
    }

    public String getProductName() {
        productName().waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        return productName().textContent();
    }

    public String getProductPrice() {
        productPrice().waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        return productPrice().textContent();
    }

    public boolean isAddToCartButtonEnabled() {
        return addToCartButton().isEnabled();
    }


    public boolean isProductDetailsPageDisplayed() {
        return productName().isVisible() && productPrice().isVisible();
    }

    public boolean isProductNameVisible() {
        return productName().isVisible();
    }

    public boolean isProductPriceVisible() {
        return productPrice().isVisible();
    }
}
