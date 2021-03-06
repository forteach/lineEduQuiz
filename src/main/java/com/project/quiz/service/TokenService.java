package com.project.quiz.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.project.quiz.common.DefineCode;
import com.project.quiz.common.MyAssert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.project.quiz.common.Dic.*;

/**
 * @Auther: zhangyy
 * @Email: zhang10092009@hotmail.com
 * @Date: 2019/2/17 17:27
 * @Version: 1.0
 * @Description:
 */
@Slf4j
@Service
public class TokenService {

    @Value("${token.salt}")
    private String salt;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private String getToken(ServerHttpRequest request){
        return request.getHeaders().getFirst("token");
    }

    private String getValue(String token, int index){
        return JWT.decode(token).getAudience().get(index);
    }

    private String getKey(String userId){
        return USER_PREFIX.concat(userId);
    }

    private JWTVerifier verifier(String openId) {
        return JWT.require(Algorithm.HMAC256(salt.concat(openId))).build();
    }

    /**
     * 通过token 获取openId
     * @param request
     * @return
     */
    private String getOpenId(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst("token");
        Assert.notNull(token, "token is null");
        MyAssert.blank(token, DefineCode.ERR0004, "token is null");
        return JWT.decode(token).getAudience().get(0);
    }

    /**
     * 根据token 查找对应的学生信息
     * @param request
     * @return
     */
    public String getStudentId(ServerHttpRequest request){
        if (!stringRedisTemplate.opsForHash().hasKey(USER_PREFIX.concat(getOpenId(request)), "studentId")){
            MyAssert.isNull(null, DefineCode.ERR0004, "token 已经失效");
        }
        return String.valueOf(stringRedisTemplate.opsForHash().get(USER_PREFIX.concat(getOpenId(request)), "studentId"));
    }


    /**
     * 通过token 类型判断转换为教师id
     * @param request
     * @return Optional<String>
     */
    public Optional<String> getTeacherId(ServerHttpRequest request){
        String token = request.getHeaders().getFirst("token");
        if (TOKEN_TEACHER.equals(JWT.decode(token).getAudience().get(1))){
            return Optional.of(JWT.decode(token).getAudience().get(0));
        }
        MyAssert.isNull(null, DefineCode.ERR0010, "token 非法");
        return Optional.empty();
    }

    /**
     * 查询学生对应的班级id
     * @param request
     * @return
     */
    public String getClassId(ServerHttpRequest request) {
        String token = getToken(request);
        if (TOKEN_STUDENT.equals(getValue(token, 1))){
            return String.valueOf(stringRedisTemplate.opsForHash().get(getKey(getValue(token, 0)), "classId"));
        }
        return null;
    }



    /**
     * 校验token 是否有效
     * @param request
     * @return
     */
    public Mono<Boolean> check(ServerRequest request){
        String token = getToken(request);
        MyAssert.blank(token, DefineCode.ERR0004, "token is null");
        try {
            String openId = JWT.decode(token).getAudience().get(0);
            verifier(openId).verify(token);
        } catch (JWTVerificationException e) {
            log.error("token check false 401");
            MyAssert.fail(DefineCode.ERR0004, e, "401");
        }
        return Mono.just(true);
    }

    /**
     * 获取token
     * @param request
     * @return
     */
    private String getToken(ServerRequest request){
        AtomicReference<String> token = new AtomicReference<>(request.exchange().getRequest().getHeaders().getFirst("token"));
        if (StrUtil.isEmpty(token.get())){
            Mono.zip(request.bodyToMono(String.class),
                    Mono.just(request.remoteAddress()
                            .map(InetSocketAddress::getHostString)
                            .orElseThrow(RuntimeException::new)))
                    .flatMap(tuple -> {
                        String bodyData = tuple.getT1();
                        String remoteIp = tuple.getT2();
                        JSONObject jsonObject = JSONObject.parseObject(bodyData);
                        token.set(String.valueOf(jsonObject.get("token")));
                        log.info("BodyData =>" + bodyData);
                        log.info("RemoteIp =>" + remoteIp);
                        return ServerResponse.ok().body(Mono.just(bodyData + "\n" + remoteIp), String.class);
                    });
        }
        return token.get();
    }

    public Mono<String> findTokenType(ServerHttpRequest request){
        String token = request.getQueryParams().getFirst("token");
        return Mono.just(getValue(token, 1));
    }

    public boolean isStudent(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst("token");
        Assert.notNull(token, "token is null");
        MyAssert.blank(token, DefineCode.ERR0004, "token is null");
        return TOKEN_STUDENT.equals(getValue(token, 1)) ? true : false;
    }
}
