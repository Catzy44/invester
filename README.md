# Market News Analyzer PROROTYPE WIP

A **Spring Boot** application that collects and analyzes financial news to estimate **EUR/USD** market trends.

## Features

- **Automated RSS Scanning**  
  Every 3 minutes, the application fetches articles from financial news sources.
  
- **Content Scraping with Selenium**  
  Uses an **undetected ChromeDriver** to scrape full article content.

- **AI Analysis with DeepSeek LLM**  
  Extracts key insights and predicts **EUR/USD market movements** based on news content.

- **Data Storage & Visualization**  
  Stores results in **MySQL** via Hibernate and generates market impact charts.

## Tech Stack

- **Backend**: Spring Boot, Hibernate (JPA), Selenium  
- **Database**: MySQL
- **AI Integration**: DeepSeek-P1 LLM exposed through LMStudio API
- **Scraping**: Selenium with *modified* ChromeDriver  
- **Scheduling**: Spring `@Scheduled` Tasks  

## How It Works

1. **Fetch & Store Articles**  
   - Every 3 minutes, scans RSS feeds.
   - Stores **new articles** in the MySQL database.

2. **Scrape Full Article Content**  
   - Uses **Selenium** to extract article text.

3. **Analyze Market Impact with AI**  
   - Sends articles to **DeepSeek LLM** for analysis.  
   - AI predicts **positive/negative market impact**.  
   - Results are stored in MySQL.

4. **Visualize Data**  
   - Generates **charts** to track market impact trends.
- Improved AI **trend prediction**  
- **Web dashboard** for data visualization  

## License
MIT License. See **LICENSE** file for details.