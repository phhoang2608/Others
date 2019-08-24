###########################################################
#
# Simple Makefile for Operating Systems Project 1
# guish
#
###########################################################
.SUFFIXES: .h .c .cpp .l .o

guish: guish.yy.o guish.o
	g++ guish.yy.o guish.o -o guish -std=c++11

guish.o: guish.cpp
	g++ -c guish.cpp -std=c++11

guish.yy.o: guish.yy.c
	gcc -c guish.yy.c 

guish.yy.c: guish.l
	flex -oguish.yy.c guish.l -std=c++11

clean:
	/bin/rm -f *.o *~ core guish guish.yy.c



