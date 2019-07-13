package com.remi.webcrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {
    private static int pageLimit;
    private HashSet<String> visitedUrls;
    private AtomicInteger pagesLeft;
    private PriorityQueue<String> urlsToVisit;
    private static boolean trimUrls;

    public WebCrawler() {
        this(50, true);
    }

    public WebCrawler(final int pageLimit, final boolean trimUrls) {
        WebCrawler.pageLimit = pageLimit;
        this.visitedUrls = new HashSet<>();
        this.pagesLeft = new AtomicInteger(pageLimit);
        this.urlsToVisit = new PriorityQueue<>(pageLimit);
        WebCrawler.trimUrls = trimUrls;
    }

    public void crawl(final String inputUrl) {
        urlsToVisit.add(inputUrl);
        while (!urlsToVisit.isEmpty()) {
            final String thisUrl = urlsToVisit.poll();
            crawlPage(thisUrl);
        }
    }

    void crawlPage(final String inputUrl) {
        String url = inputUrl;
        if (trimUrls) {
            url = WebCrawler.trimUrl(url);
        }

        final boolean urlCrawled = visitedUrls.contains(url);
        if (!url.isEmpty() && !urlCrawled) {
            try {
                visitedUrls.add(url);
                Document doc = Jsoup.connect(url).get();

                final int thisPagesLeft = pagesLeft.decrementAndGet();
                if (thisPagesLeft < 0) {
                    urlsToVisit.clear();
                    return;
                }

                System.out.print(WebCrawler.pageLimit - thisPagesLeft);
                System.out.print(": \n");
                System.out.println(url);
                WebCrawler.printPageData(doc);
                System.out.println();

                queueUrlsOnPage(doc);
            } catch (final UnsupportedMimeTypeException umte) {
                System.out.println("Skipping unhandled document type.\n");
            } catch (final MalformedURLException mue) {
                System.out.println("Skipping unhandled protocol.\n");
            } catch (final HttpStatusException hse) {
                System.err.println(hse.getMessage());
            } catch (final IllegalArgumentException iae) {
                System.err.println("Invalid URL: \"" + url + "\". ");
                iae.printStackTrace();
            } catch (final IOException ioe) {
                System.err.println("Error at URL: \"" + url + "\". ");
                ioe.printStackTrace();
            }
        }
    }

    private void queueUrlsOnPage(final Document doc) {
        final Elements pageUrls = doc.select("a[href]");
        for (final Element page : pageUrls) {
            urlsToVisit.add(page.attr("abs:href"));
        }
    }

    static String trimUrl(final String url) {
        String trimmedUrl = url.trim();
        int lastIndex;

        if (trimmedUrl.endsWith("/")) {
            trimmedUrl = trimmedUrl.substring(0, trimmedUrl.length() - 1);
        }

        lastIndex = trimmedUrl.lastIndexOf('#');
        if (lastIndex > 1) {
            trimmedUrl = trimmedUrl.substring(0, lastIndex);
        }
        return trimmedUrl;
    }

    private static void printPageData(final Document doc) {
        System.out.println(doc.title());
        final Elements metaDesc = doc.select("meta[name=description]");
        if (!metaDesc.isEmpty()) {
            System.out.println(metaDesc.get(0).attr("content"));
        } else {
            System.out.println("[No Meta Description]");
        }
    }
}
