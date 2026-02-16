package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ContactPage {
    private final Page page;

    private Locator firstNameField() {
        return page.getByLabel("First name");
    }

    private Locator lastNameField() {
        return page.getByLabel("Last name");
    }

    private Locator emailField() {
        return page.getByLabel("Email");
    }

    private Locator messageField() {
        return page.getByLabel("Message");
    }

    private Locator subjectField() {
        return page.getByLabel("Subject");
    }

    private Locator sendButton() {
        return page.getByText("Send");
    }

    private Locator attachmentInput() {
        return page.locator("#attachment");
    }

    private Locator alertMessage() {
        return page.getByRole(AriaRole.ALERT);
    }

    public ContactPage(Page page) {
        this.page = page;
    }

    public void navigateToContactPage(String baseUrl) {
        page.navigate(baseUrl + "/contact");
    }

    public void setFirstName(String firstName) {
        firstNameField().fill(firstName);
    }

    public void setLastName(String lastName) {
        lastNameField().fill(lastName);
    }

    public void setEmail(String email) {
        emailField().fill(email);
    }

    public void setMessage(String message) {
        messageField().fill(message);
    }

    public void selectSubject(String subject) {
        subjectField().selectOption(subject);
    }

    public void setAttachment(String fileName) {
        try {
            Path fileToUpload = Paths.get(getClass().getClassLoader().getResource("testdata/" + fileName).toURI());
            page.setInputFiles("#attachment", fileToUpload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + fileName, e);
        }
    }

    public void submitForm() {
        sendButton().click();
    }

    public void submitFormAndWaitForSuccess() {
        page.waitForResponse(
            response -> response.url().contains("/messages") && response.status() == 200,
            () -> {
                sendButton().click();
            }
        );
    }

    public void clearField(String fieldName) {
        page.getByLabel(fieldName).clear();
    }


    public boolean isContactFormDisplayed() {
        try {
            return emailField().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areAllFormFieldsVisible() {
        try {
            return firstNameField().isVisible() &&
                   lastNameField().isVisible() &&
                   emailField().isVisible() &&
                   subjectField().isVisible() &&
                   messageField().isVisible() &&
                   sendButton().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFieldVisible(String fieldName) {
        try {
            return page.getByLabel(fieldName).isVisible() || page.getByText(fieldName).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getAlertMessage() {
        alertMessage().waitFor();
        return alertMessage().textContent();
    }

    public boolean isSuccessMessageDisplayed() {
        return alertMessage().isVisible();
    }

    public boolean doesAlertContainText(String expectedText) {
        try {
            alertMessage().waitFor();
            String actualText = alertMessage().textContent();
            return actualText.contains(expectedText);
        } catch (Exception e) {
            return false;
        }
    }
}

