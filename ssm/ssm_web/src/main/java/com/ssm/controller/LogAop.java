package com.ssm.controller;

import com.ssm.domain.SysLog;
import com.ssm.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

//sysLog：1.访问时间，6 操作者用户名，5 访问ip，5.访问资源url，4.执行时长，3.访问方法

@Component
@Aspect
public class LogAop {
    @Autowired
    private HttpServletRequest request;//注入一个request，用来获取ip
    @Autowired
    private ISysLogService sysLogService;

    private Date visitTime;//开始访问时间
    private Class clazz;//访问类
    private Method method;//访问方法

    //前置通知:开始访问时间，访问类，访问方法
    @Before("execution(* com.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {
        visitTime = new Date();             //1 访问时间
        clazz = jp.getTarget().getClass();  //2 访问类

        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        if(args==null){                     //3 访问方法
            method=clazz.getMethod(methodName);//只能获取无参数的方法
        }else{
            Class[] classArgs = new Class[args.length];
            for(int i=0;i<args.length;i++){
                classArgs[i]=args[i].getClass();
            }
            method = clazz.getMethod(methodName, classArgs);
        }
    }
    //后置通知
    @After("execution(* com.ssm.controller.*.*(..))")
    public void doAfter() throws Exception {
        long time = new Date().getTime()-visitTime.getTime();   //4 访问时长
        //5 url：反射
        String url="";
        if(clazz!=null && method!=null && clazz!=LogAop.class){
            RequestMapping classAnnotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if(classAnnotation!=null){
                String[] classValue = classAnnotation.value();
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                if(methodAnnotation!=null){
                    String[] methodValue = methodAnnotation.value();
                    url=classValue[0]+methodValue[0];

                    String ip = request.getRemoteAddr();//6 ip地址
                    SecurityContext context = SecurityContextHolder.getContext();//从上下文中获取登录的用户
//        SecurityContext context = request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
                    User user = (User) context.getAuthentication().getPrincipal();
                    String username = user.getUsername();//5 操作者用户名


                    //封装：日志对SysLog象
                    SysLog sysLog = new SysLog();
                    sysLog.setExecutionTime(time);
                    sysLog.setIp(ip);
                    sysLog.setUrl(url);
                    sysLog.setMethod("[类名]"+clazz.getName()+"[方法名]"+method.getName());
                    sysLog.setUsername(username);
                    sysLog.setVisitTime(visitTime);
                    //调用service完成日志的添加操作
                    sysLogService.save(sysLog);
                }
            }
        }

    }
}
