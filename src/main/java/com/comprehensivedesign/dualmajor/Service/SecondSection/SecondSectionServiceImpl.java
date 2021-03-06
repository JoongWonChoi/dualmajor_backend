package com.comprehensivedesign.dualmajor.Service.SecondSection;


import com.comprehensivedesign.dualmajor.Service.MemberService.MemberService;
import com.comprehensivedesign.dualmajor.domain.Member;
import com.comprehensivedesign.dualmajor.domain.secondSection.*;
import com.comprehensivedesign.dualmajor.domain.sector.Sector;
import com.comprehensivedesign.dualmajor.dto.FinalResult;
import com.comprehensivedesign.dualmajor.dto.SecondSectionQuestionDto;
import com.comprehensivedesign.dualmajor.repository.SectorRepository;
import com.comprehensivedesign.dualmajor.repository.secondSection.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SecondSectionServiceImpl implements SecondSectionService{
    @Autowired private final SecondSectionResponseRepository secondSectionResponseRepository;
    @Autowired private final SectorRepository sectorRepository;
    @Autowired private final LanguageQuestionRepository languageQuestionRepository;
    @Autowired private final LanguageResultRepository languageResultRepository;
    @Autowired private final SocialQuestionRepository socialQuestionRepository;
    @Autowired private final SocialResultRepository socialResultRepository;
    @Autowired private final TechQuestionRepository techQuestionRepository;
    @Autowired private final TechResultRepository techResultRepository;
    @Autowired private final ScienceQuestionRepository scienceQuestionRepository;
    @Autowired private final ScienceResultRepository scienceResultRepository;
    @Autowired private final HumanityQuestionRepository humanityQuestionRepository;
    @Autowired private final HumanityResultRepository humanityResultRepository;
    @Autowired private final CollegeQuestionRepository collegeQuestionRepository;
    @Autowired private final MemberService memberService;

    @Autowired private final MemberFinalResultRepository memberFinalResultRepository;
    @Autowired private final MajorDetailRepository majorDetailRepository;

    /* ????????? ?????? ?????? ?????? */
    @Override
    @Transactional
    public SecondSectionResponse createResponse(Member member, String sectorName) {
        SecondSectionResponse response = new SecondSectionResponse();
        /* ?????? ???????????? ?????? ????????? ????????? ????????? ?????? ?????? */
        if (sectorName.equals("???????????????")) { //language
            response.createResponse(4, "6", member, sectorName); //???????????? ??????????????? ??? ????????? ?????? ??????
        }
        else if (sectorName.equals("?????????????????????")) { //social
            response.createResponse(3, "5", member, sectorName);
        }
        else if (sectorName.equals("??????????????????")) { //science
            response.createResponse(1, "3", member, sectorName);
        }
        else if (sectorName.equals("????????????")) { //tech
            response.createResponse(1, "3", member, sectorName);
        }
        else { //humanity
            response.createResponse(0, "2", member, sectorName);
            SecondSectionResponse temp = secondSectionResponseRepository.save(response);//????????? ????????? ????????? ???????????? ?????? ?????? ??????
            saveFinalResult(member.getId());
            return temp;
        }
        return secondSectionResponseRepository.save(response);
    }

    /* ???????????? ????????? ????????? ?????? ??????id??? ?????? ?????? ???????????? ?????? */
    @Override
    @Transactional
    public Map recommendProcess(SecondSectionQuestionDto secondSectionQuestionDto, Member member) {
        SecondSectionResponse response = secondSectionResponseRepository.findByMemberId(member.getId()).get();
        if (secondSectionQuestionDto.getQuestionNum() == response.getQuestionNum()) {
            if(secondSectionQuestionDto.getQuestionNum() == 1 || secondSectionQuestionDto.getQuestionNum() == 2){
                Map map = viewCollegeQuestions(response.getTotalQuestionNum(), response.getLeftQuestions(), response.getQuestionNum());
                return map;
            }
            return viewQuestions(response.getSectorName(), response.getQuestionId(), response.getTotalQuestionNum(), response.getLeftQuestions(), response.getQuestionNum());
        }
        else{
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("success",false );
            return map;
        }
    }

    /*??????????????? ?????? return ??????*/
    @Override
    public Map viewQuestions(String sectorName, int questionId, String totalQuestionNum, int leftQuestions, int questionNum) {
        String currentQuestionContent;
        String currentResponse1;
        String currentResponse2;
        Long id = new Long(questionId);
        System.out.println(sectorName);
        if (sectorName.equals("???????????????")) { //language
            LanguageQuestion question = languageQuestionRepository.findById(id).get();
            currentQuestionContent = question.getQuestionContent();
            currentResponse1 = question.getResponse1();
            currentResponse2 = question.getResponse2();
        }
        else if (sectorName.equals("?????????????????????")) { //social
            SocialQuestion question = socialQuestionRepository.findById(id).get();
            currentQuestionContent = question.getQuestionContent();
            currentResponse1 = question.getResponse1();
            currentResponse2 = question.getResponse2();
        }
        else if (sectorName.equals("??????????????????")) { //science
            ScienceQuestion question = scienceQuestionRepository.findById(id).get();
            currentQuestionContent = question.getQuestionContent();
            currentResponse1 = question.getResponse1();
            currentResponse2 = question.getResponse2();
        }
        else if (sectorName.equals("????????????")) { //tech
            TechQuestion question = techQuestionRepository.findById(id).get();
            currentQuestionContent = question.getQuestionContent();
            currentResponse1 = question.getResponse1();
            currentResponse2 = question.getResponse2();
        }
        else { //humanity
            HumanityQuestion question = humanityQuestionRepository.findById(id).get();
            currentQuestionContent = question.getQuestionContent();
            currentResponse1 = question.getResponse1();
            currentResponse2 = question.getResponse2();
        }
        Map<String, Object> map = new LinkedHashMap<>(); //???????????? ????????? ?????? ??? ?????? ????????? JSON???????????? ??????
        map.put("questionNum", questionNum);
        map.put("totalQuestionNum", totalQuestionNum);
        map.put("questionId", questionId);
        map.put("questionContent", currentQuestionContent);
        map.put("response1", currentResponse1);
        map.put("response2", currentResponse2);
        return map;
    }
    @Override
    public Map viewCollegeQuestions(String totalQuestionNum, int leftQuestions, int questionNum) {
        String currentQuestionContent;
        String currentResponse1;
        String currentResponse2;
        //?????? ?????? 1, 2?????? ????????? ????????????
        if(questionNum == 1){
            CollegeQuestion question = collegeQuestionRepository.findById(1L).get();
            currentQuestionContent = question.getQuestionContent();
            currentResponse1 = question.getResponse1();
            currentResponse2 = question.getResponse2();
        }
        else {
            CollegeQuestion question = collegeQuestionRepository.findById(2L).get();
            currentQuestionContent = question.getQuestionContent();
            currentResponse1 = question.getResponse1();
            currentResponse2 = question.getResponse2();
        }
        //?????? campus????????? ?????? ?????? ?????? ??? ?????? id??? ?????? ?????? ??????????????? ?????? quesetionNum??? ??????, ???????????? ??????
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("questionNum", questionNum);
        map.put("totalQuestionNum", totalQuestionNum);
        map.put("questionContent", currentQuestionContent);
        map.put("response1", currentResponse1);
        map.put("response2", currentResponse2);
        return map;
    }

    /*?????? ????????? ?????? 1,2??? ?????? ??????*/
    @Override
    @Transactional
    public boolean saveCollegeAnswer(SecondSectionQuestionDto secondSectionQuestionDto, Long memberId) {
        SecondSectionResponse response = secondSectionResponseRepository.findByMemberId(memberId).get();
        if (secondSectionQuestionDto.getQuestionNum() == 1) { //1??? ?????? ???vs???
            response.setCampusQ1(secondSectionQuestionDto.getAnswer());
            response.afterCollege();
            return true;
        }
        else{ //2????????? ??????????????? ??????vs?????????
            response.setCampusQ2(secondSectionQuestionDto.getAnswer()); //???????????? 2?????? ????????? ?????? ???????????? ????????? ????????? ???????????? ??????
            response.afterCollege();
            if(response.getCampusQ1().equals("1") && response.getCampusQ2().equals("2")){
                response.setCampus("???????????????");
            }
            else if(response.getCampusQ1().equals("2") && response.getCampusQ2().equals("2")){
                response.setCampus("??????????????????");
            }
            else{
                response.setCampus("?????? ??????");
            }
            return true;
        }
    }

    /* ????????? ????????? ?????? ?????? ?????? ?????? */
    @Override
    @Transactional
    public SecondSectionResponse binaryTree(String answer, Long memberId) {
        SecondSectionResponse response = secondSectionResponseRepository.findByMemberId(memberId).get();
        int currentQId = response.getQuestionId();
        int leftQuestions = response.getLeftQuestions()-1; //????????? ????????? ?????? ??? ?????? ?????? ?????? ??????-=1
        if (answer.equals("1")) { //????????? ???????????? ????????? ?????? ??? ?????? ????????? ?????? ?????? ???????????????, ?????? ?????? ?????? n * 2
            currentQId *= 2;
        }
        else{ //????????? ???????????? ????????? ?????? ??? ?????? ????????? ????????? ?????? ???????????????, ?????? ?????? ?????? (n * 2) + 1
            currentQId = (currentQId * 2) + 1;
        }
        response.updateResponse(currentQId, leftQuestions); //?????? ????????? ???????????? ?????? Id???, ?????? ????????? ?????? ????????????
        if (response.getLeftQuestions() == 0) { //????????? ????????? ???????????? ?????? ??? ?????? ????????????
            saveFinalResult(memberId);
        }
        return response;
    }
    /*??? ????????? ?????? ?????? ??? MemberFinalResult ???????????? ?????? ?????? ????????????*/
    @Transactional
    private void saveFinalResult(Long memberId) {
        Member member = memberService.findById(memberId);
        SecondSectionResponse response = secondSectionResponseRepository.findByMemberId(memberId).get();
        //????????? ????????? ?????????????????? questionId : int
        //result ???????????? ?????????????????? questionId : String
        //????????? questionId??? result???????????? ???????????? ????????? String ???????????? ???????????????.
        String questionId = Integer.toString(response.getQuestionId());
        MemberFinalResult memberFinalResult = new MemberFinalResult();
        String resultType;
        if (response.getSectorName().equals("???????????????")) {
            LanguageResult result = languageResultRepository.findByQuestionId(questionId);
            resultType = result.getResultType();
        }
        else if (response.getSectorName().equals("?????????????????????")) {
            SocialResult result = socialResultRepository.findByQuestionId(questionId);
            resultType = result.getResultType();
        }
        else if (response.getSectorName().equals("??????????????????")) {
            ScienceResult result = scienceResultRepository.findByQuestionId(questionId);
            resultType = result.getResultType();
        }
        else if (response.getSectorName().equals("????????????")) {
            TechResult result = techResultRepository.findByQuestionId(questionId);
            resultType = result.getResultType();
        }
        else{
            HumanityResult result = humanityResultRepository.findByQuestionId(questionId);
            resultType = result.getResultType();
        }
        memberFinalResult.createMemberFinalResult(member, resultType); //?????? ????????? ?????? ????????? resultType??? ?????? ?????? ?????? ???????????? ??????
        memberFinalResultRepository.save(memberFinalResult);
    }

    @Override
    public Map<String, Object> viewResult(Long id) {
        MemberFinalResult result = memberFinalResultRepository.findByMemberId(id);
        String campus = secondSectionResponseRepository.findByMemberId(id).get().getCampus();
        List<FinalResult> finalResults;
        if (campus.equals("???????????????") || campus.equals("??????????????????")) {
            //?????? campus??? ??????????????? ????????????, ?????? ??????????????? ?????? ??????????????? ?????? ?????? ?????? ??????
            finalResults = majorDetailRepository.findByResultTypeWithCampus(result.getResultType(), campus);
        }
        else{
            //????????? ?????? ?????? ?????? ?????? ??????????????? ?????? ??????
            finalResults = majorDetailRepository.findByResultType(result.getResultType());
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("info", finalResults);
        return map;
    }



}
