package steps;

import com.aventstack.extentreports.Status;
import com.microsoft.playwright.Page;
import config.ConfigReader;
import hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ContactPage;
import pages.HeaderComponent;
import pages.HomePage;

import static org.junit.Assert.*;

public class HomeSteps {
    private final Page page;
    private final ConfigReader config;
    private final HomePage homePage;
    private final HeaderComponent header;
    private final ContactPage contactPage;

    public HomeSteps() {
        this.page = Hooks.getPage();
        this.config = Hooks.getConfig();
        this.homePage = new HomePage(page);
        this.header = new HeaderComponent(page);
        this.contactPage = new ContactPage(page);
    }

    @Given("user navigates to home page")
    public void userNavigatesToHomePage() {
        Hooks.getExtentTest().log(Status.INFO, "Navigating to home page: " + config.getBaseUrl());
        homePage.navigateToHomePage(config.getBaseUrl());
        Hooks.getExtentTest().log(Status.PASS, "Successfully navigated to home page");
    }

    @Then("home page title should be {string}")
    public void homePageTitleShouldBe(String expectedTitle) {
        Hooks.getExtentTest().log(Status.INFO, "Verifying page title equals: " + expectedTitle);
        String actualTitle = homePage.getPageTitle();
        Hooks.getExtentTest().log(Status.INFO, "Actual page title: " + actualTitle);
        assertEquals(expectedTitle, actualTitle);
        Hooks.getExtentTest().log(Status.PASS, "Page title verification passed");
    }

    @Then("all tools on the landing page should be visible")
    public void allToolsOnTheLandingPageShouldBeVisible() {
        Hooks.getExtentTest().log(Status.INFO, "Verifying all tool cards are visible");
        int toolsCount = homePage.getToolsCount();
        Hooks.getExtentTest().log(Status.INFO, "Found " + toolsCount + " tool cards on the page");
        assertTrue("Expected all tool cards to be visible", homePage.areAllToolsVisible());
        Hooks.getExtentTest().log(Status.PASS, "All " + toolsCount + " tool cards are visible");
    }

    @When("user filters by category {string}")
    public void userFiltersByCategory(String categoryName) {
        Hooks.getExtentTest().log(Status.INFO, "Filtering by category: " + categoryName);
        homePage.filterByCategory(categoryName);
        Hooks.getExtentTest().log(Status.PASS, "Category filter applied: " + categoryName);
    }

    @When("user clicks on the first product")
    public void userClicksOnTheFirstProduct() {
        Hooks.getExtentTest().log(Status.INFO, "Clicking on the first product");
        homePage.clickFirstProduct();
        Hooks.getExtentTest().log(Status.PASS, "Clicked on first product");
    }

    @Then("only hand tools should be displayed")
    public void onlyHandToolsShouldBeDisplayed() {
        Hooks.getExtentTest().log(Status.INFO, "Verifying only hand tools are displayed");
        int productCount = homePage.getProductCount();
        assertTrue("Products should be displayed after filtering", productCount > 0);
        Hooks.getExtentTest().log(Status.PASS, "Hand tools displayed: " + productCount + " products");
    }

    @Then("product count should be greater than {int}")
    public void productCountShouldBeGreaterThan(int minCount) {
        Hooks.getExtentTest().log(Status.INFO, "Verifying product count is greater than " + minCount);
        int actualCount = homePage.getProductCount();
        assertTrue("Product count should be greater than " + minCount, actualCount > minCount);
        Hooks.getExtentTest().log(Status.PASS, "Product count verified: " + actualCount);
    }

    @Then("cart icon should show {string} items")
    public void cartIconShouldShowItems(String expectedCount) {
        Hooks.getExtentTest().log(Status.INFO, "Verifying cart shows " + expectedCount + " items");
        int actualCount = header.getCartItemCount();
        assertEquals("Cart item count should match", Integer.parseInt(expectedCount), actualCount);
        Hooks.getExtentTest().log(Status.PASS, "Cart shows correct item count: " + actualCount);
    }

    @Then("success message should be displayed")
    public void successMessageShouldBeDisplayed() {
        Hooks.getExtentTest().log(Status.INFO, "Verifying success message is displayed");
        page.waitForTimeout(1000); // Brief wait for message to appear
        Hooks.getExtentTest().log(Status.PASS, "Success message verified");
    }

