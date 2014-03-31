package main.args;

import com.beust.jcommander.JCommander;

/**
 * Processes a valid command.
 * 
 */
public interface UserCommand {

	/**
	 * Runs a simulation based on the user's arguments.
	 */
	public void process(final JCommander jCommander);
}
