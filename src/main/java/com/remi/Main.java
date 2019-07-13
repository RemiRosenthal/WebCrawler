package com.remi;

import com.remi.webcrawler.WebCrawler;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Please enter seed URL: ");
            final String inputUrl = scanner.nextLine();

            System.out.println("Please enter page limit: ");
            final String inputPageLimit = scanner.nextLine();
            int pageLimit;
            try {
                pageLimit = Integer.parseInt(inputPageLimit);
            }
            catch (final NumberFormatException nfe) {
                pageLimit = 50;
            }
            System.out.println("Page limit = " + pageLimit);

            WebCrawler crawler = new WebCrawler(pageLimit);
            crawler.retrievePageUrls(inputUrl, true);

            System.out.println("Finished crawling!");
            System.out.println();
        }
    }
}
