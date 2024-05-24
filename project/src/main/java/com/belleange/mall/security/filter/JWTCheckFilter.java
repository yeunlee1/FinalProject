package com.belleange.mall.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.belleange.mall.dto.MemberDTO;
import com.belleange.mall.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Preflight 요청은 체크하지 않음
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();

        log.info("check uri.............." + path);

        // 특정 경로를 검사하지 않도록 설정
        if (path.startsWith("/api/member") || 
        path.startsWith("/api/ask/list") || 
        path.startsWith("/api/event/view") || 
        path.startsWith("/api/event/list") || 
        path.startsWith("/api/products/list/set") || 
        path.startsWith("/api/products/list/all") || 
        path.startsWith("/api/products/list/one") || 
        path.startsWith("/api/products/view") || 
        path.startsWith("/api/notice/list") || 
        path.startsWith("/api/notice/view") || 
        path.startsWith("/api/search/products") ||
        path.equals("/api/v1/mails/send-certification")) { // 이메일 인증 경로는 토큰 검사 안 함
        return true;
    }

        // 특정 GET 요청을 검사하지 않도록 설정
        if (request.getMethod().equals("GET")) {
            if (path.matches("/api/ask/\\d+/?") || 
                path.matches("/api/event/\\d+/?") || 
                path.matches("/api/products/\\d+/?") || 
                path.matches("/api/notice/\\d+/?")) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("------------------------JWTCheckFilter.......................");

        String authHeaderStr = request.getHeader("Authorization");

        try {
            // Bearer accestoken...
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);

            String email = (String) claims.get("email");
            String pw = (String) claims.get("pw");
            String nickname = (String) claims.get("nickname");
            String phone = (String) claims.get("phone");
            String birth = (String) claims.get("birth");
            String useraddress = (String) claims.get("useraddress");
            String detailaddress = (String) claims.get("detailaddress");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(email, pw, nickname, phone, birth, useraddress, detailaddress, social, roleNames);

            log.info("-----------------------------------");
            log.info(memberDTO);
            log.info(memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT Check Error..............");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }
    }
}
