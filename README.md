# Market News Analyzer

A **Spring Boot** application that collects and analyzes financial news to estimate **EUR/USD** market trends.

## Data presentation

MQL4 script for data presentation is called kotek.mq4 and exists in main dir

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

## Example RSS Feeds

The application scans news from:  
- [FXStreet News](https://www.fxstreet.com/rss/news)  
- [Investing.com (Economy)](https://pl.investing.com/rss/news_14.rss)  
- [Investing.com (Indicators)](https://pl.investing.com/rss/news_95.rss)  
- [Investing.com (Stock Indices)](https://pl.investing.com/rss/stock_Indices.rss)  
- [Investing.com (Metals)](https://pl.investing.com/rss/commodities_Metals.rss)  
- [Investing.com (Fundamental Analysis)](https://pl.investing.com/rss/market_overview_Fundamental.rss)  

## Future Plans
- Implement Security
- Support for **more news sources**  
- Improved AI **trend prediction**  
- **Web dashboard** for data visualization  

## License
MIT License. See **LICENSE** file for details.

---

✨ *Contributions & feedback are welcome!* 🚀
