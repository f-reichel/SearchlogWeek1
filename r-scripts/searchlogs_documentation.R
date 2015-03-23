#Situation:
#Botlist von 11 Studenten
#stark unterschiedliche Länge (ca. 90 - 9000)
#warscheinlich zuviele erkannt (insgesamt ca. 34000)
#Masse zu groß zur händischen Überprüfung
#daher:
#	- Bots die auf mehreren Listen sind werden priorisiert
#	- alle die auf mindestens 4 Listen sind (Maximum ist 9 Listen --> Kein Bot ist auf allen Listen). Diese Liste ist 393 einträge #lang.
#	- 400er Sample der anderen Kategorien (2 oder 3 Listen / Nur eine Liste)
#		Liegt hier nicht ein Fehler?
#	- Pot-Größe ist 1193*2 (da jeder Bot von 2 Studenten überprüft werden soll)


#Genaues Vorgehen:

# The botlists.csv contains 11 list of UserIDs which are suspe-cted to be bots
pfad="~/Dropbox/teaching/regensburg/SE logs/WS2014"
botlists.csv = read.table(paste(pfad,"/botlists.csv",sep=""), header=T, sep=",")


# We want a table so wie can check the frequency of each bot. The function table() needs a vector as argument.
# So we need to transform our data.frame (botlist.csv) into a matrix. For that we use the as.matrix() function.

botlists.matrix = as.matrix(botlists.csv)

# Additionally the data.frame has 12 columns but we need only 11. The first one is just the index of each row.
# So after transforming our data into a matrix we delete the first column.

botlists.matrix = botlists.matrix[,-1]

# Now we can transform our data into a table.

botlists.table = table(botlists.matrix)


# Now we want to create a table like this, where we can see how many bots are on one list or on two, etc.:

# Number of Lists	|	9 		8 		7		6		5		4		3 		2 		1
#___________________|_______________________________________________________________________________
#  					|
# Number of Bots	|	n(9)	n(8)	n(7)	n(6)	n(5)	n(4)	n(3)	n(2)	n(1)
#  					|

# We get the numbers like this:

length(botlists.table[botlists.table==9])
#[1] 1
length(botlists.table[botlists.table==8])
#[1] 5
length(botlists.table[botlists.table==7])
#[1] 18
length(botlists.table[botlists.table==6])
#[1] 46
length(botlists.table[botlists.table ==5])
#[1] 104
length(botlists.table[botlists.table ==4])
#[1] 219
length(botlists.table[botlists.table ==3])
#[1] 1109
length(botlists.table[botlists.table ==2])
#[1] 19142
length(botlists.table[botlists.table ==1])
#[1] 13566


# The total number of bots is 34210. Way to many to check everyone of them ourselfs.
# It seems reasonable to cut the data into three categories:
#	- all IDs on 4 or more lists (393)
# 	- a sample of IDs on 2 or 3 lists (400)
#	- a sample of IDs on one list (400)

IDs.one = botlists.table[botlists.table==1]
IDs.twoOrThree = botlists.table[botlists.table==2 | botlists.table==3]
IDs.fourOrMore = botlists.table[botlists.table>3]

# Before we take the samples from the second and third category we make sure that everyone has them same results even after taking a random sample.
# For that we set the seed in R. Then we take the samples.

set.seed(1254)
IDs.sample.one = sample(IDs.one, 400)
IDs.sample.twoOrThree = sample(IDs.twoOrThree, 400)

# Finally we combine these three to create a pot of IDs we want checked.

IDs.toDistribute = c(IDs.sample.one, IDs.sample.twoOrThree, IDs.fourOrMore)

# One last thing. Because we used table() we have a list that contains the frequency of every ID.
# To get the actual IDs we need the name of each value which is the ID. Also we might want to save the number of students.

IDs.toDistribute = names(IDs.toDistribute)
students=c("Ackermann","Aigner","Beck","Brem","Edel","Eibl","Hahn","Ho","Jackermeier","Kersting","Krämer","Letia","Mattes","Nickel","Pörsch","Raab","Ratz","Reichel","Röhl","Schatz","Schenk","Schmargendorf","Schmidl","Schmitt","Schuller","Seebauer","Sellmeier","Stefani","Tirlea","Wagner","Weber","Wunderlich")
studentCount = length(students)

# we want each one to be checked by at least 2 students
IDs.toDistribute = c(IDs.toDistribute, IDs.toDistribute)

# We need to distribute the IDs to the students and we may need a little more than one line to do that.
# A good way is to define a function. And to write that function in an easy way we can write it into a seperate file,
# save it as distribution.R and load it in R.
# In this case we defined a function (distribute) that takes a vector and a number of students, and returns a matrix in which every column
# represents the IDs that need to be checked by one student.

distribute <- function(toDistribute, studentCount) {

	matrix = matrix(ncol=studentCount, nrow=(length(toDistribute)/studentCount))

	while(length(toDistribute)>studentCount){#we will loop until there are no more potential bots to distribute
		for (i in 1:ncol(matrix)){#for each student
			#get the col as a vector
			stud_alloc = matrix[,i][!is.na(matrix[,i])]
			#draw a sample that this student doesnt have already allocated
			currentSample = sample(toDistribute, 1);

		    # repeat draw until we have an element that is not already allocated to that student
			while(is.element(currentSample, stud_alloc)){
				currentSample = sample(toDistribute, 1);		
			}
	
			#get the index of the first example of the drawn sample and remove it from toDistribute
			IndexOfCurrentSample = match(currentSample, toDistribute);
			toDistribute = toDistribute[-IndexOfCurrentSample];
			#add our newly drawn sample to the student's list
			stud_alloc = c(stud_alloc, currentSample)
			length(stud_alloc)=nrow(matrix)
			matrix[,i] = stud_alloc;
			}
			print(length(toDistribute))
	}
	length(toDistribute)= studentCount
	matrix = apply(matrix, 1:2, as.numeric);
	#we want to sort the ids by user to make things a bit easier for students
	matrix = apply(matrix,2,sort)
	return (matrix);
}

IDs.distributedMatrix = distribute(IDs.toDistribute, studentCount)
colnames(IDs.distributedMatrix)= students

# Now we write the results in a file and thats it.

write.csv(IDs.distributedMatrix, paste(pfad,"/allocation.csv",sep=""))