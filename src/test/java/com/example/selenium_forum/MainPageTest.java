package com.example.selenium_forum;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Selenide.*;

public class MainPageTest {
    MainPage mainPage = new MainPage();

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
    }
    @Test
    public void login() {
        open("http://ec2-43-206-135-212.ap-northeast-1.compute.amazonaws.com:8181/login");

        $(By.id("email")).setValue("ali@pp.com");
        $(By.id("password")).setValue("ali");

        $(By.xpath("/html/body/div[2]/div/div[2]/form/div[4]/button")).click();

        String Url = webdriver().driver().getCurrentFrameUrl();
        assertEquals("http://ec2-43-206-135-212.ap-northeast-1.compute.amazonaws.com:8181/dashboard", Url);

        //webdriver().driver().clearCookies();

    }
    @Test
    public void loginFail() {
        open("http://ec2-43-206-135-212.ap-northeast-1.compute.amazonaws.com:8181/login");

        $(By.id("email")).setValue("ali@pp.com");
        $(By.id("password")).setValue("ali1");

        $(By.xpath("/html/body/div[2]/div/div[2]/form/div[4]/button")).click();

        String errorMsg = $(By.xpath("/html/body/div[2]/div/div[2]/div/ul/li")).getText();
        String Url = webdriver().driver().getCurrentFrameUrl();

        assertEquals("These credentials do not match our records.", errorMsg);
        assertEquals("http://ec2-43-206-135-212.ap-northeast-1.compute.amazonaws.com:8181/login", Url);

        webdriver().driver().clearCookies();
    }

    @Test
    public void Post() {

        open("http://ec2-43-206-135-212.ap-northeast-1.compute.amazonaws.com:8181");
        //click start a new discussion button
        $(By.xpath("/html/body/div[1]/main/section/aside/div/div/a")).click();

        $(By.id("email")).setValue("ali@pp.com");
        $(By.id("password")).setValue("ali");

        $(By.xpath("/html/body/div[2]/div/div[2]/form/div[4]/button")).click();

        //check login
        String login = webdriver().driver().getCurrentFrameUrl();
        assertEquals("http://ec2-43-206-135-212.ap-northeast-1.compute.amazonaws.com:8181/threads/create", login);

        $(By.id("title")).setValue("Selenium Test");

        //Select a Category
        Select select = new Select($(By.xpath("//*[@id=\"category\"]")));
        select.selectByIndex(1);

        //Write body
        $(By.xpath("/html/body/div[1]/main/section/article/div/div[2]/form/div/div[4]/div/trix-editor"))
                .setValue("This a post using Selenium which is a test automation tool for web applications.");

        //click post button
        $(By.xpath("/html/body/div[1]/main/section/article/div/div[2]/form/div/button")).click();

        //check if post is created
        String Url = webdriver().driver().getCurrentFrameUrl();
        String Uploaded = $(By.xpath("/html/body/div[1]/main/section/div/div[1]/span")).getText();
        assertEquals("http://ec2-43-206-135-212.ap-northeast-1.compute.amazonaws.com:8181/threads", Url);
        assertEquals("Post Uploaded!", Uploaded);

        webdriver().driver().clearCookies();


    }

    @Test
    public void likePost() throws InterruptedException {
        login();

        //go to threads page
        $(By.xpath("/html/body/div[2]/nav/div[1]/div/div[1]/div[2]/a")).click();

        //click on post
        $(By.xpath("/html/body/div[1]/main/section/div/article[1]/div/div[2]/a")).click();

        //click like button
        $(By.xpath("/html/body/div[1]/main/section/article/div/div[2]/div[2]/div[1]/div[1]/button")).click();

        Thread.sleep(2000);

        //check if like is added
        String like = $(By.xpath("/html/body/div[1]/main/section/article/div/div[2]/div[2]/div[1]/div[1]/button/span")).getText();
        assertEquals("1", like);

        webdriver().driver().clearCookies();

    }

    @Test
    public void commentPost(){
        login();

        //go to threads page
        $(By.xpath("/html/body/div[2]/nav/div[1]/div/div[1]/div[2]/a")).click();

        //click on post
        $(By.xpath("/html/body/div[1]/main/section/div/article[1]/div/div[2]/a")).click();

        //write comment
        $(By.xpath("/html/body/div[1]/main/section/div[2]/form/div[1]/input[1]")).setValue("Selenium Test Comment");

        //click comment button
        $(By.xpath("/html/body/div[1]/main/section/div[2]/form/div[2]/button")).click();

        //check if comment is added
        String uploaded = $(By.xpath("/html/body/div[1]/main/section/div[1]/span")).getText();
        assertEquals("Reply Created", uploaded);

        webdriver().driver().clearCookies();

    }
    @Test
    public void deleteReply(){
        login();

        //go to threads page
        $(By.xpath("/html/body/div[2]/nav/div[1]/div/div[1]/div[2]/a")).click();

        //click on post
        $(By.xpath("/html/body/div[1]/main/section/div/article[1]/div/div[2]/a")).click();

        //click delete button
        $(By.xpath("/html/body/div[1]/main/section/div[1]/div/div/div[1]/div[2]/div/a")).click();

        //check if post is deleted
        String deleted = $(By.xpath("/html/body/div[1]/main/section/div/span")).getText();
        assertEquals("Reply Deleted!", deleted);

        webdriver().driver().clearCookies();

    }


}
