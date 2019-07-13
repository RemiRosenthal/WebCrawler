package com.remi.webcrawler;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestWebCrawler {

    @Test
    public void givenValidUrlShouldCrawl() {
        final WebCrawler crawler = new WebCrawler(5, true);
        crawler.crawl("https://bbc.co.uk/");
    }

    @Test
    public void givenFileUrlShouldIgnore() {
        final WebCrawler crawler = new WebCrawler(1, true);
        crawler.crawlPage("https://download.mozilla.org/?product=firefox-stub&os=win64&lang=en-US");
    }

    @Test
    public void givenBadProtocolShouldIgnore() {
        final WebCrawler crawler = new WebCrawler(1, true);
        crawler.crawlPage("mailto:remirosenthal@protonmail.com");
    }

    @Test
    public void givenBadResponseShouldCatch() {
        final WebCrawler crawler = new WebCrawler(1, true);
        crawler.crawlPage("https://www.bbc.co.uk/invalidpage");
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
