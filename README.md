# 微信单车小程序

---

## 参考教程

- [共享单车微信小程序](https://www.nowcoder.com/study/vod/204)

---

## 小结

实现了用户验证码注册，显示附近单车等功能 

教程中的功能基本都实现了 对教程中的一些方面做了更改

- 教程中的发送验证码功能调用的腾讯云接口 更改为调用阿里云接口
- 删去小程序页面中的添加单车功能 利用python在北京石景山区生成了5w台单车信息
- 教程中使用的单mongodb节点存储单车信息 数据量稍微过大时加载缓慢 我搭建了3台节点的mongodb分片副本集 目前5w数据量的响应速度得到很大提升
- 后端代码中集群ip地址，端口号 阿里云短信接口部分代码需要根据自身情况填写
- 此项目部分功能尚待完善 充值功能，保修功能等


