#include <sys/wait.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

static char *hist[10];		//array store the command
static int ind=0;
char *inter;
int osh_history(char **args);  //show the history
int osh_twoExclaim(char **args);  //function !!
int osh_oneExclaim(char **args); //function !
int osh_cd(char **args);	//function cd
int osh_help(char **args);
int osh_exit(char **args);

//Function History to print out 10 command used lately
int osh_history(char **args)
{
	if(ind>=10)
	{
		int j;
		for(j=0;j<10;j++)
		{
			printf("%4d  %s\n", j+1, hist[j]);  //print out the command
		}
	}
	else{
		
		int k;
		for(k=0;k<ind;k++)
		{
			printf("%4d  %s\n", k+1, hist[k]);	//print out the command
		}
	}

    return 1;
}


int osh_cd(char **args)    //builtin command CD
{
  if (args[1] == NULL) {
    fprintf(stderr, "osh: expected argument to \"cd\"\n");
  } else {
    if (chdir(args[1]) != 0) {
      perror("osh");     //State out the error happened
    }
  }
  return 1;
}




//Function exit out of shell
int osh_exit(char **args)
{
  return 0;
}

char *builtin_cmd[] = {"cd","help","exit","history","!!","!"};


//List out the builtin function used in shell
int (*builtin_func[]) (char **) = {
  &osh_cd,
  &osh_help,
  &osh_exit,
  &osh_history,
  &osh_twoExclaim,
  &osh_oneExclaim
};


int osh_numBuitin() {
  return sizeof(builtin_cmd) / sizeof(char *); // determine the number of parameters inserted
}


//Function help to give information about the command in shell
int osh_help(char **args)  
{
  int i;
  printf("The following command are built in this shell:\n");

  for (i = 0; i < osh_numBuitin(); i++) {
    printf("  %s\n", builtin_cmd[i]);
  }

  printf("For information of other commands, use the man command!\n");
  return 1;
}

//Execute the command in shell
int osh_launch(char **args)
{
  pid_t pid;
  int status;

  pid = fork();
  if (pid == 0) { //child process
    if (execvp(args[0], args) == -1) {
      perror("osh");
    }
    exit(0);
  } else if (pid < 0) {    // Error happens
    perror("osh");
  } else {   // Parent process   
    do {
      waitpid(pid, &status, WUNTRACED);
    } while (!WIFEXITED(status) && !WIFSIGNALED(status)); //Aware of termination of child process
  }

  return 1;
}

//Check the condition of command entered
int osh_execute(char **args)
{
  int i;

	if (args[0] == NULL) {//If an empty command was entered. 
		return 1;
	}

	for (i = 0; i < osh_numBuitin(); i++) {   //check whether the command is builtin command or not
		if (strcmp(args[0], builtin_cmd[i]) == 0) {
			return (*builtin_func[i])(args);  //execute it
    }
	if((args[0][0]=='!')&(args[0][1]!='!'))  //check the command "!"
	{
		char c[3];
		int p;
		sscanf(args[0],"%c%d",c,&p);
		if(p==0)
		{
			printf("There must be a number following after the !. Re_enter the command!\n");
			return 1;
		}
		else
			return (*builtin_func[5])(args);
	}
		
  }

  return osh_launch(args);  //Execute other commands (not builtin commands)
}

//Insert the command into the history array
 void insertHist(char *args)
 {
	 int i = ind % 10;
	 int length=strlen(args);  //determine the length of command
	 hist[i]=malloc(sizeof(char)*length);  //allocate the size of array
	 strcpy(hist[i],args);
	 ind++;	//increase the couter
 }
 
//Function read the command inserted from the user 
char *osh_readLine()
{
  int bufsize = 1024;
  int position = 0;
  char *buffer = malloc(sizeof(char) * bufsize);
  int c;

  if (!buffer) {
    fprintf(stderr, "osh: Allocation error!\n");
    exit(0);
  }

  while (1) {
    c = getchar(); // Read a character 
    if (c == EOF || c == '\n') { // check whether we hit EOF
      buffer[position] = '\0';
	  if(buffer!=NULL)
	  {
		if(buffer[0]!='!')	//we do not store the command !! or ! into the history
		insertHist(buffer);		//Insert command into history array
	  }
      return buffer;
    } else {
      buffer[position] = c;
    }
    position++;
    if (position >= bufsize) { // In case we have exceeded the buffer, reallocate it!
      bufsize += 1024;
      buffer = realloc(buffer, bufsize);
      if (!buffer) {
        fprintf(stderr, "osh: Reallocation error!\n");
        exit(0);
      }
    }
  }
}

//Function split the command inserted into parameters
#define osh_token " \t\r\n\a"
char **osh_splitLine(char *line)
{
  int bufsize = 64, position = 0;
  char **tokens = malloc(bufsize * sizeof(char*));
  char *token, **tokens_backup;

  if (!tokens) {
    fprintf(stderr, "osh: allocation error\n");
    exit(0);
  }

  token = strtok(line, osh_token); //Determin every null space to figure out the parameter
  while (token != NULL) {			
    tokens[position] = token;
    position++;

    if (position >= bufsize) {
      bufsize += 64;
      tokens_backup = tokens;
      tokens = realloc(tokens, bufsize * sizeof(char*));
      if (!tokens) {
		free(tokens_backup);
        fprintf(stderr, "osh: Re_allocation error\n");
        exit(0);
      }
    }
    token = strtok(NULL, osh_token);
  }
  tokens[position] = NULL;
  return tokens;
}

 int osh_twoExclaim(char ** args)  //Function to re_execute the last command
{
	
	char **var;
	int i = ind % 10;
	char * input = hist[i-1];
	printf("osh> %s\n",input);					//Print out the command required
	insertHist(input);
	var = osh_splitLine(input);			//Begin to process the command
	osh_execute(var);
	
	free(var);			//Free the memory of variable
	return 1;
}
int osh_oneExclaim(char ** args)  //Function to re_execute the command in history array
{
	char * input;
	char **var;
	char c[3];
	int k;
	sscanf(args[0],"%c%d",c,&k);
	int i=ind%10;
	if(ind>=10){  //Check the condition of number entered after the "!"
		if(k>10)
		{		
			printf("The number must be less than 10. Re_enter command!\n");
			return 1;
		}
		else {
			
			input = hist[k-1];
			printf("osh> %s\n",input);			//Print out the command required
			insertHist(input);					//Begin to process the command
			var = osh_splitLine(input);
			osh_execute(var);
		}
	}
	else
	{
		if(k>ind)
		{		
			printf("The number must be less than %d. Re_enter command!\n",ind);
			return 1;
		}
		else {
			input = hist[k-1];
			printf("osh> %s\n",input);			//Print out the command required
			insertHist(input);					//Begin to process the command
			var = osh_splitLine(input);
			osh_execute(var);
		}
	}
	free(var);  //Free the memory of variable
	return 1;
}
int main()
{
	int i;
	for (i = 0; i < 10; i++)		//initialize the history array
		hist[i] = NULL;
	char *inputLine;  //store command
	char **para;	//store parameter
	int status;		//status of execution of command

	do {
    printf("osh> ");
    inputLine = osh_readLine();		//Read the input
    para = osh_splitLine(inputLine);	//break into parameter
    status = osh_execute(para);		//Execute the command. Update the status of execution
    free(inputLine);	//Free the memory of inputLine and para
    free(para);
	} while (status);

	return 1;
}

