package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HeaderComponent {
    private final Page page;

    private Locator logo() {
        return page.locator("[data-test='nav-logo']");
    }

    private Locator homeLink() {
        return page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Home"));
    }

    private Locator categoriesMenu() {
        return page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Categories"));
    }

    private Locator contactLink() {
        return page.getByText("Contact", new Page.GetByTextOptions().setExact(true));
    }

    private Locator signInLink() {
        return page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Sign in"));
    }

    private Locator cartIcon() {
        return page.locator("[data-test='nav-cart']");
    }

    private Locator cartItemCount() {
        return page.locator("[data-test='cart-quantity']");
    }

    private Locator userMenu() {
        return page.locator("[data-test='nav-menu']");
    }

    public HeaderComponent(Page page) {
        this.page = page;
    }

    public void clickLogo() {
        logo().click();
    }

    public void navigateToHome() {
        homeLink().click();
    }

    public void navigateToCategories() {
        categoriesMenu().click();
    }

    public void navigateToContact() {
        contactLink().click();
    }

    public void clickSignIn() {
        signInLink().click();
    }

    public void openCart() {
        cartIcon().click();
    }

    public void openUserMenu() {
        userMenu().click();
    }

    public boolean isLogoVisible() {
        return logo().isVisible();
    }

    public int getCartItemCount() {
        String count = cartItemCount().textContent();
        return Integer.parseInt(count.trim());
    }

    public boolean isUserLoggedIn() {
        return userMenu().isVisible();
    }

    public boolean isSignInVisible() {
        return signInLink().isVisible();
    }
}

