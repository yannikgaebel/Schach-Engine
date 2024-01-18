package schach.command;

import schach.ConsoleMain;

/**
 * The base class of every command
 */
public abstract class Command {

	protected String input;

	/**
	 * The constructor of the command
	 * 
	 * @param input the input which will be parsed to the command
	 * 
	 * @implNote the super constructor will assign the filed {@link #input} and call
	 *           {@link #parse()}
	 */
	Command(String input) {
		this.input = input;
		parse();
	}

	/**
	 * Parse the potential command
	 * 
	 * @return a instance of the {@link #Command}
	 * 
	 * @implNote This method gets called inside the super constructor
	 * @implSpec Use {@code "if(!isValid()) return;"} to insure that the input can
	 *           be parsed
	 */
	abstract void parse();

	/**
	 * Validate if a given input is a valid of command
	 * 
	 * @return true if the command is a move command
	 */
	abstract boolean isValid();

	/**
	 * Executes the function of the command
	 */
	abstract void execute(ConsoleMain consoleMain);
}
