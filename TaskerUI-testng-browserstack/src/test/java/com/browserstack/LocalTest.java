package com.browserstack;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LocalTest extends BrowserStackTestNGTest {

	@Test
	public void testTaskPage() throws Exception {
		driver.get("http://localhost:3000/");

		String descInput = "To Complete The UI Testing. #001";
		String dateInput = "2022-08-17";

		// Assert.assertTrue(driver.getPageSource().contains("Up and running"));
		// Check that the title is correct (correct page)
		Assert.assertTrue(driver.getTitle().equals("Task Management"));

		// Get the New Button and click
		driver.findElement(By.id("createButton")).click();

		// wait for some time
		Thread.sleep(1000);

		// Find if the element for the form exist
		WebElement descField = driver.findElement(By.id("description"));
		WebElement dateField = driver.findElement(By.id("date"));
		Assert.assertTrue(descField.isDisplayed());
		Assert.assertTrue(dateField.isDisplayed());

		dateField.sendKeys("0a");

		// Save the input
		driver.findElement(By.id("saveButton")).click();

		// wait for some time
		Thread.sleep(3000);

		// Check for error messages
		Assert.assertTrue(driver.findElement(By.id("errMsgDesc")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("errMsgDate")).isDisplayed());

		// Input valid values
		descField.sendKeys(descInput);
		dateField.sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE);
		dateField.sendKeys(dateInput);

		driver.findElement(By.id("saveButton")).click();

		// wait for some time
		Thread.sleep(5000);

		// Check that the fields are empty and error message is gone
		Assert.assertTrue(driver.findElements(By.id("errMsgDesc")).size() == 0);
		Assert.assertTrue(driver.findElements(By.id("errMsgDate")).size() == 0);
		Assert.assertEquals(descField.getText(), "");
		Assert.assertEquals(dateField.getText(), "");

		// Check that the new task is displayed
		WebElement taskDesc = driver.findElement(By.id("taskDispDesc0"));
		WebElement taskDate = driver.findElement(By.id("taskDispDate0"));
		WebElement taskCompleted = driver.findElement(By.id("taskCheckBox0"));
		Assert.assertEquals(taskDesc.getText(), descInput);
		Assert.assertEquals(taskDate.getText(), dateInput);
		Assert.assertFalse(taskCompleted.isSelected()); // Not selected

		// Close the new task box
		driver.findElement(By.id("createButtonNegative")).click();

		// wait for some time
		Thread.sleep(3000);

		// Check that element has disappeared
		Assert.assertTrue(driver.findElements(By.id("description")).size() == 0);
		Assert.assertTrue(driver.findElements(By.id("date")).size() == 0);

		// Click on the checkbox
		// input type=checkbox need to be displayed (e.g. 1px transparent in order for
		// this to work)
		taskCompleted.click();

		// wait for some time
		Thread.sleep(3000);

		Assert.assertTrue(taskCompleted.isSelected()); // Selected

		// Delete completed task
		driver.findElement(By.id("clearButton")).click();

		// wait for some time
		Thread.sleep(5000);

		String desname = "taskDispDesc";
		int i = 0;
		List<WebElement> tasklistDesc = driver.findElements(By.id(desname + String.valueOf(i)));
		while (tasklistDesc.size() > 0) {
			if (tasklistDesc.get(0).getText() == descInput) {
				Assert.assertFalse(true);
				break;
			}
			i++;
			tasklistDesc = driver.findElements(By.id(desname + String.valueOf(i)));
		}

	}

}
