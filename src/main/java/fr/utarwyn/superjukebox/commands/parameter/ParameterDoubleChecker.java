package fr.utarwyn.superjukebox.commands.parameter;

import fr.utarwyn.superjukebox.util.JUtil;

class ParameterDoubleChecker implements ParameterChecker {

	@Override
	public boolean checkParam(String stringParam) {
		return JUtil.isDouble(stringParam);
	}

}
