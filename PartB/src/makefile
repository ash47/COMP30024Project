# The compiler
CC=/usr/java1.6/bin/javac

# Source Code
SRC=aiproj/fencemaster/Referee.java aschmid_rport/fencemaster/Schmipo.java aschmid_rport/fencemaster/Schmipo_test.java

# What to execute with
RUNNER=/usr/java1.6/bin/java

# Runner Arguments
RA=-Xmx2m

# Referee
REF=aiproj/fencemaster/Referee

# Our AI
AI=aschmid_rport.fencemaster.Schmipo

# Our manual testing agent
MAN=aschmid_rport.fencemaster.Schmipo_test

# The size of the board
SIZE=6

all: compile

compile:
	$(CC) $(SRC)

run:
	$(RUNNER) $(RA) $(REF) $(SIZE) $(AI) $(AI)

test:
	$(RUNNER) $(RA) $(REF) $(SIZE) $(AI) $(MAN)