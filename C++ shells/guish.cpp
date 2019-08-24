//*********************************************************
//
// Phuong Hoang
// Operating Systems
//
//*********************************************************


//*********************************************************
//
// Includes and Defines
//
//*********************************************************
#include <iostream>
#include <unistd.h>
#include <sys/types.h>
#include <errno.h>
#include <signal.h>
#include <string.h>
#include <sys/wait.h>
#include <cstdlib>
#include <queue>
#include <string>
#include <signal.h>
#include <vector>
#include <sstream>
#include <fcntl.h>
using namespace std;
 
int ioRedirection(int);

//*********************************************************
//
// Extern Declarations
//
//*********************************************************
using namespace std;
extern "C"
{
  extern char **gettoks();
} 


void execution(char **toks)
{
	pid_t pid = fork();
	int processID, fd1, fd2, ioret;
	
	for(int ii=0; toks[ii] != NULL; ii++ )
    {

      ioret = ioRedirection(ii);
      if(ioret == 1)
      {
        fd1 = open(toks[ii+1],O_RDONLY);
        dup2(fd1,0);
        toks[ii] = NULL;
        close(fd1); 
      }
      else if(ioret == 2)
      {
        fd2 = open(toks[ii+1],O_RDWR | O_CREAT,0777);
        dup2(fd2,1);
        toks[ii] = NULL;
        close(fd2);
      }
	
	if(pid == 0) //child
	{
		if (processID = execvp(*toks,toks) != 0x0000)
		{
			cout<<"ERROR"<<endl;
			exit(1);
		}
		
	}
	else //parent
	{
		wait(NULL);
	}
}}


void runCommand(string command)
{
	string array[10];
	int index = atoi(command.c_str());
	string commandPrompt = array[index];
	char **nextToks = new char*[20];
	char *newArr = new char[commandPrompt.length()+1];
	
	strcpy(newArr, command.c_str());
	newArr = strtok(newArr, " ");
	
	for (int i = 0; newArr != NULL ; i++)
	{
		nextToks[i] = newArr;
		newArr = strtok(NULL," ");
	}
	execution(nextToks);
	
}

//*********************************************************
//
// Main Function
//
//*********************************************************
int main( int argc, char *argv[] )
{
  // local variables
  signal(SIGINT,SIG_IGN);
  signal(SIGQUIT,SIG_IGN);
  signal(SIGTSTP,SIG_IGN);
  int ii;
  char **toks;
  string input;
  vector<string> history;
  int retval;
  //int status;
  // initialize local variables
  ii = 0;
  toks = NULL;
  retval = 0;
 
  
  // main (infinite) loop
  while( true )
    {
		cout<<"myShell: ";
      // get arguments
      toks = gettoks();
	  input = "";
      if( toks[0] != NULL )
	{
	  // simple loop to echo all arguments


	  if( !strcmp( toks[0], "exit" ))
	    break;
	  else
	  {
		  	  ii = 0;  
			  while(toks[ii] != NULL)
			  {
				input+= toks[ii];
				input+= " ";
				ii++;
			  }
			  if(history.size() == 10)
			  {
				history.pop_back();
			  }
			  history.push_back(input);
			  
		 if(input.compare("hist ") == 0)
		 {
			int count = 0;
			 for(const string& entry : history)
			 {
				cout << count<< " " << entry << "\n";
				count++;
			 }
			history.push_back(input);
		 }
		 else if(input[0] == 'r')
		 {
			 int n;
			 if(toks[1] != NULL)
			 {
				n = atoi(toks[1]);
			 }
			 else
			 {
				n = history.size() - 2;
			 }
			 string newCommand = history.at(n);
			 runCommand(newCommand);
			 cout << "command: " << history.at(n) << "\n";

		 }	
		 else
		 {
			execution(toks);
		 }
	  }
			  
	}
    }

  // return to calling environment
  return( retval );
}

int ioRedirection(int ii)
{
	char** toks;
    int count= 0;
   
        if( !strcmp( toks[ii], "<" ))
            count++;
 
        if( !strcmp( toks[ii], ">" ))
            count=+2; 

    return count;
 
}


