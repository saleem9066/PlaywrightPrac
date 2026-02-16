Feature: Home Page

  @cart @smoke @regression
  Scenario: Add product to cart
    Given user navigates to home page
    When user clicks on the first product
    And user sets quantity to "2"
    And user clicks add to cart button
    Then cart icon should show "2" items
    And success message should be displayed

  @pagination @regression
  Scenario: Navigate through product pages
    Given user navigates to home page
    When user clicks on next page
    Then page 2 products should be displayed
    And pagination should show page 2 as active

  @contact @smoke
  Scenario: Verify contact page is accessible from home page
    Given user navigates to home page
    When user clicks on contact link
    Then contact form should be displayed

