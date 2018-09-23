package com.pdfbox;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.util.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class ReadPdfWithURL {
	
	URL pdfURL;
	BufferedInputStream bis;
	PDFParser pdfParser;
	
	String getURL;
	String[] textToVerify = {"Adobe Systems Incorporated", "Parameters for Opening PDF Files"};
	WebDriver driver;
	
	@Test
	public void readPDFText() throws InterruptedException, IOException {
		
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\JMXPSX\\Downloads\\geckodriver-v0.22.0-win64\\geckodriver.exe");
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			
		driver.get("http://www.google.com");
		driver.findElement(By.id("lst-ib")).sendKeys("pdf file url", Keys.ENTER);
		
		driver.findElement(By.xpath("//a[contains(text(),'Parameters')]")).click();
		Thread.sleep(3000);
		
		getURL = driver.getCurrentUrl();
		
		pdfURL = new URL(getURL);
		InputStream inputStr = pdfURL.openStream();
		
		bis = new BufferedInputStream(inputStr);
		
		pdfParser = new PDFParser(bis);
		pdfParser.parse();
		
		String pdfData = new PDFTextStripper().getText(pdfParser.getPDDocument());
		
		Stream<String> stream1 = Arrays.stream(textToVerify);
		stream1.forEach(text -> Assert.assertTrue(pdfData.contains(text)));
		
		//Assert.assertTrue(pdfData.contains(textToVerify));
		
		System.out.println("Text is found on PDF file " + textToVerify);
		
		//https://www.adobe.com/content/dam/acom/en/devnet/acrobat/pdfs/pdf_open_parameters.pdf
		
		//https://www.youtube.com/watch?v=L0iEJv0xiz0
		
	}
	
	@AfterTest
	public void closeBrowser() {
		driver.close();
	}
	

}
