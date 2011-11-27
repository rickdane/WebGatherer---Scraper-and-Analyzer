--This is not complete yet, please check in soon for full README



WebGatherer is a lightweight application coded in Java. This project is meant as a module that can be used, by a developer, to either run from directly within an IDE / from command line or to be integrated into a larger project. Most likely this project will never have a UI but it is a possibility that one will be created as a separate project that will be loosely coupled to this project.

What its for:

A tool meant to provide developers with a simple way of creating web scraping / crawling applications that then do analysis on the web pages to extract specific data. The application has been designed in such a way that the crawler is meant to be intelligent (based on custom workflows), in so that it can be controlled to guide itself to a specific page, for example, without blindly crawling every page of the website. The goal is to minimize the number of pages that must be visited to extract certain information. Long-term, a goal is to incorporate Natural Language Processing into custom worfklows to effectively evaluate content to extract just that which meets criteria programmed by the developer.

How it works:

WebGatherer is workflow powered, meaning that it requires writing custom workflows (which are currently implemented directly with reflection, without using a 3rd party workflow engine). Default worklows exist in the project and will continue to be added, so its possible that the application can be used with minimal custom coding needed.

For each "instance" of the application that is run, the developer must program in 3 workflows. The first handles the launching of the other threads and won't be discussed any further here as the default should suffice for most all cases for now. The next one is for a thread that runs the web gathering aspect of the application, which is essentially a web crawler that saves the web pages into memory. The last workflow handles the analyzes / extraction portion of the web page processing.

The application has been built, from its core to use multiple threads, it relies heavily on queues which decouples the web page gathering process from the data processing process.
Nearly all parts of the application are loosely coupled, using Interfaces and Inversion of Control (provided through the Guice IOC framework). This enables a developer to easily put in alternate implementations of certain parts of the application.
Currently the application uses Selenium to interact with web pages and save their content. This is a fairly "heavyweight", slow way of grabbing pages, but it does provide the advantage of being able to interact more effectively with web pages that rely heavily on JavaScript, which is why I elected to use this. If the specific task calls for primarily non-dynamic web pages then this could easily be swapped out for a more light-weight method of grabbing web pages.
Underlying Principles

At its core, WebGatherer is meant to be lightweight, traditional web crawlers and scrapers tend to use the brute-force approach by blindly visiting large numbers of pages and possibly saving all of them to a database. Although a workflow could save every page it comes across to a webpage, this is not the intention (as the default workflows will show) as this is often unnecessary, adding to data storage costs. Rather, the goal is to use the two "primary" workflows to guide the web crawler to data that meets criteria and then only extract, and save, the relevant data.

Rather than continuing with a wall of text, to get into further detail of how the application works, I will soon be putting up a visual diagram that will better show the collaboration between the threads and the overall premise of how WebGatherer is intended to be used. It is meant to essentially be a framework so its really up to the developer's creativity with the workflows to make it do something great.