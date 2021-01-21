package com.example.demo.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.example.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void register(User user) {
        mongoTemplate.insert(user);
    }

    //实现阿里云短信接口 具体方法教程可去官方文档查看
    @Override
    public boolean sendMsg(String countryCode, String phoneNum) throws ClientException {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = " ";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = " ";//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(phoneNum);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(" ");
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(" ");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        //参考：request.setTemplateParam("{\"变量1\":\"值1\",\"变量2\":\"值2\",\"变量3\":\"值3\"}")
        String code_demo = (int)(Math.random() * 9 + 1) * 1000 + "";
        System.out.println(phoneNum);
        System.out.println("{\"code\":\""+code_demo+"\"}");
        request.setTemplateParam("{\"code\":\""+code_demo+"\"}");
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            stringRedisTemplate.opsForValue().set(phoneNum,code_demo,600, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    @Override
    public void update(User user) {
        //如果数据不存在 则存入；如果存在 则更新
        Update update = new Update();
        if(user.getDeposit() != null){
            update.set("deposit",user.getDeposit());
        }
        if(user.getStatus() != null){
            update.set("status",user.getStatus());
        }
        if(user.getName() != null){
            update.set("name",user.getName());
        }
        if(user.getIdNum() != null){
            update.set("idNUm",user.getIdNum());
        }
        mongoTemplate.updateFirst(new Query(Criteria.where("phoneNum").is(user.getPhoneNum())),
                update,User.class);
    }

    //与redis中缓存的信息对比验证
    @Override
    public boolean verify(String phoneNum, String verifyCode) {
        //调用redis 根据手机号作为key查找对应验证码
        int i = phoneNum.indexOf("=");
        int j = phoneNum.indexOf("&");
        String phone = phoneNum.substring(i+1,j);
        String code = stringRedisTemplate.opsForValue().get(phone);
        boolean flag = false;
        if(code != null && code.equals(verifyCode)){
            flag = true;
        }
        return flag;
    }
}
