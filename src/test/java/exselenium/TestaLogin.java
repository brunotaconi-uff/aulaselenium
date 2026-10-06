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

public class TestaLogin {

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
		"naoexiste@teste.com, 123456",
		"a@b.co, senhaerrada987",
		"naoexiste@teste.com, 1",
		"naoexiste@teste.com, senhamuitolongasenhamuitolongasenhamuitolonga",
		"nome.sobrenome+tag@dominio.com.br, Senha@123"
	})
	public void loginComEmailESenhaIncorretos(String email, String senha) {
		assertEquals("Automation Exercise", driver.getTitle());

		driver.findElement(By.cssSelector("a[href='/login']")).click();
		assertTrue(driver.findElement(By.xpath("//h2[text()='Login to your account']")).isDisplayed());

		driver.findElement(By.cssSelector("input[data-qa='login-email']")).sendKeys(email);
		driver.findElement(By.cssSelector("input[data-qa='login-password']")).sendKeys(senha);
		driver.findElement(By.cssSelector("button[data-qa='login-button']")).click();

		assertTrue(driver.findElement(By.xpath("//p[text()='Your email or password is incorrect!']")).isDisplayed());
	}

	@ParameterizedTest
	@CsvSource({
		"'', 123456",
		"naoexiste@teste.com, ''",
		"usuarioteste.com, 123456",
		"usuario@, 123456",
		"@teste.com, 123456"
	})
	public void loginComFormatoInvalido(String email, String senha) {
		assertEquals("Automation Exercise", driver.getTitle());

		driver.findElement(By.cssSelector("a[href='/login']")).click();
		assertTrue(driver.findElement(By.xpath("//h2[text()='Login to your account']")).isDisplayed());

		driver.findElement(By.cssSelector("input[data-qa='login-email']")).sendKeys(email);
		driver.findElement(By.cssSelector("input[data-qa='login-password']")).sendKeys(senha);
		driver.findElement(By.cssSelector("button[data-qa='login-button']")).click();

		assertTrue(driver.findElement(By.xpath("//h2[text()='Login to your account']")).isDisplayed());
		assertFalse(driver.getPageSource().contains("Your email or password is incorrect!"));
	}

	@AfterEach
	public void quitDriver() {
		driver.quit();
	}
}
