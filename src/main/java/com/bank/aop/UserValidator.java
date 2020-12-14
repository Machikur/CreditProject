package com.bank.aop;

import com.bank.domain.User;
import com.bank.exception.OperationException;
import com.bank.service.AccountService;
import com.bank.service.CreditService;
import com.bank.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class UserValidator {

    private final CreditService creditService;
    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public UserValidator(CreditService creditService, AccountService accountService, UserService userService) {
        this.creditService = creditService;
        this.accountService = accountService;
        this.userService = userService;
    }

    @Around("execution(* ((@com.bank.aop.ToValidate *)).*(..))&&(args(id,..))||" +
            "execution(* com.bank.controller..*(..))&&@annotation(com.bank.aop.ToValidate)&&(args(id,..))")
    public Object validateUser(ProceedingJoinPoint proceedingJoinPoint, Long id) throws Throwable {
        ValidateType validateType = getAnnotation(proceedingJoinPoint).value();
        User user;
        switch (validateType) {
            case ACCOUNT:
                user = accountService.findAccount(id).getUser();
                break;
            case CREDIT:
                user = creditService.findCredit(id).getUser();
                break;
            case USER:
                user = userService.findById(id);
                break;
            default:
                throw new OperationException("Nieznana opcja");
        }
        if (user == null || !user.isEnable()) {
            throw new OperationException("Użytkownik został zablokowany lub nie istnieje");
        }
        return proceedingJoinPoint.proceed();
    }

    private ToValidate getAnnotation(ProceedingJoinPoint proceedingJoinPoint) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Class<?> annotationClass = proceedingJoinPoint.getTarget().getClass();
        ToValidate annotation = proceedingJoinPoint.getTarget().getClass().getMethod(signature.getName(), signature.getMethod().getParameterTypes()).getAnnotation(ToValidate.class);
        return annotation != null ? annotation : annotationClass.getAnnotation(ToValidate.class);
    }

}