package fr.utarwyn.superjukebox.commands.parameter;

import fr.utarwyn.superjukebox.util.JUtil;

class ParameterFloatChecker implements ParameterChecker {

	@Override
	public boolean checkParam(String stringParam) {
		return JUtil.isFloat(stringParam);
	}

}
