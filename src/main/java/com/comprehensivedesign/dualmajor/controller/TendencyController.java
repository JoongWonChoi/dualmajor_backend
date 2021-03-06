package com.comprehensivedesign.dualmajor.controller;


import com.comprehensivedesign.dualmajor.Service.FirstSection.CarrierSevice.CarrierService;
import com.comprehensivedesign.dualmajor.Service.FirstSection.FirstSectionDivisionService.FirstSectionDivisionService;
import com.comprehensivedesign.dualmajor.Service.FirstSection.MemberSector.MemberSectorService;
import com.comprehensivedesign.dualmajor.Service.FirstSection.TendencyService.TendencyService;
import com.comprehensivedesign.dualmajor.Service.MajorService.MajorService;
import com.comprehensivedesign.dualmajor.config.auth.MemberAdapter;
import com.comprehensivedesign.dualmajor.domain.firstSection.Carrier.CareerQuestion;
import com.comprehensivedesign.dualmajor.domain.firstSection.Tendency.TendencyQuestion;
import com.comprehensivedesign.dualmajor.domain.sector.Sector;
import com.comprehensivedesign.dualmajor.dto.FirstSectionDto;
import com.comprehensivedesign.dualmajor.dto.FirstSectionQuestionDto;
import com.comprehensivedesign.dualmajor.repository.firstSection.carrier.CareerQuestionRepository;
import com.comprehensivedesign.dualmajor.repository.firstSection.tendency.TendencyQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TendencyController {
    @Autowired private final FirstSectionDivisionService firstSectionDivisionService;
    @Autowired private final TendencyQuestionRepository tendencyQuestionRepository;
    @Autowired private final TendencyService tendencyService;
    @Autowired private final CareerQuestionRepository carrierQuestionRepository;
    @Autowired private final CarrierService carrierService;
    @Autowired private final MajorService majorService;
    @Autowired private final MemberSectorService memberSectorService;


    /*?????? ??????*/
    @PostMapping("/firstSectionQuestion")
    public Object firstSectionQuestion(@RequestBody FirstSectionQuestionDto firstSectionQuestionDto,
                                       @AuthenticationPrincipal MemberAdapter memberAdapter) {
        FirstSectionQuestionDto questionAPI = new FirstSectionQuestionDto();
        String response = firstSectionDivisionService.findResponse(memberAdapter.getMember().getId());
        if (firstSectionQuestionDto.getQuestionNum().equals("1") || response.equals("1")) { //1??? ?????? ???????????????, 1??? ???????????? ?????? ?????? ????????? ??? ??????
            TendencyQuestion byQuestionNum = tendencyQuestionRepository.findByQuestionNum(firstSectionQuestionDto.getQuestionNum());//??????????????? ????????? ?????? ????????? ???????????? ?????? ???????????? ????????? ?????? ?????? ?????????
            questionAPI.setQuestionData(byQuestionNum.getQuestionNum(), byQuestionNum.getQuestionContent(), byQuestionNum.getResponse1(), byQuestionNum.getResponse2());
            return questionAPI.getQuestionData(); //????????? ?????? ????????? ?????? ?????? ????????? ??????
        }
        else{ //1??? ?????? ????????? ?????????, 1??? ????????? ????????? ?????? ?????? ???????????? ?????? ??????
            CareerQuestion byQuestionNum = carrierQuestionRepository.findByQuestionNum(firstSectionQuestionDto.getQuestionNum());
            questionAPI.setQuestionData(byQuestionNum.getQuestionNum(), byQuestionNum.getQuestionContent(), byQuestionNum.getResponse1(), byQuestionNum.getResponse2());
            return questionAPI.getQuestionData(); //????????? ?????? ????????? ?????? ?????? ????????? ??????
        }
    }
    /*????????? ?????? ??????*/
    @PostMapping("/firstSectionAnswer")
    public Object firstSectionAnswer(@RequestBody FirstSectionQuestionDto firstSectionQuestionDto,
                                     @AuthenticationPrincipal MemberAdapter memberAdapter) throws Exception{//?????? ????????? ??????????????? ?????? ?????? ????????????
        Map<String, Boolean> map = new HashMap<>();
        if (firstSectionQuestionDto.getQuestionNum().equals("1")) {
            firstSectionDivisionService.createResponse(memberAdapter.getMember().getId(), firstSectionQuestionDto.getAnswer());
            map.put("success", true);
        }
        else{ // 1????????? ?????? ?????? vs ????????? ?????? ??????
            String response = firstSectionDivisionService.findResponse(memberAdapter.getMember().getId());
            boolean b;
            if (response.equals("1")) { //?????? ?????? ?????? ??????
                b = tendencyService.resultProcess(firstSectionQuestionDto, memberAdapter.getMember().getId());}
            else{ //?????? ?????? ?????? ??????
                b = carrierService.resultProcess(firstSectionQuestionDto, memberAdapter.getMember().getId());}
            /* ?????? ?????? ?????? ?????? */
            if (b == true) {map.put("success", true);}
            else{map.put("success", false);}
        }
        return map;
    }

    /*?????? ?????? ?????? ?????? ??????*/
    @PostMapping("/firstSectionResult")
    public Map viewMemberSector(@RequestBody FirstSectionDto firstSectionDto, @AuthenticationPrincipal MemberAdapter memberAdapter) throws Exception {
        if(firstSectionDto.getResultType().equals("result20")){
            List<Sector> memberSector;
            Map<Long, List> dualMajor;
            try {
                memberSector = memberSectorService.findMemberSector(memberAdapter.getMember().getId());
                dualMajor = majorService.findDualMajor(memberAdapter.getMember().getId());
            } catch (Exception e) {
                Map<String, Boolean> map = new HashMap<>();
                map.put("findSectors", false);
                return map;
            }
            FirstSectionDto firstSectionApi = new FirstSectionDto();
            firstSectionApi.setMemberSectorApi(memberSector, dualMajor);
            return firstSectionApi.getMemberSectorApi();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("accessed",false);
        return map;
    }
}
