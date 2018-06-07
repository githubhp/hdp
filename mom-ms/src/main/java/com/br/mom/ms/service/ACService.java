package com.br.mom.ms.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 *
 * @author xin.cao@100credit.com
 */
@Service
public class ACService implements ApplicationContextAware {

    private static ApplicationContext ac;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        ACService.ac = ac;
    }

    public static <T> T getBean(Class<T> clazz) {
        return ACService.ac.getBean(clazz);
    }
}