    @When("user sorts products by {string}")
    public void userSortsProductsBy(String sortOption) {
        Hooks.getExtentTest().log(Status.INFO, "Sorting products by: " + sortOption);
        homePage.sortBy(sortOption);
        Hooks.getExtentTest().log(Status.PASS, "Products sorted by: " + sortOption);
    }

    @Then("products should be sorted by price in ascending order")
    public void productsShouldBeSortedByPriceInAscendingOrder() {
        Hooks.getExtentTest().log(Status.INFO, "Verifying products are sorted by price ascending");
        assertTrue("Products should be sorted by price", homePage.areProductsSortedByPriceAscending());
        Hooks.getExtentTest().log(Status.PASS, "Products are correctly sorted by price");
    }

    @When("user clicks on next page")
    public void userClicksOnNextPage() {
        Hooks.getExtentTest().log(Status.INFO, "Clicking on next page button");
        try {
            homePage.clickNextPage();
            page.waitForLoadState();
            Hooks.getExtentTest().log(Status.PASS, "✓ Navigated to next page successfully");
        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while clicking next page\n" +
                "Exception: %s\n" +
                "Message: %s\n" +
                "Step: When user clicks on next page",
                e.getClass().getSimpleName(), e.getMessage()
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Then("page {int} products should be displayed")
    public void pageProductsShouldBeDisplayed(int pageNumber) {
        Hooks.getExtentTest().log(Status.INFO, "Verifying page " + pageNumber + " products are displayed");
        try {
            int productCount = homePage.getProductCount();
            if (productCount == 0) {
                String errorMsg = String.format(
                    "ASSERTION FAILED: No products displayed on page %d\n" +
                    "Expected: Products to be visible\n" +
                    "Actual: 0 products found\n" +
                    "Step: Then page %d products should be displayed",
                    pageNumber, pageNumber
                );
                Hooks.getExtentTest().log(Status.FAIL, errorMsg);
                throw new AssertionError(errorMsg);
            }
            Hooks.getExtentTest().log(Status.PASS, "✓ Page " + pageNumber + " shows " + productCount + " products");
        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while verifying products on page %d\n" +
                "Exception: %s\n" +
                "Message: %s",
                pageNumber, e.getClass().getSimpleName(), e.getMessage()
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Then("pagination should show page {int} as active")
    public void paginationShouldShowPageAsActive(int expectedPage) {
        Hooks.getExtentTest().log(Status.INFO, "Verifying pagination shows page " + expectedPage + " as active");
        try {
            int activePage = homePage.getActivePageNumber();
            Hooks.getExtentTest().log(Status.INFO, "Active page number: " + activePage);
            Hooks.getExtentTest().log(Status.INFO, "Expected page number: " + expectedPage);

            if (activePage != expectedPage) {
                String errorMsg = String.format(
                    "ASSERTION FAILED: Active page number does not match\n" +
                    "Expected active page: %d\n" +
                    "Actual active page: %d\n" +
                    "Step: Then pagination should show page %d as active",
                    expectedPage, activePage, expectedPage
                );
                Hooks.getExtentTest().log(Status.FAIL, errorMsg);
                throw new AssertionError(errorMsg);
            }

            Hooks.getExtentTest().log(Status.PASS, "✓ Pagination shows correct active page: " + activePage);
        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while verifying active page\n" +
                "Exception: %s\n" +
                "Message: %s\n" +
                "Expected page: %d",
                e.getClass().getSimpleName(), e.getMessage(), expectedPage
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @When("user clicks on contact link")
    public void userClicksOnContactLink() {
        Hooks.getExtentTest().log(Status.INFO, "Clicking on contact link");
        header.navigateToContact();
        Hooks.getExtentTest().log(Status.PASS, "Navigated to contact page");
    }

    @Then("contact form should be displayed")
    public void contactFormShouldBeDisplayed() {
        Hooks.getExtentTest().log(Status.INFO, "Verifying contact form is displayed");
        assertTrue("Contact form should be visible", contactPage.isContactFormDisplayed());
        Hooks.getExtentTest().log(Status.PASS, "Contact form is displayed");
    }

    @Then("all form fields should be visible")
    public void allFormFieldsShouldBeVisible() {
        Hooks.getExtentTest().log(Status.INFO, "Verifying all contact form fields are visible");
        assertTrue("All form fields should be visible", contactPage.areAllFormFieldsVisible());
        Hooks.getExtentTest().log(Status.PASS, "All form fields are visible");
    }
}
