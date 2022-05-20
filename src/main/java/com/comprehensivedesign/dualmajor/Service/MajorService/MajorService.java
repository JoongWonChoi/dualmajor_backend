package com.comprehensivedesign.dualmajor.Service.MajorService;

import com.comprehensivedesign.dualmajor.domain.DualMajor;
import com.comprehensivedesign.dualmajor.domain.FirstMajor;

import java.util.List;
import java.util.Map;

public interface MajorService {
    FirstMajor findFirstMajorById(Long firstMajorId) throws Exception;
    DualMajor findDualMajorById(Long dualMajorId) throws Exception;
    Map<Long, List> findDualMajor(Long memberId) throws Exception;

}
