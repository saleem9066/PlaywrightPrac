package steps;

import com.aventstack.extentreports.Status;
import com.microsoft.playwright.Page;
import config.ConfigReader;
import hooks.Hooks;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ContactPage;

import java.util.List;
import java.util.Map;


public class ContactFormSteps {
    private final ConfigReader config;
    private final ContactPage contactPage;

    public ContactFormSteps() {
        Page page = Hooks.getPage();
        this.config = Hooks.getConfig();
        this.contactPage = new ContactPage(page);
    }

    @Given("user navigates to contact page")
    public void userNavigatesToContactPage() {
        Hooks.getExtentTest().log(Status.INFO, "Navigating to contact page");
        contactPage.navigateToContactPage(config.getBaseUrl());
        Hooks.getExtentTest().log(Status.PASS, "Successfully navigated to contact page");
    }

    @When("user fills contact form with following details")
    public void userFillsContactFormWithFollowingDetails(DataTable dataTable) {
        Map<String, String> formData = dataTable.asMap(String.class, String.class);

        Hooks.getExtentTest().log(Status.INFO, "Filling contact form with details: " + formData);

        try {
            if (formData.containsKey("firstName")) {
                contactPage.setFirstName(formData.get("firstName"));
                Hooks.getExtentTest().log(Status.INFO, "✓ First Name: " + formData.get("firstName"));
            }

            if (formData.containsKey("lastName")) {
                contactPage.setLastName(formData.get("lastName"));
                Hooks.getExtentTest().log(Status.INFO, "✓ Last Name: " + formData.get("lastName"));
            }

            if (formData.containsKey("email")) {
                contactPage.setEmail(formData.get("email"));
                Hooks.getExtentTest().log(Status.INFO, "✓ Email: " + formData.get("email"));
            }

            if (formData.containsKey("subject")) {
                contactPage.selectSubject(formData.get("subject"));
                Hooks.getExtentTest().log(Status.INFO, "✓ Subject: " + formData.get("subject"));
            }

            if (formData.containsKey("message")) {
                contactPage.setMessage(formData.get("message"));
                Hooks.getExtentTest().log(Status.INFO, "✓ Message: " + formData.get("message").substring(0, Math.min(50, formData.get("message").length())) + "...");
            }

            Hooks.getExtentTest().log(Status.PASS, "Contact form filled successfully");

        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while filling contact form\n" +
                "Exception: %s\n" +
                "Message: %s\n" +
                "Form data: %s\n" +
                "Step: When user fills contact form with following details",
                e.getClass().getSimpleName(), e.getMessage(), formData
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @When("user uploads attachment {string}")
    public void userUploadsAttachment(String fileName) {
        Hooks.getExtentTest().log(Status.INFO, "Uploading attachment: " + fileName);

        try {
            contactPage.setAttachment(fileName);
            Hooks.getExtentTest().log(Status.PASS, "✓ Attachment uploaded: " + fileName);
        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while uploading attachment\n" +
                "Exception: %s\n" +
                "Message: %s\n" +
                "File name: %s\n" +
                "Step: When user uploads attachment \"%s\"",
                e.getClass().getSimpleName(), e.getMessage(), fileName, fileName
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @When("user submits the contact form")
    public void userSubmitsTheContactForm() {
        Hooks.getExtentTest().log(Status.INFO, "Submitting contact form (validation scenario)");

        try {
            contactPage.submitForm();
            Hooks.getExtentTest().log(Status.PASS, "✓ Contact form submitted");
        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while submitting contact form\n" +
                "Exception: %s\n" +
                "Message: %s\n" +
                "Step: When user submits the contact form",
                e.getClass().getSimpleName(), e.getMessage()
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @When("user submits the contact form and waits for success")
    public void userSubmitsTheContactFormAndWaitsForSuccess() {
        Hooks.getExtentTest().log(Status.INFO, "Submitting contact form and waiting for /messages API response");

        try {
            contactPage.submitFormAndWaitForSuccess();
            Hooks.getExtentTest().log(Status.PASS, "✓ Contact form submitted successfully and API response (200) received");
        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while submitting contact form\n" +
                "Exception: %s\n" +
                "Message: %s\n" +
                "Step: When user submits the contact form and waits for success",
                e.getClass().getSimpleName(), e.getMessage()
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @When("user clears the {string} field")
    public void userClearsTheField(String fieldName) {
        Hooks.getExtentTest().log(Status.INFO, "Clearing field: " + fieldName);

        try {
            contactPage.clearField(fieldName);
            Hooks.getExtentTest().log(Status.PASS, "✓ Field cleared: " + fieldName);
        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while clearing field\n" +
                "Exception: %s\n" +
                "Message: %s\n" +
                "Field name: %s\n" +
                "Step: When user clears the \"%s\" field",
                e.getClass().getSimpleName(), e.getMessage(), fieldName, fieldName
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Then("success message should be displayed with text {string}")
    public void successMessageShouldBeDisplayedWithText(String expectedMessage) {
        Hooks.getExtentTest().log(Status.INFO, "Verifying success message contains: " + expectedMessage);

        try {
            Hooks.getPage().waitForTimeout(500);

            if (!contactPage.isSuccessMessageDisplayed()) {
                String errorMsg = "ASSERTION FAILED: Success message alert is not displayed on the page";
                Hooks.getExtentTest().log(Status.FAIL, errorMsg);
                throw new AssertionError(errorMsg);
            }

            String actualMessage = contactPage.getAlertMessage();
            Hooks.getExtentTest().log(Status.INFO, "Actual message: '" + actualMessage + "'");
            Hooks.getExtentTest().log(Status.INFO, "Expected message: '" + expectedMessage + "'");

            if (!contactPage.doesAlertContainText(expectedMessage)) {
                String errorMsg = String.format(
                    "ASSERTION FAILED: Alert message does not contain expected text\n" +
                    "Expected to contain: '%s'\n" +
                    "Actual message: '%s'\n" +
                    "Step: Then success message should be displayed with text \"%s\"",
                    expectedMessage, actualMessage, expectedMessage
                );
                Hooks.getExtentTest().log(Status.FAIL, errorMsg);
                throw new AssertionError(errorMsg);
            }

            Hooks.getExtentTest().log(Status.PASS, "✓ Success message verified successfully");

        } catch (AssertionError e) {
            throw e; // Re-throw to fail the test
        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while verifying success message\n" +
                "Exception: %s\n" +
                "Message: %s\n" +
                "Step: Then success message should be displayed with text \"%s\"",
                e.getClass().getSimpleName(), e.getMessage(), expectedMessage
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new AssertionError(errorMsg, e);
        }
    }

    @Then("error message should be displayed with text {string}")
    public void errorMessageShouldBeDisplayedWithText(String expectedError) {
        Hooks.getExtentTest().log(Status.INFO, "Verifying error message contains: " + expectedError);

        try {
            if (!contactPage.isSuccessMessageDisplayed()) {
                String errorMsg = "ASSERTION FAILED: Error message alert is not displayed on the page";
                Hooks.getExtentTest().log(Status.FAIL, errorMsg);
                throw new AssertionError(errorMsg);
            }

            String actualMessage = contactPage.getAlertMessage();
            Hooks.getExtentTest().log(Status.INFO, "Actual error: '" + actualMessage + "'");
            Hooks.getExtentTest().log(Status.INFO, "Expected error: '" + expectedError + "'");

            if (!contactPage.doesAlertContainText(expectedError)) {
                String errorMsg = String.format(
                    "ASSERTION FAILED: Error message does not contain expected text\n" +
                    "Expected to contain: '%s'\n" +
                    "Actual error message: '%s'\n" +
                    "Step: Then error message should be displayed with text \"%s\"",
                    expectedError, actualMessage, expectedError
                );
                Hooks.getExtentTest().log(Status.FAIL, errorMsg);
                throw new AssertionError(errorMsg);
            }

            Hooks.getExtentTest().log(Status.PASS, "✓ Error message verified successfully");

        } catch (AssertionError e) {
            throw e; // Re-throw to fail the test
        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while verifying error message\n" +
                "Exception: %s\n" +
                "Message: %s\n" +
                "Step: Then error message should be displayed with text \"%s\"",
                e.getClass().getSimpleName(), e.getMessage(), expectedError
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new AssertionError(errorMsg, e);
        }
    }

    @Then("all contact form fields should be visible")
    public void allContactFormFieldsShouldBeVisible(DataTable dataTable) {
        Hooks.getExtentTest().log(Status.INFO, "Verifying all contact form fields are visible");

        List<String> fieldNames = dataTable.asList(String.class);
        List<String> missingFields = new java.util.ArrayList<>();

        try {
            for (String fieldName : fieldNames) {
                boolean isVisible = contactPage.isFieldVisible(fieldName);
                if (isVisible) {
                    Hooks.getExtentTest().log(Status.PASS, "✓ " + fieldName + " is visible");
                } else {
                    Hooks.getExtentTest().log(Status.FAIL, "✗ " + fieldName + " is NOT visible");
                    missingFields.add(fieldName);
                }
            }

            if (!missingFields.isEmpty()) {
                String errorMsg = String.format(
                    "ASSERTION FAILED: Some form fields are not visible\n" +
                    "Missing fields: %s\n" +
                    "Expected all fields to be visible: %s\n" +
                    "Step: Then all contact form fields should be visible",
                    missingFields, fieldNames
                );
                Hooks.getExtentTest().log(Status.FAIL, errorMsg);
                throw new AssertionError(errorMsg);
            }

            Hooks.getExtentTest().log(Status.PASS, "✓ All contact form fields are visible");

        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = String.format(
                "EXCEPTION OCCURRED while verifying form fields visibility\n" +
                "Exception: %s\n" +
                "Message: %s\n" +
                "Fields to check: %s",
                e.getClass().getSimpleName(), e.getMessage(), fieldNames
            );
            Hooks.getExtentTest().log(Status.FAIL, errorMsg);
            throw new AssertionError(errorMsg, e);
        }
    }
}

