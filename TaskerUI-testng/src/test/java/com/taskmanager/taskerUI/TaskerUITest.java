package com.taskmanager.taskerUI;

import java.util.List;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
/**
 * Testing UI component
 *
 */
public class TaskerUITest {

	WebDriver driver;
	String myId = UUID.randomUUID().toString();
	String descInput = "UI Testing Task. #Testing:" + myId;
	String dateInput = "2023-02-17";
	String envUrl = System.getProperty("UI_URL");

	@BeforeTest
	public void setupClass() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		try {
			MyScreenRecorder.startRecording("TestRecord");
			System.out.println("Start recording");
		} catch (Exception e) {
			System.out.println("Screen recording failed");
		}
		driver.manage().window().maximize();
	}

	@AfterTest
	public void teardown() {
		try {
			MyScreenRecorder.stopRecording();
			System.out.println("Stop recording");
		} catch (Exception e) {
			System.out.println("Screen recording failed");
		}

		driver.quit();
	}

	@Test(priority = 1, description = "Check the correct site is reached and open create tab")
	public void ReachSiteTest() throws InterruptedException {
		if(envUrl == null) {
			driver.get("http://150.230.10.235:3000/");
		}else {
			driver.get(envUrl);
		}

		// Assert.assertTrue(driver.getPageSource().contains("Up and running"));
		// Check that the title is correct (correct page)
		Assert.assertTrue(driver.getTitle().equals("Task Management"));

		// wait for some time
		Thread.sleep(2000);

		// Get the New Button and click
		driver.findElement(By.id("createButton")).click();

		// wait for some time
		Thread.sleep(2000);

		// Find if the element for the form exist
		WebElement descField = driver.findElement(By.id("description"));
		WebElement dateField = driver.findElement(By.id("date"));
		Assert.assertTrue(descField.isDisplayed());
		Assert.assertTrue(dateField.isDisplayed());

	}

	@Test(priority = 2, description = "Input Invalid values and test new task form")
	public void InputInvalidTest() throws InterruptedException {
		WebElement dateField = driver.findElement(By.id("date"));
		dateField.sendKeys("0a");

		// Save the input
		driver.findElement(By.id("saveButton")).click();

		// wait for some time
		Thread.sleep(3000);

		// Check for error messages
		Assert.assertTrue(driver.findElement(By.id("errMsgDesc")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.id("errMsgDate")).isDisplayed());
	}

	@Test(priority = 3, description = "Create new task and test exception")
	public void SaveTaskTest() throws InterruptedException {
		WebElement descField = driver.findElement(By.id("description"));
		WebElement dateField = driver.findElement(By.id("date"));

		// Input valid values
		descField.sendKeys(descInput);
		dateField.sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE);
		dateField.sendKeys(dateInput);

		// wait for some time
		Thread.sleep(2000);

		driver.findElement(By.id("saveButton")).click();

		// wait for some time
		Thread.sleep(3000);

		// Check that the fields are empty and error message is gone
		Assert.assertTrue(driver.findElements(By.id("errMsgDesc")).size() == 0);
		Assert.assertTrue(driver.findElements(By.id("errMsgDate")).size() == 0);
		Assert.assertEquals(descField.getText(), "");
		Assert.assertEquals(dateField.getText(), "");
	}

	@Test(priority = 4, description = "Check the task list, select completed and delete")
	public void CheckTaskInList() throws InterruptedException {
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
	}

	@Test(priority = 5, description = "Check the task list, select completed and delete")
	public void CompleteAndDeleteTask() throws InterruptedException {

		// Check that element has disappeared
		Assert.assertTrue(driver.findElements(By.id("description")).size() == 0);
		Assert.assertTrue(driver.findElements(By.id("date")).size() == 0);

		WebElement taskCompleted = driver.findElement(By.id("taskCheckBox0"));
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
		Thread.sleep(4000);

		String desname = "taskDispDesc";
		int i = 0;
		List<WebElement> tasklistDesc = driver.findElements(By.id(desname + String.valueOf(i)));
		while (tasklistDesc.size() > 0) {
			//Loop and ensure that none of the messages is the one input
			if (tasklistDesc.get(0).getText() == descInput) {
				Assert.assertFalse(true);
				break;
			}
			i++;
			tasklistDesc = driver.findElements(By.id(desname + String.valueOf(i)));
		}
	}

}
