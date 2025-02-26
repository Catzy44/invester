RSS Investment News Analyzer

Overview

This project is a Spring Boot-based application that scans investment-related RSS feeds, extracts new articles, analyzes their content using DeepSeek LLM, and visualizes the results through charts.

Workflow

RSS Scanning (every 3 minutes):

Fetches articles from configured RSS feeds.

Identifies and stores new articles in a MySQL database.

Content Scraping:

Uses Selenium with a modified (undetected) ChromeDriver to extract full article content.

AI Analysis:

Sends article content to DeepSeek LLM for analysis.

Receives JSON-formatted results predicting EUR/USD market impact.

Stores AI-generated insights in the database.

Visualization:

Uses collected data to generate charts representing market trends.

Technologies Used

Spring Boot - Core framework for backend logic.

Hibernate - ORM for database interactions.

MySQL - Storage for articles and analysis results.

Selenium - Web scraping for full article content.

DeepSeek LLM - AI-based market trend analysis.

Scheduled Tasks - Periodic execution of scraping and analysis processes.

Installation & Setup

Clone the repository.

Configure database connection in application.properties.

Run the application using:

./gradlew bootRun

Future Improvements

Support for additional financial news sources.

Enhanced AI prompt tuning for better market predictions.

Web dashboard for real-time data visualization.

This project automates the collection and analysis of investment news to provide valuable insights into currency market movements.

