package matchingGoal.matchingGoal.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.image.service.ImageService;
import matchingGoal.matchingGoal.member.dto.OtherMemberInfoResponse;
import matchingGoal.matchingGoal.member.dto.UpdateMemberInfoDto;
import matchingGoal.matchingGoal.member.dto.UpdatePasswordDto;
import matchingGoal.matchingGoal.member.exception.MemberNotFoundException;
import matchingGoal.matchingGoal.member.exception.UnmatchedPasswordException;
import matchingGoal.matchingGoal.member.exception.WithdrawnMemberAccessException;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    /**
     * 닉네임 중복 체크
     * @param nickname - 닉네임
     * @return 중복 닉네임 존재시, false 반환
     */
    public Boolean isDuplicatedNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isEmpty();
    }

    /**
     * 비밀번호 변경
     * @param token - 토큰
     * @param passwordDto - 기존 비밀번호, 새로운 비밀번호
     * @return "변경완료"
     */
    @Transactional
    public String updatePassword(String token, UpdatePasswordDto passwordDto) {
        Member member = getMemberByToken(token);
        isMatchedPassword(passwordDto.getOldPassword(), member.getPassword());
        member.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        return "변경완료";
    }

    /**
     * 아이디를 사용하여 회원 정보 조회
     * @param id - 회원 ID
     * @return Member
     */
    public Member getMemberById(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        if (member.isDeleted())
            throw new WithdrawnMemberAccessException();

        return member;
    }

    /**
     * 토큰을 사용하여 회원 정보 조회
     * @param token - 토큰
     * @return Member
     */
    public Member getMemberByToken(String token){
        jwtTokenProvider.validateToken(token);
        return getMemberById(jwtTokenProvider.getId(token));
    }

    /**
     * 개인 정보 수정
     * @param token - 토큰
     * @param updateDto - 정보 수정 dto (이름, 닉네임, 소개, 지역, 이미지)
     * @return "수정완료"
     */
    @Transactional
    public String editMemberInfo(String token, UpdateMemberInfoDto updateDto) {
        Member member = getMemberByToken(token);

        member.setName(updateDto.getName());
        member.setNickname(updateDto.getNickname());
        member.setIntroduction(updateDto.getIntroduction());
        member.setRegion(updateDto.getRegion());
        member.setImageId(updateDto.getImageId());
        return "수정완료";
    }

    /**
     * 다른 회원 정보 조회
     * @param id - 조회하고 싶은 회원 ID
     * @return OtherMemberInfoResponse - 닉네임, 소개, 지역, 이미지url
     */
    public OtherMemberInfoResponse getOtherMemberInfo(Long id) {
        Member member = getMemberById(id);
        String imageUrl = imageService.getImageUrl(member.getImageId());

        return OtherMemberInfoResponse.builder()
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .region(member.getRegion())
                .imageUrl(imageUrl)
                .build();
    }

    /**
     * 암호화된 비밀번호와 암호화되지 비밀번호 비교
     * @param rawPassword - 암호화되지 않은 비밀번호
     * @param encodedPassword - 암호화 된 비밀번호
     */
    public void isMatchedPassword(String rawPassword, String encodedPassword){
        boolean isMatch = passwordEncoder.matches(rawPassword,encodedPassword);
        if (!isMatch)
            throw new UnmatchedPasswordException();
    }
}
