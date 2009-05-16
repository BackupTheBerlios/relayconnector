package org.kisst.cordys.script.commands;

import org.kisst.cordys.script.CompilationContext;
import org.kisst.cordys.script.ExecutionContext;
import org.kisst.cordys.script.Step;
import org.kisst.cordys.script.expression.Expression;
import org.kisst.cordys.script.expression.ExpressionParser;
import org.kisst.cordys.script.expression.XmlExpression;

import com.eibus.xml.nom.Node;

public class ReplaceTextStep implements Step {
	private final XmlExpression start;
	private final String elementsNamed;
	private final Expression expr;
	
	public ReplaceTextStep(CompilationContext compiler, final int node) {
		start=new XmlExpression(compiler, Node.getAttribute(node, "start"));
		elementsNamed = Node.getAttribute(node, "elementsNamed");
		compiler.declareTextVar("it"); // TODO: remove after compilation
		expr=ExpressionParser.parse(compiler, Node.getAttribute(node, "expression"));
	}

	public void executeStep(ExecutionContext context) {
		int node=start.getNode(context);
		replace(context, node);
	}

	private void replace(ExecutionContext context, int node) {
		if (elementsNamed==null || elementsNamed.equals(Node.getLocalName(node))) {
			context.setTextVar("it", Node.getData(node));
			Node.setDataElement(node, "", expr.getString(context));
		}
		if (elementsNamed!=null) {
			int child=Node.getFirstElement(node);
			while (child!=0) {
				replace(context,child);
				child=Node.getNextSibling(child);
			}
		}
	}
}