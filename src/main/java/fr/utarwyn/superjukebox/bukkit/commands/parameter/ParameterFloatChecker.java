package fr.utarwyn.superjukebox.bukkit.commands.parameter;

import fr.utarwyn.superjukebox.bukkit.util.JUtil;

class ParameterFloatChecker implements ParameterChecker {

	@Override
	public boolean checkParam(String stringParam) {
		return JUtil.isFloat(stringParam);
	}

}
