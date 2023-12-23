package com.letter.interceptor;

import com.letter.annotation.User;
import com.letter.exception.CustomException;
import com.letter.exception.ErrorCode;
import com.letter.jwt.JwtProvider;
import com.letter.member.entity.Member;
import com.letter.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

import static com.letter.jwt.JwtProperties.SECRET;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    /**
     * 현재 parameter를 resolver가 지원할지 true/false로 반환한다.
     * 해당 메서드가 참이라면 resolveArgument()를 반환한다.
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // hasParameterAnnotation()을 사용하여 @User 어노테이션이 있는지 확인
        return parameter.hasParameterAnnotation(User.class);
    }

    /**
     * 실제 바인딩할 객체를 반환한다.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception       {

        String token = webRequest.getHeader("Authorization").substring(7);
        log.info("토큰 확인!! {}",token);

        jwtProvider.validateToken(token, SECRET);
        String memberId = jwtProvider.getUserInfoFromToken(token, SECRET);
        final Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()){
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        log.info("회원 아이디: {}",memberId);

        return member.orElseThrow();
    }
}
