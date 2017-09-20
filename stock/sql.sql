/****** Script for SelectTopNRows command from SSMS  ******/
-- SQL server配置信息
-- sql server host：47.92.26.6
-- 用户名：wd
-- 密码：Abcd1234

/**1 **/
SELECT mp.TS_CODE,ts.name,mp.[CLOSE],mp.PCT_CHANGE FROM [zzfin].[dbo].[MKT_D_PRICE] mp, [zzfin].[dbo].[TS_SECURITY] ts 
WHERE mp.TRADE_DATE = (select max(trade_date) from MKT_D_PRICE) AND mp.PCT_CHANGE >= 7 
AND mp.TS_CODE = ts.TS_CODE
ORDER BY mp.PCT_CHANGE DESC 

/**2**/
SELECT mp.TS_CODE,ts.name,mp.[CLOSE],mp.PCT_CHANGE FROM [zzfin].[dbo].[MKT_D_PRICE] mp, [zzfin].[dbo].[TS_SECURITY] ts 
WHERE mp.TRADE_DATE = (select max(trade_date) from MKT_D_PRICE) AND mp.PCT_CHANGE <= -7 
AND mp.TS_CODE = ts.TS_CODE
ORDER BY mp.PCT_CHANGE DESC

/**37 基金重仓流通股**/


/**49 连续跌停, order by count**/
  select top 10 ts_code, count(*) as days from MKT_D_PRICE 
  where high=low and amount>0 and high<PRE_CLOSE and TRADE_DATE > dateadd(day, -15, (select max(trade_date) from MKT_D_PRICE)) 
  group by TS_CODE order by days desc;


/**50 连续涨停 order by count**/
  select top 10 ts_code, count(*) as days from MKT_D_PRICE 
  where high=low and amount>0 and high>PRE_CLOSE and TRADE_DATE > dateadd(day, -15, (select max(trade_date) from MKT_D_PRICE)) 
  group by TS_CODE order by days desc;


/**51 历史新高**/
  with max_close_table as ( SELECT [TS_CODE]
      ,max(PRE_CLOSE) as max_close
  FROM [zzfin].[dbo].[MKT_D_PRICE] group by TS_CODE)

  select top 10 * from MKT_D_PRICE inner join max_close_table 
  on max_close_table.TS_CODE = MKT_D_PRICE.TS_CODE 
  where max_close_table.max_close=MKT_D_PRICE.[CLOSE] order by MKT_D_PRICE.AMOUNT;


/**52 历史新低**/
  with max_close_table as ( SELECT [TS_CODE]
      ,max(PRE_CLOSE) as max_close
  FROM [zzfin].[dbo].[MKT_D_PRICE] group by TS_CODE)

  select top 10 * from MKT_D_PRICE inner join max_close_table 
  on max_close_table.TS_CODE = MKT_D_PRICE.TS_CODE 
  where max_close_table.max_close=MKT_D_PRICE.[CLOSE] order by MKT_D_PRICE.AMOUNT;


/**53 放量大涨**/
  with temp_table as ( SELECT [TS_CODE]
      ,3*AVG(AMOUNT) as MADTV3
	  ,max(trade_date) as date
  FROM [zzfin].[dbo].[MKT_D_PRICE] where TRADE_DATE >= DATEADD(day,-190,GETDATE())  group by TS_CODE),

 today_data as (select top 5000 * from MKT_D_PRICE where TRADE_DATE = (select max(trade_date) from MKT_D_PRICE))

  select top 10 * from today_data left join temp_table 
  on  temp_table.TS_CODE = today_data.TS_CODE
  where temp_table.MADTV3<today_data.AMOUNT order by today_data.AMOUNT;


/**54 放量大跌**/
  with temp_table as ( SELECT [TS_CODE]
      ,3*AVG(AMOUNT) as MADTV3
	  ,max(trade_date) as date
  FROM [zzfin].[dbo].[MKT_D_PRICE] where TRADE_DATE >= DATEADD(day,-190,GETDATE())  group by TS_CODE),

 today_data as (select top 5000 * from MKT_D_PRICE where TRADE_DATE = (select max(trade_date) from MKT_D_PRICE))

  select top 10 * from today_data left join temp_table 
  on  temp_table.TS_CODE = today_data.TS_CODE
  where temp_table.MADTV3>today_data.AMOUNT order by today_data.AMOUNT;

  