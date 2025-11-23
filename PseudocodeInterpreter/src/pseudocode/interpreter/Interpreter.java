/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.interpreter;

/**
 *
 * @author Josh
 */
import pseudocode.parser.*;
import pseudocode.parser.expressions.*;
import pseudocode.errors.RuntimeError;

import java.util.List;

public class Interpreter implements ASTVisitor<Object> {
    private Environment env = new Environment();
    private final Environment functions = new Environment();

    public void runProgram(ProgramNode program) {
        program.accept(this);
    }

    @Override
    public Object visitProgram(ProgramNode program) {
        for (ASTNode stmt : program.statements) {
            stmt.accept(this);
        }
        return null;
    }

    // -------- statements ------------
    @Override
    public Object visitAssignment(AssignmentNode node) {
        Object value = evaluate(node.expression);
        env.set(node.name, value);
        return null;
    }

    @Override
    public Object visitPrint(PrintNode node) {
        Object v = evaluate(node.expression);
        BuiltInFunctions.builtinPrint(v);
        return null;
    }

    @Override
    public Object visitInput(InputNode node) {
        Object value = BuiltInFunctions.builtinInput(null);
        env.set(node.varName, value);
        return null;
    }

    @Override
    public Object visitIf(IfNode node) {
        Object cond = evaluate(node.condition);
        boolean truth = isTruthy(cond);
        if (truth) {
            node.thenBlock.accept(this);
        } else if (node.elseBlock != null) {
            node.elseBlock.accept(this);
        }
        return null;
    }

    @Override
    public Object visitWhile(WhileNode node) {
        Object cond = evaluate(node.condition);
        while (isTruthy(cond)) {
            node.body.accept(this);
            cond = evaluate(node.condition);
        }
        return null;
    }

    @Override
    public Object visitFor(ForNode node) {
        Object startO = evaluate(node.startExpr);
        Object endO = evaluate(node.endExpr);
        if (!(startO instanceof Double) || !(endO instanceof Double)) {
            throw new RuntimeError("For loop bounds must be numbers");
        }
        double start = (Double) startO;
        double end = (Double) endO;
        for (double i = start; i <= end; i++) {
            env.set(node.iterator, i);
            node.body.accept(this);
        }
        return null;
    }

    @Override
    public Object visitBlock(BlockNode node) {
        Environment previous = env;
        env = env.createChild();
        try {
            for (ASTNode stmt : node.statements) stmt.accept(this);
        } finally {
            env = previous;
        }
        return null;
    }

    @Override
    public Object visitFunction(FunctionNode node) {
        Function func = new Function(node.name, node.parameters, node.body);
        functions.set(node.name, func);
        return null;
    }

    @Override
    public Object visitReturn(ReturnNode node) {
        Object value = null;
        if (node.expression != null) {
            value = evaluate(node.expression);
        }
        throw new ReturnException(value);
    }

    // -------- expressions ------------
    private Object evaluate(ASTNode node) {
        return node.accept(this);
    }

    @Override
    public Object visitBinary(BinaryOpNode node) {
        Object left = evaluate(node.left);
        Object right = evaluate(node.right);
        String op = node.op;

        // handle logical operators
        if (op.equalsIgnoreCase("AND")) {
            return isTruthy(left) && isTruthy(right);
        }
        if (op.equalsIgnoreCase("OR")) {
            return isTruthy(left) || isTruthy(right);
        }

        // handle strings for +
        if (op.equals("+") && (left instanceof String || right instanceof String)) {
            return stringify(left) + stringify(right);
        }

        double a = asNumber(left);
        double b = asNumber(right);

        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            case "%" -> a % b;
            case ">" -> a > b;
            case ">=" -> a >= b;
            case "<" -> a < b;
            case "<=" -> a <= b;
            case "==" -> isEqual(left, right);
            case "!=" -> !isEqual(left, right);
            default -> throw new RuntimeError("Unknown binary operator: " + op);
        };
    }

    @Override
    public Object visitUnary(UnaryOpNode node) {
        Object val = evaluate(node.expr);
        String op = node.op;
        if (op.equals("-")) {
            return -asNumber(val);
        }
        if (op.equalsIgnoreCase("NOT")) {
            return !isTruthy(val);
        }
        throw new RuntimeError("Unknown unary operator: " + op);
    }

    @Override
    public Object visitLiteral(LiteralNode node) {
        return node.value;
    }

    @Override
    public Object visitVariable(VariableNode node) {
        Object value = env.get(node.name);
        if (value == null) return 0.0;
        return value;
    }

    @Override
    public Object visitCall(CallNode node) {
        Object funcObj = functions.get(node.name);
        if (funcObj == null) {
            throw new RuntimeError("Function '" + node.name + "' is not defined");
        }
        if (!(funcObj instanceof Function)) {
            throw new RuntimeError("'" + node.name + "' is not a function");
        }
        Function func = (Function) funcObj;

        if (func.parameters.size() != node.arguments.size()) {
            throw new RuntimeError("Function '" + node.name + "' expects " + func.parameters.size() + 
                " arguments but got " + node.arguments.size());
        }

        // Evaluate arguments
        List<Object> args = new java.util.ArrayList<>();
        for (ASTNode arg : node.arguments) {
            args.add(evaluate(arg));
        }

        // Save current environment and create new scope for function
        Environment previous = env;
        env = new Environment(previous); // Create child scope to access outer variables
        try {
            // Bind parameters
            for (int i = 0; i < func.parameters.size(); i++) {
                env.set(func.parameters.get(i), args.get(i));
            }

            // Execute function body
            try {
                func.body.accept(this);
                return null; // No return statement executed
            } catch (ReturnException ret) {
                return ret.value;
            }
        } finally {
            env = previous;
        }
    }

    // helpers
    private boolean isTruthy(Object o) {
        if (o == null) return false;
        if (o instanceof Boolean) return (Boolean) o;
        if (o instanceof Double) return ((Double) o) != 0.0;
        if (o instanceof String) return !((String) o).isEmpty();
        return true;
    }

    private double asNumber(Object o) {
        if (o instanceof Double) return (Double) o;
        if (o instanceof Integer) return ((Integer) o).doubleValue();
        if (o instanceof String) {
            try { return Double.parseDouble((String) o); }
            catch (NumberFormatException e) { throw new RuntimeError("Expected number but got string: " + o); }
        }
        throw new RuntimeError("Expected number value but got: " + (o == null ? "null" : o.getClass().getSimpleName()));
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        if (a instanceof Double && b instanceof Double) return ((Double) a).doubleValue() == ((Double) b).doubleValue();
        return a.equals(b);
    }

    private String stringify(Object o) {
        if (o == null) return "null";
        if (o instanceof Double) {
            double d = (Double) o;
            if (d == (long) d) return String.format("%d", (long) d);
            return Double.toString(d);
        }
        return o.toString();
    }
}
