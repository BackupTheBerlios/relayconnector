/**
Copyright 2008, 2009 Mark Hooijkaas

This file is part of the RelayConnector framework.

The RelayConnector framework is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The RelayConnector framework is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with the RelayConnector framework.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.kisst.cordys.script.commands;

import org.kisst.cordys.script.CompilationContext;
import org.kisst.cordys.script.ExecutionContext;
import org.kisst.cordys.script.GenericCommand;
import org.kisst.cordys.script.Step;
import org.kisst.cordys.util.ReflectionUtil;

import com.eibus.xml.nom.Node;

public class ImportStep implements Step {
	public ImportStep(CompilationContext compiler, final int node) {
		String name = Node.getAttribute(node, "name");
		String className = Node.getAttribute(node, "class");
		Class<?> cls=ReflectionUtil.findClass(className);
		if (name==null)
			ReflectionUtil.invoke(cls, null, "compile", new Object[]{compiler, node});
		else {
			if (! Step.class.isAssignableFrom(cls))
				throw new RuntimeException("imported class "+cls.getName()+"with name "+name+" should implement the Step interface ");
			compiler.addCommand(name, new GenericCommand(cls));
		}
	}

	public void executeStep(ExecutionContext context) {
		// do nothing: import statement is only processed during compilation phase
	}
}
