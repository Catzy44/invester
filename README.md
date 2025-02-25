built on Spring Boot Framework
WORKFLOW:

1. every 3 minutes scan all provided RSS URL's
2. find new articles (not yet existing in db) on these RSS sites
3. scrape content of every article through Selenium /w modified (undetected) ChromeDriver and put it in MySQL DB through Hibernate
4. schedule task that asks DeepSeek LLM to analyze contents of these articles, save results in MySQL (ask DeepSeek to answer in JSON so i can parse it)
5. draw charts using collected data

DeepSeek prompt is built so it analyzes these articles (these are from investing news websites) and estimates changes in EURUSD market
