package net.somta.juggle.console.domain.expression.condition.parser;

import net.somta.juggle.console.domain.expression.condition.enums.OperatorEnum;
import net.somta.juggle.core.model.node.ConditionNode.ConditionExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author husong
 */
public class DateParser implements IExpressionParser {
    private final static Logger logger = LoggerFactory.getLogger(DateParser.class);

    @Override
    public String genExpression(ConditionExpression conditionExpression) {
        OperatorEnum operatorEnum = OperatorEnum.getByCode(conditionExpression.getOperator());
        String expression = null;
        switch (operatorEnum) {
            case EQUAL:
                expression = "date.eq("+conditionExpression.getEnvKey()+","+conditionExpression.getValue()+")";
                break;
            case NOT_EQUAL:
                expression = "!date.eq("+conditionExpression.getEnvKey()+","+conditionExpression.getValue()+")";
                break;
            case GREATER_THAN:
                expression = "date.gt("+conditionExpression.getEnvKey()+","+conditionExpression.getValue()+")";
                break;
            case NOT_LESS_THAN:
                expression = "date.ge("+conditionExpression.getEnvKey()+","+conditionExpression.getValue()+")";
                break;
            case LESS_THAN:
                expression = "date.lt("+conditionExpression.getEnvKey()+","+conditionExpression.getValue()+")";
                break;
            case NOT_GREATER_THAN:
                expression = "date.le("+conditionExpression.getEnvKey()+","+conditionExpression.getValue()+")";
                break;
            default:
                logger.error("日期类型不支持该操作符");
                break;
        }
        return expression;
    }
}
