package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import main.args.HelpCommand;
import main.args.SimulationBillesCommand;
import main.args.SimulationJeu5Couleurs;
import main.args.SimulationSchellingCommand;
import main.args.SimulationWatorCommand;
import main.args.UserCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * Entry point.
 * 
 */
public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	/**
	 * Reads the arguments, then executes the user request.
	 * 
	 * @param args
	 *            The arguments.
	 */
	public static void main(final String args[]) {

		final JCommander jCommander = new JCommander();
		final Map<String, UserCommand> commands = new HashMap<String, UserCommand>();

		Main.initCommands(jCommander, commands);

		try {
			// First, we parse the arguments
			jCommander.parse(args);

			final String commandName = jCommander.getParsedCommand();

			// Then, we execute the user request.
			if (commandName == null) {
				jCommander.usage();
			} else {
				commands.get(commandName).process(jCommander);
			}
		} catch (final ParameterException e) {
			Main.LOGGER.error(e.getMessage());
		}
	}

	/**
	 * Initializes JCommander and a UserCommand map.
	 * 
	 * The map will contain couples of (command name, command).
	 * 
	 * @param jCommander
	 *            The JCommander to initialize.
	 * @param commands
	 *            The map to fill with the existing commands.
	 */
	private static void initCommands(final JCommander jCommander,
			final Map<String, UserCommand> commands) {
		// The possible commands
		final HelpCommand helpCommand = new HelpCommand();
		final SimulationBillesCommand simulationBillesCommand = new SimulationBillesCommand();
		final SimulationSchellingCommand simulationSchellingCommand = new SimulationSchellingCommand();
		final SimulationWatorCommand simulationWatorCommand = new SimulationWatorCommand();
		final SimulationJeu5Couleurs simulationJeu5Couleurs = new SimulationJeu5Couleurs();

		// The help command will use the keyword "help"
		commands.put("help", helpCommand);
		// The simulation command will use the keyword "billes"
		commands.put("billes", simulationBillesCommand);
		// The simulation command will use the keyword "schelling"
		commands.put("schelling", simulationSchellingCommand);
		// The simulation command will use the keyword "wator"
		commands.put("wator", simulationWatorCommand);
		// The simulation command will use the keyword "5couleurs"
		commands.put("5couleurs", simulationJeu5Couleurs);

		for (final Entry<String, UserCommand> commandEntry : commands
				.entrySet()) {
			jCommander.addCommand(commandEntry.getKey(),
					commandEntry.getValue());
		}
	}
}
