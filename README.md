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

## License
MIT License. See **LICENSE** file for details.