package net.somta.juggle.console.domain.domain.repository;

import net.somta.juggle.console.domain.domain.DomainEntity;
import net.somta.juggle.console.domain.domain.vo.DomainQueryVO;
import net.somta.juggle.console.domain.domain.vo.DomainVO;

import java.util.List;

public interface IDomainRepository {
    void addDomain(DomainEntity domainEntity);

    void deleteDomainById(Long domainId);

    void updateDomain(Long domainId, DomainEntity domainEntity);

    List<DomainVO> queryDomainListNoPage();

    Long queryDomainListCount(DomainQueryVO domainQueryVO);

    List<DomainVO> queryDomainList(DomainQueryVO domainQueryVO);
}
