package ru.skypro.homework.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.debug("Отсутствует Authorization header или не начинается с Bearer");
            logger.debug("Заголовки запроса: " + Collections.list(request.getHeaderNames()));
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authorizationHeader.substring(7);
        logger.debug("Извлечен JWT токен: " + jwtToken);

        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        logger.debug("Извлечен username из токена: " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.debug("Загрузка UserDetails для пользователя: " + username);

            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                logger.debug(" UserDetails успешно загружен: " + userDetails.getUsername());

                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    logger.debug("Токен валиден, создаем аутентификацию");

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    System.out.println("Устанавливаем аутентификацию в SecurityContext: " + authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.debug("Аутентификация установлена в SecurityContext");
                } else {
                    logger.debug("Токен не прошел валидацию");
                }
            } catch (UsernameNotFoundException e) {
                logger.debug("Пользователь не найден: " + username);
            }
        } else {
            if (username == null) {
                logger.debug("Не удалось извлечь username из токена");
            } else {
                logger.debug(" Аутентификация уже установлена в контексте");
            }
        }
        filterChain.doFilter(request, response);
    }
}