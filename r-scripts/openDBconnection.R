library(RMySQL);
con <- dbConnect(RMySQL::MySQL(), dbname= "searchlogs", group = "searchlogs");
summary(con);