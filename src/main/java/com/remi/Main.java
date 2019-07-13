package com.remi;

import com.remi.webcrawler.WebCrawler;

import java.util.Scanner;

public class Main {
    private static final int DEFAULT_PAGE_LIMIT = 50;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            mainLoop(scanner);
        }
    }

    private static void mainLoop(final Scanner scanner) {
        System.out.println("Please enter seed URL: ");
        final String inputUrl = scanner.nextLine();

        System.out.println("Please enter page limit: ");
        final String inputPageLimit = scanner.nextLine();
        int pageLimit;
        try {
            pageLimit = Integer.parseInt(inputPageLimit);
            if (pageLimit < 1) pageLimit = DEFAULT_PAGE_LIMIT;
        }
        catch (final NumberFormatException nfe) {
            pageLimit = DEFAULT_PAGE_LIMIT;
        }
        System.out.println("Page limit = " + pageLimit);

        final WebCrawler crawler = new WebCrawler(pageLimit, true);
        crawler.crawl(inputUrl);

        System.out.println("Finished crawling!");
        System.out.println();
    }
}
