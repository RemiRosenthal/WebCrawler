package com.remi.webcrawler;


import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestWebCrawler {

    @Test
    public void givenValidUrlShouldCrawl() {
        final WebCrawler crawler = new WebCrawler(5);
        crawler.retrievePageUrls("https://bbc.co.uk/", true);
    }

    @Test
    public void givenFileUrlShouldIgnore() {
        final WebCrawler crawler = new WebCrawler(1);
        crawler.retrievePageUrls("https://download.mozilla.org/?product=firefox-stub&os=win64&lang=en-US", true);
    }

    @Test
    public void givenBadProtocolShouldIgnore() {
        final WebCrawler crawler = new WebCrawler(1);
        crawler.retrievePageUrls("mailto:remirosenthal@protonmail.com", true);
    }

    @Test
    public void givenUrlShouldTrimTrailingSlash() {
        final String trimmedUrl = WebCrawler.trimUrl("https://bbc.co.uk/");
        assertEquals("https://bbc.co.uk", trimmedUrl);
    }

    @Test
    public void givenUrlShouldNotTrimOtherSlash() {
        final String trimmedUrl = WebCrawler.trimUrl("https://www.bbc.co.uk/news/england");
        assertEquals("https://www.bbc.co.uk/news/england", trimmedUrl);
    }

    @Test
    public void givenUrlShouldTrimFragment() {
        final String trimmedUrl = WebCrawler.trimUrl("https://www.bbc.co.uk/news/england#fragment");
        assertEquals("https://www.bbc.co.uk/news/england", trimmedUrl);
    }
}
