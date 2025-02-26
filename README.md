# Market News Analyzer

A **Spring Boot** application that collects and analyzes financial news to estimate **EUR/USD** market trends.

## 📌 Features

- **Automated RSS Scanning**  
  Every 3 minutes, the application fetches articles from financial news sources.
  
- **Content Scraping with Selenium**  
  Uses an **undetected ChromeDriver** to scrape full article content.

- **AI Analysis with DeepSeek LLM**  
  Extracts key insights and predicts **EUR/USD market movements** based on news content.

- **Data Storage & Visualization**  
  Stores results in **MySQL** via Hibernate and generates market impact charts.

## 🔧 Tech Stack

- **Backend**: Spring Boot, Hibernate (JPA), Selenium  
- **Database**: MySQL
- **AI Integration**: DeepSeek-P1 LLM exposed through LMStudio API
- **Scraping**: Selenium with *modified* ChromeDriver  
- **Scheduling**: Spring `@Scheduled` Tasks  

## 🚀 How It Works

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

## 📂 Example RSS Feeds

The application scans news from:  
- [FXStreet News](https://www.fxstreet.com/rss/news)  
- [Investing.com (Economy)](https://pl.investing.com/rss/news_14.rss)  
- [Investing.com (Indicators)](https://pl.investing.com/rss/news_95.rss)  
- [Investing.com (Stock Indices)](https://pl.investing.com/rss/stock_Indices.rss)  
- [Investing.com (Metals)](https://pl.investing.com/rss/commodities_Metals.rss)  
- [Investing.com (Fundamental Analysis)](https://pl.investing.com/rss/market_overview_Fundamental.rss)  

## 🛠️ Installation & Setup

### 1️⃣ Clone the Repository  
Run this command:  
`git clone https://github.com/your-username/your-repo.git`  
Then navigate to the project folder:  
`cd your-repo`  

### 2️⃣ Configure Database  
Update **application.properties** with your MySQL credentials:  

spring.datasource.url=jdbc:mysql://localhost:3306/your_db  
spring.datasource.username=your_user  
spring.datasource.password=your_password  

### 3️⃣ Run the Application  
Use this command to start:  
`./gradlew bootRun`  

## 🧠 AI Processing Output Example  

DeepSeek LLM processes each article and returns JSON like this:  
`[
	{type: 1, impact: 7, timestampStart: "2025-02-25T16:06:00.000Z", timestampEnd: "2025-02-26T00:00:00.000Z", scream: "Weaker US Treasuries"},
	{type: 0, impact: 8, timestampStart: "2025-02-25T16:06:00.000Z", timestampEnd: "2025-03-02T00:00:00.000Z", scream: "Tariff Threats Sour Mood"} 
]`

## 📌 Future Plans
- Support for **more news sources**  
- Improved AI **trend prediction**  
- **Web dashboard** for data visualization  

## 📜 License
MIT License. See **LICENSE** file for details.

---

✨ *Contributions & feedback are welcome!* 🚀
