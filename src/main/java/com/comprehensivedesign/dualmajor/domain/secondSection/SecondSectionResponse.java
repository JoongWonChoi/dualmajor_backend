package com.comprehensivedesign.dualmajor.domain.secondSection;

import com.comprehensivedesign.dualmajor.domain.Member;
import com.comprehensivedesign.dualmajor.domain.sector.Sector;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "SECOND_SECTION_RESPONSE")
public class SecondSectionResponse {
    @Id @GeneratedValue
    private Long id;
    //현재 위치해있는 노드 id
    private int questionId;
    //남아있는 질문의 수
    private int leftQuestions;
    //진행되는 질문 번호
    private int questionNum;
    //섹터별 총 질문 갯수
    private String totalQuestionNum; //캠퍼스 공통 질문 포함한 값
    //섹터 공통 캠퍼스 질문1
    private String campusQ1;
    //섹터 공통 캠퍼스 질문1
    private String campusQ2;
    //섹터 공통 캠퍼스 질문의 결과
    private String campus;
    @OneToOne //한명의 회원은 하나의 응답지를 갖고, 하나의 응답지는 한명의 회원의 것임
    @JoinColumn(name = "member_id")
    private Member member;
    private String sectorName;


    /*응답지 생성*/
    public void createResponse(int leftQuestions, String totalQuestionNum, Member member, String sectorName) {
        this.questionId = 1;
        this.questionNum = 1;
        this.totalQuestionNum = totalQuestionNum;
        this.leftQuestions = leftQuestions;
        this.member = member;
        this.sectorName = sectorName;
    }

    /*응답지 업데이트*/
    public void updateResponse(int questionId, int leftQuestions) {
        this.questionId = questionId;
        this.leftQuestions = leftQuestions;
        this.questionNum += 1; //질문 응답 시 마다 현재 진행중인 질문의 번호 +1
    }

    /*공통 캠퍼스 질문 후 questionNum 증가 메서드*/
    public void afterCollege() {
        this.questionNum+=1; //자동으로 1씩 증가
    }
}
