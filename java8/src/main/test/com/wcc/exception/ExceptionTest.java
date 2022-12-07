package com.wcc.exception;

import com.wcc.exception.custom.BusinessException;
import com.wcc.exception.custom.MustDoException;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ExceptionTest {

    /**
     *  运行时异常，在编译阶段不报错，可以不处理，运行时报错
     *
     */
    @Test
    public void testRuntimeException(){

        //NullPointerException
        String name=null;
        boolean success=true;
        try {
            System.out.println(name.length());
        }catch (NullPointerException e){
            // 处理异常
            e.printStackTrace();
            success=false;
        }finally {
            System.out.println(success);
        }

    }

    /**
     * 可检测异常，在使用的时候必需进行处理，一个时throws向上抛出，由其他调用者处理
     * 一个是类似运行时异常处理方式，try-catch-finally 处理。
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testCheckException() throws UnsupportedEncodingException {
        byte[] bs = toGBK("中文");
        System.out.println(Arrays.toString(bs));
        System.out.println(Arrays.toString("中文".getBytes()));
        System.out.println(Arrays.toString("中文".getBytes("abc")));
    }
    static byte[] toGBK(String s) {
        try {
            // 用指定编码转换String为byte[]:
            return s.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            // 如果系统不支持GBK编码，会捕获到UnsupportedEncodingException:
            System.out.println(e); // 打印异常信息
            return s.getBytes(); // 尝试使用用默认编码
        }
    }

    /**
     * 自定义异常也包括了运行时异常和编译时异常
     *
     * 当我们在主动抛出异常时，使用的语法时throw
     */
    @Test
    public void testCustomException(){

        try {
            // 可以抛出许多异常
            throw new MustDoException();
        } catch (MustDoException e) { // 可以捕获处理每一个异常
            System.out.println("发生了必需处理的异常");
        }

        throw new BusinessException("001001","服务接口不存在");
    }
}
