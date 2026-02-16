Feature: Home Page 2

  @smoke @regression
  Scenario: Open home page and verify title
    Given user navigates to home page
    Then home page title should be "Practice Software Testing - Toolshop - v5.0"
    And all tools on the landing page should be visible

  @filters @regression
  Scenario: Filter products by category using checkbox
    Given user navigates to home page
    When user filters by category "Hand Tools"
    Then only hand tools should be displayed
    And product count should be greater than 0

  @filters @regression
  Scenario: Filter products by multiple categories
    Given user navigates to home page
    When user filters by category "Hand Tools"
    And user filters by category "Power Tools"
    Then product count should be greater than 0

  @product @regression
  Scenario: View product details
    Given user navigates to home page
    When user clicks on the first product
    Then product details page should be displayed
    And product name should be visible
    And product price should be visible
    And add to cart button should be enabled

