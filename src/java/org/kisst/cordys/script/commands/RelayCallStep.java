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
import org.kisst.cordys.script.Step;
import org.kisst.cordys.script.expression.XmlExpression;
import org.kisst.cordys.script.xml.ElementAppender;
import org.kisst.cordys.util.SoapUtil;

import com.eibus.xml.nom.Node;

public class RelayCallStep implements Step {

	private final XmlExpression message;
	private final boolean async;
	private final ElementAppender appender;
	private final String resultVar;

	
	public RelayCallStep(CompilationContext compiler, final int node) {
		String str=Node.getAttribute(node,	"message");
		if (str==null)
			throw new RuntimeException("relay-call step needs a message attribute");
		message=new XmlExpression(compiler, str);
		async=compiler.getSmartBooleanAttribute(node, "async", false);
		appender=new ElementAppender(compiler, node);
		
		resultVar = Node.getAttribute(node,	"resultVar");
		if (resultVar==null)
			throw new RuntimeException("resultVar should be defined when using methodExpression");
		compiler.declareXmlVar(resultVar);
	}

	public void executeStep(final ExecutionContext context) {
		int msg=message.getNode(context);
		int content=SoapUtil.getContent(msg);

		int method = context.createMethod(Node.getNamespaceURI(content), Node.getLocalName(content)).node;
		SoapUtil.mergeResponses(msg, method); // TODO: name of this method is strange
		appender.append(context, method);
		callMethod(context, method);
	}
	
	protected void callMethod(final ExecutionContext context, int method) {
		if (async)
			context.callMethodAsync(method, resultVar);
		else {
			int response = context.callMethod(method, resultVar);
			context.setXmlVar(resultVar, SoapUtil.getContent(response));
		}
	}
	
}
