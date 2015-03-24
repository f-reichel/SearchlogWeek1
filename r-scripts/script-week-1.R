# start by loading in the data file you created
#queries = read.table("/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/user-ct-test-collection-01.csv",header=TRUE,sep=",")
queries = dbReadTable(conn = con,name = 'user_ct_test_collection_01');

# We need to handle dates
#set epoc GMT
epoc = ISOdatetime(1970,1,1,0,0,0);
epoc = epoc +60 *60; #(changes the timezone)

# Add a few more columns to our dataframes by transforming the epoc into an r date object
queries$rdate = (queries$epoc/1000)+epoc
queries$rday = format(queries$rdate,"%d %b %Y")

#get an array of all the days in the data
dates=seq(min(queries$rdate),length.out=max(queries$rdate)-min(queries$rdate), by = "1 day")
days = format(dates,"%d %b %Y")

#
# A few functions we will need - make sure you understand what these are doing.
# If you have any questions - ask!
#

#
# Get query counts per day for all users
#
# queries is the dataframe (dataset) we are working with
# users are unique users we are interested in
# dates are the days we are interested in
#
# returns a matrix with users as rows and dates as columns where each cell
# represents the count of queries submitted for a userRow on dayCol
#

get_daily_counts = function (queries, users, dates) {
# create a matrix size users x dates
dailyQueries = matrix(nrow=length(users), ncol=length(dates));
for (i in seq(from=1, to=length(users), by=1)){ # for all dates
usrdailyQueries =c(length(dates)); #new vector
for (j in seq(from=1, to=length(dates), by=1)){
usrdailyQueries[j]= queries_on_day(queries, users[i],dates[j]);
}
dailyQueries[i,] = usrdailyQueries; # set the first row to the created
vector
}
return (dailyQueries);
}

#
# gets number of queries on a given day for a given user
# queries is the dataframe (dataset) we are working with
# day is a date Object
# user is a userId
#
queries_on_day = function(queries, user, day) {
  #return the number of usrqueries that match the day in question
  # i.e.
  return(length(queries$userId[queries$userId==user&queries$rday==day]))
}

#####
# End of functions
#####

#take a sample of users
set.seed(1254);
samplesize = 1000;
sampleusers = sample(unique(queries$userId),samplesize)
samplequeries = queries[queries$userId%in%sampleusers,]

# create a matrix user x days for counts
daily_counts_mat = get_daily_counts(samplequeries,sampleusers,days);

# create a vector of our data
# we are really just flattening the data and losing the user information
v= c(daily_counts_mat)
hist(v[v<200])
summary(v[v<200])

hist(v[v>100])
length(v[v>100])

# out of
length(v)

# Let's take a look at those days and get a list of the users who submitted these:
Biguser_wh = which(daily_counts_mat>100, arr.ind=TRUE)

# for those users how many days have more than 100 queries?
bdq = as.matrix(table(Biguser_wh[,1]))
head(bdq)

