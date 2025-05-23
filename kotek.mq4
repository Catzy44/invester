//+------------------------------------------------------------------+
#property strict

#include <hash.mqh>
#include <json.mqh>

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void OnStart()
  {
// Define trendline properties
   /*string lineName = "MyTrendline";
   datetime startTime = iTime(Symbol(), PERIOD_M1, 10);  // 10 candles ago
   double startPrice = iClose(Symbol(), PERIOD_M1, 10);  // Close price 10 candles ago
   datetime endTime = iTime(Symbol(), PERIOD_M1, 0);     // Current candle
   double endPrice = iClose(Symbol(), PERIOD_M1, 0);     // Current Close price

   // Check if the object exists and remove it before creating a new one
   if(ObjectFind(lineName) == 0)
      ObjectDelete(lineName);

   // Create the trendline
   ObjectCreate(0, lineName, OBJ_TREND, 0, startTime, startPrice, endTime, endPrice);

   // Set properties
   ObjectSetInteger(0, lineName, OBJPROP_COLOR, clrRed);
   ObjectSetInteger(0, lineName, OBJPROP_WIDTH, 2);*/

   int period = Period(); //CURRENT PERIOD IN MINUTES!
   Print("Period: "+period);
   long chartId=ChartID();
   datetime currentTime = TimeCurrent();//CURRENT TIME IN SECONDS

   int daysBack = 30;
   int daysForw = 0;

//datetime back = currentTime-period*daysBack;
//for(int i = 0; i < daysBack+daysForw; i++) {
//datetime chartBarDT = back+period*i;
   datetime chartBarDT = currentTime;
   int i = 1;

   int impactData[2];
   getImpact(TimeToString(chartBarDT), impactData);

     {
      string blockName = "bloczekG"+IntegerToString(i);
      ObjectDelete(chartId,blockName);

      datetime startTime = chartBarDT;
      datetime endTime = chartBarDT+(period*60);
      double startPrice = Ask;  // 50 pips below the current price
      double endPrice = Ask + ((1*impactData[0]) * Point);    // 50 pips above the current price

      // Create the rectangle (block)
      if(!ObjectCreate(chartId, blockName, OBJ_RECTANGLE, 0, startTime, startPrice, endTime, endPrice))
        {
         Print("Error creating block: ", GetLastError());
         return;
        }

      ObjectSetInteger(chartId, blockName, OBJPROP_COLOR, clrGreen);      // Set color to red
      ObjectSetInteger(chartId, blockName, OBJPROP_BORDER_TYPE, BORDER_FLAT);  // Flat border
      ObjectSetInteger(chartId, blockName, OBJPROP_STYLE, STYLE_SOLID);  // Solid line
      ObjectSetInteger(chartId, blockName, OBJPROP_WIDTH, 1);            // Line width (border)

      ObjectSetInteger(chartId, blockName, OBJPROP_BGCOLOR, clrLightBlue); // Background color
     }
     {
      string blockName = "bloczekR"+IntegerToString(i);
      ObjectDelete(chartId,blockName);

      datetime startTime = chartBarDT;
      datetime endTime = chartBarDT+(period*60);
      double startPrice = Ask;  // 50 pips below the current price
      double endPrice = Ask - ((1*impactData[1]) * Point);    // 50 pips above the current price

      // Create the rectangle (block)
      if(!ObjectCreate(chartId, blockName, OBJ_RECTANGLE, 0, startTime, startPrice, endTime, endPrice))
        {
         Print("Error creating block: ", GetLastError());
         return;
        }

      ObjectSetInteger(chartId, blockName, OBJPROP_COLOR, clrRed);      // Set color to red
      ObjectSetInteger(chartId, blockName, OBJPROP_BORDER_TYPE, BORDER_FLAT);  // Flat border
      ObjectSetInteger(chartId, blockName, OBJPROP_STYLE, STYLE_SOLID);  // Solid line
      ObjectSetInteger(chartId, blockName, OBJPROP_WIDTH, 1);            // Line width (border)

      ObjectSetInteger(chartId, blockName, OBJPROP_BGCOLOR, clrLightBlue); // Background color
     }

//}
   ChartRedraw();
  }

struct ImpactData
  {
   int               positive;
   int               negative;
  };

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void getImpact(string date, int &impactData[])
  {
   JSONValue *response = request(date);

   if(response.isObject())
     {
      JSONObject *jo = response;

      if(!jo.getInt("positive",impactData[0]))
        {
         delete response;
         return;
        }
      if(!jo.getInt("negative",impactData[1]))
        {
         delete response;
         return;
        }

      delete jo;
     }
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+


#define COOKIES NULL
#define HEADERS "Authorization: Bearer asdf\r\nContent-Type: application/json\r\n"
#define TIMEOUT 5000

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
JSONValue* request(string date)
  {
   string responseStr = commonRequest("https://invester.endimc.pl/api/market_events/estimate","{\"timestamp\":\""+date+"\"}");
   JSONParser *parser = new JSONParser();
   JSONValue *jv = parser.parse(responseStr);

   delete parser;

   return jv;
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
string commonRequest(string url, string data)
  {

   char post[];
   StringToCharArray(data,post);

   char result[];
   string resultHeaders;

   ResetLastError();

   int resultCode = WebRequest("POST",url,HEADERS,TIMEOUT,post,result,resultHeaders);

   return CharArrayToString(result);
  }


//+------------------------------------------------------------------+
