package com.letter.member;

import com.letter.exception.CustomException;
import com.letter.exception.ErrorCode;
import com.letter.member.dto.MemberRequest;
import com.letter.member.dto.MemberResponse;
import com.letter.member.dto.MemberStatusResponse;
import com.letter.member.dto.role.UserStatusRole;
import com.letter.member.entity.Couple;
import com.letter.member.entity.InviteOpponent;
import com.letter.member.entity.Member;
import com.letter.member.repository.*;
import com.letter.question.entity.Answer;
import com.letter.question.entity.Question;
import com.letter.question.entity.SelectQuestion;
import com.letter.question.repository.AnswerRepository;
import com.letter.question.repository.QuestionRepository;
import com.letter.question.repository.SelectQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final InviteOpponentRepository inviteOpponentRepository;
    private final AnswerRepository answerRepository;
    private final SelectQuestionRepository selectQuestionRepository;

    private final InviteOpponentCustomRepositoryImpl inviteOpponentCustomRepository;


    /**
     * 상대 초대 링크 생성 api
     *
     * @param request
     * @return
     */
    public MemberResponse.CreateInviteLinkResponse createInviteLink(MemberRequest.CreateInviteLinkRequest request, Member member) {

        // 초대하는 사람이 커플인지 확인. 커플이면 링크 생성 불가
        if(member.getCouple() != null && member.getCouple().getId() != null){
            log.error("이미 커플이 된 회원입니다.");
            throw new CustomException(ErrorCode.ALREADY_COUPLE);
        }

        // 기존에 보낸 초대 링크가 있다면 초대 링크 키의 유효시간 체크. 링크 키가 생성된지 3일 이내 이면 초대 링크 생성 불가
        InviteOpponent existLinkData = inviteOpponentCustomRepository.existByMemberIdAndCreatedAt(member.getId());

        if(existLinkData != null){
            log.error("기존에 생성된 초대링크가 있습니다.");
            throw new CustomException(ErrorCode.ALREADY_LINK);
        }

        // 링크 고유 키 생성
        String uuid = UUID.randomUUID().toString();

        // 질문 아이디 조회
        Question question = questionRepository.findQuestionById(request.getQuestionId()).orElseThrow(
                () -> new CustomException(ErrorCode.QUESTION_NOT_FOUND)
        );

        // 상대 초대 테이블에 정보 request 셋팅
        InviteOpponent inviteOpponent = request.toCreateInviteLink(uuid, question, member);

        // 상대 초대 테이블에 정보 등록
        inviteOpponentRepository.save(inviteOpponent);


        return MemberResponse.CreateInviteLinkResponse.builder()
                .linkKey(uuid)
                .question(question.getQuestionContents())
                .invitedPersonName(member.getName())
                .build();
    }



    /**
     * 초대 수락 API
     *
     * @param request
     * @return
     */
    @Transactional
    public MemberResponse.AcceptInviteLinkResponse acceptedInvite(MemberRequest.AcceptInviteLinkRequest request, Member member) {

        // 링크 고유 값으로 상대 초대 테이블에 해당 회원 조회
        InviteOpponent inviteOpponent = inviteOpponentRepository.findQuestionByLinkKey(request.getLinkKey()).orElseThrow(
                () -> new CustomException(ErrorCode.BAD_REQUEST)
        );

        // 초대한 사람의 아이디와 초대된 사람의 아이디가 같을 경우 에러 처리
        if(inviteOpponent.getMember().getId().equals(member.getId())){
            log.info("초대한 사람의 아이디와 초대된 사람의 아이디가 같습니다.");
            throw new CustomException(ErrorCode.MEMBER_BAD_REQUEST);
        }

        Member findMember = memberRepository.findById(inviteOpponent.getMember().getId()).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 초대한 회원이 이미 커플이 된 경우 에러처리
        if(findMember.getCouple() != null && findMember.getCouple().getId() != null)
        {
            log.error("상대는 이미 커플이 된 회원입니다.");
            throw new CustomException(ErrorCode.OPPONENT_ALREADY_COUPLE);
        }

        //초대 링크로 들어온 사용자가 커플인지 확인. 커플이면 에러처리
        if(member.getCouple() != null && member.getCouple().getId() != null){
            log.error("이미 커플이 된 회원입니다.");
            throw new CustomException(ErrorCode.ALREADY_COUPLE);
        }

        // TODO 쿼리 보내는 것을 줄여보기

        //상대 초대 테이블에 노출 여부가 N인 경우 에러처리
        boolean isExistIsShowN = inviteOpponentCustomRepository.existByMemberIdAndIsShow(member.getId());

        if(isExistIsShowN){
            log.error("이미 커플이 된 회원이여서 링크 노출 여부가 N 입니다.");
            throw new CustomException(ErrorCode.ALREADY_COUPLE);
        }

        // 초대 링크 키의 유효시간 체크. 링크 키가 생성된지 1일 이내 이면 초대 수락 불가
        InviteOpponent existValidLink = inviteOpponentCustomRepository.existByLinkKeyAndCreatedAt(request.getLinkKey());

        if(existValidLink == null){
            log.error("해당 링크는 유효시간이 만료되었습니다.");
            throw new CustomException(ErrorCode.EXPIRED_LINK);
        }

        // 커플 정보 request 셋팅
        // 선택 질문 테이블 등록될 때 자동으로 등록 됨
        Couple couple = request.toCoupleInfo();

        if (Objects.isNull(inviteOpponent.getQuestion())) {
            log.error("질문 아이디가 null 입니다.");
        }

        // 회원 테이블에 커플 아이디 업데이트(초대한 회원)
        Optional<Member> inviteMember = memberRepository.findById(inviteOpponent.getMember().getId());
        inviteMember.ifPresent(invite -> {
            invite.setCouple(couple);
        });

        // 회원 테이블에 커플 아이디 업데이트(초대된 회원)
        Optional<Member> invitedMember = memberRepository.findById(member.getId());
        invitedMember.ifPresent(invited -> {
            invited.setCouple(couple);
        });

        // 선택 질문 정보 request 셋팅
        SelectQuestion selectQuestion = request.toSelectQuestion(couple, inviteOpponent.getQuestion());

        // 선택 질문 테이블에 정보 등록
        selectQuestionRepository.save(selectQuestion);

        // 초대한 사람의 답변 등록일시는 초대 상대 테이블의 등록일시를 가져와서 저장.
        // 초대된 사람의 답변 등록일시는 LocalDateTime.now() 로 저장
        // 답변 테이블에 정보 request 셋팅
        Answer answer1 = request.toInvitedPersonAnswerInfo(member, selectQuestion, request.getAnswer()); // 링크로 초대된 사람 답변
        Answer answer2 = request.toInvitePersonAnswerInfo(inviteOpponent,selectQuestion); // 초대한 사람 답변
        List<Answer> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);
        // 답변 테이블에 정보 등록
        answerRepository.saveAll(answers);

        // 초대 상대 테이블에 노출 여부 'N' 으로 변경
        inviteOpponentCustomRepository.updateIsShow(inviteOpponent.getMember().getId());

        return MemberResponse.AcceptInviteLinkResponse.builder()
                .selectedQuestionId(selectQuestion.getId())
                .build();
    }



    /**
     * 초대 링크로 랜딩되는 페이지 관련 API
     *
     * @param linkKey
     * @return
     */
    public MemberResponse.InvitedPersonInfoResponse getInvitedPersonInfo(String linkKey) {

        // 상대 테이블에서 링크 고유 값으로 회원 조회
        InviteOpponent inviteOpponent = inviteOpponentRepository.findQuestionByLinkKey(linkKey).orElseThrow(
                () -> new CustomException(ErrorCode.BAD_REQUEST)
        );

        // 회원 이름
        String name = inviteOpponent.getMember().getName();

        // 질문
        String question = inviteOpponentCustomRepository.findSelectedQuestionIdByLinkKey(linkKey);

        return MemberResponse.InvitedPersonInfoResponse.builder()
                .invitedPersonName(name)
                .question(question)
                .build();
    }



    // TODO 개발자 주석 추가
    public MemberStatusResponse getUserStatus(Member member) {
        boolean isCouple = member.getCouple() != null;
        String linkKey = inviteOpponentCustomRepository.getLinkKey(member);

        UserStatusRole userStatus;
        if (isCouple) {
            userStatus = UserStatusRole.COUPLE_USER;
        } else {
            userStatus = linkKey == null ? UserStatusRole.NON_COUPLE_USER : UserStatusRole.COUPLE_WAITING_USER;
        }

        return new MemberStatusResponse(userStatus, linkKey);
    }

}
