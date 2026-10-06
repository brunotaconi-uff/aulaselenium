package exselenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class TestaRegistro {

	protected WebDriver driver;

	@BeforeEach
	public void createDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--host-resolver-rules=MAP *.googlesyndication.com 127.0.0.1, MAP *.doubleclick.net 127.0.0.1");
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://automationexercise.com");
	}

	@ParameterizedTest
	@CsvSource({
		"A",
		"Maria Silva",
		"Joao Pedro de Souza Oliveira Santos Pereira Lima"
	})
	public void registrarUsuario(String nome) {
		String email = "teste" + System.currentTimeMillis() + "@teste.com";

		assertEquals("Automation Exercise", driver.getTitle());

		driver.findElement(By.cssSelector("a[href='/login']")).click();
		assertTrue(driver.findElement(By.xpath("//h2[text()='New User Signup!']")).isDisplayed());

		driver.findElement(By.cssSelector("input[data-qa='signup-name']")).sendKeys(nome);
		driver.findElement(By.cssSelector("input[data-qa='signup-email']")).sendKeys(email);
		driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();

		assertTrue(driver.findElement(By.xpath("//b[text()='Enter Account Information']")).isDisplayed());

		driver.findElement(By.id("id_gender1")).click();
		driver.findElement(By.id("password")).sendKeys("Senha@123");
		new Select(driver.findElement(By.id("days"))).selectByValue("10");
		new Select(driver.findElement(By.id("months"))).selectByValue("5");
		new Select(driver.findElement(By.id("years"))).selectByValue("2000");

		driver.findElement(By.id("newsletter")).click();
		driver.findElement(By.id("optin")).click();

		driver.findElement(By.id("first_name")).sendKeys(nome);
		driver.findElement(By.id("last_name")).sendKeys("Teste");
		driver.findElement(By.id("company")).sendKeys("UFF");
		driver.findElement(By.id("address1")).sendKeys("Rua Passo da Patria, 156");
		driver.findElement(By.id("address2")).sendKeys("Bloco E");
		new Select(driver.findElement(By.id("country"))).selectByValue("Canada");
		driver.findElement(By.id("state")).sendKeys("Ontario");
		driver.findElement(By.id("city")).sendKeys("Toronto");
		driver.findElement(By.id("zipcode")).sendKeys("12345");
		driver.findElement(By.id("mobile_number")).sendKeys("21999999999");

		driver.findElement(By.cssSelector("button[data-qa='create-account']")).click();
		assertTrue(driver.findElement(By.cssSelector("h2[data-qa='account-created']")).isDisplayed());

		driver.findElement(By.cssSelector("a[data-qa='continue-button']")).click();
		assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logged in as')]")).getText().contains(nome));

		driver.findElement(By.cssSelector("a[href='/delete_account']")).click();
		assertTrue(driver.findElement(By.cssSelector("h2[data-qa='account-deleted']")).isDisplayed());
		driver.findElement(By.cssSelector("a[data-qa='continue-button']")).click();
	}

	@ParameterizedTest
	@CsvSource({
		"'', usuario@teste.com",
		"Maria Silva, ''",
		"Maria Silva, usuarioteste.com",
		"Maria Silva, usuario@"
	})
	public void registrarComDadosInvalidos(String nome, String email) {
		assertEquals("Automation Exercise", driver.getTitle());

		driver.findElement(By.cssSelector("a[href='/login']")).click();
		assertTrue(driver.findElement(By.xpath("//h2[text()='New User Signup!']")).isDisplayed());

		driver.findElement(By.cssSelector("input[data-qa='signup-name']")).sendKeys(nome);
		driver.findElement(By.cssSelector("input[data-qa='signup-email']")).sendKeys(email);
		driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();

		assertTrue(driver.findElement(By.xpath("//h2[text()='New User Signup!']")).isDisplayed());
		assertFalse(driver.getPageSource().contains("Enter Account Information"));
	}

	@AfterEach
	public void quitDriver() {
		driver.quit();
	}
}
