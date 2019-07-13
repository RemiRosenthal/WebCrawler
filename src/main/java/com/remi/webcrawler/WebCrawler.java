package com.remi.webcrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {
    private HashSet<String> urlSet;
    private AtomicInteger pagesLeft;

    public WebCrawler() {
        this(50);
    }

    public WebCrawler(final int pageLimit) {
        urlSet = new HashSet<>();
        pagesLeft = new AtomicInteger(pageLimit);
    }

    public void retrievePageUrls(final String inputUrl, final boolean trimUrls) {
        String url = inputUrl;
        if (trimUrls) {
            url = WebCrawler.trimUrl(url);
        }

        final boolean urlCrawled = urlSet.contains(url);
        if (!url.isEmpty() && !urlCrawled) {
            try {
                final int thisPagesLeft = pagesLeft.decrementAndGet();
                if (thisPagesLeft < 0) {
                    return;
                }

                urlSet.add(url);
                System.out.println(url);
                Document doc;
                try {
                    doc = Jsoup.connect(url).get();
                } catch (final UnsupportedMimeTypeException umte) {
                    System.out.println("Skipping unhandled document type.");
                    return;
                } catch (final MalformedURLException mue) {
                    System.out.println("Skipping unhandled protocol.");
                    return;
                }
                WebCrawler.printPageData(doc);
                System.out.println();

                if (thisPagesLeft > 0) {
                    final Elements pageUrls = doc.select("a[href]");
                    for (final Element page : pageUrls) {
                        retrievePageUrls(page.attr("abs:href"), trimUrls);
                    }
                }
            } catch (final IllegalArgumentException iae) {
                System.err.println("Invalid URL: \"" + url + "\". ");
                iae.printStackTrace();
            } catch (final IOException ioe) {
                System.err.println("Error at URL: \"" + url + "\". ");
                ioe.printStackTrace();
            }
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
