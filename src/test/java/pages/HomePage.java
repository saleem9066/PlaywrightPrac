package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage {
    private final Page page;

    private Locator toolCards() {
        return page.locator("[data-test='product-name']");
    }

    private Locator sortDropdown() {
        return page.locator("select#sort");
    }

    private Locator nextPageButton() {
        return page.locator("a[aria-label*='Next'], button:has-text('Next'), li:not(.disabled) a[aria-label*='Next']").first();
    }

    private Locator activePage() {
        return page.locator("li.page-item.active a").first();
    }

    private Locator productPrices() {
        return page.locator("[data-test='product-price']");
    }

    private Locator productCards() {
        return page.locator("[data-test^='product-']");
    }


    public HomePage(Page page) {
        this.page = page;
    }

    public void navigateToHomePage(String baseUrl) {
        page.navigate(baseUrl);
    }

    public void filterByCategory(String categoryName) {
        page.waitForResponse(
            response -> response.url().contains("products?") && response.status() == 200,
            () -> {
                page.locator("label:has-text('" + categoryName + "')").click();
            }
        );
    }


    public void clickFirstProduct() {
        page.waitForResponse(
            response -> response.url().contains("/related") && response.status() == 200,
            () -> {
                productCards().first().click();
            }
        );
    }

    public void sortBy(String sortOption) {
        page.waitForResponse(
            response -> response.url().contains("products?") && response.status() == 200,
            () -> {
                sortDropdown().selectOption(sortOption);
            }
        );
    }

    public void clickNextPage() {
        try {
            Locator nextButton = nextPageButton();

            if (nextButton.count() > 0) {
                boolean isDisabled = page.locator("li.disabled a[aria-label*='Next']").count() > 0;

                if (!isDisabled) {
                    page.waitForResponse(
                        response -> response.url().contains("products") && response.status() == 200,
                        () -> {
                            nextButton.click();
                        }
                    );
                } else {
                    throw new RuntimeException("Next page button is disabled");
                }
            } else {
                throw new RuntimeException("Next page button not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to click next page: " + e.getMessage(), e);
        }
    }


    public String getPageTitle() {
        return page.title();
    }

    public boolean areAllToolsVisible() {
        int count = toolCards().count();
        if (count == 0) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            if (!toolCards().nth(i).isVisible()) {
                return false;
            }
        }
        return true;
    }

    public int getToolsCount() {
        return toolCards().count();
    }

    public int getActivePageNumber() {
        try {
            String ariaLabel = activePage().getAttribute("aria-label");
            if (ariaLabel != null && ariaLabel.contains("Page-")) {
                String pageNum = ariaLabel.replace("Page-", "").trim();
                return Integer.parseInt(pageNum);
            }
            String pageText = activePage().textContent();
            return Integer.parseInt(pageText.trim());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get active page number: " + e.getMessage(), e);
        }
    }

    public boolean areProductsSortedByPriceAscending() {
        int count = productPrices().count();
        if (count < 2) return true;

        double previousPrice = 0;
        for (int i = 0; i < count; i++) {
            String priceText = productPrices().nth(i).textContent();
            double currentPrice = Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
            if (currentPrice < previousPrice) {
                return false;
            }
            previousPrice = currentPrice;
        }
        return true;
    }

    public int getProductCount() {
        return productCards().count();
    }
}
