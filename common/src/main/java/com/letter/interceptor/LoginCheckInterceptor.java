package com.letter.interceptor;

import com.letter.annotation.LoginCheck;
import com.letter.jwt.JwtProvider;
import com.letter.member.entity.Member;
import com.letter.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // HandlerMethod 을 상속받은 클래스가 아닌 경우 인터셉터 로직을 실행시키지 않도록 바로 true 를 반환하여 통과시켜 줌
        if(!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginCheck loginCheck = handlerMethod.getMethodAnnotation(LoginCheck.class);

        if (request.getRequestURI().contains("/api") && loginCheck == null) {
            //&& handler instanceof HandlerMethod
            return true;
        }
        else if(request.getRequestURI().contains("/api") && loginCheck != null) {

            String token = jwtProvider.bringToken(request);
            jwtProvider.validateToken(token, JwtProvider.ACCESS_TYPE);
            String memberId = jwtProvider.getUserInfoFromToken(token);
            final Optional<Member> member = memberRepository.findById(memberId);

            if (handlerMethod.hasMethodAnnotation(LoginCheck.class) && member.isEmpty()) { //실행하고자 하는 Controller method의 어노테이션 중 LoginCheck가 있는지 체크
                // 회원 아이디가 비어있으면 로그인 되어있지 않은 상태이므로 예외를 발생시킵니다.
                throw new AuthenticationException(request.getRequestURI());
            }
        }
        return true;
    }
}
