package com.mon.fpc.utils;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chen
 * @date 2022-11-01 10:07
 * @description:
 */

@Component
@Slf4j
public class MailUtils {

    /**
     * Spring官方提供的集成邮件服务的实现类，目前是Java后端发送邮件和集成邮件服务的主流工具。
     */
    @Resource
    private JavaMailSender mailSender;

    /**
     * 从配置文件中注入发件人的姓名
     */
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 发送文本邮件
     *
     * @param to      收件人
     * @param title   标题
     * @param content 正文
     */
    public void sendSimpleMail(String to, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        //发件人
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * 发送html邮件
     */
    public void sendHtmlMail(String to, String title, String content) {
        try {
            //注意这里使用的是MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(title);
            //第二个参数：格式是否为html
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送邮件时发生异常！", e);
        }
    }

    /**
     * 发送模板邮件
     */
    public void sendTemplateMail(String to, String title, Map<String, Object> map, String templatePath) {
        try {
            // 获得模板
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templatePath);
            // 传入数据模型到模板，替代模板中的占位符，并将模板转化为html字符串
            String templateHtml = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            // 该方法本质上还是发送html邮件，调用之前发送html邮件的方法
            this.sendHtmlMail(to, title, templateHtml);
        } catch (TemplateException | IOException e) {
            log.error("发送邮件时发生异常！", e);
        }
    }

    //发送验证码
    public void sendCodeMail(String to, String code) {
        String title = "验证码";
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        sendTemplateMail(to, title, map, "code.html");
    }

    /**
     * 发送带附件的邮件
     *
     * @param to
     * @param title
     * @param content
     * @param filePath
     */
    public void sendAttachmentsMail(String to, String title, String content, String filePath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            //要带附件第二个参数设为true
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送邮件时发生异常！", e);
        }

    }
}