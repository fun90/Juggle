package net.somta.juggle.console.domain.variable.repository;

import net.somta.juggle.console.domain.variable.VariableInfoEntity;
import net.somta.juggle.console.domain.variable.vo.VariableInfoVO;

import java.util.List;


public interface IVariableInfoRepository {
    VariableInfoEntity queryVariableInfo(Long flowDefinitionId);

    Boolean addVariable(VariableInfoEntity variableInfoEntity);

    Boolean deleteVariableById(Long variableId);

    Boolean updateVariable(Long variableId, VariableInfoEntity variableInfoEntity);

    List<VariableInfoVO> queryVariableInfoList(Long flowDefinitionId);
}
