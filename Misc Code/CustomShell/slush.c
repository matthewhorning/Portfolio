#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>

#define BUFFERSIZE 1024

//GRADING NOTES:
//
// This program appears to work correctly for single commands, CTRL-C handling,
// and CD handling. Pipelines do not appear to work correctly in that they
// do not pass data from one process to the next, and after running a
// pipeline command the terminal becomes unresponsive.
//
// Score: 85/100

//Declaration for the built in shell command (cd dir):
int slushCD(char **args);

//Function for cd dir command
int slushCD(char **args)
{
	if (args[1] == NULL){
		fprintf(stderr, "slush: expected arg");
	}
	else{
		if (chdir(args[1]) != 0){
			perror("slush");
		}
	}
	return 1;
}

//Function for running the main loop, so that SignalHandler can prompt with a new line when CTRL+C is keyed
void runLoop()
{
	while(1)
	{
		printf(">");
		char buffer[BUFFERSIZE];
		char* input = fgets(buffer, BUFFERSIZE, stdin);
		if(input == NULL){
			break; //Terminate program if null
		}
		//Create separate commandString to modify later so input remains static until user changes in next loop iteration
		char* commandString = input;
		buffer[strcspn(buffer, "\n")] = 0; //strips new line from input

		char* commands[15];
		//parse input with strtok to remove (, put each command in commands vector
		commandString = strtok(buffer, "(");
		commands[0] = commandString;
		int curCmdIndex = 0;
		int numCommands = 0;
		while(commandString != NULL)
		{
			commandString = strtok(NULL, "(");
			curCmdIndex++;
			numCommands = numCommands + 1;
			commands[curCmdIndex] = commandString;
		}

		char* myargv[15];

		curCmdIndex = curCmdIndex - 1; //Removes null character, or we get a segmentation fault
		int p = 0;
		int pipeFD[2];
		while(curCmdIndex >= 0) //Move through array in reverse
		{
			//Create the pipe and initiate it if not in last process
			int oldPipeFD;
			oldPipeFD = pipeFD[0];
			if(curCmdIndex > 0 && curCmdIndex < numCommands){ 
				pipe(pipeFD);
			}
			
			commandString = strtok(commands[curCmdIndex], " ");
			myargv[0] = commandString;
			int tokenPos = 0; //PLEASE STOP
			//Strips " " from command and creates tokens
			while(commandString != NULL)
			{
				commandString = strtok(NULL, " ");
				tokenPos++;
				myargv[tokenPos] = commandString;
			}
			curCmdIndex--;

			//Check if it's a cmd
			//Must be outside of the fork as it does not work with exit()
			//GRADING NOTE: You should be doing CD checking outside of the while loop
			//in its own code path.
			if (strcmp(myargv[0], "cd") == 0)
			{
				slushCD(myargv);
			}
			else //Fork and exec child processes
			{
				int val = fork();
				if(val == 0){
					if(curCmdIndex == numCommands){
						//redirect output to pipefd[1]
						//close pipefd[0]
						close(pipeFD[0]);
						dup2(pipeFD[1], STDOUT_FILENO);
					}

					if(curCmdIndex > 0 && curCmdIndex < numCommands){
						//redirect input to stdoutput from oldpipefd
						//redirect output to pipefd[1]
						//close pipefd[0]
						close(pipeFD[0]);
						//GRADING NOTE: This line below is part of your problems-
						// I have fixed it, but this does not fix all of your
						// programs behavior.
						//
						//The middle process wants to redirect its input from
						//the old pipe, and redirect its output to the new
						//pipe.
						//dup2(pipeFD[1], oldPipeFD);
						dup2(pipeFD[1], STDOUT_FILENO );
						dup2(oldPipeFD, STDIN_FILENO );
					}

					if(curCmdIndex == 0){
						//redirect input from oldpipefd
						dup2(oldPipeFD, STDOUT_FILENO);
					}

					execvp(myargv[0], myargv);
					perror("Could not exec");
					//Must exit child process
					exit(-1);
				}
				//Must finish off parent process
				if (curCmdIndex == numCommands)
				{
					close(pipeFD[1]);
				}
				if (curCmdIndex > 0 && curCmdIndex < numCommands)
				{
					close(oldPipeFD);
					close(pipeFD[1]);
				}
				if (curCmdIndex == 0)
				{
					close(oldPipeFD);
					waitpid(val, NULL, 0);
				}
			}
		}
	}
}


//GRADING NOTE: You don't want to call runLoop() inside your signal handler-
// all you need is an empty signal handler, which will cause signals to be 
// "ignored" in the sense that they are executed but do nothing. 
//
//Checks for signal and initiates runLoop() to clear output and allow fresh input
void signalHandler(int signum)
{
	printf("\n");
	if (signum == 2)
	{
		runLoop();
	}
	exit(-1); //Must always exit on outside to avoid nested loops
}

int main(int argc, char* argv[]){
	//Activate signal handler
	signal(2, signalHandler);

	runLoop();
	
	return 0;
}
