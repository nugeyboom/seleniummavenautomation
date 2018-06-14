package com.dice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SearchFromTxtFile {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("JobKeyword.txt"));
		String line = "";

		List<String> keyword = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			keyword.add(line);

		}
		List<String> newList = new ArrayList<>();

		String newString = "";
		for (int i = 0; i < keyword.size(); i++) {

			// set up chrome driver path
			WebDriverManager.chromedriver().setup();
			// invoke selenium webdriver
			WebDriver driver = new ChromeDriver();
			// fullscreen
			driver.manage().window().fullscreen();
			// set universal wait time in case web page is slow
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			// step 1. launch browser and navigate to https://dice.com
			String url = "https://dice.com";
			driver.get(url);

			String actualTitle = driver.getTitle();
			String expectedTitle = "Job Search for Technology Professionals | Dice.com";

			if (actualTitle.equals(expectedTitle)) {
				System.out.println("Step pass. Dice hompage successfully loaded");
			} else {
				System.out.println("Step FAIL. Dice homepage did not load successfully");
				throw new RuntimeException("Step FAIL. Dice hompage did not load successfully");
			}

			driver.findElement(By.id("search-field-keyword")).clear();
			driver.findElement(By.id("search-field-keyword")).sendKeys(keyword.get(i));

			String location = "20783";

			driver.findElement(By.id("search-field-location")).clear();
			driver.findElement(By.id("search-field-location")).sendKeys(location);

			driver.findElement(By.id("findTechJobs")).click();

			String count = driver.findElement(By.id("posiCountId")).getText();
			System.out.println(count);

			int countResult = Integer.parseInt(count.replace(",", ""));
			if (countResult > 0) {
				System.out.println(
						"Keyword: " + keyword.get(i) + " Search returned " + countResult + " Results in " + location);
				newString = keyword.get(i) + "-" + countResult;
				newList.add(newString);
			} else {
				System.out.println("Step Fail: keyword : " + keyword.get(i) + " search returned" + countResult
						+ " Results in " + location);
			}
			driver.close();

		}
		System.out.println(newList.toString());
		System.out.println("Test completed -" + LocalDateTime.now());

	}

}
