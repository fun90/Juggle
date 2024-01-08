package net.somta.juggle.console.domain.expression.condition.parser;

import net.somta.juggle.console.domain.expression.condition.enums.OperatorEnum;
import net.somta.juggle.core.model.node.ConditionNode.ConditionExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author husong
 */
public class StringParser implements IExpressionParser {
    private final static Logger logger = LoggerFactory.getLogger(StringParser.class);
    @Override
    public String genExpression(ConditionExpression conditionExpression) {
        OperatorEnum operatorEnum = OperatorEnum.getByCode(conditionExpression.getOperator());
        String expression = null;
        switch (operatorEnum) {
            case EQUAL:
                expression = conditionExpression.getEnvKey() + "==" + conditionExpression.getValue();
                break;
            case NOT_EQUAL:
                expression = conditionExpression.getEnvKey() + "!=" + conditionExpression.getValue();
                break;
            case EMPTY:
                expression = "string.empty("+conditionExpression.getEnvKey()+")";
                break;
            case NOT_EMPTY:
                expression = "!string.empty("+conditionExpression.getEnvKey()+")";
                break;
            case CONTAINS:
                expression = "string.contains("+conditionExpression.getEnvKey()+","+conditionExpression.getValue()+")";
                break;
            case NOT_CONTAINS:
                expression = "!string.contains("+conditionExpression.getEnvKey()+","+conditionExpression.getValue()+")";
                break;
            default:
                logger.error("字符串类型不支持该操作符");
                break;
        }
        return expression;
    }
}
