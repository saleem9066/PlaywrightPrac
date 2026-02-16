Feature: Contact Form

  @contact @smoke @regression
  Scenario: Submit complete contact form successfully
    Given user navigates to home page
    When user clicks on contact link
    Then contact form should be displayed
    When user fills contact form with following details
      | firstName | Sarah-Jane |
      | lastName  | Smith      |
      | email     | sarah@example.com |
      | subject   | Warranty   |
      | message   | A very long message to the warranty service about a warranty on a product! |
    And user uploads attachment "sample-data.txt"
    And user submits the contact form and waits for success
    Then success message should be displayed with text "Thanks for your message! We will contact you shortly."


