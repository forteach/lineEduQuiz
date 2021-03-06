package com.project.quiz.config;

import com.alibaba.fastjson.JSON;
import com.project.quiz.common.WebResult;
import com.project.quiz.exceptions.AssertErrorException;
import com.project.quiz.exceptions.CustomException;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/11/9 11:19
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    static final String NULL_POINTEREXCEPTION_EMPTY_DATA = "The mapper returned a null value.";

    @ExceptionHandler(AssertErrorException.class)
    @ResponseBody
    public Mono<WebResult> serverExceptionHandler(AssertErrorException ex) {
        WebResult wr = WebResult.failResult(ex.getErrorCode(), ex.getMessage());
        if (log.isDebugEnabled()) {
            log.debug("MyAssert : [{}]", JSON.toJSONString(wr));
        }
        return Mono.just(wr);
    }

    @ExceptionHandler(Exception.class)
    public Mono<WebResult> serverExceptionHandler(ServerWebExchange exchange, Exception e) {
        log.error("全局异常拦截器  [{}]   请求地址 [{}]  /  异常信息  [{}]", e, exchange.getRequest().getPath(), e.getMessage());
        return WebResult.failResultMono();
    }

    @ExceptionHandler(RedisException.class)
    public Mono<WebResult> serverExceptionHandler(ServerWebExchange exchange, RedisException e) {
        log.error("全局异常拦截器  redis 异常 [{}]   请求地址 [{}]  /  异常信息  [{}]", e, exchange.getRequest().getPath(), e.getMessage());
        return WebResult.failResultMono();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Mono<WebResult> methodArgumentNotValidException(ServerWebExchange exchange, MethodArgumentNotValidException e) {
        log.error("全局异常拦截器 参数校验不正确  [{}] 请求地址 [{}]  /  异常信息  [{}]", e, exchange.getRequest().getPath(), e.getMessage());
        return WebResult.failResultMono(9000);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Mono<WebResult> httpMessageNotReadableException(ServerWebExchange exchange, HttpMessageNotReadableException e) {
        log.error("全局异常拦截器 请求参数格式不正确  [{}] 请求地址 [{}]  /  异常信息  [{}]", e, exchange.getRequest().getPath(), e.getMessage());
        return WebResult.failResultMono(9100);
    }

    @ExceptionHandler(value = CustomException.class)
    public Mono<WebResult> customException(ServerWebExchange exchange, CustomException e) {
        log.error("全局异常拦截器 校验及自反馈前端  [{}] 请求地址 [{}]  /  异常信息  [{}]", e, exchange.getRequest().getPath(), e.getMessage());
        return WebResult.okCustomResultMono(3000, e.getMessage());
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Mono<WebResult> nullPointerException(ServerWebExchange exchange, NullPointerException e) {
        log.error("全局异常拦截器 校验及自反馈前端  [{}] 请求地址 [{}]  /  异常信息  [{}]", e, exchange.getRequest().getPath(), e.getMessage());

        if (NULL_POINTEREXCEPTION_EMPTY_DATA.equals(e.getMessage())) {
            return WebResult.okCustomResultMono(3000);
        }
        return WebResult.failResultMono(9000);
    }

}
