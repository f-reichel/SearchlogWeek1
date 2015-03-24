# http://www.jason-french.com/blog/2014/07/03/using-r-with-mysql-databases/

library(RMySQL);
con <- dbConnect(RMySQL::MySQL(), dbname= "searchlogs", group = "searchlogs");
summary(con);